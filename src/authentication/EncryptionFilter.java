package authentication;

public class EncryptionFilter
{
	public static int MAX = 255;
	public static String encrypt(String plainText, String key)
	{
		StringBuilder cipherText = new StringBuilder(plainText.length());
		for (int i = 0; i < plainText.length(); i++)
		{
			char temp = (char)(plainText.charAt(i) + (key.charAt(i % key.length())));
			cipherText.append(temp);
		}
		return cipherText.toString();
	}
	
	public static String decrypt(String cipherText, String key)
	{
		StringBuilder plainText = new StringBuilder(cipherText.length());
		for (int i = 0; i < cipherText.length(); i++)
		{
			char temp = (char)(cipherText.charAt(i) - (key.charAt(i % key.length())));
			
			if ((int)(temp) < 0)
			{
				temp += MAX;
			}
			plainText.append(temp);
		}
		return plainText.toString();
	}

}