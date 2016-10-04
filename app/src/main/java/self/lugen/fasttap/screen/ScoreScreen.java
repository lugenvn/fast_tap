package self.lugen.fasttap.screen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import self.lugen.fasttap.R;
import self.lugen.fasttap.base.CustomBaseActivity;
import self.lugen.fasttap.object.JobID;
import self.lugen.fasttap.utils.Constant;

@SuppressLint("ValidFragment")
public class ScoreScreen extends BaseScreen implements View.OnClickListener {

    private TextView mTextScore;
    private TextView mTextBestScore;

    private ImageButton mButtonReplay;
    private ImageButton mButtonShare;
    private Button mButtonHighScore;

    private int mNewScore = 0;
    private int mBestScore = 0;

    SharedPreferences sharePref;

    public ScoreScreen() {
        super();
    }

    public ScoreScreen(CustomBaseActivity.ActivityCallback callback) {
        super(callback);
    }

    @Override
    public void updateScreen(JobID jobID, Bundle data) {

    }

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharePref = getActivity().getPreferences(Context.MODE_PRIVATE);

        mBestScore = sharePref.getInt(Constant.SHARE_KEY_BEST_SCORE, 0);
    }

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_score_screen, container, false);
        mTextScore = (TextView) view.findViewById(R.id.text_score);
        mTextBestScore = (TextView) view.findViewById(R.id.text_best_score);

        mButtonShare = (ImageButton) view.findViewById(R.id.button_share);
        mButtonReplay = (ImageButton) view.findViewById(R.id.button_play);
        mButtonHighScore = (Button) view.findViewById(R.id.button_top);

        new CountDownTimer(500, 500) {
            @Override
            public void onTick(long l) {

            }

            @Override
            public void onFinish() {
                initClickListenner();
            }
        }.start();

        Bundle data = getArguments();
        mNewScore = data.getInt(Constant.BUNDLE_KEY_SCORE, mBestScore);

        mTextScore.setText(String.valueOf(mNewScore));

        if (mNewScore > mBestScore) {
            mBestScore = mNewScore;
            SharedPreferences.Editor editor = sharePref.edit();
            editor.putInt(Constant.SHARE_KEY_BEST_SCORE, mBestScore);
            editor.commit();
        }
        mTextBestScore.setText(String.valueOf(mBestScore));


        MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-7034676815737306/2399468077");


        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }

    private void initClickListenner() {
        mButtonShare.setOnClickListener(this);
        mButtonReplay.setOnClickListener(this);
        mButtonHighScore.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_play:
                // Close and replay
                callback.onFragmentDone(JobID.REPLAY, null);
                break;
            case R.id.button_share:
                share();
                break;
            case R.id.button_top:
                break;
        }
    }

    private void share() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=self.lugen.fasttap");
        startActivity(Intent.createChooser(shareIntent, "share via"));
    }

}
