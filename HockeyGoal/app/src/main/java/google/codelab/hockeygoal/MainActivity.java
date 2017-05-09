package google.codelab.hockeygoal;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

public class MainActivity extends AppCompatActivity {

    private MediaPlayer mPlayer;
    private Button mButton;
    private FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        mFirebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build());
        int cacheExpiration = (BuildConfig.DEBUG)?  0 : 3600; // Cache is invalid after an hour in production
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "Fetch Succeeded",
                                    Toast.LENGTH_SHORT).show();

                            // Once the config is successfully fetched it must be activated before newly fetched
                            // values are returned.
                            mFirebaseRemoteConfig.activateFetched();
                        } else {
                            Toast.makeText(MainActivity.this, "Fetch Failed",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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
        return mFirebaseRemoteConfig.getString("button_text");
    }
}
