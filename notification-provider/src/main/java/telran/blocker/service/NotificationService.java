package telran.blocker.service;

import java.util.List;

import telran.blocker.dto.ServiceEmails;

public interface NotificationService {
	boolean existByServiceName(String webService);
	ServiceEmails getServiceEmails(String webService);
	List<ServiceEmails> getNotificationList();

}