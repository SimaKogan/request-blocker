package telran.blocker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.model.RedisModel;
import telran.blocker.repo.RedisRepo;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class RedisExplorerAppl {
	
	final RedisRepo redisRepo;
	private static final long TIMEOUT = 20000;
	
	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication.run(RedisExplorerAppl.class, args);
		Thread.sleep(TIMEOUT);
		ctx.close();
		
	}

	@Bean
	String whatInRedisTest() {
		
		Iterable<RedisModel> allRepo = redisRepo.findAll();
		if(!allRepo.iterator().hasNext()) {
			log.debug("Redis is emply");
		} else {
			allRepo.forEach(it -> {
				log.debug("Tour in Redis for IP: {}", it.getIP());
				it.getWebServicesTimestamps()
				.stream()
				.forEach(l -> 
				log.debug("WebService: {}, Timestamp: {}", l.webService(), l.timestamp()));
		    	}
			);
		}
		
		log.debug("Tour in Redis finished");
		return "Tour in Redis finished.";
	}
	
	
	String deleteAll() {
		redisRepo.deleteAll();
		log.debug("Redis is cleaned");
		return "Redis is cleaned";
		
	}
	
}