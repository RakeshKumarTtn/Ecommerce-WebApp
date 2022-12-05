package com.org.ecom.entity.registration;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "ADMIN")
@Getter
@Setter
@NoArgsConstructor
@PrimaryKeyJoinColumn(name = "ID")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Admin extends UserEntity {

}
