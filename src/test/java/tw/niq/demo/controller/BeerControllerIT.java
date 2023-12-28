package tw.niq.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import jakarta.transaction.Transactional;
import tw.niq.demo.dto.BeerDto;
import tw.niq.demo.exception.NotFoundException;
import tw.niq.demo.repository.BeerRepository;

@SpringBootTest
class BeerControllerIT {
	
	@Autowired
	BeerController beerController;

	@Autowired
	BeerRepository beerRepository;
	
	BeerDto testBeer;
	
	UUID testBeerId;
	
	String testBeerName;
	
	@BeforeEach
	void setUp() throws Exception {
		
		testBeer = beerController.getBeers().get(0);
		
		testBeerId = testBeer.getId();
		
		testBeerName = "New Beer Name";
	}

	@Test
	void testGetBeers() {
		
		List<BeerDto> beers = beerController.getBeers();
		
		assertThat(beers.size()).isEqualTo(3);
	}

	@Test
	void testGetBeerById() {
		
		BeerDto beer = beerController.getBeerById(testBeerId);
		
		assertThat(beer).isNotNull();
		assertThat(beer.getId()).isEqualTo(testBeerId);
	}
	
    @Test
    void testGetBeerById_whenIdNotFound_willThrowNotFoundException() {
    	
        assertThrows(NotFoundException.class, () -> {
            beerController.getBeerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
	@Test
	void testCreateBeer() {
    	
		BeerDto beer = testBeer;
		beer.setId(null);
		ResponseEntity<Void> responseEntity = beerController.createBeer(beer);
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
		
		String[] locationUuid = responseEntity.getHeaders().getLocation().getPath().split("/");
		UUID createdUuid = UUID.fromString(locationUuid[4]);
		BeerDto createdBeer = beerController.getBeerById(createdUuid);
		
		assertThat(createdBeer).isNotNull();
	}

    @Rollback
    @Transactional
	@Test
	void testUpdateBeerById() {
    	
		ResponseEntity<Void> responseEntity = beerController.updateBeerById(testBeerId, BeerDto.builder().beerName(testBeerName).build());
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
		
		BeerDto updatedBeer = beerController.getBeerById(testBeerId);
		
		assertThat(updatedBeer.getBeerName()).isEqualTo(testBeerName);
	}
    
    @Test
    void testUpdateBeerById_whenIdNotFound_willThrowNotFoundException() {
    	
        assertThrows(NotFoundException.class, () -> {
            beerController.updateBeerById(UUID.randomUUID(), BeerDto.builder().build());
        });
    }

    @Rollback
    @Transactional
	@Test
	void testPatchBeerById() {
    	
    	ResponseEntity<Void> responseEntity = beerController.patchBeerById(testBeerId, BeerDto.builder().beerName(testBeerName).build());
    	
    	assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    	
		BeerDto patchedBeer = beerController.getBeerById(testBeerId);
		
		assertThat(patchedBeer.getBeerName()).isEqualTo(testBeerName);
	}
    
    @Test
    void testPatchBeerById_whenIdNotFound_willThrowNotFoundException() {
    	
        assertThrows(NotFoundException.class, () -> {
            beerController.patchBeerById(UUID.randomUUID(), BeerDto.builder().build());
        });
    }

    @Rollback
    @Transactional
	@Test
	void testDeleteBeerById() {
    	
    	ResponseEntity<Void> responseEntity = beerController.deleteBeerById(testBeerId);
    	
    	assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    	assertThat(beerRepository.findById(testBeerId).isEmpty());
	}

    @Test
    void testDeleteBeerById_whenIdNotFound_willThrowNotFoundException() {
    	
        assertThrows(NotFoundException.class, () -> {
            beerController.deleteBeerById(UUID.randomUUID());
        });
    }
}
