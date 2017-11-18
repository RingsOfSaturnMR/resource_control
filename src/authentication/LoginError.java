package authentication;

/**
 * These enums all describe conditions that will cause a login to fail
 * 
 * @author Nils
 *
 */
public enum LoginError
{
	/**
	 * Password does not decyrpt to be the entered password
	 */
	INVALID_PASSWORD,

	/**
	 * Username entered does not match a found username
	 */
	USER_NOT_FOUND,

	/**
	 * There are no users
	 */
	NO_USERS,

	/**
	 * Input for name or password is too short. Flags condition in which a database
	 * call should not be made
	 */
	INVALID_ATTEMPT
}
