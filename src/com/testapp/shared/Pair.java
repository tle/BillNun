package com.testapp.shared;

public class Pair<K,V> {
	private K key;
	private V value;
	
	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}
	
	public K key() {
		return key;
	}
	
	public V value() {
		return value;
	}
	
}
