package tw.niq.demo.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;

import tw.niq.demo.model.BeerCsvRecord;

class BeerCsvServiceTest {
	
	BeerCsvService beerCsvService = new BeerCsvServiceImpl();

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testConvertCsv() throws FileNotFoundException {
		
		File file = ResourceUtils.getFile("classpath:csv/beers.csv");
		
		List<BeerCsvRecord> beerCsvRecords = beerCsvService.convertCsv(file);
		
		assertThat(beerCsvRecords.size()).isGreaterThan(0);
	}

}
