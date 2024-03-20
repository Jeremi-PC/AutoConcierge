package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.Address;
import com.example.auto_concierge.entity.ServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCenterRepository extends JpaRepository <ServiceCenter, Long> {
    boolean existsByAddress(Address address);
}
