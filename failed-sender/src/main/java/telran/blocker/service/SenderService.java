package telran.blocker.service;

import telran.blocker.dto.IpData;
public interface SenderService {
	public String getRandomWEB_ServiceName();
	public IpData getRandomIpData (String IP, String webService);
}