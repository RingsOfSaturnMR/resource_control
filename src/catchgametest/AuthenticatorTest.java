package catchgametest;

import static org.junit.Assert.assertEquals;

import org.junit.jupiter.api.Test;

import authentication.EncryptionFilter;

public class AuthenticatorTest
{

	@Test
	public void testEntryptionFilterShouldEncryptsAndDecyrpts()
	{
		assertEquals("Hello", EncryptionFilter.encrypt("Hello", "Hello"), "Hello Should entrypt to hello");
	}
	
}
