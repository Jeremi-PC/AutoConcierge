package com.example.auto_concierge.service;

import com.example.auto_concierge.entity.Role;
import com.example.auto_concierge.entity.ServiceCenter;
import com.example.auto_concierge.entity.User;
import com.example.auto_concierge.repository.ServiceCenterRepository;
import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ServiceCenterService {

    private final ServiceCenterRepository serviceCenterRepository;
    private final UserRepository userRepository;

    @Autowired
    public ServiceCenterService(ServiceCenterRepository serviceCenterRepository, UserRepository userRepository) {
        this.serviceCenterRepository = serviceCenterRepository;
        this.userRepository = userRepository;
    }

    public ServiceCenter createServiceCenter(Long userId, ServiceCenter serviceCenter) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getRole() == Role.SERVICE_CENTER) {
            if (serviceCenterRepository.existsByAddress(serviceCenter.getAddress())) {
                throw new RuntimeException("СТО с таким адресом уже существует");
            }
            serviceCenter.setOwner(user);
            return serviceCenterRepository.save(serviceCenter);
        } else {
            throw new RuntimeException("Недостаточно прав для создания сервис центра или пользователь с указанным ID не найден");
        }
    }

    public List<ServiceCenter> getAllServiceCenters() {
        return serviceCenterRepository.findAll();
    }

    public ServiceCenter getServiceCenterById(Long id) {
        return serviceCenterRepository.findById(id).orElse(null);
    }

    public ServiceCenter updateServiceCenter(Long id, ServiceCenter serviceCenterDetails) {
        ServiceCenter serviceCenter = serviceCenterRepository.findById(id).orElse(null);
        if (serviceCenter != null) {
            serviceCenter.setName(serviceCenterDetails.getName());
            serviceCenter.setAddress(serviceCenterDetails.getAddress());
            serviceCenter.setContactNumber(serviceCenterDetails.getContactNumber());
            serviceCenter.setWebsite(serviceCenterDetails.getWebsite());
            serviceCenter.setAverageRating(serviceCenterDetails.getAverageRating());
            serviceCenter.setSchedule(serviceCenterDetails.getSchedule());
            return serviceCenterRepository.save(serviceCenter);
        } else {
            throw new RuntimeException("Service center not found");
        }
    }

    public void deleteServiceCenter(Long id) {
        serviceCenterRepository.deleteById(id);
    }
}
