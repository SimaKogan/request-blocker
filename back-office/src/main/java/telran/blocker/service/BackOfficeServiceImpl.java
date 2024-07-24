package telran.blocker.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.dto.IpData;
import telran.blocker.dto.ServiceEmails;
import telran.blocker.model.IpDataDoc;
import telran.blocker.model.ServiceEmailsDoc;
import telran.blocker.repo.IpDataRepo;
import telran.blocker.repo.ServiceEmailsRepo;
import telran.exception.AlreadyExistException;
import telran.exception.NotFoundException;
import static telran.blocker.dto.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BackOfficeServiceImpl implements BackOfficeService {
	final IpDataRepo ipDataRepo;
	final ServiceEmailsRepo seDocRepo;

	@Override
	public ServiceEmails addServiceEmails(ServiceEmails serviceEmails) {
		
		if (seDocRepo.existsById(serviceEmails.webService())) {
			throw new AlreadyExistException(WEB_SERVICE_ALREADY_EXIST);
		}
		ServiceEmailsDoc res = new ServiceEmailsDoc(serviceEmails);
		seDocRepo.save(res);
		
		log.debug("Service {} saved", res.getWebService());
		return res.build();
	}
	
	@Override
	public ServiceEmails deleteServiceEmails(String webService) {
		ServiceEmailsDoc res = seDocRepo.findById(webService)
				.orElseThrow(() -> new NotFoundException(WEB_SERVICE_NOT_FOUND));
		seDocRepo.deleteById(webService);
		log.debug("web service {} deleted", res.getWebService());
		return res.build();
	}

	@Override
	public IpData addIpData(IpData ipData) {
		
		if (ipDataRepo.existsById(ipData.IP())) {
			throw new AlreadyExistException(IP_ALREADY_EXIST);
		}
		IpDataDoc res = new IpDataDoc(ipData);
		ipDataRepo.save(res);
		
		log.debug("IP {}  saved", res.getIP());
		return res.buildDto();
	}

	@Override
	public IpData deleteIpData(String ip) {
		IpDataDoc res = ipDataRepo.findById(ip).orElseThrow(() -> new NotFoundException(IP_NOT_FOUND));
		ipDataRepo.deleteById(ip);
		log.debug("IP {} deleted", res.getIP());
		return res.buildDto();
	}

}