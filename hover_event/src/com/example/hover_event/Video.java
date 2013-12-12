package com.example.hover_event;

import android.net.Uri;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Video extends Activity {

	private String mycapoluogo="ROMA";
	private Boolean completed=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_video);
		// Show the Up button in the action bar.
		getActionBar().hide();
		Intent intent = getIntent();
		String message = intent.getStringExtra("MESSAGE_CAPOLUOGO");
		TextView tvCapoluogo=(TextView) findViewById(R.id.capoluogoIndovinato);
		tvCapoluogo.setText(message);
		Button buttonVideo=(Button) findViewById(R.id.link);
		
		synchronized(this){
			mycapoluogo=message;
			completed=true;
			notifyAll();
		}
		
		buttonVideo.setOnClickListener(new View.OnClickListener(){
		    public void onClick(View v) {
		    	Video videoContext=(Video) v.getContext();
		    	String message="";

		    	synchronized(videoContext){
		    		while (videoContext.completed==false){
		    			try {
							videoContext.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
		    		}
		    		message=videoContext.getMyCapoluogo();
		    	}
		    	//seleziono l'uri giusto
				String toUri="";
				if (message.equals("GENOVA")){toUri="https://www.youtube.com/watch?v=5dJkqdveQC0";}
				if (message.equals("TORINO")){toUri="https://www.youtube.com/watch?v=f5AkPdMUKIY";}
				if (message.equals("AOSTA")){toUri="https://www.youtube.com/watch?v=d5sdnsXV6do";}
				if (message.equals("MILANO")){toUri="https://www.youtube.com/watch?v=IuH5rX2IF-s";}
				if (message.equals("VENEZIA")){toUri="https://www.youtube.com/watch?v=zBR5mSmQGwM";}
				if (message.equals("TRENTO")){toUri="https://www.youtube.com/watch?v=Fx0KhBh5cps";}
				if (message.equals("TRIESTE")){toUri="https://www.youtube.com/watch?v=rFVtnbM7JNg";}
				if (message.equals("BOLOGNA")){toUri="https://www.youtube.com/watch?v=xKBLBa84yyU";}
				if (message.equals("FIRENZE")){toUri="https://www.youtube.com/watch?v=WMs3DUa2RRY";}
				if (message.equals("PERUGIA")){toUri="https://www.youtube.com/watch?v=6PTaCO3Vgt4";}
				if (message.equals("ANCONA")){toUri="https://www.youtube.com/watch?v=y7buIIhJeow";}
				if (message.equals("LAQUILA")){toUri="https://www.youtube.com/watch?v=0lW0R8TTycg";}
				if (message.equals("CAMPOBASSO")){toUri="https://www.youtube.com/watch?v=o16EWb7s8yc";}
				if (message.equals("ROMA")){toUri="https://www.youtube.com/watch?v=PtTMP47Q4bU";}
				if (message.equals("NAPOLI")){toUri="https://www.youtube.com/watch?v=PHENmTSXWLc";}
				if (message.equals("POTENZA")){toUri="https://www.youtube.com/results?search_query=citta+potenza+centro+storico&sm=3";}
				if (message.equals("BARI")){toUri="https://www.youtube.com/watch?v=Mw0MdOU2afo";}
				if (message.equals("CATANZARO")){toUri="https://www.youtube.com/watch?v=D5bm15ex6jc";}
				if (message.equals("PALERMO")){toUri="https://www.youtube.com/watch?v=F8exg213txM";}
				if (message.equals("CAGLIARI")){toUri="https://www.youtube.com/watch?v=hidGoFXzkkw";}
				
				Uri webpage = Uri.parse(toUri);
				Intent webIntent = new Intent(Intent.ACTION_VIEW, webpage);
				startActivity(webIntent);
				
		    }
		}
		);
	
	}
	
	public String getMyCapoluogo(){
		return mycapoluogo;
	}
	

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */


}
