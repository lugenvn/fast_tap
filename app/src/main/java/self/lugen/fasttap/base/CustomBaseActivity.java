package self.lugen.fasttap.base;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import self.lugen.fasttap.object.JobID;
import self.lugen.fasttap.R;
import self.lugen.fasttap.screen.BaseScreen;

public class CustomBaseActivity extends FragmentActivity {

    private boolean activityVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void switchScreen(BaseScreen screen, Bundle b) {
        // Create fragment and give it an argument specifying the article it should show
        screen.setArguments(b);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.enter, R.anim.exit);

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, screen);
        transaction.addToBackStack(screen.getClass().getName());

        // Commit the transaction
        transaction.commit();
    }

    public boolean isActivityVisible() {
        return activityVisible;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityVisible = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityVisible = false;
    }

    public interface ActivityCallback{
        void onFragmentDone(JobID jobID, Bundle data);
    }
}
