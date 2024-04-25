package com.example.auto_concierge.controller;

import com.example.auto_concierge.dto.car.CarDTO;
import com.example.auto_concierge.dto.serviceCenter.ServiceCenterDTO;
import com.example.auto_concierge.entity.Phone;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.exception.NotFoundException;
import com.example.auto_concierge.service.CarService;
import com.example.auto_concierge.service.ServiceCenterService;
import com.example.auto_concierge.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {
    private final CarService carService;
    private final UserService userService;
    private final UserHolder userHolder;

    @Autowired
    public UserController(CarService carService, UserService userService, UserHolder userHolder) {
        this.carService = carService;
        this.userService = userService;
        this.userHolder = userHolder;
    }
    @RolesAllowed({"ADMIN","CLIENT","SERVICE_CENTER","PART_SUPPLIER"})
    @GetMapping("/profile")
    public String getUserProfile(Model model) {
        User user = userHolder.getUserFromPrincipal();
        List<CarDTO> cars;

        try {
            cars = carService.getCarDTOByUserId(user.getId());

        } catch (NotFoundException e) {
            cars = Collections.emptyList();
        }

        model.addAttribute("user", user);
        model.addAttribute("cars", cars);

        return "redirect:/user/profile";
    }
    @GetMapping("/add_phone")
    public String addContactNumber(){
        return "add_phone";
    }
    @PostMapping("/add_phone")
    @ResponseBody
    public String addPhoneNumber(
            @RequestParam("countryCode") String countryCode,
            @RequestParam("areaCode") String areaCode,
            @RequestParam("phoneNumber") String phoneNumber) {

        User user = userHolder.getUserFromPrincipal();

        Phone phone = new Phone();
        phone.setCountryCode(countryCode);
        phone.setAreaCode(areaCode);
        phone.setNumber(phoneNumber);
        List<Phone> phones = new ArrayList<>();
        phones.add(phone);
        user.setPhoneNumber(phones);

        userService.updateUser(user.getId(), user);

        return "user_profile";
    }

}
