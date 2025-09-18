package com.ecom.inventory.dtos.requestdto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDto {

    @Min(5) @Max(10)
    private String name;
    @Min(5)
    @Max(100)
    private String address;
}
