package tw.niq.demo.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import tw.niq.demo.repository.BeerRepository;
import tw.niq.demo.repository.CustomerRepository;
import tw.niq.demo.service.BeerCsvService;
import tw.niq.demo.service.BeerCsvServiceImpl;

@DataJpaTest
@Import(BeerCsvServiceImpl.class)
class DataLoaderTest {

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	CustomerRepository customerRepository;
	
	@Autowired
	BeerCsvService beerCsvService;

	DataLoader dataLoader;

	@BeforeEach
	void setUp() throws Exception {
		dataLoader = new DataLoader(beerRepository, customerRepository, beerCsvService);
	}

	@Test
	void testRun() throws Exception {

		dataLoader.run();

		assertThat(beerRepository.count()).isEqualTo(2413);
		assertThat(customerRepository.count()).isEqualTo(3);
	}

}
