package no.group3.springquiz;
import no.group3.springquiz.data.*;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by josoder on 06.12.17.
 */
@RestController
public class AuthApi {
    private UserDetailsService userDetailsService;
    private AuthenticationManager authenticationManager;
    private UserService userService;

    public AuthApi(UserDetailsService userDetailsService, AuthenticationManager authenticationManager,
                   UserService userService) {
        this.userDetailsService = userDetailsService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @RequestMapping("/user")
    public ResponseEntity<AuthUserDto> currentUser(Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthUserDto dto = new AuthUserDto(principal.getName(),
                authentication.getAuthorities().stream().map(r -> r.getAuthority()).collect(Collectors.toSet()));
        return ResponseEntity.ok(dto);
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> register(@ModelAttribute(name = "the_user") String username,
                                         @ModelAttribute(name = "the_password") String password){
        if(userService.userExists(username)){
            return ResponseEntity.status(409).build();
        }

        if(password==null||password.length() < 4){
            return ResponseEntity.status(400).build();
        }

        boolean created = userService.createUser(username, password, Stream.of("USER").collect(Collectors.toSet()));

        // Already checked the 2 constraints so probably is a bug
        if(!created){
            return ResponseEntity.status(500).build();
        }

        authUser(username, password);

        return ResponseEntity.status(201).build();
    }

    @PostMapping(value = "/signIn", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public ResponseEntity<Void> login(@ModelAttribute(name= "the_user") String username,
                                      @ModelAttribute(name= "the_password") String password){

        if(!userService.validateUser(username, password)){
            return ResponseEntity.status(403).build();
        }

        authUser(username, password);

        return ResponseEntity.status(204).build();
    }

    /**
     * Authenticate user
     * @param username
     * @param password
     * @return
     */
    private UsernamePasswordAuthenticationToken authUser(String username, String password) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                userDetails, password, userDetails.getAuthorities());
        authenticationManager.authenticate(token);
        if(token.isAuthenticated()){
            SecurityContextHolder.getContext().setAuthentication(token);
        }
        return token;
    }
}
