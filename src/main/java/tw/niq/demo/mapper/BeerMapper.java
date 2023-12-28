package tw.niq.demo.mapper;

import org.mapstruct.Mapper;

import tw.niq.demo.dto.BeerDto;
import tw.niq.demo.entity.Beer;

@Mapper
public interface BeerMapper {

	Beer toEntity(BeerDto dto);
	
	BeerDto toDto(Beer entity);
	
}
