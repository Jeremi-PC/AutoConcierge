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

    public ServiceRecord createServiceRecord(Long userId, Long carId, Long serviceCenterId, ServiceRecordDto serviceRecordDto) {
        Car car = carService.getCarByUserIdAndCarId(userId, carId);
        ServiceCenter serviceCenter = serviceCenterService.getServiceCenterById(serviceCenterId);
        if (car != null && serviceCenter != null) {
            ServiceRecord serviceRecord = new ServiceRecord(car, serviceCenter, serviceRecordDto.getDateTime(), serviceRecordDto.getServices());
            // Заполнение данных из DTO или других параметров
            // serviceRecord.setSomeField(serviceRecordDTO.getSomeField());
            // serviceRecord.setSomeOtherField(serviceRecordDTO.getSomeOtherField());
            serviceRecord.setCar(car);
            serviceRecord.setServiceCenter(serviceCenter);
            // Сохранение записи обслуживания
            return serviceRecordRepository.save(serviceRecord);
        } else {
            throw new RuntimeException("Car or Service Center not found");
        }
    }

    // Другие методы для обновления, удаления и получения записей обслуживания
}