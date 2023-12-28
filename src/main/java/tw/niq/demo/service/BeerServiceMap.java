package tw.niq.demo.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tw.niq.demo.dto.BeerDto;
import tw.niq.demo.entity.BeerStyle;

@Service
public class BeerServiceMap implements BeerService {

	private Map<UUID, BeerDto> beerMap;

	public BeerServiceMap() {
		
		this.beerMap = new HashMap<>();

		BeerDto beer1 = BeerDto.builder()
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

		BeerDto beer2 = BeerDto.builder()
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

		BeerDto beer3 = BeerDto.builder()
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
	public List<BeerDto> getBeers() {
		
		return new ArrayList<>(beerMap.values());
	}

	@Override
	public BeerDto getBeerById(UUID id) {
		
		return beerMap.get(id);
	}

	@Override
	public BeerDto createBeer(BeerDto beer) {
		
		BeerDto createdBeer = BeerDto.builder()
				.id(UUID.randomUUID())
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.beerName(beer.getBeerName())
				.beerStyle(beer.getBeerStyle())
				.quantityOnHand(beer.getQuantityOnHand())
				.upc(beer.getUpc())
				.price(beer.getPrice())
				.build();

		beerMap.put(createdBeer.getId(), createdBeer);

		return createdBeer;
	}

	@Override
	public Optional<BeerDto> updateBeerById(UUID id, BeerDto beer) {
		
		BeerDto existing = beerMap.get(id);

		existing.setBeerName(beer.getBeerName());
		existing.setPrice(beer.getPrice());
		existing.setUpc(beer.getUpc());
		existing.setQuantityOnHand(beer.getQuantityOnHand());
		
		AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();
		atomicReference.set(Optional.of(existing));
		
		return atomicReference.get();
	}

	@Override
	public Optional<BeerDto> patchBeerById(UUID id, BeerDto beer) {
		
		BeerDto existing = beerMap.get(id);

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
		
		AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();
		atomicReference.set(Optional.of(existing));
		
		return atomicReference.get();
	}

	@Override
	public Boolean deleteBeerById(UUID id) {
		beerMap.remove(id);
		return true;
	}

}
