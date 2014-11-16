package com.jalil.environ.html;

public class Post {

	private String meta;
	private String body;
	
	public Post(String meta, String body) {
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
	    return "Post [meta=" + meta + ", body=" + body + "]";
    }
}
