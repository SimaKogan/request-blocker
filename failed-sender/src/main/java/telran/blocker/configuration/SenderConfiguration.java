package telran.blocker.configuration;

import java.util.*;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;

@Configuration
@Getter
public class SenderConfiguration {
	@Value("${app.sender.binding.name:senderIpData-out-0}")
	String bindingName;
	
	@SuppressWarnings("serial")
	HashMap<String, Integer> IPsMap = new HashMap<>() {
		{
			put("151.151.151.151", 1);
			put("152.152.152.152", 2);
			put("153.153.153.153", 3);
			put("154.154.154.154", 4);
			put("155.155.155.155", 5);
			put("156.156.156.156", 6);
			put("157.157.157.157", 7);
			put("158.158.158.158", 8);
			put("159.159.159.159", 9);
			put("160.160.160.160", 10);
		}
	};
	Set<String> webServiceSet = new HashSet<>(Arrays.asList(
			"web_A_service",
			"web_B_service",
			"web_C_service"
			));
	
}