package com.greenlink.greenlink.dto.collection;

import com.greenlink.greenlink.domain.plant.Plant;

import java.time.LocalDateTime;

public record CollectionListResponse(
        Long plantId,
        String name,
        String category,
        String imageUrl,
        boolean collected,
        long harvestCount,
        LocalDateTime firstHarvestedAt
) {

    public static CollectionListResponse of(
            Plant plant,
            boolean collected,
            long harvestCount,
            LocalDateTime firstHarvestedAt
    ) {
        return new CollectionListResponse(
                plant.getId(),
                plant.getName(),
                plant.getCategory(),
                plant.getImageUrl(),
                collected,
                harvestCount,
                firstHarvestedAt
        );
    }
}