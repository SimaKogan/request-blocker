package telran.blocker.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import telran.blocker.dto.ServiceEmails;

@Document(collection = "service-emails")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
public class ServiceEmailsDoc {
	@Id
	String webService;
	String[] emails;

	public ServiceEmailsDoc(ServiceEmails serviceEmails) {
		this.webService = serviceEmails.webService();
		this.emails = serviceEmails.emails();
	}

	public ServiceEmails build() {
		return new ServiceEmails(webService, emails);
	}
}