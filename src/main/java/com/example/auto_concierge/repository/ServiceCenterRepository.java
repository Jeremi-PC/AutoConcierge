package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.serviceCenter.Address;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceCenterRepository extends JpaRepository <ServiceCenter, Long> {
    boolean existsByAddress(Address address);
}
