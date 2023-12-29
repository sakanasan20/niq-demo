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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
import tw.niq.demo.entity.BeerStyle;
import tw.niq.demo.exception.NotFoundException;
import tw.niq.demo.service.BeerService;

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

	@Autowired
	ObjectMapper objectMapper;

	List<BeerDto> testBeers;

	BeerDto testBeer;
	
	String testBeerName;

	@BeforeEach
	void setUp() throws Exception {

		testBeers = Arrays.asList(
				BeerDto.builder()
						.id(UUID.randomUUID())
						.beerName("Galaxy Cat")
						.beerStyle(BeerStyle.PALE_ALE)
						.upc("12356")
						.price(new BigDecimal("12.99"))
						.quantityOnHand(122)
						.createdDate(LocalDateTime.now())
						.updateDate(LocalDateTime.now())
						.build(),
				BeerDto.builder()
						.id(UUID.randomUUID())
						.beerName("Crank")
						.beerStyle(BeerStyle.PALE_ALE)
						.upc("12356222")
						.price(new BigDecimal("11.99"))
						.quantityOnHand(392)
						.createdDate(LocalDateTime.now())
						.updateDate(LocalDateTime.now())
						.build(),
				BeerDto.builder()
						.id(UUID.randomUUID())
						.beerName("Sunshine City")
						.beerStyle(BeerStyle.IPA)
						.upc("12356")
						.price(new BigDecimal("13.99"))
						.quantityOnHand(144)
						.createdDate(LocalDateTime.now())
						.updateDate(LocalDateTime.now())
						.build());

		testBeer = testBeers.get(0);
		
		testBeerName = "New Beer Name";
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
	void testCreateBeer_whenBeerNameIsNull_willReturnBadRequestStatus() throws Exception {

		BeerDto beer = testBeer;
		beer.setId(null);
		beer.setBeerName(null);

		mockMvc.perform(post(BeerController.PATH_V1_BEER)
						.content(objectMapper.writeValueAsString(beer))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	void testUpdateBeerById() throws JsonProcessingException, Exception {

		given(beerService.updateBeerById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.of(BeerDto.builder().build()));

		mockMvc.perform(put(BeerController.PATH_V1_BEER_ID, testBeer.getId())
						.content(objectMapper.writeValueAsString(testBeer))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(beerService).updateBeerById(any(UUID.class), any(BeerDto.class));
	}
	
	@Test
	void testUpdateBeerById_whenBeerNameIsBlank_willReturnBadRequestStatus() throws JsonProcessingException, Exception {

		BeerDto beer = testBeer;
		beer.setBeerName("");
		
		mockMvc.perform(put(BeerController.PATH_V1_BEER_ID, testBeer.getId())
						.content(objectMapper.writeValueAsString(testBeer))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	void testPatchBeerById() throws JsonProcessingException, Exception {

		Map<String, Object> beerMap = new HashMap<>();
		beerMap.put("beerName", testBeerName);

		given(beerService.patchBeerById(any(UUID.class), any(BeerDto.class))).willReturn(Optional.of(BeerDto.builder().build()));

		mockMvc.perform(patch(BeerController.PATH_V1_BEER_ID, testBeer.getId())
						.content(objectMapper.writeValueAsString(beerMap))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(beerService).patchBeerById(uuidArgumentCaptor.capture(), beerArgumentCaptor.capture());

		assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
		assertThat(beerMap.get("beerName")).isEqualTo(beerArgumentCaptor.getValue().getBeerName());
	}
	
	@Test
	void testPatchBeerById_whenBeerNameIsBlank_willReturnBadRequestStatus() throws JsonProcessingException, Exception {

		Map<String, Object> beerMap = new HashMap<>();
		beerMap.put("beerName", "");

		mockMvc.perform(patch(BeerController.PATH_V1_BEER_ID, testBeer.getId())
						.content(objectMapper.writeValueAsString(beerMap))
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.length()", is(1)));
	}

	@Test
	void testDeleteBeerById() throws JsonProcessingException, Exception {
		
		given(beerService.deleteBeerById(any(UUID.class))).willReturn(true);

		mockMvc.perform(delete(BeerController.PATH_V1_BEER_ID, testBeer.getId())
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());

		verify(beerService).deleteBeerById(uuidArgumentCaptor.capture());

		assertThat(testBeer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
	}

}
