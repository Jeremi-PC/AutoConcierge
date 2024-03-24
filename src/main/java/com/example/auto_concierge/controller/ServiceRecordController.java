package com.example.auto_concierge.controller;

import com.example.auto_concierge.dto.ServiceRecordDto;
import com.example.auto_concierge.entity.Car;
import com.example.auto_concierge.entity.ServiceCenter;
import com.example.auto_concierge.entity.ServiceRecord;
import com.example.auto_concierge.service.CarService;
import com.example.auto_concierge.service.ServiceCenterService;
import com.example.auto_concierge.service.ServiceRecordService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{userId}/car/{carId}/service-center/{serviceCenterId}")
public class ServiceRecordController {
    private final ServiceRecordService serviceRecordService;
    private final CarService carService;
    private final ServiceCenterService serviceCenterService;

    public ServiceRecordController(ServiceRecordService serviceRecordService, CarService carService, ServiceCenterService serviceCenterService) {
        this.serviceRecordService = serviceRecordService;
        this.carService = carService;
        this.serviceCenterService = serviceCenterService;
    }


    @PostMapping("/create")
    public ServiceRecord createAppointment(@PathVariable Long userId,
                                           @PathVariable Long carId,
                                           @PathVariable Long serviceCenterId,
                                           @RequestBody ServiceRecordDto serviceRecordDto) {
        Car car = carService.getCarByUserIdAndCarId(userId, carId);
        ServiceCenter serviceCenter = serviceCenterService.getServiceCenterById(serviceCenterId);

        ServiceRecord serviceRecord = new ServiceRecord(car, serviceCenter, serviceRecordDto.getDateTime(), serviceRecordDto.getServices());

        return serviceRecordService.createServiceRecord(serviceRecord);
    }
}
