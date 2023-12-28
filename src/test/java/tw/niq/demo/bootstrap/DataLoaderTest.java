package tw.niq.demo.bootstrap;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tw.niq.demo.repository.BeerRepository;
import tw.niq.demo.repository.CustomerRepository;

@DataJpaTest
class DataLoaderTest {

	@Autowired
	BeerRepository beerRepository;

	@Autowired
	CustomerRepository customerRepository;

	DataLoader dataLoader;

	@BeforeEach
	void setUp() throws Exception {
		dataLoader = new DataLoader(beerRepository, customerRepository);
	}

	@Test
	void testRun() throws Exception {

		dataLoader.run();

		assertThat(beerRepository.count()).isEqualTo(3);
		assertThat(customerRepository.count()).isEqualTo(3);
	}

}
