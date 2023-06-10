package com.bike.inventory.web.controllers;

import com.bike.common.BikeInventoryDto;
import com.bike.inventory.repository.BikeInventoryRepository;
import com.bike.inventory.web.mappers.BikeInventoryMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerInventoryController {

    private final BikeInventoryRepository bikeInventoryRepository;
    private final BikeInventoryMapper bikeInventoryMapper;

    @GetMapping("api/v1/bike/{bikeId}/inventory")
    List<BikeInventoryDto> listBikesById(@PathVariable UUID bikeId) {
        log.debug("Finding Inventory for beerId:" + bikeId);

        return bikeInventoryRepository.findAllByBikeId(bikeId)
                .stream()
                .map(bikeInventoryMapper::bikeInventoryToBikeInventoryDto)
                .collect(Collectors.toList());
    }
}
