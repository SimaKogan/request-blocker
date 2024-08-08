package telran.blocker.service;

import java.util.List;
import java.util.Set;

import telran.blocker.dto.IpData;

public interface BlockingDataProviderService {
	boolean existsById (String IP);
	List<IpData> getBlockingList ();
	IpData getIpData (String IP);
	Set<String> getSetIps();

}