package mat.unical.it.dotsandboxes.ai;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

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
import it.unical.mat.embasp.specializations.dlv2.android.DLV2AndroidService;
import mat.unical.it.dotsandboxes.model.Edge;
import mat.unical.it.dotsandboxes.model.Graph;
import mat.unical.it.dotsandboxes.model.Player;

public class PlayerASP extends Player {
    private Handler handler;
    private InputProgram fatti;
    private InputProgram var;
    private String encodingResource="dotsandboxes";
    private Context context;



    private class MyCallback implements Callback {
        @Override
        public void callback(Output o) {

            if (!(o instanceof AnswerSets)) return;
            AnswerSets answersets = (AnswerSets) o;
            Log.i("DLV", ((AnswerSets) o).getAnswerSetsString());
            
            for(AnswerSet a: answersets.getAnswersets()) {
                try {

                    for(Object obj:a.getAtoms()){
                        //Scartiamo tutto cio' che non e' un oggetto della classe Assegno
                        if(!(obj instanceof Assegno)) continue;
                        Assegno mossa = (Assegno) obj;
                        Edge daAggiungere= new Edge(mossa.getX(), mossa.getY(), mossa.getHorizontal());

                        if(!check(game, daAggiungere)) {
                            System.out.println("Non aggiungo edge - continue");
                            continue;
                        }
                        fatti.addObjectInput(daAggiungere);
                        var.clearAll();

                        game.addMove(daAggiungere);
                        //game.toNextPlayer();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }



        }
    }

    public boolean check(Graph b, Edge e) {
        ArrayList <NoEdge> mosse = b.getMosseDisponibili();
        for (int i = 0; i<mosse.size(); i++) {
            int x=mosse.get(i).getX();
            int y=mosse.get(i).getY();
            int h=mosse.get(i).getHorizontal();
            if (x==e.getRow() && y==e.getColumn() && h==e.getHorizontal()) {
                return true;
            }
        }
        return false;
    }

    public PlayerASP(String name, Context c){
        super(name);
        handler = new AndroidHandler(c, DLV2AndroidService.class);
        fatti = new ASPInputProgram();

        try {
            ASPMapper.getInstance().registerClass(Edge.class);
            ASPMapper.getInstance().registerClass(NoEdge.class);
            ASPMapper.getInstance().registerClass(Size.class);
            ASPMapper.getInstance().registerClass(Assegno.class);
        } catch (ObjectNotValidException | IllegalAnnotationException e1) {
            e1.printStackTrace();
        }

        handler.addProgram(fatti);
        this.context=c;

        String encoding = getEncodingFromResources(c);
        handler.addProgram(new InputProgram(encoding));
    }

    @Override
    public Edge move() {
        return null;
    }

    @Override
    public void moveDLV() {

        //Log.i("DLV-Player","Tocca a me e ti faccio aspettare");
        if (game.getLatestLine()!=null) {
            try {
                fatti.addObjectInput(game.getLatestLine());
                game.setLatestLine(null);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        var = new ASPInputProgram();
        ArrayList<NoEdge> mosse = game.getMosseDisponibili();
        for (NoEdge e : mosse) {
            try {
                var.addObjectInput(new NoEdge(e.getX(),e.getY(),e.getHorizontal()));
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        handler.addProgram(var);
        Log.i("FATTI",fatti.getPrograms());
        //Log.i("VAR", var.getPrograms());

        Callback callback = new MyCallback();
        handler.startAsync(callback);
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



}
