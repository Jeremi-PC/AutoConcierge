package com.example.auto_concierge.controller.serviceCenter;

import com.example.auto_concierge.entity.ServiceCenter;
import com.example.auto_concierge.service.ServiceCenterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/service-centers")
public class GeneralServiceCenterController {

    private final ServiceCenterService serviceCenterService;

    @Autowired
    public GeneralServiceCenterController(ServiceCenterService serviceCenterService) {
        this.serviceCenterService = serviceCenterService;
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