package telran.blocker.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;
import trlran.blocker.dto.WebServiceTimestamp;

@RedisHash
@Getter

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class RedisModel
@Id
String IP;
List<WebServiceTimestamp> webServicesTimestamps;
public RedisModel(String IP) {
	this.IP = IP;
	webServicesTimestamps = new ArrayList<>();
}
@Override
public int hashCode( ) {
	return Objects.hash(IP);
}
@Override
public boolean equals(Object obj) {
	if (this == obj)
		return false;
	RedisModel other = (RedisModel) obj;
	return Object.equals(IP, other.IP);
}