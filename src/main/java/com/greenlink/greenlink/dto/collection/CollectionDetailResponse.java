package com.greenlink.greenlink.dto.collection;

import com.greenlink.greenlink.domain.plant.Plant;
import com.greenlink.greenlink.domain.plant.UserPlant;

import java.util.List;

public record CollectionDetailResponse(
        Long plantId,
        String name,
        String category,
        String description,
        String imageUrl,
        boolean collected,
        long harvestCount,
        List<HarvestedPlantResponse> harvestedPlants
) {

    public static CollectionDetailResponse of(
            Plant plant,
            List<UserPlant> harvestedPlants
    ) {
        return new CollectionDetailResponse(
                plant.getId(),
                plant.getName(),
                plant.getCategory(),
                plant.getDescription(),
                plant.getImageUrl(),
                !harvestedPlants.isEmpty(),
                harvestedPlants.size(),
                harvestedPlants.stream()
                        .map(HarvestedPlantResponse::from)
                        .toList()
        );
    }
}