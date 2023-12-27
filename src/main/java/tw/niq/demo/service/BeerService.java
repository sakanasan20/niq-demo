package tw.niq.demo.service;

import java.util.List;
import java.util.UUID;

import tw.niq.demo.dto.BeerDto;

public interface BeerService {
	
	List<BeerDto> getBeers();

	BeerDto getBeerById(UUID id);

	BeerDto createBeer(BeerDto beer);

	void updateBeerById(UUID id, BeerDto beer);

	void patchBeerById(UUID id, BeerDto beer);

	void deleteBeerById(UUID id);
	
}
