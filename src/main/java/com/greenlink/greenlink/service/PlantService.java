package com.greenlink.greenlink.service;

import com.greenlink.greenlink.domain.plant.Plant;
import com.greenlink.greenlink.dto.plant.PlantDetailResponse;
import com.greenlink.greenlink.dto.plant.PlantListResponse;
import com.greenlink.greenlink.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PlantService {

    private final PlantRepository plantRepository;

    public List<PlantListResponse> getPlants() {
        return plantRepository.findAllByDeletedFalse()
                .stream()
                .map(PlantListResponse::from)
                .toList();
    }

    public PlantDetailResponse getPlant(Long plantId) {
        Plant plant = plantRepository.findByIdAndDeletedFalse(plantId)
                .orElseThrow(() -> new IllegalArgumentException("식물을 찾을 수 없습니다."));

        return PlantDetailResponse.from(plant);
    }
}