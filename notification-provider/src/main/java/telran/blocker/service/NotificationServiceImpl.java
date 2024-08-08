package telran.blocker.service;

import java.util.List;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import telran.blocker.dto.ServiceEmails;
import telran.blocker.model.ServiceEmailsDoc;
import telran.blocker.repo.ServiceEmailsRepo;
import telran.exception.NotFoundException;

import static telran.blocker.dto.ErrorMessages.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
	final ServiceEmailsRepo seDocRepo;

	@Override
	public boolean existByServiceName(String webService) {
		boolean res = seDocRepo.existsById(webService);
		log.debug("webService {} exist: {}", webService, res);

		return res;
	}

	@Override
	public ServiceEmails getServiceEmails(String webService) {
		ServiceEmailsDoc sed = seDocRepo.findById(webService)
				.orElseThrow(() -> new NotFoundException(WEB_SERVICE_NOT_FOUND));
		log.debug("webService {} emails: {}", webService, sed.getEmails());
		
		return sed.build();
	}

	@Override
	public List<ServiceEmails> getNotificationList() {
		List<ServiceEmails> res = null;
		List<ServiceEmailsDoc> all = seDocRepo.findAll();
		if (!all.isEmpty()) {
			res = all.stream().map(sed -> sed.build()).toList();
			log.trace("there is {} in ServiceEmailsRepo", res);
		} else {
			log.debug("ServiceEmailsRepo is empty");
		}

		return res;
	}

}