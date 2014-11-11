package com.jalil.environ.rss;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "rss")
public class RssFeed {
	
	@XmlElement(name = "channel")
	private Channel channel;
	
	public RssFeed() {}
	
	public RssFeed(Channel channel) {
		this.channel = channel;
	}
	
	public Channel getChannel() {
		return channel;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || !(o instanceof RssFeed))
			return false;
		
		RssFeed that = (RssFeed) o;
		
		if (!channel.equals(that.channel))
			return false;
		
		return true;
	}
	
	@Override
	public int hashCode() {
		int result = channel.hashCode();
		return result;
	}

	@Override
    public String toString() {
	    return "RssFeed [channel=" + channel + "]";
    }
}
