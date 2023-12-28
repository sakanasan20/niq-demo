package tw.niq.demo.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import tw.niq.demo.entity.Beer;

public interface BeerRepository extends JpaRepository<Beer, UUID> {

}
