package self.lugen.fasttap;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import self.lugen.fasttap.base.CustomBaseActivity;
import self.lugen.fasttap.object.JobID;
import self.lugen.fasttap.screen.FlashScreen;
import self.lugen.fasttap.screen.HomeScreen;
import self.lugen.fasttap.screen.PlayScreen;
import self.lugen.fasttap.screen.ScoreScreen;

/**
 * @author lugen
 *         The Tap counting activity
 */
public class MainActivity extends CustomBaseActivity implements CustomBaseActivity.ActivityCallback {
    public static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) {
            return;
        }

        Fragment startScreen = new FlashScreen(this);
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, startScreen, startScreen.getClass
                ().getName()).setCustomAnimations(R.anim.enter, R.anim.exit).commit();
    }

    @Override
    public void onFragmentDone(JobID jobID, Bundle data) {
        switch (jobID) {
            case OPEN_MAIN:
                switchScreen(new HomeScreen(this), null);
                break;
            case START_GAME:
                switchScreen(new PlayScreen(this), null);
                break;
            case SHOW_SCORE:
                switchScreen(new ScoreScreen(this), data);
                break;
            case REPLAY:
                FragmentManager fm = getSupportFragmentManager();
                fm.popBackStack();

                Fragment fragment = fm.findFragmentByTag(PlayScreen.class.getName());
                if (fragment instanceof PlayScreen) {
                    ((PlayScreen) fragment).updateScreen(JobID.REPLAY, null);
                }
                break;
        }

    }

    @Override
    public void onBackPressed() {
        // Block back button
    }
}
