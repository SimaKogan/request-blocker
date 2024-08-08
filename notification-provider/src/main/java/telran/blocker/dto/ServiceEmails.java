package telran.blocker.dto;

import java.util.Arrays;
import java.util.Objects;

public record ServiceEmails (String webService, String[] emails) {

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(emails);
		result = prime * result + Objects.hash(webService);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceEmails other = (ServiceEmails) obj;
		return Arrays.equals(emails, other.emails) && Objects.equals(webService, other.webService);
	}

}