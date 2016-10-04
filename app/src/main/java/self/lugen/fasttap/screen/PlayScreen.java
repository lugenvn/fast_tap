package self.lugen.fasttap.screen;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import self.lugen.fasttap.MainActivity;
import self.lugen.fasttap.R;
import self.lugen.fasttap.base.CustomBaseActivity;
import self.lugen.fasttap.object.JobID;
import self.lugen.fasttap.utils.Constant;

@SuppressLint("ValidFragment")
public class PlayScreen extends BaseScreen implements View.OnClickListener {

    private static final int STATE_PREPARE = 0;
    private static final int STATE_PLAY = 1;
    private static final int STATE_FINISH = 2;

    private static final int ONE_SEC = 1000;

    // To make easier to control
    // Change this variable to change the target duration.
    private static final int DURATION_TIME = 5;

    private static final int PROGRESS_MAX = ONE_SEC * DURATION_TIME;

    // The button for counting taps
    private Button mTapButton;

    // The UI counter
    private TextView mCounterText;

    // Check the first tap to start timer
    private int mPlayState;

    // Timer for deadline of tapping
    private CountDownTimer mCountDownTimer;

    // Count numbers of times user tap in a times
    private int mTapCount;

    // Progress bar
    private ProgressBar mProgressBar;

    // Tutor image
    private ImageView mImgTutorial;

    public PlayScreen() {
        super();
    }

    public PlayScreen(CustomBaseActivity.ActivityCallback callback) {
        super(callback);
    }

    @Override
    public void updateScreen(JobID jobID, Bundle data) {
        switch (jobID) {
            case REPLAY:
                startAgain();
                break;
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_play_screen, container, false);

        mTapButton = (Button) v.findViewById(R.id.tap_button);
        mTapButton.setOnClickListener(this);

        mCounterText = (TextView) v.findViewById(R.id.counter);

        mProgressBar = (ProgressBar) v.findViewById(R.id.progress_bar);
        mProgressBar.setMax(PROGRESS_MAX);

        mImgTutorial = (ImageView) v.findViewById(R.id.tutor);
        return v;
    }


    /**
     * Action when user tap the button
     *
     * @param view the clicked view
     */
    @Override
    public void onClick(View view) {
        if (mPlayState == STATE_PREPARE) {
            // Start by first tap
            mPlayState = STATE_PLAY;
            mTapCount = 1;
            startTimer();
            AnimationSet animationSet = new AnimationSet(false);
            Animation fadeOutAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.exit);
            fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mImgTutorial.setImageAlpha(0);
                    } else {
                        mImgTutorial.setAlpha(0.0f);
                    }
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            animationSet.addAnimation(fadeOutAnimation);
            mImgTutorial.startAnimation(animationSet);
        } else {
            // Only increase when playing
            if (mPlayState == STATE_PLAY) {
                mTapCount++;
            }
        }
        mCounterText.setText(String.valueOf(mTapCount));
    }

    /**
     * Start the count down timer when user start the first tap
     */
    private void startTimer() {
        mCountDownTimer = new CountDownTimer(PROGRESS_MAX, 10) {
            @Override
            public void onTick(long l) {
                // Do nothing
                mProgressBar.setProgress((int) (PROGRESS_MAX - l));
            }

            @Override
            public void onFinish() {
                mProgressBar.setProgress(PROGRESS_MAX);
                mPlayState = STATE_FINISH;
                showResult();
            }
        }.start();
    }

    /**
     * Show the result to user
     */
    private void showResult() {
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            if (activity.isActivityVisible()) {
                Bundle data = new Bundle();
                data.putInt(Constant.BUNDLE_KEY_SCORE, mTapCount);
                activity.onFragmentDone(JobID.SHOW_SCORE, data);
            } else {
                startAgain();
            }
        }
    }

    /**
     * Start game again. Reset all value and view
     */
    private void startAgain() {
        mProgressBar.setProgress(0);

        // Reset state
        mPlayState = STATE_PREPARE;

        // Reset screen
        mCounterText.setText("0");

        // Stop counter if needed
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mImgTutorial.setImageAlpha(255);
        } else {
            mImgTutorial.setAlpha((float) 1);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        startAgain();
    }
}
