package telran.blocker.test;

import static telran.blocker.dto.ErrorMessages.*;

import telran.exception.AlreadyExistException;
import telran.exception.NotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import telran.blocker.controller.BackOfficeController;
import telran.blocker.dto.IpData;
import telran.blocker.dto.ServiceEmails;
import telran.blocker.repo.ServiceEmailsRepo;
import telran.blocker.service.BackOfficeService;

@WebMvcTest
public class BOControllerTest {

	@Autowired
	BackOfficeController BOController;
	
	@MockBean
	BackOfficeService BOService;
	
	@MockBean
	ServiceEmailsRepo repo;
	
	@Autowired
	ObjectMapper mapper;
	
	@Autowired
	MockMvc mockMvc;
	
	@Value("${app.back.office.ip.url")
	String ipUrl;
	@Value("${app.back.office.srv.url")
	String serviceUrl;
	
	private static final String IP_1 = "111.111.111";
	private static final String IP_NOT_EXIST = "000.000.000";
	private static final IpData IpData_1 = new IpData(IP_1, "web_service_111", 0);
	private static final String SERVICE_1 = "web_EXIST_service";
	private static final String SERVICE_NOT_EXIST = "web_NOT_EXIST_service";
	private static final ServiceEmails SE_1 = new ServiceEmails(SERVICE_1,
                    new String[] { "service1@email.com", "service2@email.com" });
	
	
	@Test
	@DisplayName(TestDisplayNames.ADD_IP_SUCCESS)
	void addIP_Success() throws Exception {
		
		OngoingStubbing<IpData> ongoingStubbing = when(BOService.addIpData(IpData_1));
		addItem_Success(IpData_1, ipUrl, ongoingStubbing);
	}
	
	@Test
	@DisplayName(TestDisplayNames.ADD_SERVICE_SUCCESS)
	void addSE_Success() throws Exception {
		
		OngoingStubbing<ServiceEmails> ongoingStubbing = when(BOService.addServiceEmails(SE_1));
		addItem_Success(SE_1, serviceUrl, ongoingStubbing);
	}
	
	private <T> void addItem_Success(T item, String url, OngoingStubbing<T> ongoingStubbing) throws Exception {
		ongoingStubbing.thenReturn(item);
		String itemDataJSON = mapper.writeValueAsString(item);
		String response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(itemDataJSON))
				.andDo(print()).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
	@SuppressWarnings("unchecked")
	T actual = (T) mapper.readValue(response, (Class<? extends T>) item.getClass());
	
	assertEquals(item, actual);
	}
	
	
	
	
	@Test
	@DisplayName(TestDisplayNames.ADD_IP_EXCEPT)
	void addIP_Except() throws Exception {
		
		OngoingStubbing<IpData> ongoingStubbing = when(BOService.addIpData(IpData_1));
		String errorMessage = IP_ALREADY_EXIST;
		addItem_Except(IpData_1, ipUrl, ongoingStubbing, errorMessage);
	}
	
	@Test
	@DisplayName(TestDisplayNames.ADD_SERVICE_EXCEPT)
	void addSE_Except() throws Exception {
		
		OngoingStubbing<ServiceEmails> ongoingStubbing = when(BOService.addServiceEmails(SE_1));
		String errorMessage = WEB_SERVICE_ALREADY_EXIST;
		addItem_Except(SE_1, serviceUrl, ongoingStubbing, errorMessage);
	}
	
	private <T> void  addItem_Except(T item, String url, OngoingStubbing<T> ongoingStubbing, String errorMessage)
	                   throws Exception {
		RuntimeException exception = new AlreadyExistException(errorMessage);
		ongoingStubbing.thenThrow(exception);
		String itemDataJSON = mapper.writeValueAsString(item);
		String response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(itemDataJSON))
				.andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		assertEquals(errorMessage, response);
	}
	
	
	
	
	@Test
	@DisplayName(TestDisplayNames.DELETE_IP_SUCCESS)
	void deleteIP_Success() throws Exception {
		String fullUrl = ipUrl + "/" + IP_1;
		OngoingStubbing<IpData> ongoingStubbing = when(BOService.deleteIpData(IP_1));
		deleteItem_Success(IpData_1, fullUrl, ongoingStubbing);
	}
	
	@Test
	@DisplayName(TestDisplayNames.DELETE_SERVICE_SUCCESS)
	void deleteSE_Success() throws Exception {
		String fullUrl = serviceUrl + "/" + SERVICE_1;
		OngoingStubbing<ServiceEmails> ongoingStubbing = when(BOService.deleteServiceEmails(SERVICE_1));
		deleteItem_Success(SE_1, fullUrl, ongoingStubbing);
	}
	
	private <T> void deleteItem_Success(T item, String url, OngoingStubbing<T> ongoingStubbing) throws Exception {
		ongoingStubbing.thenReturn(item);
		String response = mockMvc.perform(delete(url)).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		@SuppressWarnings("unchecked")
		T actual = (T) mapper.readValue(response, (Class<? extends T>) item.getClass());
		
		assertEquals(item, actual);
	}
	
	
	
	
	@Test
	@DisplayName(TestDisplayNames.DELETE_IP_EXCEPT)
	void deleteIP_Except() throws Exception {
		String fullUrl = ipUrl + "/" + IP_NOT_EXIST;
		OngoingStubbing<IpData> ongoingStubbing = when(BOService.deleteIpData(IP_NOT_EXIST));
		String errorMessage = IP_NOT_FOUND;
		deleteItem_Except(fullUrl, ongoingStubbing, errorMessage);
	}
	
	@Test
	@DisplayName(TestDisplayNames.DELETE_SERVICE_EXCEPT)
	void deleteSE_Except() throws Exception {
		String fullUrl = serviceUrl + "/" + SERVICE_NOT_EXIST;
		OngoingStubbing<ServiceEmails> ongoingStubbing = when(BOService.deleteServiceEmails(SERVICE_NOT_EXIST));
		String errorMessage = WEB_SERVICE_NOT_FOUND;
		deleteItem_Except(fullUrl, ongoingStubbing, errorMessage);
	}
	
	private <T> void deleteItem_Except(String url, OngoingStubbing<T> ongoingStubbing, String errorMessage) throws Exception {
		ongoingStubbing.thenThrow(new NotFoundException(errorMessage));
		String response = mockMvc.perform(delete(url)).andDo(print()).andExpect(status().is4xxClientError())
				.andReturn().getResponse().getContentAsString();
		
		assertEquals(errorMessage, response);
	}
		
}