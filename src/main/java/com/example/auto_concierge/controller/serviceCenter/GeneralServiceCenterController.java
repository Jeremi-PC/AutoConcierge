package com.example.auto_concierge.controller.serviceCenter;

import com.example.auto_concierge.dto.serviceCenter.ServiceCenterDTO;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.service.ServiceCenterService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/service-centers")
public class GeneralServiceCenterController {

    private final ServiceCenterService serviceCenterService;

    @Autowired
    public GeneralServiceCenterController(ServiceCenterService serviceCenterService) {
        this.serviceCenterService = serviceCenterService;
    }
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @GetMapping()
    public List<ServiceCenterDTO> getAllServiceCenters(){
        return serviceCenterService.getAllServiceCenters();
    }
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @GetMapping("/{serviceCenterId}")
    public ServiceCenterDTO getServiceCenterById(@PathVariable Long serviceCenterId) {
        return serviceCenterService.getServiceCenterDTOById(serviceCenterId);
    }
    @RolesAllowed(value = {"SERVICE_CENTER","ADMIN"})
    @DeleteMapping("/{serviceCenterId}")
    public void deleteServiceCenter(@PathVariable Long serviceCenterId) {
        serviceCenterService.deleteServiceCenter(serviceCenterId);
    }
    @RolesAllowed(value = {"CLIENT","PART_SUPPLIER","ADMIN"})
    @GetMapping("/within-radius")
    public ResponseEntity<List<ServiceCenter>> findServiceCentersWithinRadius(
            @RequestParam("latitude") double latitude,
            @RequestParam("longitude") double longitude,
            @RequestParam("radius") double radius) {
        List<ServiceCenter> serviceCenters = serviceCenterService.findServiceCentersWithinRadius(latitude, longitude, radius);
        return ResponseEntity.ok(serviceCenters);
    }
}