package telran.blocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "telran")
public class BackOfficeAppl {
	
	public static void main(String[] args) {
		SpringApplication.run(BackOfficeAppl.class, args);
		
	}

}