package com.greenlink.greenlink.repository;

import com.greenlink.greenlink.domain.iot.GrowSpace;
import com.greenlink.greenlink.domain.iot.GrowSpacePlant;
import com.greenlink.greenlink.domain.plant.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GrowSpacePlantRepository extends JpaRepository<GrowSpacePlant, Long> {

    List<GrowSpacePlant> findAllByGrowSpaceAndDeletedFalse(GrowSpace growSpace);

    List<GrowSpacePlant> findAllByGrowSpaceAndActiveTrueAndDeletedFalse(GrowSpace growSpace);

    Optional<GrowSpacePlant> findByUserPlantAndDeletedFalse(UserPlant userPlant);

    Optional<GrowSpacePlant> findByUserPlantAndActiveTrueAndDeletedFalse(UserPlant userPlant);

    Optional<GrowSpacePlant> findByGrowSpaceAndUserPlantAndDeletedFalse(
            GrowSpace growSpace,
            UserPlant userPlant
    );

    boolean existsByUserPlantAndDeletedFalse(UserPlant userPlant);

    boolean existsByGrowSpaceAndUserPlantAndDeletedFalse(
            GrowSpace growSpace,
            UserPlant userPlant
    );
}