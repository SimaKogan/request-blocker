package telran.blocker.service;

import telran.blocker.dto.IpData;
import telran.blocker.dto.ServiceEmails;

public interface BackOfficeService {
	ServiceEmails addServiceEmails(ServiceEmails serviceEmails);
	ServiceEmails deleteServiceEmails(String webService);
	
	IpData addIpData(IpData ipData);
	IpData deleteIpData(String ip);
	
}