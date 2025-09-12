package com.ecom.inventory.dtos.requestdto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
@AllArgsConstructor
public class UserProfileUpdateDto {

    private String name;
    private String password;
    private String address;
}
