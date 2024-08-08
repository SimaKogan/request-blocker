package telran.blocker.components;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.configuration.ServiceEmailsConfiguration;
import telran.blocker.model.IpDataDoc;
import telran.blocker.model.ServiceEmailsDoc;
import telran.blocker.repo.IpDataRepo;
import telran.blocker.repo.ServiceEmailsRepo;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServiceEmailsPopulation {

	final ServiceEmailsRepo seRepo;
	final IpDataRepo ipRepo;
	final ServiceEmailsConfiguration configuration;

	@PostConstruct
	void setUp() {
		List<ServiceEmailsDoc> listSE = seRepo.findAll();
		if (!listSE.isEmpty()) {
			log.debug("ServiceEmailsRepo is not empty");
		} else {
			populationSERepo();
		};
		
		List<IpDataDoc> listIP = ipRepo.findAll();
		if(!listIP.isEmpty()) {
			log.debug("IpDataRepo is not empty");
		} else {
			populateIPRepo();
		};
	}


	public void populationSERepo() {
		List<ServiceEmailsDoc> configurationSEList = configuration.getConfigurationSEList();
		seRepo.saveAll(configurationSEList);
		log.debug("Configuration service-emails list has been saved to ServiceEmailsRepo");

	}
	
	private void populateIPRepo() {
		List<IpDataDoc> configurationIPList = configuration.getConfigurationIPList();
		ipRepo.saveAll(configurationIPList);
		log.debug("Configuration IP list has been saved to IpDataRepo");
		
	}
}