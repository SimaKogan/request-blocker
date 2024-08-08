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

import telran.blocker.components.ServiceEmailsPopulation;
import telran.blocker.controller.BackOfficeController;
import telran.blocker.dto.IpData;
import telran.blocker.dto.ServiceEmails;
import telran.blocker.service.BackOfficeService;

@WebMvcTest
public class BOControllerTest {

	@Autowired
	BackOfficeController BOController;

	@MockBean
	BackOfficeService BOService;
	
	@MockBean
	ServiceEmailsPopulation pop;

	@Autowired
	ObjectMapper mapper;

	@Autowired
	MockMvc mockMvc;

	@Value("${app.back.office.ip.url}")
	String ipUrl;
	@Value("${app.back.office.srv.url}")
	String serviceUrl;

	private static final String IP_1 = "111.111.111";
	private static final String IP_NOT_EXIST = "000.000.000";
	private static final IpData ipData_1 = new IpData(IP_1, "web_service_111", 0);
	
	private static final IpData ipData_ipNULL = new IpData(null, "webservice", 0); //IllegalArgumentException
	
	private static final IpData ipData_ipLength3 = new IpData("999", "webservice", 0); //MethodArgumentNotValidException
	private static final IpData ipData_missingWS = new IpData("9999", null, 0); //MethodArgumentNotValidException
	
	private static final String SERVICE_1 = "web_EXIST_service";
	private static final String SERVICE_NOT_EXIST = "web_NOT_EXIST_service";
	private static final ServiceEmails SE_1 = new ServiceEmails(SERVICE_1,
			new String[] { "service1@email.com", "service2@email.com" });
	
	@Test
	@DisplayName(TestDisplayNames.IP_ILLEGAL_ARGUMENT)
	void iP_illegal_argument() throws Exception {
		String url = ipUrl;
		IpData item = ipData_ipNULL;
		String errorMessage = ILLEGAL_ARGUMENT;
		when(BOService.addIpData(item)).thenThrow(new IllegalArgumentException(errorMessage));
		String itemDataJSON = mapper.writeValueAsString(item);
		String response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(itemDataJSON))
				.andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		assertEquals(errorMessage, response);
	}
	
	@Test
	@DisplayName(TestDisplayNames.IP_ARGUMENT_NOT_VALID_LENGTH)
	void iP_not_valid_length() throws Exception {
		String url = ipUrl;
		IpData item = ipData_ipLength3;
		String expectedErrorMessage = "size must be between 4 and 2147483647"; // This should match your validation message

	    // Convert IpData to JSON
	    String itemDataJSON = mapper.writeValueAsString(item);

	    // Perform the POST request
	    String result = mockMvc.perform(post(url)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(itemDataJSON))
	            .andDo(print())
	            .andExpect(status().isNotAcceptable()) // HTTP 406
	            .andReturn().getResponse().getContentAsString();

	    // Verify response
	    assertTrue(result.contains(expectedErrorMessage));
	}
	
	
	@Test
	@DisplayName(TestDisplayNames.IP_ARGUMENT_NOT_VALID_MISSING)
	void iP_missing_WS() throws Exception {
		String url = ipUrl;
		IpData item = ipData_missingWS;
		String expectedErrorMessage = "Missing the webservice name"; // This should match your validation message

	    // Convert IpData to JSON
	    String itemDataJSON = mapper.writeValueAsString(item);

	    // Perform the POST request
	    String result = mockMvc.perform(post(url)
	            .contentType(MediaType.APPLICATION_JSON)
	            .content(itemDataJSON))
	            .andDo(print())
	            .andExpect(status().isNotAcceptable()) // HTTP 406
	            .andReturn().getResponse().getContentAsString();

	    // Verify response
	    assertTrue(result.contains(expectedErrorMessage));
	}


	@Test
	@DisplayName(TestDisplayNames.ADD_IP_SUCCESS)
	void addIP_Success() throws Exception {

		OngoingStubbing<IpData> ongoingStubbing = when(BOService.addIpData(ipData_1));
		addItem_Success(ipData_1, ipUrl, ongoingStubbing);
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

		OngoingStubbing<IpData> ongoingStubbing = when(BOService.addIpData(ipData_1));
		String errorMessage = IP_ALREADY_EXIST;
		addItem_Except(ipData_1, ipUrl, ongoingStubbing, errorMessage);
	}
	
	@Test
	@DisplayName(TestDisplayNames.ADD_SERVICE_EXCEPT)
	void addSE_Except() throws Exception {

		OngoingStubbing<ServiceEmails> ongoingStubbing = when(BOService.addServiceEmails(SE_1));
		String errorMessage = WEB_SERVICE_ALREADY_EXIST;
		addItem_Except(SE_1, serviceUrl, ongoingStubbing, errorMessage);
	}

	private <T> void addItem_Except(T item, String url, OngoingStubbing<T> ongoingStubbing, String errorMessage)
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
		deleteItem_Success(ipData_1, fullUrl, ongoingStubbing);
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


	
	@Test
	@DisplayName(TestDisplayNames.ADD_SERVICE_EMAILS_SUCCESS)
	void addSE_EMAILS_SUCCESS2() throws Exception {
		
		String url = serviceUrl;
		ServiceEmails item = SE_1;		
		String errorMessage = ADD_SERVICE_EMAILS_SUCCESS;
		RuntimeException exception = new AlreadyExistException(errorMessage);
		
		when(BOService.addServiceEmails(SE_1)).thenThrow(exception);
		
		String itemDataJSON = mapper.writeValueAsString(item);
		String response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(itemDataJSON))
				.andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		assertEquals(errorMessage, response);
		
		
	}
	
	@Test
	@DisplayName(TestDisplayNames.ADD_SERVICE_EMAILS_EXCEPTION)
	void addSE_EMAILS_EXCEPTION2() throws Exception {
		
		String url = serviceUrl;
		ServiceEmails item = SE_1;
		String errorMessage = ADD_SERVICE_EMAILS_EXCEPTION:_ALREADY_EXISTS;		
		RuntimeException exception = new AlreadyExistException(errorMessage);

		when(BOService.addServiceEmails(SE_1)).thenThrow(exception);
		

		String itemDataJSON = mapper.writeValueAsString(item);
		String response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(itemDataJSON))
				.andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		assertEquals(errorMessage, response);
	}	
	
	@Test
	@DisplayName(TestDisplayNames.SERVICE_EMAILS_VALIDATION_ERROR)
	void addSE_EMAILS_VALIDATION_ERROR() throws Exception {
		
		String url = serviceUrl;
		ServiceEmails item = SE_1;
		String errorMessage = SERVICE_EMAILS_VALIDATION_ERROR;		
		RuntimeException exception = new AlreadyExistException(errorMessage);

		when(BOService.addServiceEmails(SE_1)).thenThrow(exception);
		

		String itemDataJSON = mapper.writeValueAsString(item);
		String response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(itemDataJSON))
				.andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		assertEquals(errorMessage, response);
	}	
	
	@Test
	@DisplayName(TestDisplayNames.DELETE_SERVICE_EMAILS_SUCCESS)
	void deleteSE_EMAILS_SUCCESS2() throws Exception {
		
		String url = serviceUrl;
		ServiceEmails item = SE_1;
		String errorMessage = DELETE_SERVICE_EMAILS_SUCCESS;		
		RuntimeException exception = new AlreadyExistException(errorMessage);

		when(BOService.addServiceEmails(SE_1)).thenThrow(exception);
		

		String itemDataJSON = mapper.writeValueAsString(item);
		String response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(itemDataJSON))
				.andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		assertEquals(errorMessage, response);
		
	}
	
	@Test
	@DisplayName(TestDisplayNames.DELETE_SERVICE_EMAILS_EXCEPTION)
	void deleteSE_EmailsExeption_Except2() throws Exception {
		
		String url = serviceUrl;
		ServiceEmails item = SE_1;
		String errorMessage = DELETE_SERVICE_EMAILS_EXCEPTION:_NOT_FOUND;		
		RuntimeException exception = new AlreadyExistException(errorMessage);

		when(BOService.addServiceEmails(SE_1)).thenThrow(exception);
		

		String itemDataJSON = mapper.writeValueAsString(item);
		String response = mockMvc.perform(post(url).contentType(MediaType.APPLICATION_JSON).content(itemDataJSON))
				.andDo(print()).andExpect(status().is4xxClientError()).andReturn().getResponse().getContentAsString();
		assertEquals(errorMessage, response);
	}	
	
	
	
	
}