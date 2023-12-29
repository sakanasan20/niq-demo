package tw.niq.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import jakarta.validation.ConstraintViolationException;
import tw.niq.demo.entity.Beer;
import tw.niq.demo.entity.BeerStyle;

@DataJpaTest
class BeerRepositoryTest {
	
	@Autowired
	BeerRepository beerRepository;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSave() {
		
		Beer savedBeer = beerRepository.save(Beer.builder()
				.beerName("Test Beer")
				.beerStyle(BeerStyle.PALE_ALE)
				.upc("12345678")
				.price(new BigDecimal("11.99"))
				.build());
		
		beerRepository.flush();
		
		assertThat(savedBeer).isNotNull();
		assertThat(savedBeer.getId()).isNotNull();
	}
	
	@Test
	void testSave_whenBeerNameTooLong_will() {

		assertThrows(ConstraintViolationException.class, () -> {
			beerRepository.save(Beer.builder()
					.beerName("Test Beer 01234567890123456789012345678901234567890123456789")
					.beerStyle(BeerStyle.PALE_ALE)
					.upc("12345678")
					.price(new BigDecimal("11.99"))
					.build());
			
			beerRepository.flush();
		});
	}

}
