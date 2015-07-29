package com.xhanshawn.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EventListener;
import java.util.EventObject;

public class NotiArrayList<E> extends ArrayList<E>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3870442338424687112L;
	
	
	private OnSizeChangeListener listener;
	
	@Override
	public boolean add(E object) {
		// TODO Auto-generated method stub
		if(object != null && listener != null) notifyListener(SizeChangeEvent.ADD, this.size(), this.size() + 1);

		return super.add(object);
	}
	
	

	@Override
	public boolean addAll(Collection<? extends E> collection) {
		// TODO Auto-generated method stub
		if(!collection.isEmpty() && listener != null) notifyListener(SizeChangeEvent.ADD, this.size(), this.size() + collection.size());

		return super.addAll(collection);
	}
	
	private void notifyListener(int event, int oldValue, int newValue) {
		
		listener.sizeChange(new SizeChangeEvent(this, event, oldValue, newValue));
	}
	
	public void setOnSizeChangeListener(OnSizeChangeListener newListener) {
	    listener = newListener;
	}
	
	
	
	
	
	
	
	
	
	
	public interface OnSizeChangeListener extends EventListener{
		
		public void sizeChange(SizeChangeEvent event);
	}
	
	
	
	
	
	
	
	public static class SizeChangeEvent extends EventObject {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		final public static int ADD = 111;
		final public static int REMOVE = 222;

		private int event;
		private int oldSize;
		private int newSize;
		
		public SizeChangeEvent(Object source,int event , int oldSize, int newSize) {
			super(source);
			// TODO Auto-generated constructor stub
			this.event =  event;
			this.oldSize = oldSize;
			this.newSize = newSize;
		}
		
		public int getEvent(){
			return this.event;
		}
		
		public int getNewSize(){
			return this.newSize;
		}
		
		public int getOldSize(){
			return this.oldSize;
		}
	}
}
