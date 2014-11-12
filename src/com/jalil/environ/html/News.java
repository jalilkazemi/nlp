package com.jalil.environ.html;

public class News {

	private String meta;
	private String body;
	
	public News(String meta, String body) {
		this.meta = meta;
		this.body = body;
	}

	public String getMeta() {
    	return meta;
    }

	public String getBody() {
    	return body;
    }

	@Override
    public String toString() {
	    return "News [meta=" + meta + ", body=" + body + "]";
    }
}
