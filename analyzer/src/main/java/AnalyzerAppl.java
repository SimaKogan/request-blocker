package telran.request;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import telran.analyzer.SensorRangeProviderService;
import telran.analyzer.dto.AnalyzerData;

@SpringBootAplication
@RequiredArgsConstructor
public class AnalyzerAppl {
final SensorRangeProviderService providerService;
	public static void main(String[] args) {
		SpringApplication.run(AnalyzerAppl.class, args)

	}
@Bean
public Consumer<AnalyzerData> consumerAnalyzerData() {
	return this::consumeMethod;
}
void consumeMethod(AnalyzerData analyzerData)

}
