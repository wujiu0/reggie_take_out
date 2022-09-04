package org.example.reggie.dto;

import lombok.Data;
import org.example.reggie.entity.User;

@Data
public class UserDto extends User {
    private String code;

}
