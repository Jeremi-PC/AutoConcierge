package com.example.auto_concierge.controller;

import com.example.auto_concierge.dto.ServiceRecordDto;
import com.example.auto_concierge.entity.ServiceRecord;
import com.example.auto_concierge.service.ServiceRecordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/record")
public class ServiceRecordController {
    private final ServiceRecordService serviceRecordService;

    public ServiceRecordController(ServiceRecordService serviceRecordService) {
        this.serviceRecordService = serviceRecordService;
    }
//    @PostMapping("/create")
//    public ServiceRecord createAppointment(@PathVariable Long userId, @RequestBody ServiceRecordDto serviceRecordDto) {
//        return serviceRecordService.createServiceRecord(userId, serviceRecordDto);
//    }
}
