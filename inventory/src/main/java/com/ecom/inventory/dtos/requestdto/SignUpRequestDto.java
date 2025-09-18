package com.ecom.inventory.dtos.requestdto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

    @Min(5) @Max(10)
    private String name;

    @Email
    private String email;
    @NotNull @Min(5) @Max(10) @NotBlank
    private String password;
    private String address;

    private String type;

    @Min(6) @Max(6)
    private String otp;
}
