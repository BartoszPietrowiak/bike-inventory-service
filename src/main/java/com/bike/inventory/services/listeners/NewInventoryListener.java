package com.bike.inventory.services.listeners;

import com.bike.common.events.NewInventoryEvent;
import com.bike.inventory.config.JmsConfig;
import com.bike.inventory.domain.BikeInventory;
import com.bike.inventory.repository.BikeInventoryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NewInventoryListener {

    private final BikeInventoryRepository bikeInventoryRepository;

    @Transactional
    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent event) {
        log.info("Get Event: " + event.toString());

        bikeInventoryRepository.save(BikeInventory.builder()
                .bikeId(event.getBikeDto().getId())
                .upc(event.getBikeDto().getUpc())
                .quantity(event.getBikeDto().getQuantity())
                .build());
    }
}
