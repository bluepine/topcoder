package fr.inria.wimmics.tc.practice;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

public class ContestWinner {
	public int getWinner(int[] events) {
		HashMap<Integer, Integer> hm = new HashMap<Integer, Integer>();
		ValueComparator bvc =  new ValueComparator(hm);
		TreeMap<Integer,Integer> sorted_map = new TreeMap(bvc);
		
		for(int i=0;i<events.length;i++){
			int contestant  = events[i];
			Integer count = hm.get(contestant);
			if(count==null)
				count = 0;
			count++;
			hm.put(contestant, count);
		}
		sorted_map.putAll(hm);
        
//		
//		for (Integer key : hm.keySet()) {
//            System.out.println("key/value: " + key + "/"+hm.get(key));
//        }
//        
//        System.out.println("#######");
//		
//		
//        for (Integer key : sorted_map.keySet()) {
//            System.out.println("key/value: " + key + "/"+sorted_map.get(key));
//        }		
		
        if(events.length>1) {
        	Iterator it = sorted_map.values().iterator();
        	
        	Integer max1 = (Integer)it.next();
        	
        	 //System.out.println("Max1:"+max1);
        	//
        	Integer max2 = (Integer)it.next();
        	if(max1==max2) {
        		Iterator enit = sorted_map.entrySet().iterator();
        		Integer minDistance = 1000000;
        		Integer minDistanceCon = 0;
        		while(enit.hasNext()) {
        			Map.Entry<Integer, Integer> me2 = (Map.Entry<Integer, Integer>)enit.next();
        			Integer conSolved = me2.getValue();
        			Integer curCon = me2.getKey();
        			if(conSolved!=max1) {
        				break;
        			}
        			Integer distance = 0;
        			Integer cSolved = 0;
        			for(int mi=0;mi<events.length;mi++) {
        				if(curCon==events[mi]) {
        					cSolved++;
        				}
        				if(cSolved==max1) {
        					distance = mi;
        					break;
        				}
        			}
        			if(minDistance>distance) {
        				minDistance = distance;
        				minDistanceCon = curCon;
        			}
        			
        		}
        		return minDistanceCon;
        		
        	}
        	
        } 	
        Integer res = sorted_map.firstKey();
		return res;
       
	}
	
	public static void main(String agr[]) {
		ContestWinner a = new ContestWinner();
		int[] events = {123,123,456,456,456,123};
		int res = a.getWinner(events);
		System.out.println(res);
	}
	
}

class ValueComparator implements Comparator {

	Map base;

	public ValueComparator(Map base) {
		this.base = base;
	}

	public int compare(Object a, Object b) {

		if ((Integer) base.get(a) < (Integer) base.get(b)) {
			return 1;
		} else if ((Integer) base.get(a) == (Integer) base.get(b)) {
			// return 0;
			if ((Integer) a < (Integer) b) {
				return -1;
			} else if ((Integer) a == (Integer) b) {
				return 0;
			} else {
				return 1;
			}
		} else {
			return -1;
		}
	}
}
