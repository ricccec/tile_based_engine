/**
 * 
 */
package pokemon_online.utils;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Cecchi
 *
 */
public class Tuple<K,V> {
	
	public static <K, V> Tuple<K,V> newTuple(K key, V value) {
		return new Tuple<K,V>(key, value);
	}
	
	private final Map.Entry<K, V> entry;
	
	public Tuple(K key, V value) {
		entry = new AbstractMap.SimpleImmutableEntry<K,V>(key, value);
	}
	
	public K getKey() {
		return entry.getKey();
	}


	public V getValue() {
		return entry.getValue();
	}

	@Override
	public String toString() {
		return entry.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entry == null) ? 0 : entry.hashCode());
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
		Tuple other = (Tuple) obj;
		if (entry == null) {
			if (other.entry != null)
				return false;
		} else if (!entry.equals(other.entry))
			return false;
		return true;
	}
}
