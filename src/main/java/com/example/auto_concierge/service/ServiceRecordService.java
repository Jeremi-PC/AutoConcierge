package com.example.auto_concierge.service;

import com.example.auto_concierge.dto.ServiceRecordDto;
import com.example.auto_concierge.entity.Car;
import com.example.auto_concierge.entity.ServiceCenter;
import com.example.auto_concierge.entity.ServiceRecord;
import com.example.auto_concierge.repository.ServiceRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class ServiceRecordService {

    private final ServiceRecordRepository serviceRecordRepository;
    private final CarService carService;
    private final ServiceCenterService serviceCenterService;

    @Autowired
    public ServiceRecordService(ServiceRecordRepository serviceRecordRepository,
                                CarService carService,
                                ServiceCenterService serviceCenterService) {
        this.serviceRecordRepository = serviceRecordRepository;
        this.carService = carService;
        this.serviceCenterService = serviceCenterService;
    }

    public ServiceRecord createServiceRecord(Long carId, Long serviceCenterId, ServiceRecordDto serviceRecordDto) {
        Car car = carService.getOneCar(carId);
        ServiceCenter serviceCenter = serviceCenterService.getServiceCenterById(serviceCenterId);
        if (car != null && serviceCenter != null) {
            ServiceRecord serviceRecord = new ServiceRecord(
                    car,
                    serviceCenter,
                    serviceRecordDto.getServiceType(),
                    serviceRecordDto.getAppoitmentDateTime(),
                    serviceRecordDto.getServices()
);

            // Сохранение записи обслуживания
            return serviceRecordRepository.save(serviceRecord);
        } else {
            throw new RuntimeException("Car or Service Center not found");
        }
    }

    // Другие методы для обновления, удаления и получения записей обслуживания
}