package com.testapp.server;

import java.util.*;
import java.util.Map.Entry;

import com.testapp.server.Graph.Edge;

public class DebtShufflingCore {	
	
	public static Edge createEdge(long from, long to, int value) {
		return new Edge(from,to,value);
	}
	
	static Comparator<Entry<Long, Integer>> entryComparator = new Comparator<Entry<Long,Integer>>() {
		
		@Override
		public int compare(Entry<Long, Integer> o1, Entry<Long, Integer> o2) {
            return Math.abs(o1.getValue()) - Math.abs(o2.getValue());
		}
	};
	
	public static class MinTransactionShuffleGraph extends Graph {
		@Override
		public Set<Edge> shuffle() {
			Set<Entry<Long, Integer>> netWorths = getNetWorths();
			List<Entry<Long, Integer>> positiveNetworths = new ArrayList<Entry<Long,Integer>>();
			List<Entry<Long, Integer>> negativeNetworths = new ArrayList<Entry<Long,Integer>>();
			for (Entry<Long, Integer> netWorth : netWorths) {
				if (netWorth.getValue().intValue()<0) {
					negativeNetworths.add(netWorth);
				}
				if (netWorth.getValue().intValue()>0) {
					positiveNetworths.add(netWorth);
				}
				
			}			
			Collections.sort(positiveNetworths, entryComparator);
			Collections.sort(negativeNetworths, entryComparator);

            /*
            System.out.println("TEST .... ");
			System.out.println(positiveNetworths.toString());
			System.out.println(negativeNetworths.toString());
            System.out.println("\n\n\n");
            */

            Set<Edge> newGraph = new HashSet<Edge>();

            int positiveIndex = positiveNetworths.size()-1;
            int negativeIndex = negativeNetworths.size()-1;

            while (true) {
                //creating a new edge from A to B ( A owes B x amount of money i.e. A gives B x amount of money)

                //this user has to take in some amount of money  e.g  B
                Entry<Long, Integer> largestPositive = positiveNetworths.get(positiveIndex);
                //this user has to give away some amount of money  e.g  A
                Entry<Long, Integer> smallestNegative = negativeNetworths.get(negativeIndex);

                if(largestPositive.getValue()==0 && smallestNegative.getValue()==0) {
                    break;
                }

                int newEdgeValue = (largestPositive.getValue()>=-1*smallestNegative.getValue())?
                        -1*smallestNegative.getValue():largestPositive.getValue();
                Edge newEdge = new Edge(smallestNegative.getKey(),largestPositive.getKey(),newEdgeValue);
                newGraph.add(newEdge);

                //updating the current list of networths
                largestPositive.setValue(largestPositive.getValue()-newEdgeValue);
                smallestNegative.setValue(smallestNegative.getValue()+newEdgeValue);

                Collections.sort(positiveNetworths, entryComparator);
                Collections.sort(negativeNetworths, entryComparator);

                /*
                //DEBUG
                System.out.println("TEST .... ");
                System.out.println(newEdge.toString());
                System.out.println(positiveNetworths.toString());
			    System.out.println(negativeNetworths.toString());
			    */
            }

			return newGraph;
		}
	}
}
