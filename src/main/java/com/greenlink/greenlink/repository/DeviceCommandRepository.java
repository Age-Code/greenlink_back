package com.greenlink.greenlink.repository;

import com.greenlink.greenlink.domain.iot.CommandStatus;
import com.greenlink.greenlink.domain.iot.CommandType;
import com.greenlink.greenlink.domain.iot.DeviceCommand;
import com.greenlink.greenlink.domain.iot.GrowSpace;
import com.greenlink.greenlink.domain.iot.IotDevice;
import com.greenlink.greenlink.domain.iot.PumpChannel;
import com.greenlink.greenlink.domain.plant.UserPlant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface DeviceCommandRepository extends JpaRepository<DeviceCommand, Long> {

    Optional<DeviceCommand> findByIdAndDeletedFalse(Long id);

    List<DeviceCommand> findAllByGrowSpaceAndDeletedFalseOrderByRequestedAtDesc(
            GrowSpace growSpace
    );

    List<DeviceCommand> findAllByUserPlantAndDeletedFalseOrderByRequestedAtDesc(
            UserPlant userPlant
    );

    List<DeviceCommand> findAllByIotDeviceAndDeletedFalseOrderByRequestedAtAsc(
            IotDevice iotDevice
    );

    List<DeviceCommand> findAllByIotDeviceAndCommandStatusAndDeletedFalseOrderByRequestedAtAsc(
            IotDevice iotDevice,
            CommandStatus commandStatus
    );

    List<DeviceCommand> findAllByIotDeviceAndCommandStatusInAndDeletedFalseOrderByRequestedAtAsc(
            IotDevice iotDevice,
            Collection<CommandStatus> commandStatuses
    );

    List<DeviceCommand> findAllByIotDeviceAndCommandTypeAndCommandStatusAndDeletedFalseOrderByRequestedAtAsc(
            IotDevice iotDevice,
            CommandType commandType,
            CommandStatus commandStatus
    );

    boolean existsByUserPlantAndCommandTypeAndCommandStatusInAndDeletedFalse(
            UserPlant userPlant,
            CommandType commandType,
            Collection<CommandStatus> commandStatuses
    );

    boolean existsByPumpChannelAndCommandTypeAndCommandStatusInAndDeletedFalse(
            PumpChannel pumpChannel,
            CommandType commandType,
            Collection<CommandStatus> commandStatuses
    );
}