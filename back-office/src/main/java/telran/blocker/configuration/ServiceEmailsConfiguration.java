package telran.blocker.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import telran.blocker.model.ServiceEmailsDoc;

@RequiredArgsConstructor
@Configuration
@Getter
public class ServiceEmailsConfiguration {

	@SuppressWarnings("serial")
	List<ServiceEmailsDoc> ConfigurationSEList = new ArrayList<>() {
		
		{
			add(new ServiceEmailsDoc("web_A_service", new String[] {"hana2user@gmail.com", "hana2user+1@gmail.com" }));
			add(new ServiceEmailsDoc("web_B_service", new String[] {"hana2user@gmail.com", "hana2user+2@gmail.com" }));
			add(new ServiceEmailsDoc("web_C_service", new String[] {"hana2user@gmail.com", "hana2user+3@gmail.com" }));
			add(new ServiceEmailsDoc("web_D_service", new String[] {"hana2user@gmail.com", "hana2user+4@gmail.com" }));
		
		}
	};
	
}