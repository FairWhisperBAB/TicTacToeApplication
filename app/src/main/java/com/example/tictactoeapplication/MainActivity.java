package com.example.tictactoeapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton[][] buttons = new ImageButton[3][3];

    private boolean player1Turn = true;

    private int roundCount;

    private int player1Points;
    private int player2Points;

    private TextView textViewPlayer1;
    private TextView textViewPlayer2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewPlayer1 = findViewById(R.id.text_view_p1);
        textViewPlayer2 = findViewById(R.id.text_view_p2);

        for (int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                String buttonID = "button_" + i + j;
                int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
                buttons[i][j].setBackgroundResource(R.drawable.blank);
                buttons[i][j].setTag("blank");
            }
        }

        Button buttonReset = findViewById(R.id.button_reset);
        buttonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetGame();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (!((ImageButton) v).getTag().equals("blank"))
        {
            return;
        }
        if (player1Turn)
        {
            ((ImageButton) v).setBackgroundResource(R.drawable.starwars);
            ((ImageButton)v).setTag("starwars");
        }
        else
        {
            ((ImageButton) v).setBackgroundResource(R.drawable.startrek);
            ((ImageButton)v).setTag("startrek");
        }

        roundCount++;

        if (checkForWin())
        {
            if (player1Turn)
            {
                player1Wins();
            }
            else
            {
                player2Wins();
            }
        }
        else if(roundCount == 9)
        {
            Draw();
        }
        else
        {
            player1Turn = !player1Turn;
        }

    }

    private boolean checkForWin()
    {
        Object[][] field = new Object[3][3];

        for (int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                field[i][j] = buttons[i][j].getTag();
            }
        }

        for (int i = 0; i < 3; i++)
        {
            if (field[i][0].equals(field[i][1])
                    && field[i][0].equals(field[i][2])
                    && !field[i][0].equals("blank"))
            {
                return true;
            }
        }

        for (int i = 0; i < 3; i++)
        {
            if (field[0][i].equals(field[1][i])
                    && field[0][i].equals(field[2][i])
                    && !field[0][i].equals("blank"))
            {
                return true;
            }
        }

        if (field[0][0].equals(field[1][1])
                && field[0][0].equals(field[2][2])
                && !field[0][0].equals("blank"))
        {
            return true;
        }

        if (field[0][2].equals(field[1][1])
                && field[0][2].equals(field[2][0])
                && !field[0][2].equals("blank"))
        {
            return true;
        }

        return false;
    }

    private void player1Wins()
    {
        MediaPlayer scream = MediaPlayer.create(this, R.raw.wilhelmscream);
        scream.start();

        player1Points++;
        Toast.makeText(this, "Star Wars wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resetBoard();
            }
        }, 2000);
    }

    private void player2Wins()
    {
        MediaPlayer nowin = MediaPlayer.create(this, R.raw.vo_dontbelievenowin);
        nowin.start();

        player2Points++;
        Toast.makeText(this, "Star Trek wins!", Toast.LENGTH_SHORT).show();
        updatePointsText();

        new Handler().postDelayed(new Runnable() {

            public void run() {
                resetBoard();
            }
        }, 2000);
    }

    private void Draw()
    {
        MediaPlayer nice = MediaPlayer.create(this, R.raw.nioce);
        nice.start();

        Toast.makeText(this, "Draw!", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            public void run() {
                resetBoard();
            }
        }, 2000);
    }

    private void updatePointsText()
    {
        textViewPlayer1.setText("Star Wars: " + player1Points);
        textViewPlayer2.setText("Star Trek: " + player2Points);
    }

    private void resetBoard()
    {
        for (int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                buttons[i][j].setBackgroundResource(R.drawable.blank);
                buttons[i][j].setTag("blank");
            }
        }

        roundCount = 0;
        player1Turn = true;
    }

    private void resetGame()
    {
        player1Points = 0;
        player2Points = 0;
        updatePointsText();
        resetBoard();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("roundCount", roundCount);
        outState.putInt("player1Points", player1Points);
        outState.putInt("player2Points", player2Points);
        outState.putBoolean("player1Turn", player1Turn);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        roundCount = savedInstanceState.getInt("roundCount");
        player1Points = savedInstanceState.getInt("player1Points");
        player2Points = savedInstanceState.getInt("player2Points");
        player1Turn = savedInstanceState.getBoolean("player1Turn");
    }
}