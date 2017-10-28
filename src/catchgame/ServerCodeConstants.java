package catchgame;

public class ServerCodeConstants
{
	// login response codes
	public final static int LOGIN_SUCCESS_CODE = 0;
	public final static int LOGIN_ERR_INVALID_PASSWORD_CODE = 1;
	public final static int LOGIN_ERR_USER_NOT_FOUND_CODE = 2;
	public final static int LOGIN_ERR_NO_USERS_FOUND_CODE = 3;
	public final static int LOGIN_ERR_UNKNOWN_ERROR_CODE = 4;
	

	// new user response codes
	public final static int NEW_USER_SUCESS_CODE = 10;
	public final static int NEW_USER_ERR_NAME_TAKEN_CODE = 11;
	public final static int NEW_USER_ERR_ILLEGAL_PW_CODE = 12; 
	public final static int NEW_USER_ERR_ILLEGAL_NAME_CODE = 13; 
	public final static int NEW_USER_ERR_UNKNOWN_CODE = 13; 

	
	// request codes
	public static final int REQUEST_RANDOM_SEACREATURE_CODE = 20;
	public static final int REQUEST_COD_CODE = 21;
	public static final int REQUEST_SALMON_CODE = 22;
	public static final int REQUEST_TUNA_CODE = 23;
	
	
}
