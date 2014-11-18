package com.jalil.environ.fetch;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class UriStreamerImpl implements UriStreamer {

	@Override
    public InputStream stream(String addr) throws MalformedURLException, IOException {
	    return new URL(addr).openConnection().getInputStream();
    }
}
