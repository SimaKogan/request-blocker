package telran.blocker;

import java.util.List;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.dto.IpData;
import telran.blocker.service.AnalyzerService;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class AnalyzerAppl {
	final AnalyzerService service;
	final StreamBridge streamBridge;
	
	@Value("${app.analyzer.binding.name}")
	String bindingName;
	
	public static void main(String[] args) {
		SpringApplication.run(AnalyzerAppl.class, args);
		
	}
	@Bean
	Consumer<IpData> consumerAnalyzer() {
		return this::processAnalyzer;
	}
	
	void processAnalyzer(IpData ipData) {
		log.trace("IPData: {}", ipData);
		String IP = ipData.IP();
		List<IpData> list = service.getList(ipData);
		if(list != null) {
			for(IpData 1: list) {
				log.trace("IP: {} service {} candidate to blocking list", 1.IP(), 1.webService());
				streamBridge.send(bindingName,  1);
			}
		} else {
			log.trace("IP {} not yet candidate", IP);
		}
		
	}
	
}