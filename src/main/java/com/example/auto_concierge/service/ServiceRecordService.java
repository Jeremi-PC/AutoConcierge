package com.example.auto_concierge.service;

import com.example.auto_concierge.dto.serviceRecord.ServiceRecordDTO;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import com.example.auto_concierge.mapper.CarMapper;
import com.example.auto_concierge.mapper.ServiceCenterMapper;
import com.example.auto_concierge.mapper.ServiceRecordMapper;
import com.example.auto_concierge.repository.ServiceRecordRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Optional;

@Service
@Transactional
public class ServiceRecordService {

        private final ServiceRecordRepository serviceRecordRepository;
        private final CarService carService;
        private final ServiceCenterService serviceCenterService;
        private final ServiceRecordMapper serviceRecordMapper;

        public ServiceRecordService(ServiceRecordRepository serviceRecordRepository, CarService carService, ServiceCenterService serviceCenterService, ServiceRecordMapper serviceRecordMapper) {
            this.serviceRecordRepository = serviceRecordRepository;
            this.carService = carService;
            this.serviceCenterService = serviceCenterService;
            this.serviceRecordMapper = serviceRecordMapper;
        }

    public ServiceRecord createServiceRecord(ServiceRecordDTO serviceRecordDTO) {
        Car car = carService.getCarById(serviceRecordDTO.carId());
        ServiceCenter serviceCenter = serviceCenterService.getServiceCenterById(serviceRecordDTO.serviceCenterId());
        if (car != null && serviceCenter != null) {
            ServiceRecord serviceRecord = serviceRecordMapper.serviceRecordDtoToServiceRecord(serviceRecordDTO);
            serviceRecord.setCar(car);
            serviceRecord.setServiceCenter(serviceCenter);
            serviceRecord.setCreatingTime(ZonedDateTime.now());
            return serviceRecordRepository.save(serviceRecord);
        } else {
            throw new RuntimeException("Car or Service Center not found");
        }
    }

    @Transactional
    public Optional<ServiceRecord> getServiceRecord(Long id) {
        return serviceRecordRepository.findById(id);
    }
}