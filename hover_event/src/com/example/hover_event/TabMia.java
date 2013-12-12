package com.example.hover_event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;


public class TabMia extends TableLayout{
	
	private boolean firstTime=true;
	
	private enum Posizioni{
		LD,LU,RD,RU
	}
	
	private class Coppia{
		private float x;
		private float y;
		
		public Coppia(float xx,float yy){
			x=xx;
			y=yy;
		}
		
		public float getX(){
			return x;
		}
		
		public float getY(){
			return y;
		}
	}
	//mappa definisce i button che un certo button può toccare
	private HashMap<Integer,ArrayList<Button>> mappa;
	
	private ArrayList<Integer> percorso=null;
	private ArrayList<Button> buttons=null;
	private TextView textCitta=null;
	private HashMap<Integer,HashMap<Posizioni,Coppia>> dimButton;
	private HashMap<Integer,Button> chiaveBottone;
	private Boolean dimButtonSet=false;
	private String capoluogo=new String("");
	private Context activityContext=null;
	private ArrayList<String> nomeCapoluoghi;
	//matriceDisplacement viene utilizzata per individuare le celle occupate o meno
	private Boolean matriceDisplacement[][]=new Boolean[4][4];
	private static String MESSAGE_CAPOLUOGO="MESSAGE_CAPOLUOGO";
	
	private int[][] indexPosizioniArray=new int[6][6]; 
	
	//26 lettere
	private String[] Lettere={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	
	public TabMia(Context context){
		super(context);
		activityContext=context;
		// TODO Auto-generated constructor stub
        inflateLayout(context);
        
        assegnaCapoluoghi();
        
        riempiIndexArray();
        	
		displaceCapoluogo();

	}
	
	private void inflateLayout(Context context) {
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.activity_main, this); 
		invalidate();
		buttons=this.bundleButtons(view);
		textCitta=(TextView) view.findViewById(R.id.citta);
		mappa=this.creaMappa(view);
	}
	
	private void assegnaCapoluoghi(){
		nomeCapoluoghi=new ArrayList<String>();
		nomeCapoluoghi.add("GENOVA");
		nomeCapoluoghi.add("TORINO");
		nomeCapoluoghi.add("AOSTA");
		nomeCapoluoghi.add("MILANO");
		nomeCapoluoghi.add("VENEZIA");
		nomeCapoluoghi.add("TRENTO");
		nomeCapoluoghi.add("TRIESTE");
		nomeCapoluoghi.add("BOLOGNA");
		nomeCapoluoghi.add("FIRENZE");
		nomeCapoluoghi.add("PERUGIA");
		nomeCapoluoghi.add("ANCONA");
		nomeCapoluoghi.add("LAQUILA");
		nomeCapoluoghi.add("CAMPOBASSO");
		nomeCapoluoghi.add("ROMA");
		nomeCapoluoghi.add("NAPOLI");
		nomeCapoluoghi.add("POTENZA");
		nomeCapoluoghi.add("BARI");
		nomeCapoluoghi.add("CATANZARO");
		nomeCapoluoghi.add("PALERMO");
		nomeCapoluoghi.add("CAGLIARI");
	}
	
	private void displaceCapoluogo(){
		ArrayList<Coppia> displacement=new ArrayList<Coppia>();
		Random random = new Random();
		//scegli città
		int posCitta=randomize(0, 19, random);
		String citta=nomeCapoluoghi.get(posCitta);
		char[] explodeCitta=citta.toCharArray();

		int X=randomize(1,4,random);
		int Y=randomize(1,4,random);
		int indexToGo=0;
		Coppia cellToGo=null;
		
		//primo displacement
		Coppia xy=new Coppia(X,Y);
		displacement.add(xy);
		ArrayList<Coppia> boundary=boundaryCells(xy);
		boundary=eliminaCelleNulle(boundary);
		
		//boundary ora contiene solo le celle in cui è possibile continuare il displacement
		//secondo displacement
		indexToGo=randomize(0,boundary.size()-1,random);
		cellToGo=(Coppia) boundary.get(indexToGo);
		displacement.add(cellToGo);
		boundary.clear();
		boundary=boundaryCells(cellToGo);
		boundary=eliminaCelleNulle(boundary);
		boundary=eliminaCelleInDisplacement(boundary,displacement);
		
		//displacement delle altre explodeCitta.length-3 cifre 
		for (int i=2;i<explodeCitta.length;i++){
			indexToGo=randomize(0,boundary.size()-1,random);
			if (boundary.size()==0)
				indexToGo=indexToGo+0;
			cellToGo=(Coppia) boundary.get(indexToGo);
			displacement.add(cellToGo);
			boundary.clear();
			boundary=boundaryCells(cellToGo);
			boundary=eliminaCelleNulle(boundary);
			boundary=eliminaCelleInDisplacement(boundary,displacement);
			if ((i+1)<(explodeCitta.length-1)){
				boundary=eliminaCelleIsolate(boundary,displacement,(explodeCitta.length-i));	
			}
		}
		//
			resetMatrice();
		//
		//ho creato il displacement ora lo piazzo
		for (int i=0;i<displacement.size();i++){
			Coppia disp=displacement.get(i);
			matriceDisplacement[(int) (disp.getX()-1)][(int) (disp.getY()-1)]=true;
			if (disp.getX()==1 && disp.getY()==1){
				buttons.get(0).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==1 && disp.getY()==2){
				buttons.get(1).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==1 && disp.getY()==3){
				buttons.get(2).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==1 && disp.getY()==4){
				buttons.get(3).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==2 && disp.getY()==1){
				buttons.get(4).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==2 && disp.getY()==2){
				buttons.get(5).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==2 && disp.getY()==3){
				buttons.get(6).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==2 && disp.getY()==4){
				buttons.get(7).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==3 && disp.getY()==1){
				buttons.get(8).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==3 && disp.getY()==2){
				buttons.get(9).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==3 && disp.getY()==3){
				buttons.get(10).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==3 && disp.getY()==4){
				buttons.get(11).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==4 && disp.getY()==1){
				buttons.get(12).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==4 && disp.getY()==2){
				buttons.get(13).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==4 && disp.getY()==3){
				buttons.get(14).setText((String.valueOf(explodeCitta[i])));}
			if (disp.getX()==4 && disp.getY()==4){
				buttons.get(15).setText((String.valueOf(explodeCitta[i])));}
		}
		
		//riempimento celle non occupate
		
		for (int i=0;i<4;i++){
			for (int j=0;j<4;j++){
				if (!matriceDisplacement[i][j]){
					buttons.get(i*4+j).setText(randomLettera(random));
				}
			}
		}
		
		if (firstTime)
			Toast.makeText(activityContext, "Qual è il capoluogo italiano nascosto?", Toast.LENGTH_SHORT).show();
		firstTime=false;
	}
	
	private String randomLettera(Random r){
		int indexLettera=randomize(0,25,r);
		return Lettere[indexLettera];
	}
	
	private void resetMatrice(){
		for (int i=0;i<4;i++){
			for (int j=0;j<4;j++){
				matriceDisplacement[i][j]=false;
			}
		}
	}
	
	private ArrayList<Coppia> eliminaCelleInDisplacement(ArrayList<Coppia> boundary,ArrayList<Coppia> displacement){
		int dim=boundary.size();
		ArrayList<Coppia> toReturn=new ArrayList<Coppia>();
		boolean segnale=true;
		for (int i=0;i<dim;i++){
			Coppia xyBoundary=boundary.get(i);
			segnale=true;
			for (int j=0;j<displacement.size() && segnale;j++){
				Coppia xyDisplacement=displacement.get(j);
				if (xyBoundary.getX()==xyDisplacement.getX() && xyDisplacement.getY()==xyBoundary.getY()){
					segnale=false;
				}
			}
			if (segnale){
				toReturn.add(xyBoundary);
			}
		}
		boundary.clear();
		return toReturn;
	}
	
	//il seguente metodo elimina elementi dall'array passato
	//INPUT: boundary sono le celle in cui è possibile muoversi, displacement è il displace scelto finora, toPosix indica il numero di lettere che rimangono da posizionare
	private ArrayList<Coppia> eliminaCelleIsolate(ArrayList<Coppia> boundary,ArrayList<Coppia> displacement, int toPosix){
		boolean segnale=true;
		int dim=boundary.size();
		ArrayList<Coppia> toReturn=new ArrayList<Coppia>();
		
		//caso base non ho più lettere da schierare
		if (toPosix==0){
			boundary.clear();
			toReturn.add(new Coppia(1,1));
			return toReturn;
		}
		else{
		for(int i=0;i<dim;i++){
			Coppia xybound=boundary.get(i);
			ArrayList<Coppia> boundaryBound=boundaryCells(xybound);
			segnale=true;
			for(int j=0;j<boundaryBound.size() && segnale;j++){
				Coppia xybounded=boundaryBound.get(j);
				if (indexPosizioniArray[(int) xybounded.getX()][(int) xybounded.getY()]==0){
					segnale=true;
				}
				else{
					if (inDisplacement(xybounded,displacement)){
						segnale=true;
					}
					else{
						segnale=false;
					}
				}
			}
			
			if (!segnale){
				//la cella xybound è un possibile candidato dato che ha almeno una cella da cui continuare il displacement
				//ora vedo se esiste dalla cella xybound un percorso di almeno toPosix celle
				ArrayList<Coppia> newDisplace=deepCopy(displacement);
				newDisplace.add(xybound);
				ArrayList<Coppia> newBoundary=boundaryCells(xybound);
				newBoundary=eliminaCelleNulle(newBoundary);
				newBoundary=eliminaCelleInDisplacement(newBoundary,newDisplace);
				newBoundary=eliminaCelleIsolate(newBoundary,newDisplace,toPosix-1);
				if (newBoundary.size()!=0)
					toReturn.add(xybound);
			}
		}
		boundary.clear();
		return toReturn;
		}
	}
	
	private ArrayList<Coppia> deepCopy(ArrayList<Coppia> displacement){
		ArrayList<Coppia> newDisplace=new ArrayList<Coppia>();
		for (int i=0;i<displacement.size();i++){
			newDisplace.add(new Coppia(displacement.get(i).getX(),displacement.get(i).getY()));
		}
		return newDisplace;
	}
	
	private boolean inDisplacement(Coppia xy,ArrayList<Coppia> displacement){
		for (int i=0;i<displacement.size();i++){
			Coppia inDisplacement=displacement.get(i);
			if (inDisplacement.getX()==xy.getX() && inDisplacement.getY()==xy.getY()){
				return true;
			}
		}
		return false;
	}
	
	//ritorna sempre 8 vicini; 8 perchè sono considerati anche quelli fittizi
	private ArrayList<Coppia> boundaryCells(Coppia xy){
		ArrayList<Coppia> boundary= new ArrayList<Coppia>();
		int X=(int) xy.getX();
		int Y=(int) xy.getY();
		//boundary sx
		boundary.add(new Coppia(X,Y-1));
		//boundary dx
		boundary.add(new Coppia(X,Y+1));
		//boundary up
		boundary.add(new Coppia(X-1,Y));
		//boundary dw
		boundary.add(new Coppia(X+1,Y));
		//boundary obliquo sx up
		boundary.add(new Coppia(X-1,Y-1));
		//boundary obliquo sx dw
		boundary.add(new Coppia(X+1,Y-1));
		//boundary obliquo dx up
		boundary.add(new Coppia(X-1,Y+1));
		//boundary obliquo dx dw
		boundary.add(new Coppia(X+1,Y+1));
		return boundary;
		
	}
	
	//il seguente metodo elimina elementi dall'array passato
	private ArrayList<Coppia> eliminaCelleNulle(ArrayList<Coppia> boundary){
		int dim=boundary.size();
		ArrayList<Coppia> toReturn=new ArrayList<Coppia>();
		for (int i=0;i<dim;i++){
			Coppia xy=boundary.get(i);
			if (!(indexPosizioniArray[(int) xy.getX()][(int) xy.getY()]==0)){
				toReturn.add(xy);
			}
		}
		boundary.clear();
		return toReturn;
	}
	
	private int randomize(int from, int to, Random random){
		long range = (long)to - (long)from + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * random.nextDouble());
	    int randomNumber =  (int)(fraction + from);
	    return randomNumber;
	}
	
	//crea entry da mettere nel db
	
	private void riempiIndexArray(){
		//riempi prima riga
		for (int i=0;i<6;i++){
			indexPosizioniArray[0][i]=0;
		}
		//riempi prima colonna
		for (int i=0;i<6;i++){
			indexPosizioniArray[i][0]=0;
		}
		//riempi ultima colonna
		for (int i=0;i<6;i++){
			indexPosizioniArray[i][5]=0;
		}
		//riempi ultima riga
		for (int i=0;i<6;i++){
			indexPosizioniArray[5][i]=0;
		}
		//riempi contenuto
		Integer indexProgressivo=1;
		for (int i=1;i<5;i++){
			for (int j=1;j<5;j++){
				indexPosizioniArray[i][j]=indexProgressivo;
				indexProgressivo++;
			}
		}
	}
	
	@SuppressLint("UseSparseArrays")
	public void setSizeButtons(){
		HashMap<Integer,HashMap<Posizioni,Coppia>> mapButtons=new HashMap<Integer,HashMap<Posizioni,Coppia>>();
		
		for (int i=0;i<buttons.size();i++){
    		View target = buttons.get(i);
            View parent = (View) target.getParent();
            int left = target.getLeft();
            int top = target.getTop();
            while(parent != null){
            	left += parent.getLeft();
            	top += parent.getTop();
            try{
            	parent = (View) parent.getParent();
            }
            catch (ClassCastException e){parent=null;}
            }
            HashMap<Posizioni,Coppia> size=new HashMap<Posizioni,Coppia>();
            /*
            Coppia ld=new Coppia(left,top);
            Coppia lu=new Coppia(left,Math.abs(top-buttons.get(i).getHeight()));
            Coppia rd=new Coppia(left+buttons.get(i).getWidth(),top);
            Coppia ru=new Coppia(left+buttons.get(i).getWidth(),Math.abs(top-buttons.get(i).getHeight()));
            */
            Coppia ld=new Coppia(left,top+(buttons.get(i).getHeight()/2));
            Coppia lu=new Coppia(left,Math.abs(top-(buttons.get(i).getHeight()/2)));
            Coppia rd=new Coppia(left+buttons.get(i).getWidth(),top+(buttons.get(i).getHeight()/2));
            Coppia ru=new Coppia(left+buttons.get(i).getWidth(),Math.abs(top-(buttons.get(i).getHeight()/2)));

            size.put(Posizioni.LD,ld);
            size.put(Posizioni.LU,lu);
            size.put(Posizioni.RD,rd);
            size.put(Posizioni.RU,ru);
            mapButtons.put(buttons.get(i).getId(),size);
        }
		dimButton=mapButtons;	
	}
	
	@Override
    public boolean onTouchEvent(MotionEvent ev) {
		//set sizes of your button if necessary
		if (!dimButtonSet)
			this.setSizeButtons();
		dimButtonSet=true;
		
		float x=ev.getX(ev.getPointerCount()-1);
        float y=ev.getY(ev.getPointerCount()-1);
    	Coppia pt=new Coppia(x,y);
    	boolean segnale=true;
		
    	switch (ev.getAction()) {
        
		case MotionEvent.ACTION_DOWN:
			
			for (int j=0;j<buttons.size() && segnale;j++){
				if (isCovered(dimButton.get(buttons.get(j).getId()),pt)){
					segnale=false;
					buttons.get(j).setBackgroundResource(R.drawable.mybuttontouch);
					percorso=new ArrayList<Integer>();
					percorso.add(buttons.get(j).getId());
					String conc=new String((String) buttons.get(j).getText());
					capoluogo=capoluogo + conc;
					textCitta.setText(capoluogo);
					invalidate();
				}
			}
			return true;
		
        
		case MotionEvent.ACTION_MOVE:
			
			if (percorso!=null){
			for (int j=0;j<buttons.size() && segnale;j++){
				if (isCovered(dimButton.get(buttons.get(j).getId()),pt)){
					segnale=false;
					Integer lastVisited=percorso.get(percorso.size()-1);
					
					if (!lastVisited.equals(buttons.get(j).getId())){
						boolean segnaleVisit=false;
						ArrayList<Button>toVisit=mappa.get(lastVisited);
						for(int h=0;h<toVisit.size() && !segnaleVisit;h++){
							Integer visit=toVisit.get(h).getId();
							if (visit.equals(buttons.get(j).getId())) segnaleVisit=true;
						}
						if (segnaleVisit && !inPercorso(buttons.get(j).getId())){
							buttons.get(j).setBackgroundResource(R.drawable.mybuttontouch);
							percorso.add(buttons.get(j).getId());
							String conc=new String((String) buttons.get(j).getText());
							capoluogo=capoluogo + conc;
							textCitta.setText(capoluogo);
							invalidate();
						}
					}
				}
			}
			}
			return true;

		case (MotionEvent.ACTION_UP):
			if (percorso!=null){
				
				//controllo se la parola inserita è una parola corretta
				boolean shot=false;
				for (int i=0;i<nomeCapoluoghi.size() && !shot;i++){
					if (capoluogo.equals(nomeCapoluoghi.get(i)))
						shot=true;
				}
				//shot=true => capoluogo trovato
				if (shot){
				    Intent intent = new Intent(activityContext, Video.class);
				    intent.putExtra(MESSAGE_CAPOLUOGO, capoluogo);
					activityContext.startActivity(intent);
						for (int i=0;i<percorso.size();i++){
							Button butt=chiaveBottone.get(percorso.get(i));
							butt.setBackgroundResource(R.drawable.mybutton);
						}
						percorso.clear();
						percorso=null;
						capoluogo=new String("");
						displaceCapoluogo();
					
				}
				else{
					for (int i=0;i<percorso.size();i++){
						Button butt=chiaveBottone.get(percorso.get(i));
						butt.setBackgroundResource(R.drawable.mybutton);
					}
					percorso.clear();
					percorso=null;
					capoluogo=new String("");
				}
			}
			capoluogo=new String("");
			//capoluogo contiene la stringa

			return true;
		}

		return true;

	
	}
	
	private boolean inPercorso(Integer key){
		for(int i=0;i<percorso.size();i++){
			if (percorso.get(i).equals(key)) return true;
		}
		return false;
	}
	
	@SuppressLint("NewApi")
	@Override
	public boolean onInterceptTouchEvent (MotionEvent event){
		return true;
	}
	
	private boolean isCovered(HashMap<Posizioni,Coppia> sizes,Coppia pt){
		
		//use for debugging, getting values
		/*
		float ldx=sizes.get(Posizioni.LD).getX();
		float ldy=sizes.get(Posizioni.LD).getY();
		float rdx=sizes.get(Posizioni.RD).getX();
		float rdy=sizes.get(Posizioni.RD).getY();
		float lux=sizes.get(Posizioni.LU).getX();
		float luy=sizes.get(Posizioni.LU).getY();
		float rux=sizes.get(Posizioni.RU).getX();
		float ruy=sizes.get(Posizioni.RU).getY();
		float xx=pt.getX();
		float xy=pt.getY();
		*/
		
		if (sizes.get(Posizioni.LD).getX()<=pt.getX() && pt.getX()<=sizes.get(Posizioni.RD).getX()){
			if (sizes.get(Posizioni.LU).getY()<=pt.getY() && pt.getY()<=sizes.get(Posizioni.LD).getY())
				return true;
		}
		return false;
	}
	
	@SuppressLint("UseSparseArrays")
	private ArrayList<Button> bundleButtons(View v){
		ArrayList<Button> b=new ArrayList<Button>();
		chiaveBottone=new HashMap<Integer,Button>();
		b.add((Button) v.findViewById(R.id.b1));
		b.add((Button) v.findViewById(R.id.b2));
		b.add((Button) v.findViewById(R.id.b3));
		b.add((Button) v.findViewById(R.id.b4));
		b.add((Button) v.findViewById(R.id.b5));
		b.add((Button) v.findViewById(R.id.b6));
		b.add((Button) v.findViewById(R.id.b7));
		b.add((Button) v.findViewById(R.id.b8));
		b.add((Button) v.findViewById(R.id.b9));
		b.add((Button) v.findViewById(R.id.b10));
		b.add((Button) v.findViewById(R.id.b11));
		b.add((Button) v.findViewById(R.id.b12));
		b.add((Button) v.findViewById(R.id.b13));
		b.add((Button) v.findViewById(R.id.b14));
		b.add((Button) v.findViewById(R.id.b15));
		b.add((Button) v.findViewById(R.id.b16));
		for (int j=0;j<b.size();j++){
			chiaveBottone.put(b.get(j).getId(), b.get(j));
		}
		return b;
	}
	
	@SuppressLint("UseSparseArrays")
	private HashMap<Integer,ArrayList<Button>> creaMappa(View v){
		HashMap<Integer,ArrayList<Button>> mappa=new HashMap<Integer,ArrayList<Button>>();
		ArrayList<Button> ab1=new ArrayList<Button>();
		ArrayList<Button> ab2=new ArrayList<Button>();
		ArrayList<Button> ab3=new ArrayList<Button>();
		ArrayList<Button> ab4=new ArrayList<Button>();
		ArrayList<Button> ab5=new ArrayList<Button>();
		ArrayList<Button> ab6=new ArrayList<Button>();
		ArrayList<Button> ab7=new ArrayList<Button>();
		ArrayList<Button> ab8=new ArrayList<Button>();
		ArrayList<Button> ab9=new ArrayList<Button>();
		ArrayList<Button> ab10=new ArrayList<Button>();
		ArrayList<Button> ab11=new ArrayList<Button>();
		ArrayList<Button> ab12=new ArrayList<Button>();
		ArrayList<Button> ab13=new ArrayList<Button>();
		ArrayList<Button> ab14=new ArrayList<Button>();
		ArrayList<Button> ab15=new ArrayList<Button>();
		ArrayList<Button> ab16=new ArrayList<Button>();
		// bottone1
		ab1.add((Button) v.findViewById(R.id.b2));
		ab1.add((Button) v.findViewById(R.id.b5));
		ab1.add((Button) v.findViewById(R.id.b6));
		mappa.put(R.id.b1,ab1);
		// bottone2
		ab2.add((Button) v.findViewById(R.id.b1));
		ab2.add((Button) v.findViewById(R.id.b3));
		ab2.add((Button) v.findViewById(R.id.b5));
		ab2.add((Button) v.findViewById(R.id.b6));
		ab2.add((Button) v.findViewById(R.id.b7));
		mappa.put(R.id.b2,ab2);
		// bottone3
		ab3.add((Button) v.findViewById(R.id.b2));
		ab3.add((Button) v.findViewById(R.id.b4));
		ab3.add((Button) v.findViewById(R.id.b6));
		ab3.add((Button) v.findViewById(R.id.b7));
		ab3.add((Button) v.findViewById(R.id.b8));
		mappa.put(R.id.b3,ab3);
		// bottone4
		ab4.add((Button) v.findViewById(R.id.b3));
		ab4.add((Button) v.findViewById(R.id.b7));
		ab4.add((Button) v.findViewById(R.id.b8));
		mappa.put(R.id.b4,ab4);
		// bottone5
		ab5.add((Button) v.findViewById(R.id.b1));
		ab5.add((Button) v.findViewById(R.id.b2));
		ab5.add((Button) v.findViewById(R.id.b6));
		ab5.add((Button) v.findViewById(R.id.b9));
		ab5.add((Button) v.findViewById(R.id.b10));
		mappa.put(R.id.b5,ab5);
		// bottone6
		ab6.add((Button) v.findViewById(R.id.b1));
		ab6.add((Button) v.findViewById(R.id.b2));
		ab6.add((Button) v.findViewById(R.id.b3));
		ab6.add((Button) v.findViewById(R.id.b5));
		ab6.add((Button) v.findViewById(R.id.b7));
		ab6.add((Button) v.findViewById(R.id.b9));
		ab6.add((Button) v.findViewById(R.id.b10));
		ab6.add((Button) v.findViewById(R.id.b11));
		mappa.put(R.id.b6,ab6);
		// bottone7
		ab7.add((Button) v.findViewById(R.id.b2));
		ab7.add((Button) v.findViewById(R.id.b3));
		ab7.add((Button) v.findViewById(R.id.b4));
		ab7.add((Button) v.findViewById(R.id.b6));
		ab7.add((Button) v.findViewById(R.id.b8));
		ab7.add((Button) v.findViewById(R.id.b10));
		ab7.add((Button) v.findViewById(R.id.b11));
		ab7.add((Button) v.findViewById(R.id.b12));
		mappa.put(R.id.b7,ab7);
		// bottone8
		ab8.add((Button) v.findViewById(R.id.b3));
		ab8.add((Button) v.findViewById(R.id.b4));
		ab8.add((Button) v.findViewById(R.id.b7));
		ab8.add((Button) v.findViewById(R.id.b11));
		ab8.add((Button) v.findViewById(R.id.b12));
		mappa.put(R.id.b8,ab8);
		// bottone9
		ab9.add((Button) v.findViewById(R.id.b5));
		ab9.add((Button) v.findViewById(R.id.b6));
		ab9.add((Button) v.findViewById(R.id.b10));
		ab9.add((Button) v.findViewById(R.id.b13));
		ab9.add((Button) v.findViewById(R.id.b14));
		mappa.put(R.id.b9,ab9);
		// bottone10
		ab10.add((Button) v.findViewById(R.id.b5));
		ab10.add((Button) v.findViewById(R.id.b6));
		ab10.add((Button) v.findViewById(R.id.b7));
		ab10.add((Button) v.findViewById(R.id.b9));
		ab10.add((Button) v.findViewById(R.id.b11));
		ab10.add((Button) v.findViewById(R.id.b13));
		ab10.add((Button) v.findViewById(R.id.b14));
		ab10.add((Button) v.findViewById(R.id.b15));
		mappa.put(R.id.b10,ab10);
		// bottone11
		ab11.add((Button) v.findViewById(R.id.b6));
		ab11.add((Button) v.findViewById(R.id.b7));
		ab11.add((Button) v.findViewById(R.id.b8));
		ab11.add((Button) v.findViewById(R.id.b10));
		ab11.add((Button) v.findViewById(R.id.b12));
		ab11.add((Button) v.findViewById(R.id.b14));
		ab11.add((Button) v.findViewById(R.id.b15));
		ab11.add((Button) v.findViewById(R.id.b16));
		mappa.put(R.id.b11,ab11);
		// bottone12
		ab12.add((Button) v.findViewById(R.id.b7));
		ab12.add((Button) v.findViewById(R.id.b8));
		ab12.add((Button) v.findViewById(R.id.b11));
		ab12.add((Button) v.findViewById(R.id.b15));
		ab12.add((Button) v.findViewById(R.id.b16));
		mappa.put(R.id.b12,ab12);
		// bottone13
		ab13.add((Button) v.findViewById(R.id.b9));
		ab13.add((Button) v.findViewById(R.id.b10));
		ab13.add((Button) v.findViewById(R.id.b14));
		mappa.put(R.id.b13,ab13);
		// bottone14
		ab14.add((Button) v.findViewById(R.id.b9));
		ab14.add((Button) v.findViewById(R.id.b10));
		ab14.add((Button) v.findViewById(R.id.b11));
		ab14.add((Button) v.findViewById(R.id.b13));
		ab14.add((Button) v.findViewById(R.id.b15));
		mappa.put(R.id.b14,ab14);
		// bottone15
		ab15.add((Button) v.findViewById(R.id.b10));
		ab15.add((Button) v.findViewById(R.id.b11));
		ab15.add((Button) v.findViewById(R.id.b12));
		ab15.add((Button) v.findViewById(R.id.b14));
		ab15.add((Button) v.findViewById(R.id.b16));
		mappa.put(R.id.b15,ab15);
		// bottone16
		ab16.add((Button) v.findViewById(R.id.b11));
		ab16.add((Button) v.findViewById(R.id.b12));
		ab16.add((Button) v.findViewById(R.id.b15));
		mappa.put(R.id.b16,ab16);
		return mappa;
	}
}
