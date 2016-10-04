package self.lugen.fasttap.screen;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import self.lugen.fasttap.R;
import self.lugen.fasttap.base.CustomBaseActivity;
import self.lugen.fasttap.object.JobID;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlashScreen extends BaseScreen {


    public FlashScreen() {
        super();
    }

    @SuppressLint("ValidFragment")
    public FlashScreen(CustomBaseActivity.ActivityCallback callback) {
        super(callback);
    }

    @Override
    public void updateScreen(JobID jobID, Bundle data) {

    }


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        Handler h = new Handler();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                callback.onFragmentDone(JobID.OPEN_MAIN, null);
            }
        }, 2000);

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_flash_screen, container, false);
    }
}
