package no.group3.springquiz;

import no.group3.springquiz.data.UserEntity;
import no.group3.springquiz.data.UserRepository;
import no.group3.springquiz.data.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@EnableZuulProxy
@SpringBootApplication
public class ZuulGatewayApplication {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(ZuulGatewayApplication.class, args);
	}
}

@Component
class CommandLineAppStartupRunner implements CommandLineRunner {
	@Autowired
	UserRepository userRepository;
	@Autowired
	PasswordEncoder passwordEncoder;

	@Override
	public void run(String...args) throws Exception {
		String role = "ROLE_USER";
		HashSet<String> roles= new HashSet<>();
		roles.add(role);
		userRepository.save(new UserEntity("admin",
				passwordEncoder.encode("password"), roles));
	}
}