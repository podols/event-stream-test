package com.example.checkout.repository;

import com.example.checkout.entity.CheckOutEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CheckOutRepository extends JpaRepository<CheckOutEntity, Long> {
}
