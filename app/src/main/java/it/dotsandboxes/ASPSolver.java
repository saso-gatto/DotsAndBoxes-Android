package it.dotsandboxes;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import it.dotsandboxes.classiEmbasp.Assegno;
import it.dotsandboxes.classiEmbasp.Edge;
import it.dotsandboxes.classiEmbasp.NoEdge;
import it.dotsandboxes.classiEmbasp.Size;
import it.unical.mat.embasp.base.Callback;
import it.unical.mat.embasp.base.Handler;
import it.unical.mat.embasp.base.InputProgram;
import it.unical.mat.embasp.base.Output;
import it.unical.mat.embasp.languages.IllegalAnnotationException;
import it.unical.mat.embasp.languages.ObjectNotValidException;
import it.unical.mat.embasp.languages.asp.ASPInputProgram;
import it.unical.mat.embasp.languages.asp.ASPMapper;
import it.unical.mat.embasp.languages.asp.AnswerSet;
import it.unical.mat.embasp.languages.asp.AnswerSets;
import it.unical.mat.embasp.platforms.android.AndroidHandler;
import it.unical.mat.embasp.platforms.desktop.DesktopHandler;
import it.unical.mat.embasp.specializations.dlv2.android.DLV2AndroidService;
import it.unical.mat.embasp.specializations.dlv2.desktop.DLV2DesktopService;

public class ASPSolver {

	private static String encodingResource="dotsandboxes";
	private static Handler handler;
	private boolean start;
	
	private InputProgram facts;
	private InputProgram var;

	private Edge daAggiungere;


	public ASPSolver(Context context) {

		handler = new AndroidHandler(context, DLV2AndroidService.class);

		//classe Edge che viene prima registrata all'ASPMapper
		try {
			ASPMapper.getInstance().registerClass(Edge.class);
			ASPMapper.getInstance().registerClass(NoEdge.class);
			ASPMapper.getInstance().registerClass(Size.class);
			ASPMapper.getInstance().registerClass(Assegno.class);

		} catch (ObjectNotValidException | IllegalAnnotationException e1) {
			e1.printStackTrace();
		}

		facts= new ASPInputProgram();
		this.start=true;
		String encoding = getEncodingFromResources(context);
		handler.addProgram(new InputProgram(encoding));

	}


	private class MyCallback implements Callback {
		@Override
		public void callback(Output o) {

			System.out.println("*************** Sono in callback ******************");

			if(!(o instanceof AnswerSets))return;
			AnswerSets answersets=(AnswerSets)o;
			Log.i("DLV", ((AnswerSets) o).getAnswerSetsString());
			if(answersets.getAnswersets().size()==0)return;


			System.out.println(answersets.getOptimalAnswerSets());
			for(AnswerSet a: answersets.getOptimalAnswerSets()) {
				try {
					System.out.println("Stampa AS");
					System.out.println(a.toString());

					for(Object obj:a.getAtoms()){


						//Scartiamo tutto cio' che non e' un oggetto della classe Assegno
						if(!(obj instanceof Assegno)) continue;

						Assegno mossa = (Assegno) obj;
						daAggiungere= new Edge(mossa.getX(), mossa.getY(), mossa.getHorizontal());

						//if(!check(b, ritorna)) {
							//System.out.println("Non aggiungo edge - continue");
						//	continue;
						//}
						facts.addObjectInput(daAggiungere);

						var.clearAll();
						System.out.println(daAggiungere);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}



		}
	}

	private String getEncodingFromResources(Context c){
		InputStream ins = c.getResources().openRawResource(
				c.getResources().getIdentifier(encodingResource,
						"raw", c.getPackageName()));
		BufferedReader reader=new BufferedReader(new InputStreamReader(ins));
		String line="";
		StringBuilder builder=new StringBuilder();
		try {
			while ((line = reader.readLine()) != null) {
				builder.append(line);
				builder.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}
	
	
	
	public void aggiungiFatto(Board b) {
		if(b.getMosseFatte().size()!= 0) {
			try {
				ArrayList<Edge> mosse = b.getMosseFatte();
				for (Edge e : mosse) {
					facts.addObjectInput(e);
				}
			} catch (Exception e) { e.printStackTrace(); }
			b.svuotaMosse();
		}		
	}
	
		public boolean check(Board b,Edge e) {
		ArrayList <NoEdge> mosse = b.getMosseDisponibili();
		for (int i = 0; i<mosse.size(); i++) {
			int x=mosse.get(i).getX();
			int y=mosse.get(i).getY();
			int h=mosse.get(i).getHorizontal();
			if (x==e.getX() && y==e.getY() && h==e.getHorizontal()) {
				return true;
			}
		}
		return false;
	}
	
	public void aggiungiMosseDisponibili(InputProgram var, Board b) {
		ArrayList<NoEdge> mosse = b.getMosseDisponibili();
		for (NoEdge e : mosse) {
			try {
				var.addObjectInput(new NoEdge(e.getX(),e.getY(),e.getHorizontal()));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
	
	
	public Edge getNextMove(Board b) {

		if (this.start) {
			try {
				facts.addObjectInput(new Size(b.getDim()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			var = new ASPInputProgram();
			handler.addProgram(facts);
			handler.addProgram(var);

			this.start=false;
		}
		this.aggiungiFatto(b);
		this.stampaAS();
		this.aggiungiMosseDisponibili(var,b);
		Edge ritorna=null;


		Callback callback = new MyCallback();
		handler.startAsync(callback);

		//AnswerSets answersets = (AnswerSets) o;
		/*
		if (answersets.getAnswersets().size() <= 0) {
			System.out.println("No AS");
			System.out.println();
		}
		
		System.out.println(answersets.getOptimalAnswerSets());
		for(AnswerSet a: answersets.getOptimalAnswerSets()) {	
			try {
				System.out.println("Stampa AS");
				System.out.println(a.toString());
				
				for(Object obj:a.getAtoms()){

					
					//Scartiamo tutto cio' che non e' un oggetto della classe Assegno
					if(!(obj instanceof Assegno)) continue;
					
					Assegno mossa = (Assegno) obj;
					ritorna= new Edge(mossa.getX(), mossa.getY(), mossa.getHorizontal());					
				
					if(!check(b, ritorna)) {
						//System.out.println("Non aggiungo edge - continue");
						continue;
					}		
					facts.addObjectInput(ritorna);
					
					var.clearAll();
					return ritorna;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} 
		}
		 */
		return new Edge(0,0,0);



	}
	
	
	public void stampaAS () {
		System.out.println("******** STAMPA AS ********");
		System.out.println(facts.getPrograms());
	}


}
