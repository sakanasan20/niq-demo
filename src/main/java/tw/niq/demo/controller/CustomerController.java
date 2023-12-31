package tw.niq.demo.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tw.niq.demo.dto.CustomerDto;
import tw.niq.demo.exception.NotFoundException;
import tw.niq.demo.service.CustomerService;

@RequiredArgsConstructor
@RestController
@RequestMapping(CustomerController.PATH_V1_CUSTOMER)
public class CustomerController {

	public static final String PATH_V1_CUSTOMER= "/api/v1/customer";
	public static final String PATH_V1_CUSTOMER_ID = PATH_V1_CUSTOMER + "/{customerId}";

	private final CustomerService customerService;

	@GetMapping
	public List<CustomerDto> getCustomers() {

		return customerService.getCustomers();
	}

	@GetMapping(value = "/{customerId}")
	public CustomerDto getCustomerById(@PathVariable("customerId") UUID id) {

		return customerService.getCustomerById(id);
	}

	@PostMapping
	public ResponseEntity<Void> createCustomer(@RequestBody CustomerDto customer) {

		CustomerDto savedCustomer = customerService.createCustomer(customer);

		HttpHeaders headers = new HttpHeaders();

		headers.add(HttpHeaders.LOCATION, CustomerController.PATH_V1_CUSTOMER + "/" + savedCustomer.getId().toString());

		return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}

	@PutMapping("{customerId}")
	public ResponseEntity<Void> updateCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDto customer) {

		if (customerService.updateCustomerById(id, customer).isEmpty()) {
			throw new NotFoundException();
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PatchMapping("{customerId}")
	public ResponseEntity<Void> patchCustomerById(@PathVariable("customerId") UUID id, @RequestBody CustomerDto customer) {

		if (customerService.patchCustomerById(id, customer).isEmpty()) {
			throw new NotFoundException();
		}

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("{customerId}")
	public ResponseEntity<Void> deleteCustomerById(@PathVariable("customerId") UUID id) {

		if (!customerService.deleteCustomerById(id)) {
        	throw new NotFoundException();
        }

		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

}
