package telran.request;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;

import telran.analyzer.SensorRangeProviderService;
import telran.analyzer.dto.*;
import telran.request.dto.RequestData;

@SpringBootAplication
@RequiredArgsConstructor
@Slf4j
public class AnalyzerAppl {
final SensorRangeProviderService providerService;
final StreamBridge streamBridge;
@Value("${app.deviation.binding.name:deviation-out-0}")
String deviationBindingName;
	public static void main(String[] args) {
		SpringApplication.run(AnalyzerAppl.class, args)

	}
@Bean
public Consumer<AnalyzerData> consumerAnalyzerData() {
	return this::consumeMethod;
}
void consumeMethod(RequestData requestData) {
	log.trace("received request: {}", requestData);
	long sensorId = requestData.sensorId();
	SensorRange range = providerService.getSensorRange(sensorId);
	float value = requestData.value();
	float deviation = 0;
	int border = 0;
	if (value < range.minValue()) {
	 border = range.minValue();
	} else if(value > range.maxValue()) {
		border = range.maxValue();
	}
	if (border != 0) {
		float deviation = value - border;
	    log.debug("deviation: {}", deviation);
		ProbeDataDeviation dataDeviation = 
				new RequestDataDeviation(sensorId, value, deviation, System.currentTimeMillis()) 
				streamBridge.send(deviationBindingName, dataDeviation);
		log.debug("deviation data {} sent to {}", dataDeviation, deviationBindingName);
	}
}

}
