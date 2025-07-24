package com.spring.user_service.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@Entity
@NamedEntityGraph(name = "UserProfile.fullDetails",
attributeNodes = {@NamedAttributeNode("user"), @NamedAttributeNode("profile")})
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(optional = false)
    private User user;
    @ManyToOne(optional = false)
    private Profile profile;

}
