package com.example.shipment.repository;

import com.example.shipment.entity.ShipmentCheckOutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentCheckOutRepository extends JpaRepository<ShipmentCheckOutEntity, Long> {
}
