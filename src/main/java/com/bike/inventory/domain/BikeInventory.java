package com.bike.inventory.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BikeInventory extends BaseEntity {

    @Builder
    public BikeInventory(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, UUID bikeId,
                         String upc, Integer quantity) {
        super(id, version, createdDate, lastModifiedDate);
        this.bikeId = bikeId;
        this.upc = upc;
        this.quantity = quantity;
    }

    private UUID bikeId;
    private String upc;
    private Integer quantity = 0;
}
