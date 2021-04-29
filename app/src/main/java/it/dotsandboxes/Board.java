package it.dotsandboxes;

import android.graphics.Point;

import java.util.ArrayList;

import it.dotsandboxes.classiEmbasp.Edge;
import it.dotsandboxes.classiEmbasp.NoEdge;
import it.dotsandboxes.classiEmbasp.Player;

public class Board implements Cloneable  {

    final static int RED = 0;
    final static int BLUE = 1;
    final static int BLACK = 2;
    final static int BLANK = 3;
    private int turn;
    private Player[] players;
    private int currentPlayerIndex;
    private int[][] vEdge;					//Griglia linee orizzontali
    private int[][] hEdge;					//Griglia delle linee verticali 
    private int[][] box;					//Griglia del gioco		
    
	private int dim, redScore, blueScore;		//n=numero righe e colonna.
	private Edge latestMove;
	private ArrayList<Edge> mosseFatte = new ArrayList<Edge>();
	
    public Board(int n, Player [] players) {
        vEdge = new int[n+1][n+1];
        hEdge = new int[n+1][n+1];
        box = new int[n+1][n+1];
        fill(vEdge,BLANK);					//Indica che tutte le linee orizz. sono vuote
        fill(hEdge,BLANK);					//Indica che tutte le linee verticali sono vuote
        fill(box,BLANK);					//griglia vuota
        this.dim = n;
        redScore = blueScore = 0;
        turn=BLUE;
        currentPlayerIndex=Board.BLUE;
        this.players=players;
        addPlayersToGame(this.players);
    }

    public void start() {
        while (!isComplete()) {
            System.out.println("Turno corrente: "+currentPlayerIndex);
            addMove(currentPlayer().move());
//            setChanged();
 //           notifyObservers();
        }
    }

    public Player currentPlayer() {
        return players[currentPlayerIndex];
    }

    private void addPlayersToGame(Player[] players) {
        for (Player player : players) {
            player.addToGame(this);
        }
    }

    public void addMove(Edge mossa) {
      //  if (getColoreEdge(mossa.getX(),mossa.getY(),mossa.getHorizontal())==Board.BLANK) {
      //      return;
      //  }
      //  boolean newBoxOccupied = tryToOccupyBox(move);
        boolean punto = false;
        if (mossa.getHorizontal()==0)
            punto=setHEdge(mossa.getX(),mossa.getY(),currentPlayerIndex);
        else  if (mossa.getHorizontal()==1)
            punto=setVEdge(mossa.getX(), mossa.getY(),currentPlayerIndex);
        addUltimaMossa(mossa);

        if (!punto) {
            if (currentPlayerIndex == Board.RED) {
                currentPlayerIndex = Board.BLUE;
            } else if (currentPlayerIndex == Board.BLUE) {
                currentPlayerIndex = Board.RED;
            }
        }
    }

    public void svuotaMosse() {
    	this.mosseFatte.clear();
    }
    
    public void addUltimaMossa(Edge e) {
    	this.mosseFatte.add(e);
    	this.latestMove= e;
    }

    public Edge getLatestMove() {
        return latestMove;
    }

    public ArrayList <Edge> getMosseFatte() {
    	return this.mosseFatte;
    }
    
    public Boolean checkMossa(Edge e) {
    	if(mosseFatte.contains(e)) 
    		return false;
    	return true;
    }
    
    public int[][] gethEdge() {
		return vEdge;
	}

	public void sethEdge(int[][] hEdge) {
		this.vEdge = hEdge;
	}

	public int[][] getvEdge() {
		return hEdge;
	}

	public void setvEdge(int[][] vEdge) {
		this.hEdge = vEdge;
	}

	public int[][] getBox() {
		return box;
	}

	public void setBox(int[][] box) {
		this.box = box;
	}

    public int getDim() {
		return dim;
	}

	public void setDim(int n) {
		this.dim = n;
	}


    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    private void fill(int[][] array, int val) {
        for(int i=0; i<array.length; i++)
            for(int j=0; j<array[i].length; j++)
                array[i][j]=val;
    } 


    public int getRedScore() {
        return redScore;
    }

    public int getBlueScore() {
        return blueScore;
    }

    public int getScore(int color) {
        if(color == RED) 
        	return redScore;
        else 
        	return blueScore;
    }

    public static int toggleColor(int color) { //Inverte il colore usato. Serve per i turni dei partecipanti?	
        if(color == RED)
            return BLUE;
        else
            return RED;
    }
    
    

    public ArrayList<NoEdge> getMosseDisponibili() {
        ArrayList<NoEdge> mosse = new ArrayList<NoEdge>();
        for(int i=0; i<=dim; i++)
            for(int j=0; j<dim; j++)
                if(hEdge[i][j] == BLANK) {
                    mosse.add(new NoEdge(i,j,1));
                }
        for(int i=0; i<dim; i++)
            for(int j=0; j<=dim; j++)
                if(vEdge[i][j] == BLANK) {
                    mosse.add(new NoEdge(i,j,0));
                }
        return mosse;
    }

    //Il metodo setHEdge serve ad aggiungere una linea e ad assegnare un eventuale punteggio al giocatoer
    //I due if ci permettono di controllare anche i limiti della matrice
    public boolean setVEdge(int x, int y, int color) {
    	vEdge[x][y]=color;
        boolean punto = false;
        if(y<(dim) && hEdge[x][y]!=BLANK && hEdge[x+1][y]!=BLANK && vEdge[x][y+1]!=BLANK) {
            box[x][y]=color;
            punto=true;
            if(color == RED)
            	redScore++;
            else
            	blueScore++;
        }
        if(y>0 && hEdge[x][y-1]!=BLANK && hEdge[x+1][y-1]!=BLANK && vEdge[x][y-1]!=BLANK) {
            box[x][y-1]=color;
            punto=true;
            if(color == RED)
            	redScore++;
            else 
            	blueScore++;
        }
      /*  if(punto){
            if(color==RED)
                turn=BLUE;
            else if(color==BLUE)
                turn=RED;
        } */
        return punto;
    }

    //il metodo torna i quadrati creati con l'aggiunta dell'arco orizzontale in pos X,Y.
    public boolean setHEdge(int x, int y, int color) {
        hEdge[x][y]=color;
        boolean punto = false;
        if(x<(dim) && vEdge[x][y]!=BLANK && vEdge[x][y+1]!=BLANK && hEdge[x+1][y]!=BLANK) {
            box[x][y]=color;
            punto=true;
            if(color == RED) redScore++;
            else blueScore++;
        }
        if(x>0 && vEdge[x-1][y]!=BLANK && vEdge[x-1][y+1]!=BLANK && hEdge[x-1][y]!=BLANK) {
            box[x-1][y]=color;
            punto=true;
            if(color == RED) redScore++;
            else blueScore++;
        }

      /*  if(punto){
            System.out.println("Punto fatto.");
            if(color==RED)
                turn=BLUE;
            else if(color==BLUE)
                turn=RED;
        } */
        return punto;
    }

    public int getColoreEdge(int x, int y, int h) {
        if (h == 0) { // verticale
            return vEdge[x][y];
        }
        else {
            return hEdge[x][y];
        }
    }

    public int getColoreBox(int x, int y) {
            return box[x][y];
    }

    //Condizione di stop del gioco
    public boolean isComplete() {
    	if ((redScore + blueScore) == (dim* dim)) {
    		return true;
    	}
        return false;
    }
    
    //Ritorna il vincitore
    public int getWinner() {
        if(redScore > blueScore) 
        	return RED;
        else if(redScore < blueScore) 
        	return BLUE;
        else 
        	return BLANK;
    }
    
    void stampaBoard() {
    	System.out.println("*********** BOARD ************");
    	for(int i=0; i<=dim; i++)
            for(int j=0; j<dim; j++)
               if(hEdge[i][j] == BLACK) {
               	System.out.println("Edge: "+i+", "+j+",1");
              }
   	 for(int i=0; i<dim; i++)
            for(int j=0; j<=dim; j++)
               if(vEdge[i][j] == BLACK) {
               	System.out.println("Edge: "+i+", "+j+",0");
               }
    }

    public int getTotalEdge() {
    	int cont=0;
    	 for(int i=0; i<=dim; i++)
             for(int j=0; j<dim; j++)
                if(hEdge[i][j] == BLACK) {
                	cont++;
               }
    	 for(int i=0; i<dim; i++)
             for(int j=0; j<=dim; j++)
                if(vEdge[i][j] == BLACK) {
                	cont++;
                }
        return cont;
    }

    public boolean isEdgeOccupied(int row, int column, int dir) {
        return isEdgeOccupied(new Edge(row, column, dir));
    }

    //Perché 1 e 2? Da modificare
    public boolean  isEdgeOccupied(Edge edge) {
        if (edge.getHorizontal()==1){

            if(hEdge[edge.getX()][edge.getY()] == BLANK)
                return false;
            else
                return true;
        }
        else if (edge.getHorizontal()==0){

            if(vEdge[edge.getX()][edge.getY()] == BLANK)
                return false;
            else
                return true;
        }
            System.out.println("Caso non contemplato");
            return false;
    }

    public int getLineOccupier(Edge line) {
        if (line.getHorizontal()==1)
            return hEdge[line.getX()][line.getY()];
         else
                return vEdge[line.getX()][line.getY()];


    }

}
