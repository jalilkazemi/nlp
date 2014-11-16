package com.jalil.environ.html;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "html")
public class Body {

	@XmlElementWrapper(name = "body")
	@XmlElement(name = "div")
	private Set<Division> divisions;
	
	private Body() {}
	
	public Body(Set<Division> divisions) {
		this.divisions = Collections.unmodifiableSet(new HashSet<Division>(divisions));
	}
	
	public Set<Division> getDivisions() {
		return divisions;
	}

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result
	            + ((divisions == null) ? 0 : divisions.hashCode());
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
	    Body other = (Body) obj;
	    if (divisions == null) {
		    if (other.divisions != null)
			    return false;
	    } else if (!divisions.equals(other.divisions))
		    return false;
	    return true;
    }

	@Override
    public String toString() {
	    return "Body [divisions=" + divisions + "]";
    }
}
