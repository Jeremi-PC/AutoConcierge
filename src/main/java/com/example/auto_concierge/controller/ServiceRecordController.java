package com.example.auto_concierge.controller;


import com.example.auto_concierge.dto.serviceRecord.ServiceRecordDTO;
import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import com.example.auto_concierge.service.ServiceRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ServiceRecordController {
    private final ServiceRecordService serviceRecordService;
    @Autowired
    public ServiceRecordController(ServiceRecordService serviceRecordService) {
        this.serviceRecordService = serviceRecordService;
    }

    @PostMapping("/service-records/create")
    public ServiceRecord createAppointment(@RequestBody ServiceRecordDTO serviceRecordDTO) {
        return serviceRecordService.createServiceRecord(serviceRecordDTO);
    }
    @GetMapping("/service-records/{id}")
    public ResponseEntity<ServiceRecord> getServiceRecordById(@PathVariable Long id) {
        ServiceRecord serviceRecord = serviceRecordService.getServiceRecord(id);
        if (serviceRecord != null) {
            return ResponseEntity.ok(serviceRecord);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
