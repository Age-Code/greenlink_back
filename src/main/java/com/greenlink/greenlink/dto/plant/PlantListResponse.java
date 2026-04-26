package com.greenlink.greenlink.dto.plant;

import com.greenlink.greenlink.domain.plant.Plant;

public record PlantListResponse(
        Long plantId,
        String name,
        String category,
        String imageUrl
) {

    public static PlantListResponse from(Plant plant) {
        return new PlantListResponse(
                plant.getId(),
                plant.getName(),
                plant.getCategory(),
                plant.getImageUrl()
        );
    }
}