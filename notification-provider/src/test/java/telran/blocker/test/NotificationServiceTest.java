package telran.blocker.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import jakarta.annotation.PostConstruct;
import telran.blocker.dto.ServiceEmails;
import telran.blocker.model.ServiceEmailsDoc;
import telran.blocker.repo.ServiceEmailsRepo;
import telran.blocker.service.NotificationService;
import telran.exception.NotFoundException;

@SpringBootTest
public class NotificationServiceTest {
	@Autowired
	ServiceEmailsRepo seRepo;

	@Autowired
	NotificationService ntfService;

	String serviceExist = "web_EXIST_service";
	ServiceEmailsDoc se1 = new ServiceEmailsDoc(serviceExist,
			new String[] { "project.telran@gmail.com", "project.telran+A@gmail.com" });
	ServiceEmailsDoc se2 = new ServiceEmailsDoc("test_B_service",
			new String[] { "project.telran@gmail.com", "project.telran+B@gmail.com" });
	ServiceEmailsDoc se3 = new ServiceEmailsDoc("test_C_service",
			new String[] { "project.telran@gmail.com", "project.telran+C@gmail.com" });
	ServiceEmailsDoc se4 = new ServiceEmailsDoc("test_D_service",
			new String[] { "project.telran@gmail.com", "project.telran+D@gmail.com" });
	String serviceNotExist = "web_NOT_EXIST_service";

	List<ServiceEmailsDoc> listSeDocs = List.of(se1, se2, se3, se4);
	List<ServiceEmails> listSe = listSeDocs.stream().map(sed -> sed.build()).toList();

	@PostConstruct
	void setup() {
		seRepo.deleteAll();
		seRepo.saveAll(listSeDocs);
	}

	@Test
	void existByIdTrue() {
		boolean res = ntfService.existByServiceName(serviceExist);
		assertTrue(res);
	}

	@Test
	void existByIdFalse() {
		boolean res = ntfService.existByServiceName(serviceNotExist);
		assertFalse(res);
	}

	@Test
	void getByIdExist() {
		ServiceEmails actual = ntfService.getServiceEmails(serviceExist);
		assertEquals(se1.build(), actual);
	}

	@Test
	void getByIdNotExist() {
		assertThrowsExactly(NotFoundException.class, () -> ntfService.getServiceEmails(serviceNotExist));
	}

	@Test
	void getListTest() {
		List<ServiceEmails> actual = ntfService.getNotificationList();
		assertIterableEquals(actual, listSe);
	}

}