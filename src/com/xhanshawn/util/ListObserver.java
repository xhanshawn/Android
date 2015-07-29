package com.xhanshawn.util;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

public class ListObserver<E> {
	
	private List<E> list;
	private PropertyChangeListener listener;
	public ListObserver(List<E> list){
		this.list = list;
	}
	
	
	private void notifyListener(Object object, String property, int oldValue, int newValue) {
		listener.propertyChange(new PropertyChangeEvent(this, property, oldValue, newValue));
	}
	
	public void addChangeListener(PropertyChangeListener newListener) {
	    listener = newListener;
	}
}
