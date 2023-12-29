package tw.niq.demo.bootstrap;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import tw.niq.demo.entity.Beer;
import tw.niq.demo.entity.BeerStyle;
import tw.niq.demo.entity.Customer;
import tw.niq.demo.model.BeerCsvRecord;
import tw.niq.demo.repository.BeerRepository;
import tw.niq.demo.repository.CustomerRepository;
import tw.niq.demo.service.BeerCsvService;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	private final BeerRepository beerRepository;
	private final CustomerRepository customerRepository;
	private final BeerCsvService beerCsvService;

	@Transactional
	@Override
	public void run(String... args) throws Exception {
		loadBeerData();
		loadCsvData();
		loadCustomerData();
	}

	private void loadCsvData() throws FileNotFoundException {
		
		if (beerRepository.count() < 10) {
		
			File file = ResourceUtils.getFile("classpath:csv/beers.csv");
			
			List<BeerCsvRecord> beerCsvRecords = beerCsvService.convertCsv(file);
			
			beerCsvRecords.forEach(beerCsvRecord -> {
				
                BeerStyle beerStyle = switch (beerCsvRecord.getStyle()) {
					case "American Pale Lager" -> BeerStyle.LAGER;
					case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" -> BeerStyle.ALE;
					case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
					case "American Porter" -> BeerStyle.PORTER;
					case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
					case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
					case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
					case "English Pale Ale" -> BeerStyle.PALE_ALE;
					default -> BeerStyle.PILSNER;
                };

				beerRepository.save(Beer.builder()
						.beerName(StringUtils.abbreviate(beerCsvRecord.getBeer(), 50))
						.beerStyle(beerStyle)
						.price(BigDecimal.TEN)
						.upc(beerCsvRecord.getRow().toString())
						.quantityOnHand(beerCsvRecord.getCount())
						.build());
            });
		}
	}

	private void loadBeerData() {
		
		if (beerRepository.count() == 0) {
			
			Beer beer1 = Beer.builder()
					.beerName("Galaxy Cat")
					.beerStyle(BeerStyle.PALE_ALE)
					.upc("12356")
					.price(new BigDecimal("12.99"))
					.quantityOnHand(122)
					.build();

			Beer beer2 = Beer.builder()
					.beerName("Crank")
					.beerStyle(BeerStyle.PALE_ALE)
					.upc("12356222")
					.price(new BigDecimal("11.99"))
					.quantityOnHand(392)
					.build();

			Beer beer3 = Beer.builder()
					.beerName("Sunshine City")
					.beerStyle(BeerStyle.IPA)
					.upc("12356")
					.price(new BigDecimal("13.99"))
					.quantityOnHand(144)
					.build();

			beerRepository.save(beer1);
			beerRepository.save(beer2);
			beerRepository.save(beer3);
		}
	}

	private void loadCustomerData() {

		if (customerRepository.count() == 0) {
			
			Customer customer1 = Customer.builder()
					.name("Customer 1")
					.build();

			Customer customer2 = Customer.builder()
					.name("Customer 2")
					.build();

			Customer customer3 = Customer.builder()
					.name("Customer 3")
					.version(1)
					.build();

			customerRepository.saveAll(Arrays.asList(customer1, customer2, customer3));
		}
	}

}
