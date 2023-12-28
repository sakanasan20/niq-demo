package tw.niq.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tw.niq.demo.dto.BeerDto;

public interface BeerService {
	
	List<BeerDto> getBeers();

	BeerDto getBeerById(UUID id);

	BeerDto createBeer(BeerDto beer);

	Optional<BeerDto> updateBeerById(UUID id, BeerDto beer);

	Optional<BeerDto> patchBeerById(UUID id, BeerDto beer);

	Boolean deleteBeerById(UUID id);
	
}
