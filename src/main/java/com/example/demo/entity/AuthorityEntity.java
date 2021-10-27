package com.example.demo.entity;


import com.example.demo.entity.converter.AuthorityAttributeConverter;
import com.example.demo.entity.converter.AuthorityType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Entity
@Table(name = "user_authority")
@Access(value = AccessType.FIELD)
public class AuthorityEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "authority")
    @Convert(converter = AuthorityAttributeConverter.class)
    private AuthorityType authority;

    @ManyToOne
    private UserEntity user;
}
