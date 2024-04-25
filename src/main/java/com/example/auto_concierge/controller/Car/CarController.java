package com.example.auto_concierge.controller.Car;

import com.example.auto_concierge.controller.UserHolder;
import com.example.auto_concierge.dto.car.CarDTO;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.service.CarService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/car")
public class CarController {

    private final CarService carService;
    private final UserHolder userHolder;

    @Autowired
    public CarController(CarService carService, UserHolder userHolder) {
        this.carService = carService;
        this.userHolder = userHolder;
    }
    @RolesAllowed({"ADMIN","CLIENT","SERVICE_CENTER"})
    @GetMapping("/add_car")
    public String getAddCarPage(Model model) {
        model.addAttribute("car", new Car());
        return "add_car";
    }
    @RolesAllowed({"ADMIN","CLIENT","SERVICE_CENTER"})
    @PostMapping("/add_car")
    public String addCar(@ModelAttribute("car") CarDTO carDTO) {
        User owner = userHolder.getUserFromPrincipal();
        carService.createCar(owner.getId(), carDTO);
        return "redirect:/user/profile";
    }
}
