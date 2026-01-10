package example.security;

import example.user.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import java.util.Set;

import static java.util.stream.Collectors.toSet;

@Service
public class CustomOidcUserService extends OidcUserService {

    private final UserRepository userRepository;

    public CustomOidcUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        OidcUser oidcUser = super.loadUser(userRequest);

        String subject = oidcUser.getSubject();
        if (subject == null || subject.isBlank()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_user_info", "OIDC subject (sub) is missing", null));
        }

        // Verify local user exists by oauth_subject
        var userOpt = userRepository.findByOauthSubject(subject);
        if (userOpt.isEmpty()) {
            throw new OAuth2AuthenticationException(new OAuth2Error("user_not_registered", "User not registered: " + subject, null));
        }

        // Keep provider authorities; alternatively, map local roles here when available
        Set<GrantedAuthority> authorities = oidcUser.getAuthorities().stream().collect(toSet());

        // Return a DefaultOidcUser to preserve tokens and claims
        return new DefaultOidcUser(authorities, oidcUser.getIdToken(), oidcUser.getUserInfo());
    }
}
