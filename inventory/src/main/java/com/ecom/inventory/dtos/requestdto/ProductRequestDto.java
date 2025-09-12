package com.ecom.inventory.dtos.requestdto;


import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRequestDto {
    @NotNull @NotBlank @Min(5)
    @Max(50)
    private String name;

    @NotNull @Min(50)
    private Double cost;

    private String supplier;

    @NotEmpty
    private String supplierLocation;
    private String specs;

    @Min(1) @Max(50)
    private Integer count;
    private Double discount; // optional at creation
}

