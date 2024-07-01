package telran.blocker;

import java.util.HashMap;
import java.util.stream.IntStream;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.configuration.SenderConfiguration;
import telran.blocker.dto.IpData;
import telran.blocker.service.SenderService;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class SenderAppl {
	public static final long TIMEOUT = 85000;
	final SenderService senderService;
	final StreamBridge streamBridge;
	final SenderConfiguration senderConfiguration;
	
	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication.run(SenderAppl.class, args);
		Thread.sleep(TIMEOUT);
		ctx.close();
	}
	
	@Scheduled(fixedDelayString = "${app.sender.fixed-delay:90000}")
	void roundMap() {
		HashMap<String, Integer> IPsMap = senderConfiguration.getPsMap();
		IPsMap.forEach((key, value) -> {
			getRandomipData(key, value);
			goSleep(value);
		});
	}
	
	void getRandomipData(String IP, int quality) {
		IntStream.range(0, quantity).forEach(q -> {
			String WSN = senderService.getRandomWEB_ServiceName();
			IpData ipData = senderService.getRandomIpData(IP, WSN);
			log.debug("ipData: {}", ipData);
			sendIpData(ipData);
			goSleep(1);
		});
	}
	
	void sendIpData(IpData ipData) {
		String bindingName = senderConfiguration.getBindingName();
		streamBridge.send(bindingName, ipData);
		log.debug("ipData: {}, bindingName {}", ipData, bindingName):
	}
	
	private void goSleep(int msec) {
		try {
			Thread.sleep(msec * 1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			}
	}
}
