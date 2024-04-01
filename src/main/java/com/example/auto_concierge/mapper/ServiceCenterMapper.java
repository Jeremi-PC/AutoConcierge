package com.example.auto_concierge.mapper;

import com.example.auto_concierge.dto.serviceCenter.ServiceCenterDTO;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceCenterMapper {

    ServiceCenterMapper INSTANCE = Mappers.getMapper(ServiceCenterMapper.class);

    ServiceCenter serviceCenterDtoToServiceCenter(ServiceCenterDTO serviceCenterDTO);

    @Mapping(target = "id", ignore = true) // Игнорируем маппинг поля id
    ServiceCenterDTO serviceCenterToServiceCenterDto(ServiceCenter serviceCenter);
}
