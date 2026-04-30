package com.greenlink.greenlink.dto.iot;

import com.greenlink.greenlink.domain.iot.CommandStatus;
import com.greenlink.greenlink.domain.iot.CommandType;
import com.greenlink.greenlink.domain.iot.DeviceCommand;
import com.greenlink.greenlink.domain.iot.EspSensorData;
import com.greenlink.greenlink.domain.iot.GrowSpace;
import com.greenlink.greenlink.domain.iot.PlantImage;
import com.greenlink.greenlink.domain.iot.RaspberrySensorData;
import lombok.*;

import java.time.LocalDateTime;

public class IotAppDto {

    /**
     * 내 식물 IoT 최신 상태 응답 DTO
     *
     * GET /api/user-plants/{userPlantId}/iot/latest
     *
     * environment:
     * - 라즈베리파이가 측정한 재배 공간 단위 환경 데이터
     *
     * soil:
     * - ESP가 측정한 해당 식물 단위 토양수분 데이터
     *
     * latestImage:
     * - 해당 식물의 최신 이미지
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IotLatestResDto {
        private Long userPlantId;
        private GrowSpaceSimpleDto growSpace;
        private EnvironmentDto environment;
        private SoilDto soil;
        private PlantImageDto latestImage;

        public static IotLatestResDto of(
                Long userPlantId,
                GrowSpace growSpace,
                RaspberrySensorData raspberrySensorData,
                EspSensorData espSensorData,
                PlantImage latestImage
        ) {
            return IotLatestResDto.builder()
                    .userPlantId(userPlantId)
                    .growSpace(GrowSpaceSimpleDto.from(growSpace))
                    .environment(EnvironmentDto.from(raspberrySensorData))
                    .soil(SoilDto.from(espSensorData))
                    .latestImage(PlantImageDto.from(latestImage))
                    .build();
        }
    }

    /**
     * 앱 화면에 표시할 간단한 재배 공간 정보 DTO
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GrowSpaceSimpleDto {
        private Long growSpaceId;
        private String name;

        public static GrowSpaceSimpleDto from(GrowSpace growSpace) {
            if (growSpace == null) {
                return null;
            }

            return GrowSpaceSimpleDto.builder()
                    .growSpaceId(growSpace.getId())
                    .name(growSpace.getName())
                    .build();
        }
    }

    /**
     * 라즈베리파이 환경 센서 데이터 DTO
     *
     * 온도, 습도, 조도는 특정 식물 하나의 값이 아니라
     * 해당 식물이 속한 재배 공간의 공통 환경값이다.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EnvironmentDto {
        private Long sensorDataId;
        private Double temperature;
        private Double humidity;
        private Double light;
        private LocalDateTime measuredAt;

        public static EnvironmentDto from(RaspberrySensorData data) {
            if (data == null) {
                return null;
            }

            return EnvironmentDto.builder()
                    .sensorDataId(data.getId())
                    .temperature(data.getTemperature())
                    .humidity(data.getHumidity())
                    .light(data.getLight())
                    .measuredAt(data.getMeasuredAt())
                    .build();
        }
    }

    /**
     * ESP 토양수분 데이터 DTO
     *
     * 토양수분은 식물마다 다르므로 userPlant 기준 데이터다.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SoilDto {
        private Long sensorDataId;
        private Integer soilMoistureRaw;
        private Double soilMoisturePercent;
        private LocalDateTime measuredAt;

        public static SoilDto from(EspSensorData data) {
            if (data == null) {
                return null;
            }

            return SoilDto.builder()
                    .sensorDataId(data.getId())
                    .soilMoistureRaw(data.getSoilMoistureRaw())
                    .soilMoisturePercent(data.getSoilMoisturePercent())
                    .measuredAt(data.getMeasuredAt())
                    .build();
        }
    }

    /**
     * 식물 이미지 DTO
     *
     * GET /api/user-plants/{userPlantId}/iot/images
     * GET /api/user-plants/{userPlantId}/iot/latest
     *
     * 두 API에서 공통으로 사용한다.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlantImageDto {
        private Long plantImageId;
        private String imageUrl;
        private LocalDateTime capturedAt;

        public static PlantImageDto from(PlantImage image) {
            if (image == null) {
                return null;
            }

            return PlantImageDto.builder()
                    .plantImageId(image.getId())
                    .imageUrl(image.getImageUrl())
                    .capturedAt(image.getCapturedAt())
                    .build();
        }
    }

    /**
     * 물 주기 명령 응답 DTO
     *
     * POST /api/user-plants/{userPlantId}/iot/water
     *
     * Request Body는 없다.
     * 서버에서 고정 급수 시간 5초로 DeviceCommand를 생성한다.
     */
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WaterCommandResDto {
        private Long commandId;
        private Long userPlantId;
        private Long growSpaceId;
        private Long deviceId;
        private Long pumpChannelId;
        private CommandType commandType;
        private CommandStatus commandStatus;
        private Integer durationSeconds;
        private LocalDateTime requestedAt;

        public static WaterCommandResDto from(DeviceCommand command) {
            Long pumpChannelId = command.getPumpChannel() == null
                    ? null
                    : command.getPumpChannel().getId();

            return WaterCommandResDto.builder()
                    .commandId(command.getId())
                    .userPlantId(command.getUserPlant().getId())
                    .growSpaceId(command.getGrowSpace().getId())
                    .deviceId(command.getIotDevice().getId())
                    .pumpChannelId(pumpChannelId)
                    .commandType(command.getCommandType())
                    .commandStatus(command.getCommandStatus())
                    .durationSeconds(command.getDurationSeconds())
                    .requestedAt(command.getRequestedAt())
                    .build();
        }
    }
}