package telran.blocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "telran")
public class BlockingDataProviderAppl {

	public static void main(String[] args) {
		SpringApplication.run(BlockingDataProviderAppl.class, args);

	}

}