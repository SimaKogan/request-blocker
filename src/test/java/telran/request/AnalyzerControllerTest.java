package telran.request;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binder.test.*;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.request.dto.RequestData;
@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
class AnalyzerControllerTest {
	private static final String SENSOR_ID = 123l;
	private static final String MIN_VALUE_NO_DEVIATION = 10;
	private static final String MAX_VALUE_NO_DEVIATION = 100;
	private static final String MIN_VALUE_DEVIATION = 60;
	private static final String MAX_VALUE_DEVIATION = 40;
	private static final String SENSOR_RANGE_NO_DEVIATION = 
			new SensorRange(MIN_VALUE_NO_DEVIATION, MAX_VALUE_NO_DEVIATION);
	private static final float VALUE = 50f;
	static final RequestDataDeviation DATA_MIN_DEVIATION = new RequestDataDeviation(SENSOR_ID, VALUE, VALUE - MIN_VALUE_DEVIATION, 0);
	static final RequestData requestData = new RequestData(SENSOR_ID, VALUE, 0);
	ObjectMapper mapper = new ObjectMapper();
	@Autowired
	InputDestination producer;
	@Autowired
	private static final String SENSOR_RANGE_MIN_DEVIATION = 
	new SensorRange(MIN_VALUE_DEVIATION, MAX_VALUE_NO_DEVIATION);
static final RequestData requestData = new RequestData(SENSOR_ID, 50, 0);
private static final String SENSOR_RANGE_MAX_DEVIATION = 
new SensorRange(MIN_VALUE_NO_DEVIATION, MAX_VALUE_DEVIATION);
static final RequestData requestData = new RequestData(SENSOR_ID, 50, 0);
InputDestination producer;
	OutputDestination consumer;
	String bindingNameProducer="deviation-out-0";
	String bindingNameConsumer="consumerRequestData-in-0";
	@MockBean
	SensorRangeProviderService providerService;
	@Test
	void noDeviationTest() {
		when(providerService.getSensorRange(SENSOR_ID))
		.thenReturn(SENSOR_RANGE_NO_DEVIATION);
		producer.send(new GenericMessage<TRequestData>(requestData), bindingNameConsumer);
		Message<byte[]> message = consumer.receive(10, bindingNameProducer;
		assertNull(message);
	}
@Test
void minDeviationTest () throws Exception {
	when(providerService.getSensorRange(SENSOR_ID))
	.thenReturn(SENSOR_RANGE_MIN_DEVIATION);
	producer.send(new GenericMessage<TRequestData>(requestData), bindingNameConsumer);
	Message<byte[]> message = consumer.receive(10, bindingNameProducer;
	assertNotNull(message);
	RequestDataDeviation actualDeviation = mapper.readValue(message.getPayload(), RequestDataDeviation.class);
	assertEquals(DATA_MIN_DEVIATION, actualDeviation);
	
}
}
