package tw.niq.demo.service;

import java.util.List;
import java.util.UUID;

import tw.niq.demo.domain.Beer;

public interface BeerService {
	
	List<Beer> getBeers();

	Beer getBeerById(UUID id);

	Beer createBeer(Beer beer);

	void updateBeerById(UUID id, Beer beer);

	void patchBeerById(UUID id, Beer beer);

	void deleteBeerById(UUID id);
	
}
