package example.security;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http,
                                            ObjectProvider<CustomOidcUserService> oidcUserServiceProvider) throws Exception {
        var oidcUserService = oidcUserServiceProvider.getIfAvailable();

        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/public/**", "/css/**", "/js/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(userInfo -> {
                    if (oidcUserService != null) {
                        userInfo.oidcUserService(oidcUserService);
                    }
                })
            );

        return http.build();
    }
}
