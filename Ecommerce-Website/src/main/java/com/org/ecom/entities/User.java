package com.org.ecom.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.org.ecom.audit.Auditable;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@Getter
@Setter
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.JOINED)

@AllArgsConstructor
@NoArgsConstructor
//@EntityListeners(AuditingEntityListener.class)
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class User extends Auditable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long userId;


    @Column(name = "FIRST_NAME", updatable = true, nullable = false)
    private String firstName;

    @Column(name = "MIDDLE_NAME", updatable = true, nullable = false)
    private String middleName;


    @Column(name = "LAST_NAME", updatable = true, nullable = false)
    private String lastName;


    @Column(name = "EMAIL", updatable = true, nullable = false)
    private String email;

    @Column(name = "PASSWORD", updatable = true, nullable = false)
    private String password;

    @Transient
    private String confirmPassword;


    @Column(name = "PASSWORD_UPDATE_DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Temporal(TemporalType.TIMESTAMP)
    private Date passwordUpdateDate;

    @Column(name = "IS_DELETED", updatable = true)
    private Boolean isDeleted;

    @Column(name = "IS_ACTIVE", updatable = true)
    private Boolean isActive;

    @Column(name = "IS_EXPIRED", updatable = true)
    private Boolean isExpired;

    @Column(name = "IS_LOCKED", updatable = true)
    private Boolean isLocked;

    @Column(name = "INVALID_ATTEMPT_COUNT")
    private int invalidAttemptCount;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> userAddress;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "roleId"))
    private Set<Role> roles;

    public void addAddress(Address address) {
        if (address != null) {
            if (userAddress == null) {
                userAddress = new HashSet<>();
            }
            address.setUser(this);
            userAddress.add(address);
        }
    }

    public void addRole(Role role) {
        if (role != null) {
            if (roles == null) {
                roles = new HashSet<>();
            }
            roles.add(role);
        }
    }
}
