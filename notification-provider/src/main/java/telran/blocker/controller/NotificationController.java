package telran.blocker.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.dto.ServiceEmails;
import telran.blocker.service.NotificationService;

@RestController
@RequiredArgsConstructor
@Slf4j
public class NotificationController {
	final NotificationService notificationService;
	
	@GetMapping("${app.notification.provider.exist.url}" + "/{webService}")
	boolean existByServiceName(@PathVariable(name = "webService") String webService) {
		log.debug("received webService name: {}", webService);
		boolean res = notificationService.existByServiceName(webService);
		log.debug("service name {} exist: {}", webService, res);		
		return res;
		
	}
	
	@GetMapping("${app.notification.provider.get.url}" + "/{webService}")
	ServiceEmails getServiceEmails(@PathVariable(name = "webService") String webService) {
		log.debug("received webService name: {}", webService);
		ServiceEmails res = notificationService.getServiceEmails(webService);
		log.debug("service name {} emails: {}", webService, res.emails());	
		return res;
	}
	@GetMapping("${app.notification.provider.list.url}")
	List<ServiceEmails> getNotificationList() {		
		List<ServiceEmails> res = notificationService.getNotificationList();
		log.trace("all services names-emails: {}", res);		
		return res;		
	}
	
}