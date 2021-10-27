package com.example.demo.config;


import com.example.demo.entity.AuthorityEntity;
import com.example.demo.entity.UserEntity;
import com.example.demo.entity.converter.AuthorityType;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

import static org.springframework.security.core.userdetails.User.withUsername;


@Component
@RequiredArgsConstructor
public class UserDetailsServiceImp implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserName(username);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found.");
        }

        String[] authorities = userEntity.getAuthorities().stream()
                .map(AuthorityEntity::getAuthority)
                .map(AuthorityType::name)
                .toArray(String[]::new);

        User.UserBuilder builder = withUsername(username);
        builder.disabled(!userEntity.isEnabled());
        builder.password(userEntity.getPassword());
        builder.authorities(authorities);

        return builder.build();
    }
}
