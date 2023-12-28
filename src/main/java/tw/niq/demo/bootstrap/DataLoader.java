package tw.niq.demo.bootstrap;

import java.math.BigDecimal;
import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import tw.niq.demo.entity.Beer;
import tw.niq.demo.entity.BeerStyle;
import tw.niq.demo.entity.Customer;
import tw.niq.demo.repository.BeerRepository;
import tw.niq.demo.repository.CustomerRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

	private final BeerRepository beerRepository;
	private final CustomerRepository customerRepository;

	@Override
	public void run(String... args) throws Exception {
		loadBeerData();
		loadCustomerData();
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