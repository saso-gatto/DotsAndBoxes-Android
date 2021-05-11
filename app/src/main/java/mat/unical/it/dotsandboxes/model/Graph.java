package mat.unical.it.dotsandboxes.model;

import java.util.ArrayList;
import java.util.Observable;

import mat.unical.it.dotsandboxes.ai.NoEdge;
import mat.unical.it.dotsandboxes.ai.PlayerASP;

public class Graph extends Observable {
    private Player[] players;
    private int currentPlayerIndex;
    private int width;
    private int height;
    private Player[][] occupied;
    private int[][] horizontalLines;
    private int[][] verticalLines;
    private Edge latestLine;

    public Graph(int width, int height, Player[] players) {
        this.width = width;
        this.height = height;
        this.players = players;

        occupied = new Player[height][width];
        horizontalLines = new int[height + 1][width];
        verticalLines = new int[height][width + 1];

        addPlayersToGame(players);
        currentPlayerIndex = 0;
    }

    public Player[] getPlayers() {
        return players;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Edge getLatestLine() {
        return latestLine;
    }

    public void setLatestLine(Edge e){this.latestLine=e;}

    private void addPlayersToGame(Player[] players) {
        for (Player player : players) {
            player.addToGame(this);
        }
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }


    public void start() {
        while (!isGameFinished()) {
            if (currentPlayer().getClass().equals(HumanPlayer.class)){
                addMove(currentPlayer().move());
            }
            else if(currentPlayer().getClass().equals(PlayerASP.class)){
                currentPlayer().addToGame(this);
                currentPlayer().moveDLV();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
        }
            setChanged();
            notifyObservers();
        }
    }

    public ArrayList<NoEdge> getMosseDisponibili() {
        ArrayList<NoEdge> mosse = new ArrayList<NoEdge>();
        for(int i=0; i<=5; i++)
            for(int j=0; j<5; j++)
                if(horizontalLines[i][j] == 0) {
                    mosse.add(new NoEdge(i,j,0));
                }
        for(int i=0; i<5; i++)
            for(int j=0; j<=5; j++)
                if(verticalLines[i][j] == 0) {
                    mosse.add(new NoEdge(i,j,1));
                }
        return mosse;
    }


    public void addMove(Edge move) {
        if (isLineOccupied(move)) {
            return;
        }
        boolean newBoxOccupied = tryToOccupyBox(move);
        setLineOccupied(move);
        System.out.println(move);
        latestLine = move;

       if (!newBoxOccupied){
            toNextPlayer();
       }

    }

    public Player currentPlayer() {
        return players[currentPlayerIndex];
    }

    public boolean isLineOccupied(int row, int column, int h) {
        return isLineOccupied(new Edge(row, column,h));
    }

    public boolean isLineOccupied(Edge edge) {
        switch (edge.getHorizontal()) {
            case 0:
                return (horizontalLines[edge.row()][edge.column()] == 1
                        || horizontalLines[edge.row()][edge.column()] == 2);
            case 1:
                return (verticalLines[edge.row()][edge.column()] == 1
                        || verticalLines[edge.row()][edge.column()] == 2);
        }
        throw new IllegalArgumentException();
    }

    public int getLineOccupier(Edge edge) {
        switch (edge.getHorizontal()) {
            case 0:
                return horizontalLines[edge.row()][edge.column()];
            case 1:
                return verticalLines[edge.row()][edge.column()];
        }
        throw new IllegalArgumentException();
    }

    public Player getBoxOccupier(int row, int column) {
        return occupied[row][column];
    }

    public int getPlayerOccupyingBoxCount(Player player) {
        int count = 0;
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (getBoxOccupier(i, j) == player)
                    count++;
            }
        }
        return count;
    }

    private boolean tryToOccupyBox(Edge move) {
        boolean rightOccupied = tryToOccupyRightBox(move);
        boolean underOccupied = tryToOccupyUnderBox(move);
        boolean upperOccupied = tryToOccupyUpperBox(move);
        boolean leftOccupied = tryToOccupyLeftBox(move);
        return leftOccupied || rightOccupied || upperOccupied || underOccupied;
    }

    private void setLineOccupied(Edge edge) {
        switch (edge.getHorizontal()) {
            case 0:
                horizontalLines[edge.row()][edge.column()] = currentPlayerIndex + 1;
                break;
            case 1:
                verticalLines[edge.row()][edge.column()] = currentPlayerIndex + 1;
                break;
        }
    }

    private void setBoxOccupied(int row, int column, Player player) {
        occupied[row][column] = player;
    }

    private boolean tryToOccupyUpperBox(Edge move) {
        if (move.getHorizontal() != 0 || move.row() <= 0)
            return false;
        if (isLineOccupied(new Edge(move.row()-1, move.column(), 0))
                && isLineOccupied(new Edge(move.row()-1,move.column(),1))
                && isLineOccupied(new Edge(move.row()-1,move.column()+1,1))) {
            setBoxOccupied(move.row() - 1, move.column(), currentPlayer());
            return true;
        } else {
            return false;
        }
    }

    private boolean tryToOccupyUnderBox(Edge move) {
        if (move.getHorizontal() != 0 || move.row() >= (height))
            return false;
        if (isLineOccupied(new Edge(move.row() + 1, move.column(),0))
                && isLineOccupied(new Edge(move.row(), move.column(),1))
                && isLineOccupied(new Edge(move.row(), move.column() + 1,1))) {
            setBoxOccupied(move.row(), move.column(), currentPlayer());
            return true;
        } else {
            return false;
        }
    }

    private boolean tryToOccupyLeftBox(Edge move) {
        if (move.getHorizontal() != 1 || move.column() <= 0)
            return false;
        if (isLineOccupied(new Edge(move.row(), move.column() - 1,1))
                && isLineOccupied(new Edge(move.row(), move.column() - 1,0))
                && isLineOccupied(new Edge(move.row() + 1, move.column() - 1,0))) {
            setBoxOccupied(move.row(), move.column() - 1, currentPlayer());
            return true;
        } else {
            return false;
        }
    }

    private boolean tryToOccupyRightBox(Edge move) {
        if (move.getHorizontal() != 1  || move.column() >= (width))
            return false;
        if (isLineOccupied(new Edge(move.row(), move.column() + 1,1))
                && isLineOccupied(new Edge(move.row(), move.column(),0))
                && isLineOccupied(new Edge(move.row() + 1, move.column(),0))) {
            setBoxOccupied(move.row(), move.column(), currentPlayer());
            return true;
        } else {
            return false;
        }
    }

    public void toNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % players.length;
    }

    protected boolean isGameFinished() {
        for (int i = 0; i < getHeight(); i++) {
            for (int j = 0; j < getWidth(); j++) {
                if (getBoxOccupier(i, j) == null)
                    return false;
            }
        }
        return true;
    }

    public Player getWinner() {
        if (!isGameFinished()) {
            return null;
        }

        int[] playersOccupyingBoxCount = new int[players.length];
        for (int i = 0; i < players.length; i++) {
            playersOccupyingBoxCount[i] = getPlayerOccupyingBoxCount(players[i]);
        }

        if (playersOccupyingBoxCount[0] > playersOccupyingBoxCount[1])
            return players[0];
        else
            return players[1];
    }
}
