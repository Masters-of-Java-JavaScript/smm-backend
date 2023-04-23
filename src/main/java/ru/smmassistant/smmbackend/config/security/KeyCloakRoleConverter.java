package ru.smmassistant.smmbackend.config.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * The class converts roles from <b>JWT</b> to <b>Spring Security</b> roles.
 * After that, they are already tied to the backend user, via the <b>GrantedAuthority</b> interface.
 *
 * @see GrantedAuthority
 * */
public class KeyCloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    /**
     * Method for converting roles from <b>JWT</b> to <b>Collection</b> of <b>GrantedAuthority</b>.
     *
     * @param jwt <b>AccessToken</b> from frontend application
     * @return <b>Collection</b> of <b>GrantedAuthority</b>
     * */
    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {

        /* Getting access to the "realm_access" section in accessToken */
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");

        /* If the realm_access section is not found, then there are no roles */
        if (realmAccess == null || realmAccess.isEmpty()) {
            return new ArrayList<>();
        }

        /*
         * Mapping roles to GrantedAuthority so that the Spring container understands JWT roles
         * SimpleGrantedAuthority - default implementation of GrantedAuthority
         * The prefix ROLE_ is required
         */
        Collection<GrantedAuthority> returnValue = ((List<String>) realmAccess.get("roles"))
            .stream().map(roleName -> "ROLE_" + roleName)
            .map(SimpleGrantedAuthority::new)
            .collect(Collectors.toList());

        return returnValue;
    }
}
