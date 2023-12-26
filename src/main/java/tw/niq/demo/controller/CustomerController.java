package tw.niq.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.niq.demo.domain.Customer;
import tw.niq.demo.service.CustomerService;

@RequiredArgsConstructor
@RestController
@RequestMapping(CustomerController.PATH)
public class CustomerController {

	public static final String PATH = "/api/v1/customer";

	private final CustomerService customerService;

	@RequestMapping(method = RequestMethod.GET)
	public List<Customer> listAllCustomers() {

		return customerService.getAllCustomers();
	}

	@RequestMapping(value = "/{customerId}", method = RequestMethod.GET)
	public Customer getCustomerById(@PathVariable("customerId") UUID id) {

		return customerService.getCustomerById(id);
	}

	@PostMapping
	public ResponseEntity<Void> handlePost(@RequestBody Customer customer) {

		Customer savedCustomer = customerService.saveNewCustomer(customer);

		HttpHeaders headers = new HttpHeaders();

		headers.add(HttpHeaders.LOCATION,
				String.format("%s/%s", CustomerController.PATH, savedCustomer.getId().toString()));

		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@PutMapping("{customerId}")
	public ResponseEntity<Void> updateCustomerByID(@PathVariable("customerId") UUID id, @RequestBody Customer customer) {

		customerService.updateCustomerById(id, customer);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PatchMapping("{customerId}")
	public ResponseEntity<Void> patchCustomerById(@PathVariable("customerId") UUID id, @RequestBody Customer customer) {

		customerService.patchCustomerById(id, customer);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("{customerId}")
	public ResponseEntity<Void> deleteCustomerById(@PathVariable("customerId") UUID id) {

		customerService.deleteCustomerById(id);

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
