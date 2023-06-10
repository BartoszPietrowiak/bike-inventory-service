package com.bike.common.events;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NewInventoryEvent extends BikeEvent {
    public NewInventoryEvent(BikeDto bikeDto) {
        super(bikeDto);
    }
}
