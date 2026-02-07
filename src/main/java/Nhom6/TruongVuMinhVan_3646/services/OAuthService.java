package Nhom6.TruongVuMinhVan_3646.services;

import Nhom6.TruongVuMinhVan_3646.entities.Role;
import Nhom6.TruongVuMinhVan_3646.entities.User;
import Nhom6.TruongVuMinhVan_3646.repositories.IRoleRepository;
import Nhom6.TruongVuMinhVan_3646.repositories.IUserRepository;
import Nhom6.TruongVuMinhVan_3646.constants.Provider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class OAuthService extends DefaultOAuth2UserService {

    private static final Logger logger = LoggerFactory.getLogger(OAuthService.class);

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        logger.info("OAuth2 login - processing user: {}", oauth2User.getAttributes().get("email"));

        // Get or create user in database
        User user = processOAuthPostLogin(oauth2User.getAttributes());

        // Build authorities from database roles
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        if (user != null) {
            logger.info("User found/created: {} with {} roles", user.getUsername(),
                    user.getRoles() != null ? user.getRoles().size() : 0);

            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                authorities = user.getRoles().stream()
                        .map(role -> {
                            logger.info("Adding authority: {}", role.getName());
                            return new SimpleGrantedAuthority(role.getName());
                        })
                        .collect(Collectors.toList());
            }
        }

        // If no roles, add default USER authority
        if (authorities.isEmpty()) {
            logger.warn("No roles found for user, adding default USER authority");
            authorities.add(new SimpleGrantedAuthority("USER"));
        }

        logger.info("Final authorities: {}", authorities);

        // Return OAuth2User with database authorities
        return new DefaultOAuth2User(
                authorities,
                oauth2User.getAttributes(),
                "email" // Use email as the name attribute key
        );
    }

    @Transactional
    public User processOAuthPostLogin(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // Use email as username
        String username = (email != null) ? email : name;

        if (username != null) {
            return saveOrUpdateOAuthUser(email, username);
        }
        return null;
    }

    @Transactional
    private User saveOrUpdateOAuthUser(String email, String username) {
        // Check if user already exists
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            logger.info("Existing user found: {}, roles: {}", user.getUsername(), user.getRoles());

            // If user exists but has no roles, add USER role
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                logger.info("User has no roles, adding USER role");
                Role userRole = roleRepository.findRoleById(2L);
                if (userRole != null) {
                    Set<Role> roles = new HashSet<>();
                    roles.add(userRole);
                    user.setRoles(roles);
                    user = userRepository.save(user);
                    logger.info("Role assigned successfully");
                } else {
                    logger.error("USER role (id=2) not found in database!");
                }
            }
            return user;
        }

        logger.info("Creating new user: {}", username);

        // Create new user
        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        user.setProvider(Provider.GOOGLE.value);

        // Assign USER role (id = 2) to OAuth users
        Role userRole = roleRepository.findRoleById(2L);
        if (userRole != null) {
            logger.info("Assigning USER role to new user");
            Set<Role> roles = new HashSet<>();
            roles.add(userRole);
            user.setRoles(roles);
        } else {
            logger.error("USER role (id=2) not found in database! Check role table.");
        }

        user = userRepository.save(user);
        logger.info("New user saved with id: {}", user.getId());

        return user;
    }
}