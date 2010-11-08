package com.testapp.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DebtShufflingCore {
	
	/**
	 * intermediary structure used for shuffling purpose
	 * @author tle
	 *
	 */
	public static class Edge {
		long from;
		long to;
		int value;
		Edge reverse = null;
		
		/**
		 * the user with id "from" owes "value" amount of money to the user with id "to"
		 * @param from
		 * @param to
		 * @param value
		 */
		public Edge(long from, long to, int value) {
			super();
			this.from = from;
			this.to = to;
			this.value = value;
		}
		
		@Override
		public String toString() {
			return "D("+from+","+to+") = " + value;
		}
		
		public Edge reverseEdge () {
			if (reverse == null) {
				reverse = new Edge (this.to, this.from, value * -1);
			}
			return reverse;
		}
				
	}
	
	public abstract static class Graph {
		protected Set<Edge> set = null;
		
		public Graph () {
			set = new HashSet<Edge>();
		}
		
		public Graph(Set<Edge> set) {
			this.set = set;
		}
		
		public void addEdge (Edge edge) {
			set.add(edge);
		}
		
		public Set<Map.Entry<Long, Integer>> getNetWorths () {
			Map<Long, Integer> userIdToNetWorthMap = new HashMap<Long, Integer>();
			for (Edge edge : set) {
				Long from = edge.from;
				Long to = edge.to;
				
				Integer tmpNetWorth = null;
				if (userIdToNetWorthMap.containsKey(from)) {
					tmpNetWorth = userIdToNetWorthMap.get(from);
					tmpNetWorth = tmpNetWorth - edge.value;					
				} else {
					tmpNetWorth = Integer.valueOf(edge.value*-1);
				}
				userIdToNetWorthMap.put(from, Integer.valueOf(tmpNetWorth));
				
				if (userIdToNetWorthMap.containsKey(to)) {
					tmpNetWorth = userIdToNetWorthMap.get(to);
					tmpNetWorth = tmpNetWorth + edge.value;					
				} else {
					tmpNetWorth = Integer.valueOf(edge.value);
				}
				userIdToNetWorthMap.put(to, Integer.valueOf(tmpNetWorth));
			}
			
			return userIdToNetWorthMap.entrySet();
		}
		
		public Set<Edge> getOriginalGraph () {
			return set;
		}
		
		public abstract Set<Edge> shuffle();
	}
	
	public static Edge createEdge(long from, long to, int value) {
		return new Edge(from,to,value);
	}
}
