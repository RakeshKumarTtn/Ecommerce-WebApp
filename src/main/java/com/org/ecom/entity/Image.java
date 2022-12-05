package com.org.ecom.entity;

import com.org.ecom.entity.registration.UserEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Table(name = "IMAGES")
@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long imageId;

    @Column(name = "ImageName")
    private String imageName;

    @Column(name = "Remark")
    private String remarks;

    @Column(name = "User_Id")
    UserEntity userEntity;
}
