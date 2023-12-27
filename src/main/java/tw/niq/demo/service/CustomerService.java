package tw.niq.demo.service;

import java.util.List;
import java.util.UUID;

import tw.niq.demo.domain.Customer;

public interface CustomerService {

	List<Customer> getCustomers();

	Customer getCustomerById(UUID id);

	Customer createCustomer(Customer customer);

	void updateCustomerById(UUID id, Customer customer);

	void patchCustomerById(UUID id, Customer customer);

	void deleteCustomerById(UUID id);
	
}
