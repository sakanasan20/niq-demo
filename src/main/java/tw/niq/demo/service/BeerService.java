package tw.niq.demo.service;

import java.util.List;
import java.util.UUID;

import tw.niq.demo.domain.Beer;

public interface BeerService {
	
	List<Beer> listBeers();

	Beer getBeerById(UUID id);
	
}