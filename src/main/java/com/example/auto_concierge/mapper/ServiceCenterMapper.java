package com.example.auto_concierge.mapper;

import com.example.auto_concierge.dto.serviceCenter.ServiceCenterDTO;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public interface ServiceCenterMapper {

    ServiceCenterMapper INSTANCE = Mappers.getMapper(ServiceCenterMapper.class);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)     // Ignore owner mapping
    ServiceCenter serviceCenterDtoToServiceCenter(ServiceCenterDTO serviceCenterDTO);

    @Mapping(target = "id", ignore = true) // Игнорируем маппинг поля id
    @Mapping(target = "owner", source = "owner.id")
    ServiceCenterDTO serviceCenterToServiceCenterDto(ServiceCenter serviceCenter);


}
