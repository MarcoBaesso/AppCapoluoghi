package com.example.hover_event;

import java.util.ArrayList;
import java.util.HashMap;




import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        TabMia ui = new TabMia(this);
        getActionBar().hide();
        setContentView(ui);
	}
	
}
