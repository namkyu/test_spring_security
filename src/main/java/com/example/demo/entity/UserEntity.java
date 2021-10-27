package com.example.demo.entity;

import com.example.demo.entity.converter.LocalDateTimeAttributeConverter;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;


@Getter
@Setter
@ToString
@Entity
@Table(name = "user")
@Access(value = AccessType.FIELD)
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "password")
    private String password;

    @Column(name = "enabled")
    private boolean enabled;

    @Column(name = "created")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime created;

    @Column(name = "changed")
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime changed;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "user", orphanRemoval = true)
    private Collection<AuthorityEntity> authorities = new ArrayList<>();

    public void addAuthority(AuthorityEntity authorityEntity) {
        this.authorities.add(authorityEntity);
        authorityEntity.setUser(this);
    }

    public void clearAuthority() {
        this.authorities.clear();
    }
}
