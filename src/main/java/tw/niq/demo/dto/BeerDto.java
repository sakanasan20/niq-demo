package tw.niq.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import tw.niq.demo.entity.BeerStyle;

@Builder
@Data
public class BeerDto {

	private UUID id;
	
	private Integer version;
	
	private LocalDateTime createdDate;
	
	private LocalDateTime updateDate;
	
	@NotBlank
	private String beerName;
	
	private BeerStyle beerStyle;
	
	private String upc;
	
	private Integer quantityOnHand;
	
	private BigDecimal price;
	
}
