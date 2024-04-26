package com.example.auto_concierge.service;

import com.example.auto_concierge.dto.serviceCenter.ServiceCenterDTO;
import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.exception.DuplicateItemException;
import com.example.auto_concierge.exception.InsufficientPermissionException;
import com.example.auto_concierge.exception.NotFoundException;
import com.example.auto_concierge.mapper.ServiceCenterMapper;
import com.example.auto_concierge.repository.ServiceCenterRepository;
import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class ServiceCenterService {
    // Радиус Земли в километрах
    private final ServiceCenterRepository serviceCenterRepository;
    private final ServiceCenterMapper serviceCenterMapper;
    private static final double earthRadius = 6371.0;
    private static final double unknownKoefficient = 6371.0;

    private final UserRepository userRepository;

    @Autowired
    public ServiceCenterService(ServiceCenterRepository serviceCenterRepository, ServiceCenterMapper serviceCenterMapper, UserRepository userRepository) {
        this.serviceCenterRepository = serviceCenterRepository;
        this.serviceCenterMapper = serviceCenterMapper;
        this.userRepository = userRepository;
    }

    public ServiceCenter createServiceCenter(Long userId, ServiceCenter serviceCenter) {
        return userRepository.findById(userId)
                .map(user -> {
                    if (user.getRole() != Role.SERVICE_CENTER) {
                        throw new InsufficientPermissionException("Недостаточно прав для создания сервис центра");
                    }
                    if (serviceCenterRepository.existsByAddress(serviceCenter.getAddress())) {
                        throw new DuplicateItemException("СТО с таким адресом уже существует");
                    }
                    serviceCenter.setOwner(user);
                    return serviceCenterRepository.save(serviceCenter);
                })
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
    }


    public List<ServiceCenterDTO> getAllServiceCenters() {
        List<ServiceCenter> serviceCenters = serviceCenterRepository.findAll();
        if (!serviceCenters.isEmpty()) {
            return serviceCenters.stream()
                    .map(serviceCenterMapper.INSTANCE::serviceCenterToServiceCenterDto)
                    .collect(Collectors.toList());
        }else {
            throw new NotFoundException("Сервисные центры не найдены");
        }
    }


    public ServiceCenterDTO getServiceCenterDTOById(Long id) {
        return serviceCenterRepository.findById(id)
                .map(serviceCenterMapper::serviceCenterToServiceCenterDto)
                .orElseThrow(() -> new NotFoundException("Сервисный центр с идентификатором " + id + " не найден"));
    }
    public ServiceCenter getServiceCenterById(Long id) {
        return serviceCenterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Сервисный центр с идентификатором " + id + " не найден"));
    }

    public ServiceCenter updateServiceCenter(Long id, ServiceCenter serviceCenterDetails) {
        return serviceCenterRepository.findById(id)
                .map(serviceCenter -> {
                    serviceCenter.setName(serviceCenterDetails.getName());
                    serviceCenter.setAddress(serviceCenterDetails.getAddress());
                    serviceCenter.setContactNumber(serviceCenterDetails.getContactNumber());
                    serviceCenter.setWebsite(serviceCenterDetails.getWebsite());
                    serviceCenter.setAverageRating(serviceCenterDetails.getAverageRating());
                    serviceCenter.setSchedule(serviceCenterDetails.getSchedule());
                    return serviceCenterRepository.save(serviceCenter);
                })
                .orElseThrow(() -> new IllegalArgumentException("Сервисный центр с идентификатором " + id + " не найден"));
    }


    public void deleteServiceCenter(Long id) {
        serviceCenterRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Сервисный центр с идентификатором " + id + " не найден"));
        try {
            serviceCenterRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении сервисного центра с идентификатором " + id + ": " + e.getMessage(), e);
        }
    }

    public List<ServiceCenter> findServiceCentersWithinRadius(double latitude, double longitude, double radius) {
        double radiusInKm = radius * unknownKoefficient;

        List<ServiceCenter> serviceCentersWithinRadius = serviceCenterRepository.findAll().stream()
                .filter(serviceCenter -> {
                    double distance = getDistance(latitude, longitude, serviceCenter);
                    return distance <= radiusInKm;
                })
                .collect(Collectors.toList());
        if (serviceCentersWithinRadius.isEmpty()) {
            throw new NotFoundException("Сервисные центры не найдены в указанном радиусе");
        }
        return serviceCentersWithinRadius;
    }

    private static double getDistance(double latitude, double longitude, ServiceCenter serviceCenter) {
        double serviceCenterLat = Math.toRadians(serviceCenter.getAddress().getLatitude());
        double serviceCenterLon = Math.toRadians(serviceCenter.getAddress().getLongitude());
        double deltaLat = Math.toRadians(latitude - serviceCenterLat);
        double deltaLon = Math.toRadians(longitude - serviceCenterLon);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(serviceCenterLat) * Math.cos(latitude) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadius * c;
        return distance;
    }
}

