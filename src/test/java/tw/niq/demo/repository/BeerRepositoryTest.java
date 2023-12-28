package tw.niq.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tw.niq.demo.entity.Beer;

@DataJpaTest
class BeerRepositoryTest {
	
	@Autowired
	BeerRepository beerRepository;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSave() {
		
		Beer savedBeer = beerRepository.save(Beer.builder().beerName("Test Beer").build());
		
		assertThat(savedBeer).isNotNull();
		assertThat(savedBeer.getId()).isNotNull();
	}

}
