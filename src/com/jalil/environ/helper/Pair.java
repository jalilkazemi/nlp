package com.jalil.environ.helper;

import static java.util.Map.Entry;

public class Pair<K, V> implements Entry<K, V> {

	private K k;
	private V v;
	
	public Pair(K k, V v) {
		this.k = k;
		this.v = v;
	}
	
	@Override
    public K getKey() {
	    return k;
    }

	@Override
    public V getValue() {
	    return v;
    }

	@Override
    public V setValue(V v) {
		this.v = v;
		return v;
    }

	@Override
    public int hashCode() {
	    final int prime = 31;
	    int result = 1;
	    result = prime * result + ((k == null) ? 0 : k.hashCode());
	    result = prime * result + ((v == null) ? 0 : v.hashCode());
	    return result;
    }

	@Override
    public boolean equals(Object obj) {
	    if (this == obj)
		    return true;
	    if (obj == null)
		    return false;
	    if (!(obj instanceof Entry))
		    return false;
	    Entry other = (Entry) obj;
	    if (k == null) {
		    if (other.getKey() != null)
			    return false;
	    } else if (!k.equals(other.getKey()))
		    return false;
	    if (v == null) {
		    if (other.getValue() != null)
			    return false;
	    } else if (!v.equals(other.getValue()))
		    return false;
	    return true;
	}	 
}
