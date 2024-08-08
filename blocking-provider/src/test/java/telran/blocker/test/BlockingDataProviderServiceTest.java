package telran.blocker.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import telran.blocker.dto.IpData;
import telran.blocker.model.IpDataDoc;
import telran.blocker.repo.IpDataRepo;
import telran.blocker.service.BlockingDataProviderService;

@SpringBootTest
public class BlockingDataProviderServiceTest {

	@Autowired
	BlockingDataProviderService blockingDataProviderService;
	@Autowired
	IpDataRepo ipDataRepo;
	
	String IP_1 = "111.111.111";
	String IP_NOT_EXIST = "000.000.000";
	
	IpDataDoc ipData_1 = new IpDataDoc(IP_1, "web_service_111", 0);
	IpDataDoc ipData_2 = new IpDataDoc("112.112.112", "web_service_112", 0);
	IpDataDoc ipData_3 = new IpDataDoc("113.113.113", "web_service_113", 0);
	List<IpDataDoc> ipsDataList = List.of(ipData_1, ipData_2, ipData_3);
	
	
	@BeforeEach
	void setUp () {
		ipsDataList.forEach(ipd -> ipDataRepo.save(ipd));
	}
	
	@Test
	void existsById_whenIpExists_thenReturnTrue () {
		boolean actual = blockingDataProviderService.existsById(IP_1);
		assertTrue(actual);
	}
	
	@Test
	void existsById_whenIpNotExists_thenReturnFalse () {
		boolean actual = blockingDataProviderService.existsById(IP_NOT_EXIST);
		assertFalse(actual);
	}
	
	@Test
	void getBlockingList_whenDBAccess_thenReturnListOfThree () {
		List<IpData> actual = blockingDataProviderService.getBlockingList();
		assertEquals(ipsDataList.size(), actual.size());	
		List<IpData> expected = ipsDataList.stream().map(doc -> doc.buildDto()).toList();
		assertIterableEquals(expected, actual);		
	}
	
	@Test
	void getIpData_whenIpExists_ReturnIpData () {
		IpData actual = blockingDataProviderService.getIpData(IP_1);
		assertEquals(ipData_1.buildDto(), actual);
	}
	
	@Test
	void getSetIps_whenIpsExists_thenReturnSetIps () {
		Set<String> expected = Set.of(ipData_1.getIP(), ipData_2.getIP(), ipData_3.getIP());
		Set<String> actual = blockingDataProviderService.getSetIps();
		assertEquals(expected.size(), actual.size());
		actual.forEach(ip -> 
				assertTrue(expected.contains(ip)
					  ));
		assertTrue(actual.containsAll(expected));
	}
}