package com.bike.inventory.services;

import com.bike.common.BikeOrderDto;

public interface AllocationService {
    Boolean allocateOrder(BikeOrderDto bikeOrderDto);

    void deallocateOrder(BikeOrderDto bikeOrderDto);
}
