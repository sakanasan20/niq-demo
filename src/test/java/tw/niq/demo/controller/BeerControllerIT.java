package tw.niq.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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
	
	@Autowired
    ObjectMapper objectMapper;
	
	@Autowired
	WebApplicationContext wac;
	
	MockMvc mockMvc;
	
	BeerDto testBeer;
	
	UUID testBeerId;
	
	String testBeerName;
	
	@BeforeEach
	void setUp() throws Exception {
		
		mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		
		testBeer = beerController.getBeers().get(0);
		
		testBeerId = testBeer.getId();
		
		testBeerName = "New Beer Name";
	}

	@Test
	void testGetBeers() {
		
		List<BeerDto> beers = beerController.getBeers();
		
		assertThat(beers.size()).isEqualTo(2413);
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
    
    @Test
    void testPatchBeerById_whenBeerNameTooLong_willReturnBadRequestStatus() throws JsonProcessingException, Exception {
    	
        Map<String, Object> beerMap = new HashMap<>();
        beerMap.put("beerName", "New Name 1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890");

        mockMvc.perform(patch(BeerController.PATH_V1_BEER_ID, testBeerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerMap)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.length()", is(1)))
                .andReturn();
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
