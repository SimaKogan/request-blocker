package telran.blocker.service;

import java.unit.List;

import telran.blocker.dto.IpData;

public interface AnalyzerService {
	List<IpData> getList(IpData ipData);
	
}