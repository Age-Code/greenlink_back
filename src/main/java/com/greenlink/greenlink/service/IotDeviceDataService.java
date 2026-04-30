package com.greenlink.greenlink.service;

import com.greenlink.greenlink.domain.iot.EspSensorData;
import com.greenlink.greenlink.domain.iot.GrowSpace;
import com.greenlink.greenlink.domain.iot.IotDevice;
import com.greenlink.greenlink.domain.iot.PlantImage;
import com.greenlink.greenlink.domain.iot.RaspberrySensorData;
import com.greenlink.greenlink.domain.plant.UserPlant;
import com.greenlink.greenlink.dto.iot.IotDeviceDto;
import com.greenlink.greenlink.repository.EspSensorDataRepository;
import com.greenlink.greenlink.repository.GrowSpacePlantRepository;
import com.greenlink.greenlink.repository.IotDeviceRepository;
import com.greenlink.greenlink.repository.PlantImageRepository;
import com.greenlink.greenlink.repository.RaspberrySensorDataRepository;
import com.greenlink.greenlink.repository.UserPlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IotDeviceDataService {

    private final IotDeviceRepository iotDeviceRepository;
    private final RaspberrySensorDataRepository raspberrySensorDataRepository;
    private final EspSensorDataRepository espSensorDataRepository;
    private final PlantImageRepository plantImageRepository;
    private final UserPlantRepository userPlantRepository;
    private final GrowSpacePlantRepository growSpacePlantRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

    /**
     * 라즈베리파이 환경 데이터 저장
     *
     * Header:
     * X-DEVICE-KEY: RPI-CAPSTONE-001
     */
    @Transactional
    public IotDeviceDto.RaspberryEnvironmentResDto saveRaspberryEnvironment(
            String deviceKey,
            IotDeviceDto.RaspberryEnvironmentReqDto request
    ) {
        IotDevice raspberryDevice = findActiveDeviceByKey(deviceKey);

        validateRaspberryDevice(raspberryDevice);

        GrowSpace growSpace = raspberryDevice.getGrowSpace();

        if (growSpace == null) {
            throw new IllegalStateException("라즈베리파이에 연결된 재배 공간이 없습니다.");
        }

        RaspberrySensorData sensorData = RaspberrySensorData.create(
                growSpace,
                raspberryDevice,
                request.getTemperature(),
                request.getHumidity(),
                request.getLight(),
                request.getMeasuredAt()
        );

        RaspberrySensorData savedSensorData =
                raspberrySensorDataRepository.save(sensorData);

        raspberryDevice.updateLastConnectedAt();

        return IotDeviceDto.RaspberryEnvironmentResDto.from(savedSensorData);
    }

    /**
     * ESP 토양수분 데이터 저장
     *
     * Header:
     * X-DEVICE-KEY: ESP-BASIL-001
     */
    @Transactional
    public IotDeviceDto.EspSoilMoistureResDto saveEspSoilMoisture(
            String deviceKey,
            IotDeviceDto.EspSoilMoistureReqDto request
    ) {
        IotDevice espDevice = findActiveDeviceByKey(deviceKey);

        validateEspDevice(espDevice);

        UserPlant userPlant = espDevice.getUserPlant();

        if (userPlant == null) {
            throw new IllegalStateException("ESP32에 연결된 식물이 없습니다.");
        }

        EspSensorData sensorData = EspSensorData.create(
                espDevice.getGrowSpace(),
                userPlant,
                espDevice,
                request.getSoilMoistureRaw(),
                request.getSoilMoisturePercent(),
                request.getMeasuredAt()
        );

        EspSensorData savedSensorData =
                espSensorDataRepository.save(sensorData);

        espDevice.updateLastConnectedAt();

        return IotDeviceDto.EspSoilMoistureResDto.from(savedSensorData);
    }

    /**
     * 라즈베리파이 식물 이미지 업로드
     *
     * Header:
     * X-DEVICE-KEY: RPI-CAPSTONE-001
     *
     * multipart/form-data:
     * - file
     * - userPlantId 선택
     * - capturedAt 선택
     */
    @Transactional
    public IotDeviceDto.PlantImageUploadResDto savePlantImage(
            String deviceKey,
            MultipartFile file,
            Long userPlantId,
            LocalDateTime capturedAt
    ) {
        IotDevice raspberryDevice = findActiveDeviceByKey(deviceKey);

        validateRaspberryDevice(raspberryDevice);

        GrowSpace growSpace = raspberryDevice.getGrowSpace();

        if (growSpace == null) {
            throw new IllegalStateException("라즈베리파이에 연결된 재배 공간이 없습니다.");
        }

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드할 이미지 파일이 필요합니다.");
        }

        validateImageFile(file);

        UserPlant userPlant = null;

        if (userPlantId != null) {
            userPlant = userPlantRepository.findByIdAndDeletedFalse(userPlantId)
                    .orElseThrow(() -> new IllegalArgumentException("이미지를 연결할 식물을 찾을 수 없습니다."));

            boolean connected = growSpacePlantRepository.existsByGrowSpaceAndUserPlantAndDeletedFalse(
                    growSpace,
                    userPlant
            );

            if (!connected) {
                throw new IllegalStateException("해당 식물은 라즈베리파이의 재배 공간에 연결되어 있지 않습니다.");
            }
        }

        String originalFilename = file.getOriginalFilename();
        String storedFilename = createStoredFilename(originalFilename, userPlantId);
        String imageUrl = saveFile(file, storedFilename);

        PlantImage plantImage = PlantImage.create(
                growSpace,
                userPlant,
                raspberryDevice,
                imageUrl,
                originalFilename,
                capturedAt
        );

        PlantImage savedPlantImage = plantImageRepository.save(plantImage);

        raspberryDevice.updateLastConnectedAt();

        return IotDeviceDto.PlantImageUploadResDto.from(savedPlantImage);
    }

    private IotDevice findActiveDeviceByKey(String deviceKey) {
        if (deviceKey == null || deviceKey.isBlank()) {
            throw new IllegalArgumentException("X-DEVICE-KEY가 필요합니다.");
        }

        return iotDeviceRepository.findByDeviceKeyAndActiveTrueAndDeletedFalse(deviceKey)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않았거나 비활성화된 기기입니다."));
    }

    private void validateRaspberryDevice(IotDevice device) {
        if (!device.isRaspberryPi()) {
            throw new IllegalStateException("라즈베리파이 기기만 환경 데이터 또는 이미지를 전송할 수 있습니다.");
        }
    }

    private void validateEspDevice(IotDevice device) {
        if (!device.isEsp32()) {
            throw new IllegalStateException("ESP32 기기만 토양수분 데이터를 전송할 수 있습니다.");
        }
    }

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();

        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("이미지 파일만 업로드할 수 있습니다.");
        }

        long maxSize = 10 * 1024 * 1024;

        if (file.getSize() > maxSize) {
            throw new IllegalArgumentException("이미지 파일은 10MB 이하만 업로드할 수 있습니다.");
        }

        String originalFilename = file.getOriginalFilename();

        if (originalFilename == null || originalFilename.isBlank()) {
            throw new IllegalArgumentException("파일명이 올바르지 않습니다.");
        }

        String extension = getExtension(originalFilename).toLowerCase();

        Set<String> allowedExtensions = Set.of("jpg", "jpeg", "png", "webp");

        if (!allowedExtensions.contains(extension)) {
            throw new IllegalArgumentException("jpg, jpeg, png, webp 파일만 업로드할 수 있습니다.");
        }
    }

    private String createStoredFilename(String originalFilename, Long userPlantId) {
        String extension = getExtension(originalFilename);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);

        if (userPlantId == null) {
            return "grow-space-" + timestamp + "-" + uuid + "." + extension;
        }

        return "user-plant-" + userPlantId + "-" + timestamp + "-" + uuid + "." + extension;
    }

    private String getExtension(String filename) {
        int dotIndex = filename.lastIndexOf(".");

        if (dotIndex == -1 || dotIndex == filename.length() - 1) {
            throw new IllegalArgumentException("파일 확장자가 없습니다.");
        }

        return filename.substring(dotIndex + 1);
    }

    private String saveFile(MultipartFile file, String storedFilename) {
        try {
            Path uploadPath = Path.of(uploadDir, "iot");

            Files.createDirectories(uploadPath);

            Path filePath = uploadPath.resolve(storedFilename);

            file.transferTo(filePath.toFile());

            return "/uploads/iot/" + storedFilename;
        } catch (IOException e) {
            throw new IllegalStateException("이미지 파일 저장에 실패했습니다.");
        }
    }
}