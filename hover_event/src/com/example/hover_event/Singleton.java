package com.example.hover_event;

public class Singleton {
	
	TabMia tabMainView=null;
	
	private static Singleton singleton=null;
	// Note that the constructor is private
	private Singleton() {
		// Optional Code
	}
	
	public static Singleton getInstance() {
		if (singleton == null) {
			singleton = new Singleton();
		}
		return singleton;
	}
	
	public void setTabView(TabMia view){
		tabMainView=view;
	}
	
	public TabMia getTabView(){
		return tabMainView;
	}

}
