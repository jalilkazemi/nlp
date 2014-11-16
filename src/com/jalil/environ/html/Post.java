package com.jalil.environ.html;

import java.util.Date;

public class Post {

	private String meta;
	private String body;
	private Date fetchedTime;
	
	public Post(String meta, String body, Date fetchedTime) {
		this.meta = meta;
		this.body = body;
		this.fetchedTime = fetchedTime;
	}

	public String getMeta() {
    	return meta;
    }

	public String getBody() {
    	return body;
    }

	public Date getFetchedTime() {
		return fetchedTime;
	}
	
	@Override
    public String toString() {
	    return "Post [meta=" + meta + ", body=" + body + ", fetched at=" + fetchedTime + "]";
    }
}
