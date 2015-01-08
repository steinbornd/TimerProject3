package com.myexample.timerproject3;

import java.util.Locale;
import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.Engine;
import android.speech.tts.TextToSpeech.OnInitListener;


public class MainActivity extends ActionBarActivity implements OnInitListener {
	
	private boolean timer1HasStarted = false;
	private boolean timer2HasStarted = false;
	private Button stop;
	private Button timer1;
	private Button timer2;
	private TextView remaining;
	private TextView elapsed;
	private TextToSpeech tts;
	private ToneGenerator tone;
	private String words = "Your microwave popcorn is ready";
	private String words2 = "Your stovetop popcorn is ready";
	
	private int timeInSeconds1 = 10;
	private int timeInSeconds2 = 5;
	
	private int timeInMilli1 = 10000;
	private int timeInMilli2 = 5000;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        remaining=(TextView)findViewById(R.id.time_remaining);
        elapsed=(TextView)findViewById(R.id.time_elapsed);
        timer1 = (Button) findViewById(R.id.timer1);
        timer1.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View arg0) {
                timer11.start();
                timer1HasStarted = true;
                timer1.setEnabled(false);
                timer2.setEnabled(false);
                stop.setEnabled(true);
            }});
        timer2 = (Button) findViewById(R.id.timer2);
        timer2.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View arg0) {
                timer21.start();
                timer2HasStarted = true;
                timer1.setEnabled(false);
                timer2.setEnabled(false);
                stop.setEnabled(true);
            }});
        stop = (Button) findViewById(R.id.stop);
        stop.setEnabled(false);
        stop.setOnClickListener(new OnClickListener(){
        	@Override
            public void onClick(View arg0) {
                	timer1HasStarted = false;
                	timer2HasStarted = false;
                	timer11.cancel();
                	timer21.cancel();
                	timer1.setEnabled(true);
                    timer2.setEnabled(true);
                    stop.setEnabled(false);
                	remaining.setText(String.valueOf("Time Remaining: "));
       	         	elapsed.setText(String.valueOf("Time Elapsed: "));
                	
            }});
        tts = new TextToSpeech(this, this);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    CountDownTimer timer11 = new CountDownTimer(timeInMilli1, 1000){
    		
    	     public void onTick(long millisUntilFinished) {
    	         remaining.setText(String.valueOf("Time Remaining: " + millisUntilFinished / 1000));
    	         elapsed.setText(String.valueOf("Time Elapsed: " + (timeInSeconds1 - millisUntilFinished / 1000)));
    	     }

    	     public void onFinish() {
    	    	 remaining.setText(String.valueOf("Time Remaining: " + 0));
    	    	 elapsed.setText(String.valueOf("Time Elapsed: " + timeInSeconds1));
    	         timer1.setEnabled(true);
    	    	 timer2.setEnabled(true);
    	    	 stop.setEnabled(false);
    	    	 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    	    	 v.vibrate(500);
    	    	 tts.setLanguage(Locale.US);
    	    	 speakWords(words);
    	  }
    };
    CountDownTimer timer21 = new CountDownTimer(timeInMilli2, 1000){
    	
    	public void onTick(long millisUntilFinished) {
    	    elapsed.setText(String.valueOf("Time Elapsed: " + (timeInSeconds2 - millisUntilFinished / 1000)));
    	    remaining.setText(String.valueOf("Time Remaining: " + millisUntilFinished / 1000));
    	}
    	     public void onFinish() {
    	    	 remaining.setText(String.valueOf("Time Remaining: " + 0));
    	    	 elapsed.setText(String.valueOf("Time Elapsed: " + timeInSeconds2));
    	    	 timer1.setEnabled(true);
    	    	 timer2.setEnabled(true);
    	    	 stop.setEnabled(false);
    	    	 Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    	    	 v.vibrate(500);
    	    	 speakWords(words2);
    	  }
    };
    private void speakWords(String speech) {
    	 
    	ToneGenerator tone = new ToneGenerator(AudioManager.STREAM_ALARM, 50);
   	 	tone.startTone(ToneGenerator.TONE_CDMA_ALERT_CALL_GUARD, 600);
   	 	tts.speak(" ", TextToSpeech.QUEUE_FLUSH, null);
   	 	tts.setLanguage(Locale.US);
   	 	tts.playSilence(2000, TextToSpeech.QUEUE_ADD, null);
        tts.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }
    public void onInit(int code) {
          if (code==TextToSpeech.SUCCESS) {
        	  tts.setLanguage(Locale.getDefault());

          } 
          else {
        	  tts = null;
        	  Toast.makeText(this, "Failed to initialize TTS engine.",
        	  Toast.LENGTH_SHORT).show();
          }
    }
}
