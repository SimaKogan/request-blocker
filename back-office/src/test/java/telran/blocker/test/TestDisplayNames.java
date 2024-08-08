package telran.blocker.test;

public interface TestDisplayNames {
	String ADD_IP_SUCCESS = "add IP normal";
	String ADD_IP_EXCEPT = "add IP with exception";
	String DELETE_IP_SUCCESS = "delete IP normal";
	String DELETE_IP_EXCEPT = "delete IP with exception";
	
	String IP_ILLEGAL_ARGUMENT = "IP IllegalArgumentException 1";
	String IP_ARGUMENT_NOT_VALID_LENGTH = "IP MethodArgumentNotValidException length";
	String IP_ARGUMENT_NOT_VALID_MISSING = "IP MethodArgumentNotValidException missing";

	
	
	String ADD_SERVICE_SUCCESS = "add SERVICE normal";
	String ADD_SERVICE_EXCEPT = "add SERVICE with exception";
	String DELETE_SERVICE_SUCCESS = "delete SERVICE normal";
	String DELETE_SERVICE_EXCEPT = "delete SERVICE with exception";
	
	


	String ADD_SERVICE_EMAILS_SUCCESS = "Add ServiceEmails Success";
	String ADD_SERVICE_EMAILS_EXCEPTION = "Add ServiceEmails Exception: Already Exists";
	String SERVICE_EMAILS_VALIDATION_ERROR = "ServiceEmails Validation Error";
	String DELETE_SERVICE_EMAILS_SUCCESS = "Delete ServiceEmails Success";
	String DELETE_SERVICE_EMAILS_EXCEPTION = "Delete ServiceEmails Exception: Not Found";

}