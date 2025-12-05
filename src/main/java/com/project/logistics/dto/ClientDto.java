package com.project.logistics.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClientDto {

    private Integer id;

    private Integer userId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
}
