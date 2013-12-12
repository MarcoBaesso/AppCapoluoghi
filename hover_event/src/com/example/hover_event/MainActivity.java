package com.example.hover_event;
import android.os.Bundle;
import android.app.Activity;


public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        TabMia ui = new TabMia(this);
        getActionBar().hide();
        setContentView(ui);
	}
	
}
