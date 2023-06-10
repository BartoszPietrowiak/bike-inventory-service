package com.bike.inventory.services;

import com.bike.common.BikeOrderDto;
import com.bike.common.BikeOrderLineDto;
import com.bike.inventory.domain.BikeInventory;
import com.bike.inventory.repository.BikeInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
@RequiredArgsConstructor
public class AllocationServiceImpl implements AllocationService {

    private final BikeInventoryRepository bikeInventoryRepository;

    @Override
    public Boolean allocateOrder(BikeOrderDto bikeOrderDto) {
        log.debug("Allocating OrderId: " + bikeOrderDto.getId());

        AtomicInteger totalOrdered = new AtomicInteger();
        AtomicInteger totalAllocated = new AtomicInteger();

        bikeOrderDto.getBikeOrderLines().forEach(bikeOrderLine -> {
            if ((((bikeOrderLine.getOrderQuantity() != null ? bikeOrderLine.getOrderQuantity() : 0)
                    - (bikeOrderLine.getAllocatedQuantity() != null ? bikeOrderLine.getAllocatedQuantity() : 0)) > 0)) {
                allocateBeerOrderLine(bikeOrderLine);
            }
            totalOrdered.set(totalOrdered.get() + bikeOrderLine.getOrderQuantity());
            totalAllocated.set(totalAllocated.get() + (bikeOrderLine.getAllocatedQuantity() != null ? bikeOrderLine.getAllocatedQuantity() : 0));
        });

        log.debug("Total Ordered: " + totalOrdered.get() + " Total Allocated: " + totalAllocated.get());

        return totalOrdered.get() == totalAllocated.get();
    }

    @Override
    public void deallocateOrder(BikeOrderDto bikeOrderDto) {
        log.debug("Deallocating OrderId: " + bikeOrderDto.getId());

        bikeOrderDto.getBikeOrderLines().forEach(bikeOrderLine -> {
            BikeInventory bikeInventory = BikeInventory
                    .builder()
                    .bikeId(bikeOrderLine.getBikeId())
                    .upc(bikeOrderLine.getUpc())
                    .quantity(bikeOrderLine.getAllocatedQuantity())
                    .build();

            BikeInventory savedInventory = bikeInventoryRepository.save(bikeInventory);
        });
    }

    private void allocateBeerOrderLine(BikeOrderLineDto bikeOrderLine) {
        List<BikeInventory> bikeInventoryList = bikeInventoryRepository.findAllByUpc(bikeOrderLine.getUpc());

        bikeInventoryList.forEach(bikeInventory -> {
            int inventory = (bikeInventory.getQuantity() == null) ? 0 : bikeInventory.getQuantity();
            int orderQty = (bikeOrderLine.getOrderQuantity() == null) ? 0 : bikeOrderLine.getOrderQuantity();
            int allocatedQty = (bikeOrderLine.getAllocatedQuantity() == null) ? 0 : bikeOrderLine.getAllocatedQuantity();
            int qtyToAllocate = orderQty - allocatedQty;

            if (inventory >= qtyToAllocate) { // full allocation
                inventory = inventory - qtyToAllocate;
                bikeOrderLine.setAllocatedQuantity(orderQty);
                bikeInventory.setQuantity(inventory);

                bikeInventoryRepository.save(bikeInventory);
            } else if (inventory > 0) { //partial allocation
                bikeOrderLine.setAllocatedQuantity(allocatedQty + inventory);
                bikeInventory.setQuantity(0);

            }

            if (bikeInventory.getQuantity() == 0) {
                bikeInventoryRepository.delete(bikeInventory);
            }
        });

    }
}
