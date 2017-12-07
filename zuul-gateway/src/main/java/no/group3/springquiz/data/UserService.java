package no.group3.springquiz.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by josoder on 06.12.17.
 */
@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean validateUser(String username, String password){
        UserEntity user = userRepository.findByUsername(username);

        if(user==null || (!user.getPassword().equals(passwordEncoder.encode(password)))){
            return false;
        }

        return true;
    }

    public boolean userExists(String username){
        return userRepository.exists(username);
    }

    public boolean createUser(String username, String password, Set<String> roles){
        try {
            String hash = passwordEncoder.encode(password);

            if(userRepository.exists(username)){
                return false;
            }

            UserEntity user = new UserEntity(username, hash, roles.stream().map(r -> r = "ROLE_" + r)
                    .collect(Collectors.toSet()));

            userRepository.save(user);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
