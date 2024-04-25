package com.example.auto_concierge.service;

import com.example.auto_concierge.dto.serviceCenter.ServiceCenterDTO;
import com.example.auto_concierge.dto.serviceRecord.ServiceRecordDTO;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import com.example.auto_concierge.entity.partsOrder.Status;

import com.example.auto_concierge.exception.NotFoundException;
import com.example.auto_concierge.mapper.ServiceRecordMapper;
import com.example.auto_concierge.repository.ServiceRecordRepository;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
            throw new NotFoundException("Car or Service Center not found");
        }
    }
    public ServiceRecordDTO getServiceRecord(Long id) {
        return serviceRecordRepository.findById(id)
                .map(serviceRecordMapper::serviceRecordToServiceRecordDto)
                .orElseThrow(() -> new NotFoundException("Запись на обслуживание с указаным id: " + id + " не найден"));
    }
    public List<ServiceRecordDTO> getAllServiceRecords () {
        List<ServiceRecord> serviceRecords = serviceRecordRepository.findAll();
        if (serviceRecords.isEmpty()) {
            throw new NotFoundException("Записи на обслуживание не найдены");
        }
        return serviceRecords.stream()
                .map(serviceRecordMapper::serviceRecordToServiceRecordDto)
                .collect(Collectors.toList());
    }
    public ServiceRecordDTO updateServiceRecord(Long id, ServiceRecordDTO serviceRecordDTO) {
        ServiceRecord serviceRecord = serviceRecordRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Сервисная запись с идентификатором " + id + " не найдена"));
        Car car = carService.getCarById(serviceRecordDTO.carId());
        ServiceCenter serviceCenter = serviceCenterService.getServiceCenterById(serviceRecordDTO.serviceCenterId());
        serviceRecord.setCar(car);
        serviceRecord.setServiceCenter(serviceCenter);
        serviceRecord.setServiceType(serviceRecordDTO.serviceType());
        serviceRecord.setAppointmentDateTime(serviceRecordDTO.appointmentDateTime());
        serviceRecord.setServices(serviceRecordDTO.services());
        serviceRecord.setStatus(serviceRecordDTO.status());

        ServiceRecord updatedServiceRecord = serviceRecordRepository.save(serviceRecord);
        return serviceRecordMapper.serviceRecordToServiceRecordDto(updatedServiceRecord);
    }

    public void deleteServiceRecord(Long id) {
        serviceRecordRepository.findById(id).ifPresentOrElse(
                serviceRecord -> serviceRecordRepository.deleteById(id),
                () -> {
                    throw new NotFoundException("Сервисная запись с идентификатором " + id + " не найдена");
                }
        );
    }
}