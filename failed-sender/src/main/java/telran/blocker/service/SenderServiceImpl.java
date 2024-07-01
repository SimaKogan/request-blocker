package telran.blocker.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.blocker.configuration.SenderConfiguration;
import telran.blocker.dto.IpData;

@Service
@RequiredArgsConstructor
public class SenderServiceImpl implements SenderService {
	final SenderConfiguration senderConfiguration;
	
	@Override
	public String getRandomWEB_ServiceName() {
		Set<String> webServiceSet = senderConfiguration.getWebServiceSet();
		int quantityElements = webServicesSet.size();
		int n = getRandomInt(quantityElements);
		List<String> list = new ArrayList<>(webServicesSet);
		return list.get(n);
	}
	
	@Override
	public IpData getRandomIpData(String IP, String webService) {
		return new IpData(IP, webService, System.currentTimeMillis());
	}
	
	private int getRandomInt(int max) {
		
		return ThreadLocalRandom.current().nextInt(0, max);
	}
	
}