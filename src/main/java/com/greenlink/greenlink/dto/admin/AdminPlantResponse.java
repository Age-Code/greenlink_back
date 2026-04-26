package com.greenlink.greenlink.dto.admin;

import com.greenlink.greenlink.domain.plant.Plant;

public record AdminPlantResponse(
        Long plantId,
        String name,
        String category,
        String description,
        String imageUrl,
        Integer growthDays
) {

    public static AdminPlantResponse from(Plant plant) {
        return new AdminPlantResponse(
                plant.getId(),
                plant.getName(),
                plant.getCategory(),
                plant.getDescription(),
                plant.getImageUrl(),
                plant.getGrowthDays()
        );
    }
}