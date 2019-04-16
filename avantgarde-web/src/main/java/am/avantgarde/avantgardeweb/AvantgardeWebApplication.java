package am.avantgarde.avantgardeweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@EnableJpaRepositories(basePackages = "am.avantgarde.avantgardecommon.repository")
@EntityScan(basePackages = "am.avantgarde.avantgardecommon.model")
@ComponentScan(basePackages = {"am.avantgarde.avantgardecommon.*", "am.avantgarde.avantgardeweb.*"})
@SpringBootApplication
public class AvantgardeWebApplication {

    public static void main(String[] args) {
        SpringApplication.run(AvantgardeWebApplication.class, args);
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

}
