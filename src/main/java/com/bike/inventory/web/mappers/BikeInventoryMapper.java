package com.bike.inventory.web.mappers;

import com.bike.common.BikeInventoryDto;
import com.bike.inventory.domain.BikeInventory;
import org.mapstruct.Mapper;

@Mapper(uses = {DateMapper.class})
public interface BikeInventoryMapper {

    BikeInventory bikeInventoryDtoToBikeInventory(BikeInventoryDto bikeInventoryDto);

    BikeInventoryDto bikeInventoryToBikeInventoryDto(BikeInventory bikeInventory);
}
