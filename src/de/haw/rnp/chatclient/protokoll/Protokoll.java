package de.haw.rnp.chatclient.protokoll;

public interface Protokoll {
	public static final String QUIT = ".quit";
	
	public static final String ASK_USERS = ".clients";
	public static final String USER_PREFIX = ".clients";
	public static final String USER_SPLIT = "; ";
	
	public static final boolean SERVERSTARTS = true;
	public static final String AUTH_START = ".auth";
	public static final String AUTH_START_CLIENT = "";
	public static final String AUTH_USERNAME = "";
	public static final String AUTH_ACC = ".ack";
	public static final String AUTH_FAILED = ".authfail";
	
	public static final String MSGPREFIX = ".message";
	
	public static final String USERSUPDATE = ".new";
	public static final boolean USERASK = false;
	public static final int ASKTHREAD_SLEEP = 100;
}
