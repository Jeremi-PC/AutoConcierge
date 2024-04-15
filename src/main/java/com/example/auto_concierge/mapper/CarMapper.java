package com.example.auto_concierge.mapper;

import com.example.auto_concierge.dto.car.CarDTO;
import com.example.auto_concierge.entity.car.Car;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CarMapper {

    CarMapper INSTANCE = Mappers.getMapper(CarMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)     // Ignore owner mapping
    Car carDtoToCar(CarDTO carDTO);

    @Mapping(target = "ownerId", ignore = true)      // Ignore owner mapping
    CarDTO carToCarDto(Car car);
}
