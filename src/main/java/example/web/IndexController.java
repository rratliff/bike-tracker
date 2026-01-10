/*
 * Copyright 2002-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package example.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * OAuth2 Log in controller.
 *
 * @author Joe Grandja
 * @author Rob Winch
 */
@Controller
public class IndexController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public IndexController(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/")
    public String index(Model model,
                        @AuthenticationPrincipal OAuth2User oauth2User,
                        OAuth2AuthenticationToken oauth2Auth,
                        @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient) {
        if (oauth2Auth != null && oauth2User != null) {
            if (authorizedClient != null) {
                model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
            } else if (oauth2Auth.getAuthorizedClientRegistrationId() != null) {
                ClientRegistration clientRegistration = this.clientRegistrationRepository
                    .findByRegistrationId(oauth2Auth.getAuthorizedClientRegistrationId());
                if (clientRegistration != null) {
                    model.addAttribute("clientName", clientRegistration.getClientName());
                }
            }
            model.addAttribute("userName", oauth2User.getName());
            model.addAttribute("userAttributes", oauth2User.getAttributes());
        }
        return "index";
    }

}
