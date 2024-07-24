package telran.blocker.service;

import java.util.List;

import telran.blocker.dto.IpData;

public interface AnalyzerService {
	List<IpData> getList(IpData ipData);

	List<IpData> getValue(IpData ipData);

}