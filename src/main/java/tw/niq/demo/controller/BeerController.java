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
import tw.niq.demo.domain.Beer;
import tw.niq.demo.service.BeerService;

@RequiredArgsConstructor
@RestController
@RequestMapping(BeerController.PATH)
public class BeerController {

	public static final String PATH = "/api/v1/beer";

	private final BeerService beerService;

	@GetMapping
	public List<Beer> listBeers() {
		
		return beerService.listBeers();
	}

	@GetMapping("/{beerId}")
	public Beer getBeerById(@PathVariable("beerId") UUID id) {
		
		return beerService.getBeerById(id);
	}

	@PostMapping
	public ResponseEntity<Void> handlePost(@RequestBody Beer beer) {
		
		Beer savedBeer = beerService.saveNewBeer(beer);

		HttpHeaders headers = new HttpHeaders();
		
		headers.add(HttpHeaders.LOCATION, String.format("%s/%s", BeerController.PATH, savedBeer.getId().toString()));

		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@PutMapping("/{beerId}")
	public ResponseEntity<Void> updateById(@PathVariable("beerId") UUID id, @RequestBody Beer beer) {
		
		beerService.updateBeerById(id, beer);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PatchMapping("/{beerId}")
	public ResponseEntity<Void> updateBeerPatchById(@PathVariable("beerId") UUID id, @RequestBody Beer beer) {
		
		beerService.patchBeerById(id, beer);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
    @DeleteMapping("/{beerId}")
    public ResponseEntity<Void> deleteById(@PathVariable("beerId") UUID id) {

        beerService.deleteById(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
