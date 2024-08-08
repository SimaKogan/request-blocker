package telran.blocker;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import ch.qos.logback.core.net.SyslogOutputStream;
import telran.blocker.dto.IpData;
import telran.blocker.dto.WebServiceTimestamp;
import telran.blocker.model.RedisModel;
import telran.blocker.repo.RedisRepo;
import telran.blocker.service.AnalyzerService;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class AnalyzerServiceTest {
	WebServiceTimestamp WSTS_NO_RECORD = new WebServiceTimestamp("name", System.currentTimeMillis());

	List<WebServiceTimestamp> WSsTSs_NO_BLOCK = new ArrayList<>(List.of(		
		    new WebServiceTimestamp("NO_BLOCK_1", System.currentTimeMillis()),
		    new WebServiceTimestamp("NO_BLOCK_2", System.currentTimeMillis())
		));

	List<WebServiceTimestamp> WSsTSs_BLOCK = new ArrayList<>(List.of(
		    new WebServiceTimestamp("BLOCK_1", System.currentTimeMillis()),
		    new WebServiceTimestamp("BLOCK_2", System.currentTimeMillis()),
		    new WebServiceTimestamp("BLOCK_3", System.currentTimeMillis()),
		    new WebServiceTimestamp("BLOCK_4", System.currentTimeMillis())
		));
	
	List<WebServiceTimestamp> WSsTSs_NO_BLOCK_TIMESTAMP = new ArrayList<>(List.of(
		    new WebServiceTimestamp("NO_TS_TO_DELETE", System.currentTimeMillis() - 1000000),
		    new WebServiceTimestamp("NO_TS_2", System.currentTimeMillis()),
		    new WebServiceTimestamp("NO_TS_3", System.currentTimeMillis()),
		    new WebServiceTimestamp("NO_TS_4", System.currentTimeMillis())
		));	


	final String IP_NO_RECORD = "100.100.100.100";
	final String IP_NO_BLOCK = "200.200.200.200";
	final String IP_BLOCK = "300.300.300.300";
	final String IP_NO_BLOCK_TIMESTAMP = "400.400.400.400";

	final IpData IP_DATA_NO_RECORD = new IpData(IP_NO_RECORD, "name", System.currentTimeMillis());
	final IpData IP_DATA_NO_BLOCK = new IpData(IP_NO_BLOCK, "NO_BLOCK_NEW", System.currentTimeMillis());
	final IpData IP_DATA_BLOCK = new IpData(IP_BLOCK, "BLOCK_NEW", System.currentTimeMillis());
	final IpData IP_DATA_NO_BLOCK_TIMESTAMP = new IpData(IP_NO_BLOCK_TIMESTAMP, "NO_TS_5", System.currentTimeMillis());

	final RedisModel IP_DATAS_LIST_NO_REC = new RedisModel(IP_NO_RECORD);
	final RedisModel IP_DATAS_LIST_NO_BLOCK = new RedisModel(IP_NO_BLOCK, WSsTSs_NO_BLOCK);
	final RedisModel IP_DATAS_LIST_BLOCK = new RedisModel(IP_BLOCK, WSsTSs_BLOCK);
	final RedisModel IP_DATAS_LIST_NO_BLOCK_TIMESTAMP = new RedisModel(IP_NO_BLOCK_TIMESTAMP, WSsTSs_NO_BLOCK_TIMESTAMP);

	final Map<String, RedisModel> mapRedis = new HashMap<>();

	@Autowired
	AnalyzerService analyzerService;
	@MockBean
	RedisRepo redisRepo;

	@BeforeEach
	void setUp() {
		mapRedis.clear();
		mapRedis.put(IP_NO_BLOCK, IP_DATAS_LIST_NO_BLOCK);
		mapRedis.put(IP_BLOCK, IP_DATAS_LIST_BLOCK);
		mapRedis.put(IP_NO_BLOCK_TIMESTAMP, IP_DATAS_LIST_NO_BLOCK_TIMESTAMP);
	}

	@Test
	@Order(1)
	@DisplayName("no record in DB")
	void testNoRedisRecord() {
		when(redisRepo.findById(IP_NO_RECORD)).thenReturn(Optional.ofNullable(null));
		when(redisRepo.save(IP_DATAS_LIST_NO_REC)).thenAnswer(new Answer<RedisModel>() {

			@Override
			public RedisModel answer(InvocationOnMock invocation) throws Throwable {
				mapRedis.put(IP_NO_RECORD, invocation.getArgument(0));
				return invocation.getArgument(0);
			}
		});
		List<IpData> res = analyzerService.getList(IP_DATA_NO_RECORD);
		assertNull(res);
		RedisModel redisModel = mapRedis.get(IP_NO_RECORD);
		assertNotNull(redisModel);
		assertEquals(WSTS_NO_RECORD, redisModel.getWebServicesTimestamps().get(0));
	}
	
	@Test
	@Order(2)
	@DisplayName("ip will not be blocked")
	void ipNotBlockTest() {
		
		when(redisRepo.findById(IP_NO_BLOCK)).thenReturn(Optional.of(IP_DATAS_LIST_NO_BLOCK));
		when(redisRepo.save(IP_DATAS_LIST_NO_BLOCK)).thenAnswer(new Answer<RedisModel>() {
		
			@Override
			public RedisModel answer(InvocationOnMock invocation) throws Throwable {
				mapRedis.put(IP_NO_BLOCK, invocation.getArgument(0));
				return invocation.getArgument(0);
			}
		});
		List<IpData> res = analyzerService.getList(IP_DATA_NO_BLOCK);
		assertNull(res);
		List<WebServiceTimestamp> lts = mapRedis.get(IP_NO_BLOCK).getWebServicesTimestamps();
		lts.forEach(l -> System.out.printf("w_name: %s\n", l.webService()));
		assertEquals(3, lts.size());
		
		
	}

	@Test
	@Order(3)
	@DisplayName("ip should be blocked")
	void ipBlockTest() {

		when(redisRepo.findById(IP_BLOCK)).thenReturn(Optional.of(IP_DATAS_LIST_BLOCK));
		doAnswer(invocation -> {
		    mapRedis.remove(IP_BLOCK);
		    return null; // void method, so return type is always null
		}).when(redisRepo).deleteById(IP_BLOCK);

		List<IpData> res = analyzerService.getList(IP_DATA_BLOCK);
		assertNotNull(res);
		RedisModel redisModel = mapRedis.get(IP_BLOCK);
		assertNull(redisModel);
		assertNull(mapRedis.get(IP_BLOCK));

	}
	
	@Test
	@Order(4)
	@DisplayName("ip will not be blocked because of timestamp")
	void ipNotBlockBecauseOfTimestampTest () {
		when(redisRepo.findById(IP_NO_BLOCK_TIMESTAMP)).thenReturn(Optional.of(IP_DATAS_LIST_NO_BLOCK_TIMESTAMP));
		when(redisRepo.save(IP_DATAS_LIST_NO_BLOCK_TIMESTAMP)).thenAnswer(new Answer<RedisModel>() {
		
			@Override
			public RedisModel answer(InvocationOnMock invocation) throws Throwable {
				mapRedis.put(IP_NO_BLOCK_TIMESTAMP, invocation.getArgument(0));
				return invocation.getArgument(0);
			}
		});
		List<IpData> res = analyzerService.getList(IP_DATA_NO_BLOCK_TIMESTAMP);
		assertNull(res);
		List<WebServiceTimestamp> lts = mapRedis.get(IP_NO_BLOCK_TIMESTAMP).getWebServicesTimestamps();
		lts.forEach(l -> System.out.printf("w_name: %s\n", l.webService()));
		assertEquals(4, lts.size());
		
	}

}