package com.jalil.environ.fetch;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;

public class AddrToReaderImpl implements AddrToReader {

	@Override
    public Reader reader(String addr) throws MalformedURLException, IOException {
	    return new InputStreamReader(new URL(addr).openConnection().getInputStream());
    }
}
