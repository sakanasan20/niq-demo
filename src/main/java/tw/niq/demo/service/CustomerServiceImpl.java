package tw.niq.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import tw.niq.demo.dto.CustomerDto;

@Service
public class CustomerServiceImpl implements CustomerService {

	private Map<UUID, CustomerDto> customerMap;

	public CustomerServiceImpl() {
		CustomerDto customer1 = CustomerDto.builder()
				.id(UUID.randomUUID())
				.name("Customer 1")
				.version(1)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		CustomerDto customer2 = CustomerDto.builder()
				.id(UUID.randomUUID()).name("Customer 2")
				.version(1)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		CustomerDto customer3 = CustomerDto.builder()
				.id(UUID.randomUUID())
				.name("Customer 3").version(1)
				.createdDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();

		customerMap = new HashMap<>();
		customerMap.put(customer1.getId(), customer1);
		customerMap.put(customer2.getId(), customer2);
		customerMap.put(customer3.getId(), customer3);
	}

	@Override
	public List<CustomerDto> getCustomers() {
		
		return new ArrayList<>(customerMap.values());
	}

	@Override
	public CustomerDto getCustomerById(UUID uuid) {
		
		return customerMap.get(uuid);
	}

	@Override
	public CustomerDto createCustomer(CustomerDto customer) {

		CustomerDto savedCustomer = CustomerDto.builder()
				.id(UUID.randomUUID())
				.version(1)
				.updateDate(LocalDateTime.now())
				.createdDate(LocalDateTime.now())
				.name(customer.getName())
				.build();

		customerMap.put(savedCustomer.getId(), savedCustomer);

		return savedCustomer;
	}

	@Override
	public void updateCustomerById(UUID id, CustomerDto customer) {
		
		CustomerDto existing = customerMap.get(id);
		
        existing.setName(customer.getName());
	}

	@Override
	public void patchCustomerById(UUID id, CustomerDto customer) {
		
		CustomerDto existing = customerMap.get(id);

		if (StringUtils.hasText(customer.getName())) {
			existing.setName(customer.getName());
		}
	}

	@Override
	public void deleteCustomerById(UUID id) {

		customerMap.remove(id);
	}

}
