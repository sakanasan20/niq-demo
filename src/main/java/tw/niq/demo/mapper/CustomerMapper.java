package tw.niq.demo.mapper;

import org.mapstruct.Mapper;

import tw.niq.demo.dto.CustomerDto;
import tw.niq.demo.entity.Customer;

@Mapper
public interface CustomerMapper {

	Customer toEntity(CustomerDto dto);
	
	CustomerDto toDto(Customer entity);
	
}
