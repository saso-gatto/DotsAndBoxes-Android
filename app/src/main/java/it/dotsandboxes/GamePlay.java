package it.dotsandboxes;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import com.google.android.material.internal.FlowLayout;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import it.dotsandboxes.classiEmbasp.Edge;

public class GamePlay extends View implements Observer {


    private final static int size = 8;	//Spessore linee
    private final static int dist = 50;	//Lunghezza di Edge
    //Dati utili per distanziare le line e i cerchi per il draw della matrice
    protected static final float radius = (float) 14 / 824;
    protected static final float start = (float) 6 / 824;
    protected static final float add1 = (float) 18 / 824;
    protected static final float add2 = (float) 2 / 824;
    protected static final float add3 = (float) 14 / 824;
    protected static final float add4 = (float) 141 / 824;
    protected static final float add5 = (float) 159 / 824;
    protected static final float add6 = (float) 9 / 824;

    protected Paint paint;

    private int dim;
    private Board game;
    private int turn;
    private ASPSolver blueSolver,redSolver;
    protected final int[] playerColors;
    String redName, blueName;

    public GamePlay(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        paint = new Paint();
        game= new Board(5);
        this.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                receiveInput(event);
                return false;
            }
        });
        playerColors = new int[]{getResources().getColor(R.color.arancione),
                getResources().getColor(R.color.azzurro)};
    }



    //Equivale al metodo processMove di JAVA, sono analoghi
    private void receiveInput(MotionEvent event) {
        if (event.getAction() != MotionEvent.ACTION_DOWN)
            return;

        float touchX = event.getX();
        float touchY = event.getY();
        int min = Math.min(getWidth(), getHeight());
        float start = GamePlay.start * min;
        float add1 = GamePlay.add1 * min;
        float add2 = GamePlay.add2 * min;
        float add3 = GamePlay.add3 * min;
        float add5 = GamePlay.add5 * min;
        int d = -1, a = -1, b = -1;
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if ((start + add5 * j + add1 - add3) <= touchX
                        && touchX <= (start + add5 * (j + 1) + add3)
                        && touchY >= start + add5 * i + add2 - add3
                        && touchY <= start + add5 * i + add1 - add2 + add3) {
                    d = 0;
                    a = i;
                    b = j;
                }
                if (start + add5 * i + add2 - add3 <= touchX
                        && touchX <= start + add5 * i + add1 - add2 + add3
                        && touchY >= start + add5 * j + add1 - add3
                        && touchY <= start + add5 * (j + 1) + add3) {
                    d = 1;
                    a = j;
                    b = i;
                }
            }
        }
        //a!=-1 e b!=-1?
        if ((a != -1) && (b != -1)) {
            int direction = d;
            Edge move = new Edge(a, b, direction);
            try {
                if (direction==0)
                    game.setVEdge(move.getX(),move.getY(),turn);
                else if (direction==1)
                    game.setHEdge(move.getX(),move.getY(),turn);
            } catch (Exception e) {
                Log.e("GameView", e.toString());
            }
        }
    }
    //DA RIVEDERE DOPO L'AGGIUNTA DI EMBASP
    private void manageGame() {
        while(!game.isComplete()) {

                if (turn==Board.BLUE )
                	turn = Board.RED;
                else if (turn==Board.RED)
                	turn =Board.BLUE;

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

 /*   //Object conterr� le coordinate dell'edge da aggiungere
    private Edge getSource(Object object) {
    	Edge mossa;
        for(int i=0; i<dim; i++)
            for(int j=0; j<dim-1; j++)
                if(hEdge[i][j] == object) {
                	mossa = new Edge(i,j,1);
                    return mossa;
                }
        for(int i=0; i<dim-1; i++)
            for(int j=0; j<dim; j++)
                if(vEdge[i][j] == object){
                	mossa = new Edge(i,j,0);
                    return mossa;
                }
        System.out.println("non ho trovato un oggetto - PROBLEMA");
        return new Edge();
    } */





    private boolean goBack;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawColor(0x00FFFFFF);
        int min = Math.min(getWidth(), getHeight());
        float radius = GamePlay.radius * min;
        float start = GamePlay.start * min;
        float add1 = GamePlay.add1 * min;
        float add2 = GamePlay.add2 * min;
        float add4 = GamePlay.add4 * min;
        float add5 = GamePlay.add5 * min;
        float add6 = GamePlay.add6 * min;

        //paint lines
        paint.setColor(0xFF000000);
        for (int i = 0; i < game.getDim() + 1; i++) {
            for (int j = 0; j < game.getDim(); j++) {
                Edge horizontal = new Edge(i, j,1);
                if (horizontal.equals(game.getLatestMove())) {
                    paint.setColor(0xFFFF7700);
                } else if (game.isEdgeOccupied(horizontal)) {
                    if (game.getLineOccupier(horizontal) == 1)
                        paint.setColor(playerColors[0]);
                    else
                        paint.setColor(playerColors[1]);
                } else {
                    paint.setColor(0xFFFFFFFF);
                }
                canvas.drawRect(start + add5 * j + add1, start + add5 * i
                        + add2, start + add5 * (j + 1), start + add5 * i + add1
                        - add2, paint);

                Edge vertical = new Edge(i, j,0);
                if (vertical.equals(game.getLatestMove())) {
                    paint.setColor(0xFFFF7700);
                } else if (game.isEdgeOccupied(vertical)) {
                    if (game.getLineOccupier(vertical) == 1)
                        paint.setColor(playerColors[0]);
                    else
                        paint.setColor(playerColors[1]);
                } else {
                    paint.setColor(0xFFFFFFFF);
                }
                canvas.drawRect(start + add5 * i + add2, start + add5 * j
                        + add1, start + add5 * i + add1 - add2, start + add5
                        * (j + 1), paint);
            }
        }

        //paint boxes
        for (int i = 0; i < game.getDim(); i++) {
            for (int j = 0; j < game.getDim(); j++) {
              //  paint.setColor(game.getBoxOccupier(j, i) == null ? Color.TRANSPARENT : playerColors[Player.indexIn(game.getBoxOccupier(j, i), game.getPlayers())]);
                paint.setColor(Color.BLACK);
                canvas.drawRect(start + add5 * i + add1 + add2, start
                        + add5 * j + add1 + add2, start + add5 * i + add1
                        + add4 - add2, start + add5 * j + add1 + add4
                        - add2, paint);
            }
        }

        //paint points
        paint.setColor(getResources().getColor(R.color.arancione));
        for (int i = 0; i < game.getDim() + 1; i++) {
            for (int j = 0; j < game.getDim() + 1; j++) {
                canvas.drawCircle(start + add6 + j * add5 + 1, start + add6 + i * add5 + 1,
                        radius, paint);
            }
        }

        invalidate();
    }


    @Override

    public void update(Observable o, Object arg) {

    }

}
