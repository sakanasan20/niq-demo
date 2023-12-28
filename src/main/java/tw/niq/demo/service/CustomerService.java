package tw.niq.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import tw.niq.demo.dto.CustomerDto;

public interface CustomerService {

	List<CustomerDto> getCustomers();

	CustomerDto getCustomerById(UUID id);

	CustomerDto createCustomer(CustomerDto customer);

	Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto customer);

	Optional<CustomerDto> patchCustomerById(UUID id, CustomerDto customer);

	Boolean deleteCustomerById(UUID id);
	
}
