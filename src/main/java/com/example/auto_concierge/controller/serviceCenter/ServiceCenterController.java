package com.example.auto_concierge.controller.serviceCenter;

import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.service.ServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/service-centers")
public class ServiceCenterController {

    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ServiceCenterController(ServiceCenterService serviceCenterService) {
        this.serviceCenterService = serviceCenterService;
    }

    @PostMapping("/create")
    public ServiceCenter createServiceCenter(@PathVariable Long userId, @RequestBody ServiceCenter serviceCenter) {
        return serviceCenterService.createServiceCenter(userId, serviceCenter);
    }

    @GetMapping
    public List<ServiceCenter> getAllServiceCenters() {
        return serviceCenterService.getAllServiceCenters();
    }

    @GetMapping("/{serviceCenterId}")
    public ServiceCenter getServiceCenterById(@PathVariable Long serviceCenterId) {
        return serviceCenterService.getServiceCenterById(serviceCenterId);
    }

    @PutMapping("/{serviceCenterId}")
    public ServiceCenter updateServiceCenter(@PathVariable Long serviceCenterId, @RequestBody ServiceCenter serviceCenterDetails) {
        return serviceCenterService.updateServiceCenter(serviceCenterId, serviceCenterDetails);
    }

    @DeleteMapping("/{serviceCenterId}")
    public void deleteServiceCenter(@PathVariable Long serviceCenterId) {
        serviceCenterService.deleteServiceCenter(serviceCenterId);
    }
    @GetMapping("/within-radius")
    public ResponseEntity<List<ServiceCenter>> findServiceCentersWithinRadius(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("radius") double radius) {
        List<ServiceCenter> serviceCenters = serviceCenterService.findServiceCentersWithinRadius(latitude, longitude, radius);
        return ResponseEntity.ok(serviceCenters);
    }
}
