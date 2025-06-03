package unpestudantes.sistema.biblioteca;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LoginappApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoginappApplication.class, args);
	}

}