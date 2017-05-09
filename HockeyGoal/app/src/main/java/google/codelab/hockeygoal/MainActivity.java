package google.codelab.hockeygoal;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mPlayer;
    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPlayer = MediaPlayer.create(this, R.raw.goal_sound);
        mButton = (Button) findViewById(R.id.goal);
        mButton.setText(getButtonText());
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSoundOrStop();
            }
        });
    }

    @Override
    protected void onDestroy() {
        mPlayer.stop();
        super.onDestroy();
    }

    private void playSoundOrStop() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mButton.setText(getButtonText());
        } else {
            mPlayer.start();
            mButton.setText("STOP");
        }
    }

    private String getButtonText()  {
        return "GOAL";
        // TODO: Load button text from Remote Config
    }
}
