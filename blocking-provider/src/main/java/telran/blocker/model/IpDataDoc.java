package telran.blocker.model;

import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.*;
import telran.blocker.dto.IpData;


@Document(collection = "blocking-list")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class IpDataDoc {
	@Id
	String IP;
	@Setter
	String webService;
	@Setter
	long timestamp;

	@Override
	public int hashCode() {
		return Objects.hash(IP, webService);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IpDataDoc other = (IpDataDoc) obj;
		return Objects.equals(IP, other.IP) && Objects.equals(webService, other.webService);
	}
	
	public IpDataDoc(IpData ipData) {
		this.IP = ipData.IP();
		this.webService = ipData.webService();
		this.timestamp = System.currentTimeMillis();
	}
	
	public IpData buildDto() {
		return new IpData(IP, webService, timestamp);
		
	}

}