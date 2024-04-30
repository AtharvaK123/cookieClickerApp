package com.example.cookieclicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ImageView iV, upgrade;
    int total = 0, extraP = 0, threeP = 0, twoP = 0;
    double clicks = 0;
    TextView cookieClicked, cpS, hints;
    ConstraintLayout cL;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Instantiating values
        iV = findViewById(R.id.imageView);
        cookieClicked = findViewById(R.id.textView);
        cL = findViewById(R.id.layout);
        cpS = findViewById(R.id.textView1);
        hints = findViewById(R.id.textView2);

        //Animated Background
        AnimationDrawable aD = (AnimationDrawable) cL.getBackground();
        aD.setEnterFadeDuration(2500);
        aD.setExitFadeDuration(2500);
        aD.start();

        //beginningRotationAnimation
        final RotateAnimation animation = new RotateAnimation(0f, 360.0f, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(10000);
        animation.setRepeatCount(Animation.INFINITE);
        setRotation(animation);

        //creating imageClicking Animation
        final ScaleAnimation animationS = new ScaleAnimation(1.0f, 0.5f, 1.0f, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animationS.setDuration(200);

        //Timer for hints
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                setHints.run();
            }
        }, 0, 12000);

        //Timer for upgrades
        Timer up = new Timer();
        up.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                spawnUpgrade();
            }
        }, 0, 120000);

        //ImageView Listener
        iV.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                int rand = (int)(Math.random()*2)+2;
                total += rand;
                iV.startAnimation(animationS);
                cookieClicked.setText(total + " Baskets");
                int foulDoesIt = (int)(Math.random()*5);
                if(foulDoesIt == 3){
                    extraP += 1;
                    total += 1;
                    //hints.setText("Foul Shot!");
                }
                update();

                if(rand == 2)
                    setTwo(true);
                else if(rand == 3)
                    setTwo(false);
            }
        });

        //cps
        Timer clicker = new Timer();
        clicker.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                total += (int)clicks;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        cookieClicked.setText(total + " Baskets");
                    }
                });
            }
        }, 0, 1000);

    }

    //updates click information and extra points
    public void update(){
        cpS.setText(extraP + " Foul Points\n" + threeP + " 3-Pointers\n" + twoP + " 2-Pointers\n" + clicks + " cps");
    }

    //set points
    private void setTwo(boolean point){
            int oofX;
            int oofY;

            //adding view
        final TextView[] plusOne = {new TextView(this)};
            plusOne[0].setId(View.generateViewId());
            if(point) {
                //for 2 pointers
                plusOne[0].setText("+2");
                twoP ++;
                plusOne[0].setTextColor(Color.BLUE);
            }
            else {
                // for 3 pointers
                plusOne[0].setText("+3");
                threeP++;
                plusOne[0].setTextColor(Color.RED);
            }

            //text size
            plusOne[0].setTextSize(20);

            //sets offset
            oofY = (int) (Math.random() * 800) + 300;
            plusOne[0].setY(oofY);
            oofX = (int) (Math.random() * 700) + 200;
            plusOne[0].setX(oofX);


            ConstraintLayout.LayoutParams textViewParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            plusOne[0].setLayoutParams(textViewParams);

            //Translate animation for +1
            TranslateAnimation tA = new TranslateAnimation(oofX, oofX, oofY, 0);
            plusOne[0].startAnimation(tA);

            cL.addView(plusOne[0]);

            final int[] Y = new int[1];
            Y[0] = oofY;

            Timer up = new Timer();
            up.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    if(Y[0] >= 0) {
                        Y[0] -= 1;
                        plusOne[0].setY(Y[0]);
                    }
                    else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                cL.removeView(plusOne[0]);
                                plusOne[0] = null;
                                up.cancel();
                            }
                        });
                    }
                }
            }, 0, 1);
            update();
        }


    //upgrade
    private void spawnUpgrade(){
        upgrade = new ImageView(this);
        upgrade.setId(View.generateViewId());
        upgrade.setBackgroundResource(R.drawable.basketball);

        //sets offset
        int oofY = (int) (Math.random() * 800) + 1;
        upgrade.setY(oofY);
        int oofX = (int) (Math.random() * 700) + 1;
        upgrade.setX(oofX);

        //zoom in animation
        final ScaleAnimation animationS = new ScaleAnimation(0f, 0.5f, 0f, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
        animationS.setDuration(2000);
        upgrade.startAnimation(animationS);

        upgrade.setScaleX(0.5f);
        upgrade.setScaleY(0.5f);

        ConstraintLayout.LayoutParams textViewParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        upgrade.setLayoutParams(textViewParams);

        //image clicked
        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicks += 1;
                update();

                //zoom out animation
                final ScaleAnimation animation = new ScaleAnimation(0.5f, 0f, 0.5f, 0f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f, ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(2000);
                upgrade.startAnimation(animation);

                cL.removeView(upgrade);
            }
        });
        cL.addView(upgrade);
    }
    // startsRotation Animation *no real need for this*
    private void setRotation(RotateAnimation animation){
        iV.startAnimation(animation);
    }

    //Thread that is called to create the hints
    Thread setHints = new Thread("Thread 1"){
        public void run(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    String[] hint = {"Every 100 points, you will gain a random number of points", "Did you know the highest scored in an NBA game is exactly 100 points?", "Chill out with those 3's!", "What a hooper!", "Still a long way to go", "To be the best, you have to work the hardest"};
                    int i = (int)(Math.random()*hint.length);
                    hints.setTextColor(Color.BLACK);
                    hints.setText(hint[i]);
                }
            });
        }
    };
}