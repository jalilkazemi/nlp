package com.jalil.environ.rss;

import java.net.URL;


public class Channel {
	
	private final String title;
	private final URL link;
	private final String description;
	private final String language;
	
	public Channel(String title, URL link, String description, String language) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.language = language;
	}

	public String getTitle() {
	    return title;
    }

	public URL getLink() {
	    return link;
    }

	public String getDescription() {
	    return description;
    }

	public String getLanguage() {
	    return language;
    }
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof Channel))
			return false;
		
		Channel that = (Channel) o;
		
		if (!title.equals(that.title))
			return false;
		if (!link.equals(that.link))
			return false;
		if(description != null ? !description.equals(that.description) : that.description != null)
			return false;
		if(language != null ? !language.equals(that.language) : that.language != null)
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = title.hashCode();
		result = 31 * result + link.hashCode();
		result = 31 * result + (description != null ? description.hashCode() : 0);
		result = 31 * result + (language != null ? language.hashCode() : 0);
		return result;
	}
}
