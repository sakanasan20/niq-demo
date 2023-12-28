package tw.niq.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.demo.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
