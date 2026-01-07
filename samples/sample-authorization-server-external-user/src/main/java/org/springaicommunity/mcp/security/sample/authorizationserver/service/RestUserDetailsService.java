package org.springaicommunity.mcp.security.sample.authorizationserver.service;


import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RestUserDetailsService implements UserDetailsService {

    private final RestClient restClient;
    private final PasswordEncoder passwordEncoder;

    public RestUserDetailsService(RestClient.Builder restClientBuilder, PasswordEncoder passwordEncoder) {
        this.restClient = restClientBuilder
                // TODO: set your real user service base URL
                .baseUrl("http://localhost:8081")
                .build();

        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /*
        try {
            UserDto userDto = restClient.get()
                    .uri("/users/{username}", username)
                    .retrieve()
                    .body(UserDto.class);

            if (userDto == null) {
                throw new UsernameNotFoundException("User not found: " + username);
            }

            return User.builder()
                    .username(userDto.username())
                    .password(userDto.password())
                    .authorities(userDto.roles().stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList()))
                    .build();
        } catch (Exception e) {
            throw new UsernameNotFoundException("Error fetching user: " + username, e);
        }
         */
        List<String> roles = List.of("ROLE_USER");
        return User.builder()
                .username(username)
                //.password("{noop}password") without password encoder
                .password(passwordEncoder.encode("password"))
                .authorities(roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList()))
                .build();
    }

    public record UserDto(String username, String password, List<String> roles) {}
}
