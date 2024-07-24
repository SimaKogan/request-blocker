package telran.blocker.configuration;


import java.util.List;

import org.springframework.context.annotation.Configuration;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import telran.blocker.dto.IpData;
import telran.blocker.model.IpDataDoc;

@RequiredArgsConstructor
@Configuration
@Getter
public class IpDataConfiguration {

	public List<IpDataDoc> getConfigurationIpDataList() {
		
		
		return IpData(IP, webService, timestamp);

	
		};

}