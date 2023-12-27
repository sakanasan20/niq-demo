package tw.niq.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import lombok.Builder;
import lombok.Data;
import tw.niq.demo.domain.BeerStyle;

@Builder
@Data
public class BeerDto {

	private UUID id;
	
	private Integer version;
	
	private String beerName;
	
	private BeerStyle beerStyle;
	
	private String upc;
	
	private Integer quantityOnHand;
	
	private BigDecimal price;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime updateDate;
	
}
