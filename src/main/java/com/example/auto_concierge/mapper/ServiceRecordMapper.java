package com.example.auto_concierge.mapper;

import com.example.auto_concierge.dto.serviceRecord.ServiceRecordDTO;
import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ServiceRecordMapper {

    ServiceRecordMapper INSTANCE = Mappers.getMapper(ServiceRecordMapper.class);

    ServiceRecord serviceRecordDtoToServiceRecord(ServiceRecordDTO serviceRecordDTO);

    ServiceRecordDTO serviceRecordToServiceRecordDto(ServiceRecord serviceRecord);
}
