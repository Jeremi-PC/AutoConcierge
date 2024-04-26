package com.example.auto_concierge.controller;


import com.example.auto_concierge.dto.serviceRecord.ServiceRecordDTO;
import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import com.example.auto_concierge.service.ServiceRecordService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class ServiceRecordController {
    private final ServiceRecordService serviceRecordService;
    @Autowired
    public ServiceRecordController(ServiceRecordService serviceRecordService) {
        this.serviceRecordService = serviceRecordService;
    }

    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @PostMapping("/service-records/create")
    public ServiceRecord createAppointment(@RequestBody ServiceRecordDTO serviceRecordDTO) {
        return serviceRecordService.createServiceRecord(serviceRecordDTO);
    }
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @GetMapping("/service-records/{id}")
    public ServiceRecordDTO getServiceRecordById(@PathVariable Long id) {
        return serviceRecordService.getServiceRecord(id);
    }
    @RolesAllowed(value = {"ADMIN"})
    @GetMapping("/service-records")
    public List<ServiceRecordDTO> getAllServiceRecord() {
        return serviceRecordService.getAllServiceRecords();
    }
    @RolesAllowed(value = {"CLIENT","ADMIN"})
    @PutMapping("/service-records/{id}")
    public ServiceRecordDTO updateServiceRecordById(@PathVariable Long id, @RequestBody ServiceRecordDTO serviceRecordDTO) {
        return serviceRecordService.updateServiceRecord(id, serviceRecordDTO);
    }
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @DeleteMapping("/service-records/{id}")
    public void deleteServiceRecord (@PathVariable Long id) {
        serviceRecordService.deleteServiceRecord(id);
    }
}
