package com.example.auto_concierge.controller.serviceCenter;

import com.example.auto_concierge.controller.UserHolder;
import com.example.auto_concierge.service.ServiceCenterService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.user.User;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
@RequestMapping("/service_center")
public class ServiceCenterController {

    private final ServiceCenterService serviceCenterService;
    private final UserHolder userHolder;

    @Autowired
    public ServiceCenterController(ServiceCenterService serviceCenterService, UserHolder userHolder) {
        this.serviceCenterService = serviceCenterService;
        this.userHolder = userHolder;
    }

    @RolesAllowed({"ADMIN", "CLIENT", "SERVICE_CENTER"})
    @GetMapping("/add_service_center")
    public String getAddServiceCenterPage(Model model) {
        model.addAttribute("serviceCenter", new ServiceCenter());
        return "add_service_center";
    }

    @RolesAllowed({"ADMIN", "CLIENT", "SERVICE_CENTER"})
    @PostMapping("/add_service_center")
    public String addServiceCenter(@ModelAttribute("serviceCenter") ServiceCenter serviceCenter) {
        User owner = userHolder.getUserFromPrincipal();
        serviceCenterService.createServiceCenter(owner.getId(), serviceCenter);
        return "redirect:/user/profile";
    }
}
