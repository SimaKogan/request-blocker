package telran.blocker;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import telran.blocker.configuration.SenderConfiguration;
import telran.blocker.dto.IpData;

@SpringBootTest
@Slf4j
@Import(TestChannelBinderConfiguration.class)
class SenderTest {
	@Autowired
	OutputDestination consumer;
	@Autowired
	SenderConfiguration senderConfiguration;
	
	@Test
	void test() throws Excrption {
		ObjectMapper mapper = new ObjectMapper();
		String bindingName = senderConfiguration.getBindingName();
		long timestamp = System.currentTimeMillis();
		while (System.currentTimeMillis() - timestamp < SenderAppl.TIMEOUT / 10) {
			Message<byte[]> message = consumer.receive(1000, bindingName);
			if (message != null) {
				IpData IpData = mapper.readValue(message.getPayload(), IpData.class);
				log.debug("test: {}", IpData);
			}
			Thread.sleep(1000);
			
		}
	}
	
}