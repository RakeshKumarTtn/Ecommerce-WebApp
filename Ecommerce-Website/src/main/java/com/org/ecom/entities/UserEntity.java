package com.org.ecom.entities;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;
import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


@Getter
@Setter
@Table(name = "USER")
@Inheritance(strategy = InheritanceType.JOINED)

@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class UserEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long userId;

    @NotEmpty
    @Column(name = "USER_NAME", unique = true)
    private String username;

    @Column(name = "FIRST_NAME", updatable = true, nullable = false)
    private String firstName;

    @Column(name = "MIDDLE_NAME", updatable = true, nullable = false)
    private String middleName;

    @Column(name = "LAST_NAME", updatable = true, nullable = false)
    private String lastName;

    @Column(name = "EMAIL", unique = true, updatable = true, nullable = false)
    private String email;

    @Column(name = "MOBILE", unique = true, updatable = true, nullable = false)
    private Long mobile;

    @Column(name = "PASSWORD", updatable = true, nullable = false)
    private String password;

    @Transient
    private String confirmPassword;

    @CreatedDate
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

    @CreatedBy
    @Column(nullable = false, updatable = false)
    private String createdBy;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime created;

    @LastModifiedBy
    @Column(nullable = false)
    private String modifiedBy;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime modified;

    @OneToMany(mappedBy = "userEntity", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Address> userAddress;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "USER_ROLE",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID", referencedColumnName = "roleId"))
    private List<Role> roles;

    public void addAddress(Address address) {
        if (address != null) {
            if (userAddress == null) {
                userAddress = new HashSet<>();
            }
            address.setUserEntity(this);
            userAddress.add(address);
        }
    }

    public void addRole(Role role) {
        if (role != null) {
            if (roles == null) {
                roles = new ArrayList<>();
            }
            roles.add(role);
        }
    }
}
