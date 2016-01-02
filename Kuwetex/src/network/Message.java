package network;

import java.io.Serializable;

public class Message implements Serializable {
	// headers
	public static final int LOG_OUT = 0;
	public static final int LOG_ME_IN = 1;
	public static final int GET_RAPORT = 2;
	
	private final int HEADER;
	private final String MESSAGE_TEXT;
	
	public Message (String msg, int header) {
		MESSAGE_TEXT = msg; HEADER = header;
	}
	
	

	/**
	 * @return the header
	 */
	public int getHEADER() {
		return HEADER;
	}

	/**
	 * @return the message
	 */
	public String getMessage() {
		return MESSAGE_TEXT;
	}

}
