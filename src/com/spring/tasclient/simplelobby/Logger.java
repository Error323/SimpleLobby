package com.spring.tasclient.simplelobby;

public class Logger {
	public static enum LogLevel {
		INFO,
		WARNING,
		ERROR
	}
	
	public static enum LogOutput {
		LOG_CONSOLE,
		LOG_WINDOW,
		LOG_FILE
	};
	
	public Logger() {
		
	}
	
	public static void Log(String msg, LogLevel level) {
		switch (level) {
		case INFO:
			break;
		case WARNING:
			break;
		case ERROR:
			break;
		}
	}
	
	public static void I(String msg) {
		Log(msg, LogLevel.INFO);
	}
	
	public static void W(String msg) {
		Log(msg, LogLevel.WARNING);
	}

	public static void E(String msg) {
		Log(msg, LogLevel.ERROR);
	}
}
