package com.example.auto_concierge.service;

import com.example.auto_concierge.dto.serviceCenter.ServiceCenterDTO;
import com.example.auto_concierge.dto.serviceRecord.ServiceRecordDTO;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import com.example.auto_concierge.entity.serviceRecord.Status;

import com.example.auto_concierge.mapper.CarMapper;
import com.example.auto_concierge.mapper.ServiceCenterMapper;
import com.example.auto_concierge.mapper.ServiceRecordMapper;
import com.example.auto_concierge.repository.ServiceRecordRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
@Transactional
public class ServiceRecordService {

        private final ServiceRecordRepository serviceRecordRepository;
        private final CarService carService;
        private final ServiceCenterService serviceCenterService;
        private final ServiceRecordMapper serviceRecordMapper;
        private final CarMapper carMapper;
        private final ServiceCenterMapper serviceCenterMapper;

        public ServiceRecordService(ServiceRecordRepository serviceRecordRepository, CarService carService, ServiceCenterService serviceCenterService, ServiceRecordMapper serviceRecordMapper, CarMapper carMapper, ServiceCenterMapper serviceCenterMapper) {
            this.serviceRecordRepository = serviceRecordRepository;
            this.carService = carService;
            this.serviceCenterService = serviceCenterService;
            this.serviceRecordMapper = serviceRecordMapper;
            this.carMapper = carMapper;
            this.serviceCenterMapper = serviceCenterMapper;
        }

    public ServiceRecord createServiceRecord(ServiceRecordDTO serviceRecordDTO) {
        Car car = carService.getCarById(serviceRecordDTO.carId());
        ServiceCenter serviceCenter = serviceCenterService.getServiceCenterById(serviceRecordDTO.serviceCenterId());
        ServiceCenterDTO serviceCenterDTO = serviceCenterService.getServiceCenterDTOById(serviceRecordDTO.serviceCenterId());

        if (car != null && serviceCenterDTO != null) {
            ServiceRecord serviceRecord = serviceRecordMapper.serviceRecordDtoToServiceRecord(serviceRecordDTO);
            serviceRecord.setCar(car);
            serviceRecord.setServiceCenter(serviceCenter);
            serviceRecord.setServices(serviceRecordDTO.services());
            serviceRecord.setCreatingTime(ZonedDateTime.now());
            serviceRecord.setStatus(Status.CREATED);

            return serviceRecordRepository.save(serviceRecord);
        } else {
            throw new RuntimeException("Car or Service Center not found");
        }
    }


    public ServiceRecord getServiceRecord(Long id) {
        return serviceRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ServiceRecord not found with id: " + id));
    }

}