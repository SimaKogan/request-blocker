package telran.blocker;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.GenericMessage;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import telran.blocker.dto.IpData;
import telran.blocker.service.AnalyzerService;

@TestMethodOrder(MethodOrder.OrderAnnotation.class)
@SpringBootTest
@Slf4j
@Import(TestChannelBinderConfiguration.class)
class AnalyzerControllerTest {
	@Autowired
	InputDestination producer;
	@Autowired
	OutputDestination consumer;
	
	@MockBean
	AnalyzerService analyzerService;
	@Value("${app.analyzer.bing.name:analyzerData-out-0}")
	String producerBindihgName;
	String consumerBindingName = "consumerAnalyzer-in-0";
	
	
	
	static final telran.blocker.dto.IpData IpData NO_BLOCK_IP_DATA = new IpData("100.100.100.100", "WEBname1", 1000);
	static final IpData BLOCK_IP_DATA = new IpData("200.200.200.200", "name", 0);
	static final String ipBlocked = "200.200.200.200";
	static final IpData IP_DATA_WITH_BLOCKED_IP = new IpData(ipBlocked, "name", 0);
	
	@BeforeEach
	void setUp() {
		When(analyzerService.getList(BLOCK_IP_DATA))
		.thenReturn(List.of(BLOCK_IP_DATA, BLOCK_IP_DATA, BLOCK_IP_DATA, BLOCK_IP_DATA, BLOCK_IP_DATA));
		when(analyzerService.getList(NO_BLOCK_IPDATA)).thenReturn(null);
	}

	
	
	@Test
	@Order(1)
	void testIpNoBlock() {
		producer.send(new GenericMessage<>(NO_BLOCK_IP_DATA), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, producerBindingName);
		if(message != null) {
			log.debug("message: {}", message.toString());
		}
		assertNull(message);
	}
	
	@Test
	@Order(2)
	void testIpBlocked() throws Exception {
		producer.send(new GenericMessage<IpData>(BLOCK_IP_DATA), consumerBindingName);
		Message<byte[]> message = consumer.receive(10, prodicerBindingName);
		assertNotNull(message);
		ObjectMapper mapper = new ObjectMapper();
		IpData actual = mapper.readValue(message.getPayload(), IpData.class);
		assertEquals(IP_DATA_WITH_BLOCKED_IP, actual);
	}

}