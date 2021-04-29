package it.dotsandboxes.classiEmbasp;

import org.antlr.v4.misc.Graph;

import it.dotsandboxes.Board;

public abstract class Player {
    protected final String name;
    protected Board game;

    public Player(String name) {
        this.name = name;
    }

    public static int indexIn(Player player, Player[] players) {
        for (int i = 0; i < players.length; i++) {
            if (player == players[i])
                return i;
        }
        return -1;
    }

    public abstract Edge move();

    public Board getGame() {
        return game;
    }

    public void addToGame(Board game) {
        this.game = game;
    }

    public String getName() {
        return name;
    }
}