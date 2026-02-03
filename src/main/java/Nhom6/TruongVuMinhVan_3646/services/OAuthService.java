package Nhom6.TruongVuMinhVan_3646.services;

import Nhom6.TruongVuMinhVan_3646.entities.User;
import Nhom6.TruongVuMinhVan_3646.repositories.IUserRepository;
import Nhom6.TruongVuMinhVan_3646.constants.Provider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class OAuthService extends DefaultOAuth2UserService {
    
    @Autowired
    private IUserRepository userRepository;
    
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);
        
        // Xử lý thông tin user từ OAuth2 provider
        processOAuthPostLogin(oauth2User.getAttributes());
        
        return oauth2User;
    }
    
    public void processOAuthPostLogin(Map<String, Object> attributes) {
        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");
        
        // Sử dụng email làm username nếu có, nếu không thì dùng name
        String username = (email != null) ? email : name;
        
        if (username != null) {
            saveOAuthUser(email, username);
        }
    }
    
    private void saveOAuthUser(String email, String username) {
        if (userRepository.findByUsername(username).isPresent())
            return;
        var user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(new BCryptPasswordEncoder().encode(username));
        user.setProvider(Provider.GOOGLE.value);
        userRepository.save(user);
    }
}