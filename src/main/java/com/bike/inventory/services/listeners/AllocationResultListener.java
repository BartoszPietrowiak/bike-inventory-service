package com.bike.inventory.services.listeners;

import com.bike.common.events.AllocateOrderRequest;
import com.bike.common.events.AllocateOrderResponse;
import com.bike.inventory.config.JmsConfig;
import com.bike.inventory.services.AllocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AllocationResultListener {
    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen(AllocateOrderRequest request) {
        AllocateOrderResponse.AllocateOrderResponseBuilder builder = AllocateOrderResponse.builder();
        builder.bikeOrderDto(request.getBikeOrderDto());

        try {
            Boolean allocationResult = allocationService.allocateOrder(request.getBikeOrderDto());

            if (Boolean.TRUE.equals(allocationResult)) {
                builder.inventoryPending(false);
            } else {
                builder.inventoryPending(true);
            }
            builder.allocationError(false);
        } catch (Exception e) {
            log.error("Allocation failed for Order Id:" + request.getBikeOrderDto().getId());
            builder.allocationError(true);
        }

        jmsTemplate.convertAndSend(JmsConfig.ALLOCATE_ORDER_RESULT_QUEUE, builder.build());
    }
}
