package tw.niq.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tw.niq.demo.domain.Beer;
import tw.niq.demo.domain.BeerStyle;

@Service
public class BeerServiceImpl implements BeerService {

	private Map<UUID, Beer> beerMap;

	public BeerServiceImpl() {
		
		this.beerMap = new HashMap<>();

		Beer beer1 = Beer.builder()
				.id(UUID.randomUUID())
				.version(1)
				.beerName("Galaxy Cat")
				.beerStyle(BeerStyle.PALE_ALE)
				.upc("12356")
				.price(new BigDecimal("12.99"))
				.quantityOnHand(122)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		Beer beer2 = Beer.builder()
				.id(UUID.randomUUID())
				.version(1)
				.beerName("Crank")
				.beerStyle(BeerStyle.PALE_ALE)
				.upc("12356222")
				.price(new BigDecimal("11.99"))
				.quantityOnHand(392)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		Beer beer3 = Beer.builder()
				.id(UUID.randomUUID())
				.version(1)
				.beerName("Sunshine City")
				.beerStyle(BeerStyle.IPA)
				.upc("12356")
				.price(new BigDecimal("13.99"))
				.quantityOnHand(144)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		beerMap.put(beer1.getId(), beer1);
		beerMap.put(beer2.getId(), beer2);
		beerMap.put(beer3.getId(), beer3);
	}

	@Override
	public List<Beer> listBeers() {
		
		return new ArrayList<>(beerMap.values());
	}

	@Override
	public Beer getBeerById(UUID id) {
		
		return beerMap.get(id);
	}

	@Override
	public Beer saveNewBeer(Beer beer) {
		
		Beer savedBeer = Beer.builder()
				.id(UUID.randomUUID())
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.beerName(beer.getBeerName())
				.beerStyle(beer.getBeerStyle())
				.quantityOnHand(beer.getQuantityOnHand())
				.upc(beer.getUpc())
				.price(beer.getPrice())
				.build();

		beerMap.put(savedBeer.getId(), savedBeer);

		return savedBeer;
	}

	@Override
	public void updateBeerById(UUID id, Beer beer) {
		
		Beer existing = beerMap.get(id);

		existing.setBeerName(beer.getBeerName());
		existing.setPrice(beer.getPrice());
		existing.setUpc(beer.getUpc());
		existing.setQuantityOnHand(beer.getQuantityOnHand());
	}

	@Override
	public void patchBeerById(UUID id, Beer beer) {
		
		Beer existing = beerMap.get(id);

		if (StringUtils.hasText(beer.getBeerName())) {
			existing.setBeerName(beer.getBeerName());
		}

		if (beer.getBeerStyle() != null) {
			existing.setBeerStyle(beer.getBeerStyle());
		}

		if (beer.getPrice() != null) {
			existing.setPrice(beer.getPrice());
		}

		if (beer.getQuantityOnHand() != null) {
			existing.setQuantityOnHand(beer.getQuantityOnHand());
		}

		if (StringUtils.hasText(beer.getUpc())) {
			existing.setUpc(beer.getUpc());
		}
	}

	@Override
	public void deleteById(UUID id) {
		beerMap.remove(id);
	}

}
