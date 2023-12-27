package tw.niq.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.niq.demo.dto.BeerDto;
import tw.niq.demo.service.BeerService;

@RequiredArgsConstructor
@RestController
@RequestMapping(BeerController.PATH_V1_BEER)
public class BeerController {

	public static final String PATH_V1_BEER = "/api/v1/beer";
	public static final String PATH_V1_BEER_ID = PATH_V1_BEER + "/{beerId}";

	private final BeerService beerService;

	@GetMapping
	public List<BeerDto> getBeers() {
		
		return beerService.getBeers();
	}

	@GetMapping("/{beerId}")
	public BeerDto getBeerById(@PathVariable("beerId") UUID id) {
		
		return beerService.getBeerById(id);
	}

	@PostMapping
	public ResponseEntity<Void> createBeer(@RequestBody BeerDto beer) {
		
		BeerDto createdBeer = beerService.createBeer(beer);

		HttpHeaders headers = new HttpHeaders();
		
		headers.add(HttpHeaders.LOCATION, BeerController.PATH_V1_BEER + "/" + createdBeer.getId().toString());

		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@PutMapping("/{beerId}")
	public ResponseEntity<Void> updateBeerById(@PathVariable("beerId") UUID id, @RequestBody BeerDto beer) {
		
		beerService.updateBeerById(id, beer);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PatchMapping("/{beerId}")
	public ResponseEntity<Void> patchBeerById(@PathVariable("beerId") UUID id, @RequestBody BeerDto beer) {
		
		beerService.patchBeerById(id, beer);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> deleteBeerById(@PathVariable("beerId") UUID id) {

        beerService.deleteBeerById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
