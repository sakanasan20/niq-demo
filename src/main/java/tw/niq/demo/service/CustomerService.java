package tw.niq.demo.service;

import java.util.List;
import java.util.UUID;

import tw.niq.demo.dto.CustomerDto;

public interface CustomerService {

	List<CustomerDto> getCustomers();

	CustomerDto getCustomerById(UUID id);

	CustomerDto createCustomer(CustomerDto customer);

	void updateCustomerById(UUID id, CustomerDto customer);

	void patchCustomerById(UUID id, CustomerDto customer);

	void deleteCustomerById(UUID id);
	
}
