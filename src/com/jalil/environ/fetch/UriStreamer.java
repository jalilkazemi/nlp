package com.jalil.environ.fetch;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public interface UriStreamer {
	
	public InputStream stream(String uri)  throws MalformedURLException, IOException;
}
