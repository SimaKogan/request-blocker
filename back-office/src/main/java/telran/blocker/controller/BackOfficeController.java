package telran.blocker.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.dto.IpData;
import telran.blocker.dto.ServiceEmails;
import telran.blocker.service.BackOfficeService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class BackOfficeController {
	final BackOfficeService backService;
	
	@PostMapping("${app.back.office.srv.url}")
	ServiceEmails addServiceEmails(@RequestBody @Valid ServiceEmails serviceEmails) {
		log.debug("web service {} received for addition", serviceEmails.webService());
		ServiceEmails res = backService.addServiceEmails(serviceEmails);
		log.debug("web service {} successfully added", res.webService());
		return res;
	}
	
	@DeleteMapping("${app.back.office.srv.url}" + "/{webService}")
	ServiceEmails deleteServiceEmails(@PathVariable(name = "webService") String webService) {
		log.debug("web service {} received for deletion", webService);
		ServiceEmails res = backService.deleteServiceEmails(webService);
		log.debug("webService  successfully deleted", res.webService());
		return res;
	}
	
	@PostMapping("${app.back.office.ip.url}")
	IpData addIpData(@RequestBody @Valid IpData ipData) {
		log.debug("IP {} received for addition", ipData.IP());
		IpData res = backService.addIpData(ipData);
		log.debug("IP {} successfully added", res.IP());
		return res;
	}
	
	@DeleteMapping("${app.back.office.ip.url" + "/{ip}")
	IpData deleteIpData(@PathVariable(name = "ip") String ip) {
		log.debug("IP {} received for deletion", ip);
		IpData res = backService.deleteIpData(ip);
		log.debug("IP {} successfully deleted", res.IP());
		return res;
	}

}