package telran.blocker.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.dto.IpData;
import telran.blocker.model.IpDataDoc;
import telran.blocker.repo.IpDataRepo;
import telran.exception.NotFoundException;
import static telran.blocker.dto.ErrorMessages.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlockingDataProviderServiceImpl implements BlockingDataProviderService {
	final IpDataRepo ipDataRepo;

	@Override
	public boolean existsById(String IP) {
		boolean res = ipDataRepo.existsById(IP);
		log.debug("IP: {} exists in DB: {}", IP, res);
		return res;
	}

	@Override
	public List<IpData> getBlockingList() {
		List<IpDataDoc> resDocs = ipDataRepo.findAll();
		log.debug("blocking list includes {} ips", resDocs.size());
		log.trace("received list of IPs: {}", resDocs);
		return resDocs.stream().map(doc -> doc.buildDto()).toList();
	}

	@Override
	public IpData getIpData(String IP) {
		IpDataDoc resDoc = ipDataRepo.findById(IP).orElseThrow(() -> new NotFoundException(IP_NOT_FOUND_MESSAGE));
		log.debug("received IP data: {}", resDoc);
		return resDoc.buildDto();
	}

	@Override
	public Set<String> getSetIps() {
		List<IpDataDoc> listIpDatas = ipDataRepo.findAll();
		log.debug("recieved list of IPs: {}", listIpDatas);
		Set<String> res = listIpDatas.stream().map(ipd -> ipd.getIP()).collect(Collectors.toSet());
		log.debug("recieved set of IP: {}", res);
		return res;
	}

}