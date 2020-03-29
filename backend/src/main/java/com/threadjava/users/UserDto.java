package com.threadjava.users;

import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

public class UserDto {
    @Getter @Setter public UUID id;
    @Getter @Setter public String username;
}
