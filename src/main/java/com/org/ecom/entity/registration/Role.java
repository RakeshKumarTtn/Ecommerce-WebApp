package com.org.ecom.entity.registration;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "ROLE")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @ManyToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    private Set<UserEntity> users;

    @Column(name = "AUTHORITY", updatable = true, nullable = false)
    private String authority;

}
