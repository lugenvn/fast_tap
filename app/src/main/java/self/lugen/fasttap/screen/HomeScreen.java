package self.lugen.fasttap.screen;


import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import self.lugen.fasttap.R;
import self.lugen.fasttap.base.CustomBaseActivity;
import self.lugen.fasttap.object.JobID;
import self.lugen.fasttap.utils.Constant;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeScreen extends BaseScreen implements View.OnClickListener {

    private ImageButton mButtonPlay;
    private ImageButton mButtonRate;


    public HomeScreen() {
        super();
    }

    @SuppressLint("ValidFragment")
    public HomeScreen(CustomBaseActivity.ActivityCallback callback) {
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_screen, container, false);

        mButtonPlay = (ImageButton) view.findViewById(R.id.button_play);
        mButtonPlay.setOnClickListener(this);

        mButtonRate = (ImageButton) view.findViewById(R.id.button_rate);
        mButtonRate.setOnClickListener(this);


        MobileAds.initialize(getActivity().getApplicationContext(), "ca-app-pub-7034676815737306/2399468077");


        AdView mAdView = (AdView) view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_play:
                callback.onFragmentDone(JobID.START_GAME, null);
                break;
            case R.id.button_rate:
                rateAction();
                break;
        }
    }

    private boolean MyStartActivity(Intent aIntent) {
        try {
            startActivity(aIntent);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    private void rateAction() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //Try Google play
        intent.setData(Uri.parse("market://details?id=" + Constant.APP_ID));
        if (!MyStartActivity(intent)) {
            //Market (Google play) app seems not installed, let's try to open a webbrowser
            intent.setData(Uri.parse("https://play.google.com/store/apps/details?" + Constant.APP_ID));
            if (!MyStartActivity(intent)) {
                //Well if this also fails, we have run out of options, inform the user.
                Toast.makeText(getContext(), "Could not open Android market, please install the market app.", Toast
                        .LENGTH_SHORT).show();
            }
        }
    }
}
