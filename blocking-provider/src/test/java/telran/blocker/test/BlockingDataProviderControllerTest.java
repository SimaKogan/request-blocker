package telran.blocker.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.blocker.controller.BlockingDataProviderController;
import telran.blocker.dto.IpData;
import telran.blocker.service.BlockingDataProviderService;
import telran.exception.NotFoundException;

import static telran.blocker.dto.ErrorMessages.*;
import java.util.*;

@WebMvcTest
public class BlockingDataProviderControllerTest {
	private static final String IP_EXIST = "111.111.111";
	private static final String IP_NOT_EXIST = "112.112.112";
	private static final String ERROR_NOT_FOUND = IP_NOT_FOUND_MESSAGE + " " + IP_NOT_EXIST;
	@Autowired
	BlockingDataProviderController providerController;
	@Autowired
	MockMvc mockMvc;
	@MockBean
	BlockingDataProviderService providerService;
	@Autowired
	ObjectMapper mapper;
	@Value("${app.blocking.data.exist.url:/ip/exist}")
	String blockingDataExistUrl;
	@Value("${app.blocking.data.list.url:/ip/get_all}")
	String blockingDataGetAllUrl;
	@Value("${app.blocking.data.get.url:/ip/get}")
	String blockingDataGetUrl;
	
	IpData ipData_1 = new IpData(IP_EXIST, "web_service_111", 0);
	IpData ipData_2 = new IpData("112.112.112", "web_service_112", 0);
	IpData ipData_3 = new IpData("113.113.113", "web_service_113", 0);
	List<IpData> ipsDataList = List.of(ipData_1, ipData_2, ipData_3);

	
	@Test
	void existsById_whenNormalFlow_returnTrue() throws  Exception {
		String fullUrl = "http://localhost:8484" + blockingDataExistUrl + "/" + IP_EXIST;
		when(providerService.existsById(IP_EXIST)).thenReturn(true);
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertTrue(Boolean.parseBoolean(response));
	}
	
	@Test
	void existById_whenNotExist_returnFalse() throws Exception {
		String fullUrl = "http://localhost:8484" + blockingDataExistUrl + "/" + IP_NOT_EXIST;
		when(providerService.existsById(IP_NOT_EXIST)).thenReturn(false);
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		assertFalse(Boolean.parseBoolean(response));
		
	}
	@Test
	void getBlockingList_whenNormalFlow_returnBlockingList() throws Exception {
		String fullUrl = "http://localhost:8484" + blockingDataGetAllUrl;
		when(providerService.getBlockingList()).thenReturn(ipsDataList);
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		List<IpData> actual = mapper.readValue(response, new TypeReference<List<IpData>>(){});
		assertIterableEquals(ipsDataList , actual);
	}
	@Test
	void getIpData_whenIpExists_returnIPData () throws Exception {
		String fullUrl = "http://localhost:8484" + blockingDataGetUrl + "/" + IP_EXIST;
		when(providerService.getIpData(IP_EXIST)).thenReturn(ipData_1);
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
		IpData actual = mapper.readValue(response, IpData.class);
		assertEquals(ipData_1, actual);
	}
	
	@Test
	void getIpData_whenIpNotExist_returnNotFound() throws Exception {
		String fullUrl = "http://localhost:8484" + blockingDataGetUrl + "/" + IP_NOT_EXIST;
		when(providerService.getIpData(IP_NOT_EXIST)).thenThrow(new NotFoundException(ERROR_NOT_FOUND));
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isNotFound())
				.andReturn().getResponse().getContentAsString();
		assertEquals(ERROR_NOT_FOUND, response);
	}

}