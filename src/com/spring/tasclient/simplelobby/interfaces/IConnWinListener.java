package com.spring.tasclient.simplelobby.interfaces;

public interface IConnWinListener {

	void Login(String server, int port, String username, String password);

	void Register(String server, int port, String username, String password);

}
