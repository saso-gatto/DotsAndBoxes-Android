package it.dotsandboxes;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import it.dotsandboxes.classiEmbasp.Player;

public class Activity_Game extends AppCompatActivity {

    private  GamePlay gamePlay;
    private TextView ScoreBlue,ScoreRed;
    Player[] players;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        gamePlay = findViewById(R.id.gameView);
        ASPSolver redSolver=new ASPSolver("RedSolver", getApplicationContext());
        players = new Player[]{new HumanPlayer("Human"), new HumanPlayer("Mozione di sfiducia")};
        startGame(players);
    }

    private void startGame(Player[] players) {
        gamePlay.startGame(players);
    }






}