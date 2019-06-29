package googleTaskOne;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PrefixMapSum {
	
	public Map<String,Integer> baseMap;
	
	public PrefixMapSum() {
		baseMap = new HashMap <String,Integer>();
	}

	public void insert(String key, int value) {
		baseMap.put(key, value);
	}
	
    public int sum(String prefix) {
    	int sum =0;
		Set<String> allMapValues = baseMap.keySet();
		for (String aKey : allMapValues) {
			if (aKey.contains(prefix)) {
				sum+= baseMap.get(aKey);
			}
		}
		return sum;
	}

}
