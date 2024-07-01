package telran.blocker.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.dto.IpData;
import telran.blocker.dto.WebServiceTimestamps;
import telran.blocker.model.RedisModel;
import telran.bloker.repo.RedisRepo;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyzerServiceImpl implements AnalyzerService {
	
	final RedisRepo redisRepo;
	
	@Value("${app.analyzer.max.list.size: 5}")
	int maxListSize;
	
	
	@Override
	public List<IpData> getList(IpData ipData) {
		List<IpData> res = null;
		String IP = ipData.IP();
		WebServiceTimestamp wsTs = new WebServiceTimestamp(ipData.webService(), ipData.timestamp());
	log.debug("received IP: {} webService{} timestamp{} to Redis", IP, ipData.webService(), ipData.timestamp());
	RedisModel resFromRedis = redisRepo.findById(IP).orElse(null);
	if(resFromRedis == null) {
		resFromRedis = new RedisModel(ipData.IP());
	}
	List<WebServiceTimestamp> list = resFromRedis.getWebServicesTimestamps();
	list.add(wsTs);
	if(list.size() < maxListSize) {
		log.debug("list size is {}, not enough for placing to blocking list", list.size());
		redisRepo.save(resFromRedis);
	} else {
		res = new ArrayList<IpData>();
		for(WebServiceTimestamp 1: list) {
			ipData = new IpData(IP, 1.webService(), 1.timestamp());
			log.debug("IP: {} webService{} timestamp{} ready for placing to blocking list", IP, 1.webService(), 1.timestamp());
			res.add(ipData);
		}
		
		redisRepo.deleteById(IP);
	}
	
	return res;
	}
	
}