package com.jalil.environ.html;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(namespace = "com.jalil.environ.html.Body")
public class Division {

	@XmlAttribute(name = "class")
	private String classAttr;
	
	@XmlValue
	private String content;
	
	public Division() {}
	
	public Division(String classAttr, String content) {
		this.classAttr = classAttr;
		this.content = content;
	}

	public String getClassAttr() {
    	return classAttr;
    }

	public String getContent() {
    	return content;
    }

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
	            + ((classAttr == null) ? 0 : classAttr.hashCode());
	    result = prime * result + ((content == null) ? 0 : content.hashCode());
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (getClass() != obj.getClass())
		    return false;
	    Division other = (Division) obj;
	    if (classAttr == null) {
		    if (other.classAttr != null)
			    return false;
	    } else if (!classAttr.equals(other.classAttr))
		    return false;
	    if (content == null) {
		    if (other.content != null)
			    return false;
	    } else if (!content.equals(other.content))
		    return false;
	    return true;
    }

	@Override
    public String toString() {
	    return "Division [classAttr=" + classAttr + ", content=" + content + "]";
    }
}
