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
	public static final long TIMEOUT = 110000;
	final SenderService senderService;
	final StreamBridge streamBridge;
	final SenderConfiguration senderConfiguration;
	
	public static void main(String[] args) throws InterruptedException {
		ConfigurableApplicationContext ctx = SpringApplication.run(SenderAppl.class, args);
		Thread.sleep(TIMEOUT);
		ctx.close();
	}
	
	private void goSleep(int msec) {
		try {
			Thread.sleep(msec * 1000);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			}
	}
	
	@Scheduled(fixedDelayString = "${app.sender.fixed-delay:120000}")
	void roundMap() {
		HashMap<String, Integer> IPsMap = senderConfiguration.getIPsMap();
		IPsMap.forEach((key, value);
		goSleep(value);
	});

}

void getRandomIpData(String IP, int quantity) {
	IntStream.range(0, quantity).forEach(q -> {
		String WSN = senderService.getRandomWeb_ServiceName();
		IpData ipData = senderService.getRandomIpData(IP, WSN);
		log.debug("ipData: {}", ipData);
		sendIpData(ipData);
		goSleep(1);
	});
}

void sendIpData(IpData ipData) {
	String bindingName = senderConfiguration.getBindingName();
	streamBridge.send(bindingName, ipData);
	log.debug("ipData: {}, bindingName {}", ipData, bindingName);
}



}