package telran.blocker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;
import telran.blocker.dto.WebServiceTimestamp;

@RedisHash
@Getter

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RedisModel {
	@Id
	String Ip;
	List<WebServiceTimestamp> webServicesTimestamps;
	public  RedisModel(String IP) {
		this.Ip = IP;
		webServicesTimestamps = new ArrayList<>();
	}
	@Override
	public int hashCode() {
		return Objects.hash(IP);
	}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	RedisModel other = (RedisModel) obj;
	return Objects.equals(IP, other.Ip);
  }


}