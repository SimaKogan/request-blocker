package telran.blocker.components;

import java.util.List;

import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.configuration.IpDataConfiguration;
import telran.blocker.model.IpDataDoc;
import telran.blocker.repo.IpDataRepo;

@Component
@Slf4j
@RequiredArgsConstructor
public class IpDataPopulation {

	final IpDataRepo ipDataRepo;
	final IpDataConfiguration configuration;
	
	@PostConstruct
	void setUp( ) {
		List<IpDataDoc> listAll = ipDataRepo.findAll();
		if (!listAll.isEmpty()) {
			log.debug("IpDataRepo is not empty");
		} else {
			populationIpDataRepo();
		}

	}
	
	public void populationIpDataRepo() {
		List<IpDataDoc> configurationIpDataList = configuration.getConfigurationIpDataList();
		ipDataRepo.saveAll(configurationIpDataList);
		log.debug("Configuration ip data list has been saved to IpDataRepo");
	}

}