package tw.niq.demo.service;

import java.util.List;
import java.util.UUID;

import tw.niq.demo.domain.Customer;

public interface CustomerService {

	List<Customer> getAllCustomers();

	Customer getCustomerById(UUID id);
	
}
