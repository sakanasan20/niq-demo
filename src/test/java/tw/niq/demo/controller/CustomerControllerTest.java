package tw.niq.demo.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import tw.niq.demo.dto.CustomerDto;
import tw.niq.demo.service.CustomerService;
import tw.niq.demo.service.CustomerServiceMap;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {
	
	@Autowired
	MockMvc mockMvc;
	
	@MockBean
	CustomerService customerService;
	
	@Captor
	ArgumentCaptor<UUID> uuidArgumentCaptor;
	
	@Captor
	ArgumentCaptor<CustomerDto> customerArgumentCaptor;
	
	CustomerServiceMap customerServiceImpl;
	
	@Autowired
	ObjectMapper objectMapper;
	
	List<CustomerDto> testCustomers;
	
	CustomerDto testCustomer;
	
	String testCustomerName;

	@BeforeEach
	void setUp() throws Exception {
		
		testCustomers = Arrays.asList(
				CustomerDto.builder()
						.id(UUID.randomUUID())
						.name("Customer 1")
						.version(1)
						.createdDate(LocalDateTime.now())
						.updateDate(LocalDateTime.now())
						.build(),
				CustomerDto.builder()
						.id(UUID.randomUUID())
						.name("Customer 2")
						.version(1)
						.createdDate(LocalDateTime.now())
						.updateDate(LocalDateTime.now())
						.build(),
				CustomerDto.builder()
						.id(UUID.randomUUID())
						.name("Customer 3")
						.version(1)
						.createdDate(LocalDateTime.now())
						.updateDate(LocalDateTime.now())
						.build());
		
		testCustomer = testCustomers.get(0);
		
		testCustomerName = "New Customer Name";
	}

	@Test
	void testGetCustomers() throws Exception {
		
		given(customerService.getCustomers()).willReturn(testCustomers);
		
		mockMvc.perform(get(CustomerController.PATH_V1_CUSTOMER)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.length()", is(testCustomers.size())));
	}

	@Test
	void testGetCustomerById() throws Exception {

		given(customerService.getCustomerById(any(UUID.class))).willReturn(testCustomer);
		
		mockMvc.perform(get(CustomerController.PATH_V1_CUSTOMER_ID, testCustomer.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(testCustomer.getId().toString())))
				.andExpect(jsonPath("$.name", is(testCustomer.getName())));
	}

	@Test
	void testCreateCustomer() throws JsonProcessingException, Exception {
		
		CustomerDto customer = testCustomer;
		customer.setId(null);
		
		given(customerService.createCustomer(any(CustomerDto.class))).willReturn(testCustomers.get(1));
		
		mockMvc.perform(post(CustomerController.PATH_V1_CUSTOMER)
				.content(objectMapper.writeValueAsString(customer))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(header().exists(HttpHeaders.LOCATION));
	}

	@Test
	void testUpdateCustomerById() throws JsonProcessingException, Exception {
		
		given(customerService.updateCustomerById(any(UUID.class), any(CustomerDto.class))).willReturn(Optional.of(CustomerDto.builder().build()));
		
		mockMvc.perform(put(CustomerController.PATH_V1_CUSTOMER_ID, testCustomer.getId())
				.content(objectMapper.writeValueAsString(testCustomer))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		verify(customerService).updateCustomerById(any(UUID.class), any(CustomerDto.class));
	}

	@Test
	void testPatchCustomerById() throws JsonProcessingException, Exception {
		
		Map<String, Object> customerMap = new HashMap<>();
		customerMap.put("name", testCustomerName);
		
		given(customerService.patchCustomerById(any(UUID.class), any(CustomerDto.class))).willReturn(Optional.of(CustomerDto.builder().build()));
		
		mockMvc.perform(patch(CustomerController.PATH_V1_CUSTOMER_ID, testCustomer.getId())
				.content(objectMapper.writeValueAsString(customerMap))
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		verify(customerService).patchCustomerById(uuidArgumentCaptor.capture(), customerArgumentCaptor.capture());
		
		assertThat(testCustomer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
		assertThat(customerMap.get("name")).isEqualTo(customerArgumentCaptor.getValue().getName());
	
	}

	@Test
	void testDeleteCustomerById() throws Exception {
		
		given(customerService.deleteCustomerById(any(UUID.class))).willReturn(true);
		
		mockMvc.perform(delete(CustomerController.PATH_V1_CUSTOMER_ID, testCustomer.getId())
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNoContent());
		
		verify(customerService).deleteCustomerById(uuidArgumentCaptor.capture());
		
		assertThat(testCustomer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
	}

}
