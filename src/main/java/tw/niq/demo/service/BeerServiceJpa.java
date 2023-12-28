package tw.niq.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import tw.niq.demo.dto.BeerDto;
import tw.niq.demo.entity.Beer;
import tw.niq.demo.exception.NotFoundException;
import tw.niq.demo.mapper.BeerMapper;
import tw.niq.demo.repository.BeerRepository;

@Primary
@Service
@RequiredArgsConstructor
public class BeerServiceJpa implements BeerService {

	private final BeerRepository beerRepository;
	private final BeerMapper beerMapper;

	@Override
	public List<BeerDto> getBeers() {
		
		return beerRepository.findAll()
				.stream()
				.map(beerMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public BeerDto getBeerById(UUID id) {
		
		return beerRepository.findById(id)
				.map(beerMapper::toDto)
				.orElseThrow(NotFoundException::new);
	}

	@Override
	public BeerDto createBeer(BeerDto beer) {
		
		Beer createdBeer = beerRepository.save(beerMapper.toEntity(beer));

		return beerMapper.toDto(createdBeer);
	}

	@Override
	public Optional<BeerDto> updateBeerById(UUID id, BeerDto beer) {

		AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();
		
		beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
			foundBeer.setBeerName(beer.getBeerName());
			foundBeer.setPrice(beer.getPrice());
			foundBeer.setUpc(beer.getUpc());
			foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
			atomicReference.set(Optional.of(beerMapper.toDto(beerRepository.save(foundBeer))));
		}, () -> {
			atomicReference.set(Optional.empty());
		});

		return atomicReference.get();
	}

	@Override
	public Optional<BeerDto> patchBeerById(UUID id, BeerDto beer) {
		
		AtomicReference<Optional<BeerDto>> atomicReference = new AtomicReference<>();
		
		beerRepository.findById(id).ifPresentOrElse(foundBeer -> {
			if (StringUtils.hasText(beer.getBeerName())) foundBeer.setBeerName(beer.getBeerName());
			if (beer.getBeerStyle() != null) foundBeer.setBeerStyle(beer.getBeerStyle());
			if (beer.getPrice() != null) foundBeer.setPrice(beer.getPrice());
			if (beer.getQuantityOnHand() != null) foundBeer.setQuantityOnHand(beer.getQuantityOnHand());
			if (StringUtils.hasText(beer.getUpc())) foundBeer.setUpc(beer.getUpc());
			atomicReference.set(Optional.of(beerMapper.toDto(beerRepository.save(foundBeer))));
		}, () -> {
			atomicReference.set(Optional.empty());
		});

		return atomicReference.get();
	}

	@Override
	public Boolean deleteBeerById(UUID id) {
		
		if (!beerRepository.existsById(id)) {
			return false;
		}
		
		beerRepository.deleteById(id);
		
		return true;
	}

}
