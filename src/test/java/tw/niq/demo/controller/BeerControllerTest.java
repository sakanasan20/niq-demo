package tw.niq.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.niq.demo.dto.BeerDto;
import tw.niq.demo.exception.NotFoundException;
import tw.niq.demo.service.BeerService;
import tw.niq.demo.service.BeerServiceImpl;

@WebMvcTest(BeerController.class)
class BeerControllerTest {
	
	@Autowired
	MockMvc mockMvc;

	@MockBean
	BeerService beerService;
	
	@Captor
	ArgumentCaptor<UUID> uuidArgumentCaptor;
	
	@Captor
	ArgumentCaptor<BeerDto> beerArgumentCaptor;
	
	BeerServiceImpl beerServiceImpl;
	
	ObjectMapper objectMapper;
	
	List<BeerDto> testBeers;
	
	BeerDto testBeer; 
	
	@BeforeEach
	void setUp() throws Exception {
		
		beerServiceImpl = new BeerServiceImpl();
		
		testBeers = beerServiceImpl.getBeers();
		
		testBeer = testBeers.get(0);
		
		objectMapper = new ObjectMapper();
		
		objectMapper.findAndRegisterModules();
	}

	@Test
	void testGetBeers() throws Exception {
		
		given(beerService.getBeers()).willReturn(testBeers);
		
		mockMvc.perform(get(BeerController.PATH_V1_BEER)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(testBeers.size())));
	}

	@Test
	void testGetBeerById() throws Exception {
		
		given(beerService.getBeerById(any(UUID.class))).willReturn(testBeer);
		
		mockMvc.perform(get(BeerController.PATH_V1_BEER_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
				.andExpect(jsonPath("$.beerName", is(testBeer.getBeerName())));
	}
	
	@Test
	void testGetBeerById_whenIdNotFound_willThrowNotFoundException() throws Exception {
		
		given(beerService.getBeerById(any(UUID.class))).willThrow(NotFoundException.class);
		
		mockMvc.perform(get(BeerController.PATH_V1_BEER_ID, UUID.randomUUID()))
				.andExpect(status().isNotFound());
	}

	@Test
	void testCreateBeer() throws Exception {
		
		BeerDto beer = testBeer;
		beer.setId(null);
		
		given(beerService.createBeer(any(BeerDto.class))).willReturn(testBeers.get(1));
		
		mockMvc.perform(post(BeerController.PATH_V1_BEER)
				.content(objectMapper.writeValueAsString(beer))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().exists(HttpHeaders.LOCATION));
	}

	@Test
	void testUpdateBeerById() throws JsonProcessingException, Exception {
		
		mockMvc.perform(put(BeerController.PATH_V1_BEER_ID, testBeer.getId())
				.content(objectMapper.writeValueAsString(testBeer))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		verify(beerService).updateBeerById(any(UUID.class), any(BeerDto.class));
	}

	@Test
	void testPatchBeerById() throws JsonProcessingException, Exception {
		
		Map<String, Object> beerMap = new HashMap<>();
		
		beerMap.put("beerName", "New Name");
		
		mockMvc.perform(patch(BeerController.PATH_V1_BEER_ID, testBeer.getId())
				.content(objectMapper.writeValueAsString(beerMap))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());
		
		assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
		assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
	}

	@Test
	void testDeleteBeerById() throws JsonProcessingException, Exception {
		
		mockMvc.perform(delete(BeerController.PATH_V1_BEER_ID, testBeer.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());
		
		assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
	}

}
