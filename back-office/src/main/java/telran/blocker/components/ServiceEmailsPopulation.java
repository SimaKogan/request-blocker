package telran.blocker.components;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.configuration.ServiceEmailsConfiguration;
import telran.blocker.model.ServiceEmailsDoc;
import telran.blocker.repo.ServiceEmailsRepo;

@Component
@Slf4j
@RequiredArgsConstructor
public class ServiceEmailsPopulation {

	final ServiceEmailsRepo seRepo;
	final ServiceEmailsConfiguration configuration;
	
	@PostConstruct
	void setUp( ) {
		List<ServiceEmailsDoc> listAll = seRepo.findAll();
		if (!listAll.isEmpty()) {
			log.debug("ServiceEmailsRepo is not empty");
		} else {
			populationSERepo();
		}

	}
	
	public void populationSERepo() {
		List<ServiceEmailsDoc> configurationSEList = configuration.getConfigurationSEList();
		seRepo.saveAll(configurationSEList);
		log.debug("Configuration service-email list has been saved to ServiceEmailRepo");
	}

}