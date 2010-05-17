package com.spring.tasclient.simplelobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.Queue;

public abstract class AConnection implements Runnable{
	public static final int CONNECTION_TIME_OUT = 20000;
	
	protected String           mServer;
	protected int              mPort;
	protected Socket           mConnection;
	protected volatile boolean mConnected;
	
	private BufferedReader     mIn;
	private PrintWriter        mOut;
	private Queue<String>      mData;
	private Thread             mThread;
	
	private volatile boolean   mStopped;
	
	public AConnection() {
		mStopped = false;
		mConnected = false;
		mData = new LinkedList<String>();
		mThread = new Thread(this);
		mThread.start();
	}
	
	/**
	 * 
	 * @param server
	 * @param port
	 */
	public void Connect(String server, int port) {
		synchronized (this) {
			if (mConnected) {
				System.err.println("Already connected to " + mConnection);
				return;
			}
		}
		
		System.out.println("Connecting to " + server + ":" + port);
		mServer = server;
		mPort = port;
		try {
			Connecting();
			mConnection = new Socket(mServer, mPort);
			mConnection.setSoTimeout(CONNECTION_TIME_OUT);
			mIn = new BufferedReader(
				new InputStreamReader(mConnection.getInputStream()));
			mOut = new PrintWriter(mConnection.getOutputStream(), true);
			mConnected = true;
			mData.clear();
		} catch (SocketTimeoutException e) {
			Disconnect();
			NetworkTimeout(e);
		} catch (UnknownHostException e) {
			Disconnect();
			UnknownHost(e);
		} catch (IOException e) {
			Disconnect();
			NetworkError(e);
		}
	}
	
	/**
	 * 
	 */
	public void Destroy() {
		if (mConnected)
			Disconnect();
		mStopped = true;
		mConnection = null;
		mIn = null;
		mOut = null;
		synchronized (this) {
			try {
				mThread.join();
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * 
	 */
	public void Disconnect() {
		if (mConnected) {
			System.out.println("Disconnecting from " + mConnection);
			synchronized (this) {
				mOut.close();
				try {
					mIn.close();
					mConnection.close();
					mConnected = false;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public boolean IsConnected() {
		return mConnected;
	}
	
	public void run() {
		while (!mStopped) {
			if (mConnected) {
				Update();
				try {
					if (mIn.ready())
						Received(mIn.readLine());
				} catch (IOException e) {
					Disconnect();
					NetworkError(e);
				}
				synchronized (this) {
					if (!mData.isEmpty())
						mOut.println(mData.remove());
				}
			}
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {}
		}
	}
	
	/**
	 * 
	 * @param data
	 */
	protected void Send(String data) {
		synchronized (this) {
			if (!mConnected)
				System.out.println("Cannot send " + data);
			else
				mData.add(data);
		}
	}
	
	protected abstract void Received(String data);
	protected abstract void NetworkTimeout(Exception e);
	protected abstract void UnknownHost(Exception e);
	protected abstract void NetworkError(Exception e);
	protected abstract void Update();
	protected abstract void Connecting();
}
