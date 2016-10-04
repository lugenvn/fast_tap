package self.lugen.fasttap.screen;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import self.lugen.fasttap.MainActivity;
import self.lugen.fasttap.base.CustomBaseActivity;
import self.lugen.fasttap.object.JobID;

/**
 * A simple {@link Fragment} subclass.
 */
public abstract class BaseScreen extends Fragment {
    private static final String TAG = BaseScreen.class.getSimpleName();

    protected CustomBaseActivity.ActivityCallback callback;

    public BaseScreen() {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (callback == null) {
            Activity activity = getActivity();
            if (activity instanceof MainActivity) {
                callback = (CustomBaseActivity.ActivityCallback) activity;
            }
        }
    }

    public BaseScreen(CustomBaseActivity.ActivityCallback callback) {
        this.callback = callback;
        // Required empty public constructor
    }

    public abstract void updateScreen(JobID jobID, Bundle data);
}
