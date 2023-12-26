package tw.niq.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.niq.demo.domain.Customer;
import tw.niq.demo.service.CustomerService;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/customer")
public class CustomerController {

	private final CustomerService customerService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Customer> listAllCustomers() {
		return customerService.getAllCustomers();
	}

	@RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
	public Customer getCustomerById(@PathVariable("customerId") UUID id) {
		return customerService.getCustomerById(id);
	}

}
