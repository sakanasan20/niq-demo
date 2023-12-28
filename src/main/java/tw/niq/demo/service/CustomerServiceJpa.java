package tw.niq.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import tw.niq.demo.dto.CustomerDto;
import tw.niq.demo.entity.Customer;
import tw.niq.demo.exception.NotFoundException;
import tw.niq.demo.mapper.CustomerMapper;
import tw.niq.demo.repository.CustomerRepository;

@Primary
@Service
@RequiredArgsConstructor
public class CustomerServiceJpa implements CustomerService {

	private final CustomerRepository customerRepository;
	private final CustomerMapper customerMapper;

	@Override
	public List<CustomerDto> getCustomers() {
		
		return customerRepository.findAll()
				.stream()
				.map(customerMapper::toDto)
				.collect(Collectors.toList());
	}

	@Override
	public CustomerDto getCustomerById(UUID id) {
		
		return customerRepository.findById(id)
				.map(customerMapper::toDto)
				.orElseThrow(NotFoundException::new);
	}

	@Override
	public CustomerDto createCustomer(CustomerDto customer) {
		
		Customer createdCustomer = customerRepository.save(customerMapper.toEntity(customer));

		return customerMapper.toDto(createdCustomer);
	}

	@Override
	public Optional<CustomerDto> updateCustomerById(UUID id, CustomerDto customer) {
		
		AtomicReference<Optional<CustomerDto>> atomicReference = new AtomicReference<>();
		
		customerRepository.findById(id).ifPresentOrElse(foundCustomer -> {
			foundCustomer.setName(customer.getName());
	        atomicReference.set(Optional.of(customerMapper.toDto(customerRepository.save(foundCustomer))));
		}, () -> {
			atomicReference.set(Optional.empty());
		});

        return atomicReference.get();
	}

	@Override
	public Optional<CustomerDto> patchCustomerById(UUID id, CustomerDto customer) {
		
		AtomicReference<Optional<CustomerDto>> atomicReference = new AtomicReference<>();
		
		customerRepository.findById(id).ifPresentOrElse(foundCustomer -> {
			if (StringUtils.hasText(customer.getName())) foundCustomer.setName(customer.getName());
			atomicReference.set(Optional.of(customerMapper.toDto(customerRepository.save(foundCustomer))));
		}, () -> {
			atomicReference.set(Optional.empty());
		});

		return atomicReference.get();
	}

	@Override
	public Boolean deleteCustomerById(UUID id) {
		
		if (!customerRepository.existsById(id)) {
			return false;
		}
		
		customerRepository.deleteById(id);
		
		return true;
	}

}
