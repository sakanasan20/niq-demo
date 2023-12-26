package tw.niq.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.niq.demo.domain.Beer;
import tw.niq.demo.service.BeerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {
	
	private final BeerService beerService;
	
	@GetMapping
    public List<Beer> listBeers() {
        return beerService.listBeers();
    }
	
	@GetMapping("/{beerId}")
    public Beer getBeerById(@PathVariable("beerId") UUID id) {
        return beerService.getBeerById(id);
    }

}
