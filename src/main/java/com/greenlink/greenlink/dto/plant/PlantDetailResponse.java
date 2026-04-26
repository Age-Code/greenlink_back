package com.greenlink.greenlink.dto.plant;

import com.greenlink.greenlink.domain.plant.Plant;

public record PlantDetailResponse(
        Long plantId,
        String name,
        String category,
        String description,
        String imageUrl,
        Integer growthDays
) {

    public static PlantDetailResponse from(Plant plant) {
        return new PlantDetailResponse(
                plant.getId(),
                plant.getName(),
                plant.getCategory(),
                plant.getDescription(),
                plant.getImageUrl(),
                plant.getGrowthDays()
        );
    }
}