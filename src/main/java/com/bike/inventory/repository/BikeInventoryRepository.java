package com.bike.inventory.repository;

import com.bike.inventory.domain.BikeInventory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;


public interface BikeInventoryRepository extends JpaRepository<BikeInventory, UUID> {

    List<BikeInventory> findAllByBikeId(UUID bikeId);

    List<BikeInventory> findAllByUpc(String upc);
}
