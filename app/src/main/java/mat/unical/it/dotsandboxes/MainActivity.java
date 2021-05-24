package mat.unical.it.dotsandboxes;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;


import java.util.Map;

import mat.unical.it.dotsandboxes.ai.PlayerASP;
import mat.unical.it.dotsandboxes.model.HumanPlayer;
import mat.unical.it.dotsandboxes.model.Player;
import mat.unical.it.dotsandboxes.view.GameView;
import mat.unical.it.dotsandboxes.view.PlayersStateView;


public class MainActivity extends AppCompatActivity implements PlayersStateView {

    protected GameView gameView;
    protected TextView player1name, player2name, player1occupying, player2occupying;
    ImageView currentPlayerPointer;
    Player[] players;
    Integer[] playersOccupying = new Integer[]{0, 0};
    Player currentPlayer;
    MediaPlayer music;


    private Chronometer c;
    private ImageView btnMusica;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameView = (GameView) findViewById(R.id.gameView);
        gameView.setPlayersState(this);

        player1name = (TextView) findViewById(R.id.player1name);
        player2name = (TextView) findViewById(R.id.player2name);
        player1occupying = (TextView) findViewById(R.id.player1occupying);
        player2occupying = (TextView) findViewById(R.id.player2occupying);
        currentPlayerPointer = (ImageView) findViewById(R.id.playerNowPointer);

        c = findViewById(R.id.chronometer1);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int value = extras.getInt("key");

            if(value == 1) {
                players = new Player[]{new HumanPlayer("Giocatore 1"), new HumanPlayer("Giocatore 2")};
                player1name.setText("Giocatore 1");
                player2name.setText("Giocatore 2");
            }
            else if(value == 2) {
                players = new Player[]{new HumanPlayer("Giocatore"), new PlayerASP("DLV2", getApplicationContext())};
                player1name.setText("Giocatore 1");
                player2name.setText("DLV2");
            }
            else if(value == 3) {
                players = new Player[]{new PlayerASP("DLV2", getApplicationContext()), new HumanPlayer("Giocatore")};
                player1name.setText("DLV2");
                player2name.setText("Giocatore 1");
            }
            else if(value == 4) {
                players = new Player[]{new PlayerASP("DLV2-1", getApplicationContext()), new PlayerASP("DLV2-2", getApplicationContext())};
                player1name.setText("DLV2-1");
                player2name.setText("DLV2-2");
            }

        }


        Button b = (Button) findViewById(R.id.backHome);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Torna alla Home")
                        .setPositiveButton("Esci", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(MainActivity.this, Activity_Start.class);
                                startActivity(intent);
                                finish();
                                gameView.stopGame();
                                music.stop();
                            }
                        })
                        .setNeutralButton("Annulla", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                            }
                        }).show();
            }
        });

        btnMusica = (ImageView) findViewById(R.id.btnMusicaPlayStop);
        btnMusica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(music.isPlaying()) {
                    music.pause();
                    btnMusica.setImageResource(R.drawable.mute);
                }
                else{
                    music.start();
                    btnMusica.setImageResource(R.drawable.unmute);
                }
            }
        });



            Log.i("Toggle Musica","Avvio della musica");
            music = MediaPlayer.create(MainActivity.this, R.raw.music);
            music.start();
            music.setLooping(true);



        c.setTypeface(ResourcesCompat.getFont(this, R.font.coiny));
        c.setTextSize(18);
        c.setBase(SystemClock.elapsedRealtime());
        c.start();

        startGame(players);
    }


    private void startGame(Player[] players) {
        gameView.startGame(players);
    }

    public void updateState() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (currentPlayer == players[0]) {
                    currentPlayerPointer.setImageResource(R.drawable.a1);
                } else if (currentPlayer == players[1]) {
                    currentPlayerPointer.setImageResource(R.drawable.a2);
                }
                player1occupying.setText("Punteggio " + playersOccupying[0]);
                player2occupying.setText("Punteggio " + playersOccupying[1]);
            }
        });
    }

    @Override
    public void setCurrentPlayer(Player player) {
        currentPlayer = player;
        updateState();
    }

    @Override
    public void setPlayerOccupyingBoxesCount(Map<Player, Integer> player_occupyingBoxesCount_map) {
        playersOccupying[0] = (player_occupyingBoxesCount_map.get(players[0]));
        playersOccupying[1] = (player_occupyingBoxesCount_map.get(players[1]));
        updateState();
    }


    @Override
    public void setWinner(final Player winner) {

        c = findViewById(R.id.chronometer1);
        c.stop();
        int elapsed = (int) (SystemClock.elapsedRealtime()-c.getBase());
        System.out.println(elapsed);

        music.stop();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, Activity_End.class);

                intent.putExtra("vincitore",winner.getName());
                intent.putExtra("tempo",elapsed/1000);
                intent.putExtra("punteggio1",playersOccupying[0]);
                intent.putExtra("punteggio2",playersOccupying[1]);
                startActivity(intent);
                finish();
            }
        });

    }



}
