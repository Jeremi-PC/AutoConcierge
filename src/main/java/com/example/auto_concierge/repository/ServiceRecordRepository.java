package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRecordRepository extends JpaRepository <ServiceRecord, Long> {
}
