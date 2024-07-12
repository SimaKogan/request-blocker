package telran.blocker.service;

import telran.blocker.dto.IpData;
public interface SenderService {
	public String getRandomWeb_ServiceName();
	public IpData getRandomIpData (String IP, String webService);
}