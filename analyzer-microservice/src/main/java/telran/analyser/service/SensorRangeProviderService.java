package telran.analyser.service;

import telran.analyzer.dto.SensorRange;

public interface SensorRangeProviderService {
	SensorRange getSensorRange(long sensorId);
}
