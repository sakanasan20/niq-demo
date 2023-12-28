package tw.niq.demo.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tw.niq.demo.entity.Customer;

@DataJpaTest
class CustomerRepositoryTest {
	
	@Autowired
	CustomerRepository customerRepository;

	@BeforeEach
	void setUp() throws Exception {
	}

	@Test
	void testSave() {
		
		Customer savedCustomer = customerRepository.save(Customer.builder().name("Test Customer").build());
		
		assertThat(savedCustomer).isNotNull();
		assertThat(savedCustomer.getId()).isNotNull();
	}

}
