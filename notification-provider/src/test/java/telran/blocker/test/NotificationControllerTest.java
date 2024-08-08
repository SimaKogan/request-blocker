package telran.blocker.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static telran.blocker.dto.ErrorMessages.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import telran.blocker.controller.NotificationController;
import telran.blocker.dto.ServiceEmails;
import telran.blocker.model.ServiceEmailsDoc;
import telran.blocker.service.NotificationService;
import telran.exception.NotFoundException;

@WebMvcTest
public class NotificationControllerTest {
	@Autowired
	NotificationController ntfController;
	@Autowired
	MockMvc mockMvc;
	@MockBean
	NotificationService ntfService;
	@Autowired
	ObjectMapper mapper;

	@Value("${app.notification.provider.exist.url}")
	String existUrl;
	@Value("${app.notification.provider.list.url}")
	String listUrl;
	@Value("${app.notification.provider.get.url}")
	String getUrl;

	private static final String serviceExist = "web_EXIST_service";
	ServiceEmailsDoc se1 = new ServiceEmailsDoc(serviceExist,
			new String[] { "project.telran@gmail.com", "project.telran+A@gmail.com" });
	ServiceEmailsDoc se2 = new ServiceEmailsDoc("test_B_service",
			new String[] { "project.telran@gmail.com", "project.telran+B@gmail.com" });
	ServiceEmailsDoc se3 = new ServiceEmailsDoc("test_C_service",
			new String[] { "project.telran@gmail.com", "project.telran+C@gmail.com" });
	ServiceEmailsDoc se4 = new ServiceEmailsDoc("test_D_service",
			new String[] { "project.telran@gmail.com", "project.telran+D@gmail.com" });
	private static final String serviceNotExist = "web_NOT_EXIST_service";

	List<ServiceEmailsDoc> listSeDocs = List.of(se1, se2, se3, se4);
	List<ServiceEmails> listSe = listSeDocs.stream().map(sed -> sed.build()).toList();

	@Test
	void existByIdTrue() throws Exception {
		String fullUrl = fillUrl(existUrl) + "/" + serviceExist;
		when(ntfService.existByServiceName(serviceExist)).thenReturn(true);
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		assertTrue(Boolean.parseBoolean(response));
	}

	@Test
	void existByIdFalse() throws Exception {
		String fullUrl = fillUrl(existUrl);
		when(ntfService.existByServiceName(serviceNotExist)).thenReturn(false);
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isNotFound()).andReturn()
				.getResponse().getContentAsString();
		assertFalse(Boolean.parseBoolean(response));
	}

	@Test
	void getByIdExist() throws Exception {
		String fullUrl = fillUrl(getUrl) + "/" + serviceExist;
		when(ntfService.getServiceEmails(serviceExist)).thenReturn(se1.build());
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		ServiceEmails actual = mapper.readValue(response, ServiceEmails.class);
		assertEquals(se1.build(), actual);
	}

	@Test
	void getByIdNotExist() throws Exception {
		String fullUrl = fillUrl(getUrl) + "/" + serviceNotExist;
		when(ntfService.getServiceEmails(serviceNotExist)).thenThrow(new NotFoundException(WEB_SERVICE_NOT_FOUND));
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isNotFound()).andReturn()
				.getResponse().getContentAsString();
		assertEquals(WEB_SERVICE_NOT_FOUND, response);
	}

	@Test
	void getListTest() throws Exception {
		String fullUrl = fillUrl(listUrl);
		when(ntfService.getNotificationList()).thenReturn(listSe);
		String response = mockMvc.perform(get(fullUrl)).andDo(print()).andExpect(status().isOk()).andReturn()
				.getResponse().getContentAsString();
		List<ServiceEmails> actualList = mapper.readValue(response, new TypeReference<>() {
		});
		assertIterableEquals(listSe, actualList);
	}

	private String fillUrl(String rest) {
		return "http://localhost:8585" + rest;
	}

}