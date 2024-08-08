package telran.blocker.controller;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.dto.IpData;
import telran.blocker.service.BlockingDataProviderService;

@RestController
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class BlockingDataProviderController {
	final BlockingDataProviderService providerService;
	
	@GetMapping("${app.blocking.data.exist.url:/ip/exist}" + "/{ip}")
	boolean existsById (@PathVariable(name="ip") String ip) {
		log.debug("received IP: {}", ip);
		boolean res = providerService.existsById(ip);
		log.debug("IP: {} is exists: {}", ip, res);
		return res;
	}
	@GetMapping("${app.blocking.data.list.url:/ip/get_all}")
	List<IpData> getBlockingList (String ip) {
		log.debug("received IP: {}", ip);
		List<IpData> res = providerService.getBlockingList();
		log.trace("received blocking list: {}", res);
		return res;
	}
	@GetMapping("${app.blocking.data.get.url:/ip/get}" + "/{ip}")
	IpData getIpData (@PathVariable(name="ip") String ip) {
		log.debug("received IP: {}", ip);
		IpData res = providerService.getIpData(ip);
		log.debug("recieved ip data: {}", res );
		return res;
	}
	@GetMapping("${app.blocking.data.ips.url:/ip/get_ips}")
	Set<String> getSetIps() {
		Set<String> res = providerService.getSetIps();
		log.debug("recieved IP`s: {}", res);
		return res;
	}
	
	

}