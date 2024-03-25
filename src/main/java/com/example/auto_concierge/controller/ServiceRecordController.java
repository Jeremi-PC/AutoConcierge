package com.example.auto_concierge.controller;

import com.example.auto_concierge.dto.ServiceRecordDto;
import com.example.auto_concierge.entity.Car;
import com.example.auto_concierge.entity.ServiceCenter;
import com.example.auto_concierge.entity.ServiceRecord;
import com.example.auto_concierge.entity.Status;
import com.example.auto_concierge.service.CarService;
import com.example.auto_concierge.service.ServiceCenterService;
import com.example.auto_concierge.service.ServiceRecordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
public class ServiceRecordController {
    private final ServiceRecordService serviceRecordService;
    private final CarService carService;
    private final ServiceCenterService serviceCenterService;

    public ServiceRecordController(ServiceRecordService serviceRecordService, CarService carService, ServiceCenterService serviceCenterService) {
        this.serviceRecordService = serviceRecordService;
        this.carService = carService;
        this.serviceCenterService = serviceCenterService;
    }


    @PostMapping("car/{carId}/service-center/{serviceCenterId}/create")
    public ServiceRecord createAppointment(
                                           @PathVariable Long carId,
                                           @PathVariable Long serviceCenterId,
                                           @RequestBody ServiceRecordDto serviceRecordDto) {
        return serviceRecordService.createServiceRecord(carId, serviceCenterId, serviceRecordDto);
    }
}
