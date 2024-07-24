package telran.blocker.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.MongoRepository;

import telran.blocker.dto.IpData;
import telran.blocker.dto.ServiceEmails;
import telran.blocker.model.IpDataDoc;
import telran.blocker.model.ServiceEmailsDoc;
import telran.blocker.repo.IpDataRepo;
import telran.blocker.repo.ServiceEmailsRepo;
import telran.blocker.service.BackOfficeService;
import telran.exception.AlreadyExistException;
import telran.exception.NotFoundException;

@SpringBootTest
class BOServiceTest {

	@Autowired
	BackOfficeService BOService;
	@Autowired
	IpDataRepo ipRepo;
	@Autowired
	ServiceEmailsRepo seRepo;
	
	private static final String IP_1 = "111";
	private static final IpData ipData_1 = new IpData(IP_1, "web_service_111", 0);
	private static final IpData ipData_2 = new IpData("112.112.112", "web_service_112", 0);
	private static final IpDataDoc ipDataDoc_2 = new IpDataDoc(ipData_2);
	
	private static final String SERVICE_1 = "web_1_service";
	private static final ServiceEmails SE_1 = new ServiceEmails(SERVICE_1,
	                new String[] { "service1@email.com", "service2@email.com" });
	private static final ServiceEmails SE_2 = new ServiceEmails("web_2_service",
	                new String[] { "service1@email.com", "service2@email.com" });
	private static final ServiceEmailsDoc seDoc_2 = new ServiceEmailsDoc(SE_2);
	
	@BeforeEach
	void setup() {
		ipRepo.deleteAll();
		ipRepo.save(ipDataDoc_2);
		seRepo.deleteAll();
		seRepo.save(seDoc_2);
	}
	
	@Test
	@DisplayName(TestDisplayNames.ADD_IP_SUCCESS)
	void addIP_Success() {
		Supplier<IpData> foo = () -> BOService.addIpData(ipData_1);
		addItem_Success(foo, ipData_1, ipRepo);
	}
	@Test
	@DisplayName(TestDisplayNames.ADD_SERVICE_SUCCESS)
	void addSE_Success() {
		Supplier<ServiceEmails> foo = () -> BOService.addServiceEmails(SE_1);
		addItem_Success(foo, SE_1, seRepo);
	}
	
	private <T, R> void addItem_Success(Supplier<T> foo, T item, MongoRepository<R, String> repo) {
		T itemActual = foo.get();
		
		assertEquals(item, itemActual);
		assertEquals(2, repo.count());
	}
	
	
	@Test
	@DisplayName(TestDisplayNames.ADD_IP_EXCEPT)
	void addIP_Except() {
		Runnable foo = () -> BOService.addIpData(ipData_2);
		addItem_Except(foo);
	}
	@Test
	@DisplayName(TestDisplayNames.ADD_SERVICE_EXCEPT)
	void addSE_Except() {
		Runnable foo = () -> BOService.addServiceEmails(SE_2);
		addItem_Except(foo);
	}
	
	private void addItem_Except(Runnable foo) {
		
		assertThrows(AlreadyExistException.class, foo::run);
		
	}
	
	
	@Test
	@DisplayName(TestDisplayNames.DELETE_IP_SUCCESS)
	void deleteIP_Success() {
		String name = ipData_2.IP();
		Supplier<IpData> foo = () -> BOService.deleteIpData(name);
		deleteItem_Success(foo, ipData_2, ipRepo);
	}
	@Test
	@DisplayName(TestDisplayNames.DELETE_SERVICE_SUCCESS)
	void deleteSE_Success() {
		String name = SE_2.webService();
		Supplier<ServiceEmails> foo = () -> BOService.deleteServiceEmails(name);
		deleteItem_Success(foo, SE_2, seRepo);
	}
	
	private <T, R> void deleteItem_Success(Supplier<T> foo, T item, MongoRepository<R, String> repo) {
		T itemActual = foo.get();
		
		assertTrue(repo.findAll().isEmpty());
		assertEquals(item, itemActual);
	}
	
	
	@Test
	@DisplayName(TestDisplayNames.DELETE_IP_EXCEPT)
	void deleteIP_Except() {
		String name = IP_1;
		Runnable foo = () -> BOService.deleteIpData(name);
		deleteItem_Except(foo);
	}
	@Test
	@DisplayName(TestDisplayNames.DELETE_SERVICE_EXCEPT)
	void deleteSE_Except() {
		String name = SERVICE_1;
		Runnable foo = () -> BOService.deleteServiceEmails(name);
		deleteItem_Except(foo);
	}
	
	private void deleteItem_Except(Runnable foo) {
		
		assertThrows(NotFoundException.class, foo::run);
		
	}
}