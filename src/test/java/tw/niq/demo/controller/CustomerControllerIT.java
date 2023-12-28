package tw.niq.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;

import jakarta.transaction.Transactional;
import tw.niq.demo.dto.CustomerDto;
import tw.niq.demo.exception.NotFoundException;
import tw.niq.demo.repository.CustomerRepository;

@SpringBootTest
class CustomerControllerIT {
	
	@Autowired
	CustomerController customerController;

	@Autowired
	CustomerRepository customerRepository;
	
	CustomerDto testCustomer;
	
	UUID testCustomerId;
	
	String testCustomerName;

	@BeforeEach
	void setUp() throws Exception {
		
		testCustomer = customerController.getCustomers().get(0);
		
		testCustomerId = testCustomer.getId();
		
		testCustomerName = "New Customer Name";
	}

	@Test
	void testGetCustomers() {
		
		List<CustomerDto> customers = customerController.getCustomers();
		
		assertThat(customers.size()).isEqualTo(3);
	}

	@Test
	void testGetCustomerById() {

		CustomerDto customer = customerController.getCustomerById(testCustomerId);
		
		assertThat(customer).isNotNull();
		assertThat(customer.getId()).isEqualTo(testCustomerId);
	}
	
    @Test
    void testGetBeerById_whenIdNotFound_willThrowNotFoundException() {
    	
        assertThrows(NotFoundException.class, () -> {
        	customerController.getCustomerById(UUID.randomUUID());
        });
    }

    @Rollback
    @Transactional
	@Test
	void testCreateCustomer() {

    	CustomerDto customer = testCustomer;
    	customer.setId(null);
		ResponseEntity<Void> responseEntity = customerController.createCustomer(customer);
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(201));
		assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
		
		String[] locationUuid = responseEntity.getHeaders().getLocation().getPath().split("/");
		UUID createdUuid = UUID.fromString(locationUuid[4]);
		CustomerDto createdCustomer = customerController.getCustomerById(createdUuid);
		
		assertThat(createdCustomer).isNotNull();
	}

    @Rollback
    @Transactional
	@Test
	void testUpdateCustomerById() {

		ResponseEntity<Void> responseEntity = customerController.updateCustomerById(testCustomerId, CustomerDto.builder().name(testCustomerName).build());
		
		assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
		
		CustomerDto updatedCustomer = customerController.getCustomerById(testCustomerId);
		
		assertThat(updatedCustomer.getName()).isEqualTo(testCustomerName);
	}
    
    @Test
    void testUpdateCustomerById_whenIdNotFound_willThrowNotFoundException() {
    	
        assertThrows(NotFoundException.class, () -> {
        	customerController.updateCustomerById(UUID.randomUUID(), CustomerDto.builder().build());
        });
    }

    @Rollback
    @Transactional
	@Test
	void testPatchCustomerById() {

    	ResponseEntity<Void> responseEntity = customerController.patchCustomerById(testCustomerId, CustomerDto.builder().name(testCustomerName).build());
    	
    	assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    	
    	CustomerDto patchedCustomer = customerController.getCustomerById(testCustomerId);
		
		assertThat(patchedCustomer.getName()).isEqualTo(testCustomerName);
	}
    
    @Test
    void testPatchBeerById_whenIdNotFound_willThrowNotFoundException() {
    	
        assertThrows(NotFoundException.class, () -> {
        	customerController.patchCustomerById(UUID.randomUUID(), CustomerDto.builder().build());
        });
    }

    @Rollback
    @Transactional
	@Test
	void testDeleteCustomerById() {

    	ResponseEntity<Void> responseEntity = customerController.deleteCustomerById(testCustomerId);
    	
    	assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    	assertThat(customerRepository.findById(testCustomerId).isEmpty());
	}

    @Test
    void testDeleteBeerById_whenIdNotFound_willThrowNotFoundException() {
    	
        assertThrows(NotFoundException.class, () -> {
        	customerController.deleteCustomerById(UUID.randomUUID());
        });
    }
    
}
