package catchgame;

import java.io.Serializable;

public class ServerCodePacket implements Serializable
{
	final int SERVER_CODE;

	ServerCodePacket(int serverCode)
	{
		SERVER_CODE = serverCode;
	}
}
