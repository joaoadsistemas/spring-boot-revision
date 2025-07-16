package com.spring.user_service.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
public class User {

    @EqualsAndHashCode.Include
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

}
