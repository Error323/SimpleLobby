package com.spring.tasclient.simplelobby;

import java.io.PrintStream;
import java.util.Vector;

public class Logger extends PrintStream {
	private Vector<PrintStream> mStreams;
	
	public Logger(PrintStream out) {
		super(out);
		System.setOut(this);
		System.setErr(this);
		mStreams = new Vector<PrintStream>();
	}
	
	public void AttachStream(PrintStream stream) {
		mStreams.add(stream);
	}
	
	@Override
	public void write(byte[] buf, int off, int len) {
		super.write(buf, off, len);
		for (PrintStream stream : mStreams)
			stream.write(buf, off, len);
	}
}
