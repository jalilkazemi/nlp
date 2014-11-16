package com.jalil.environ.fetch;

import java.io.IOException;
import java.io.Reader;
import java.net.MalformedURLException;

public interface AddrToReader {
	
	public Reader reader(String addr)  throws MalformedURLException, IOException;
}
