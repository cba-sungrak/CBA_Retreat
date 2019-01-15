package kr.or.sungrak.cba.cba_retreat.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import kr.or.sungrak.cba.cba_retreat.R;

public class InfoFragment extends Fragment {

    private ImageButton youtubeBtn;
    private ImageButton instaBtn;
    private ImageButton webBtn;
    private ImageButton blogBtn;
    private ImageButton callStaffBtn;
    private ImageButton callCarBtn;
    private ImageButton locationBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.info_layout2, container, false);
        youtubeBtn = rootView.findViewById(R.id.youtubeButton);
        instaBtn = rootView.findViewById(R.id.instButton);
        webBtn = rootView.findViewById(R.id.webButton);
        blogBtn = rootView.findViewById(R.id.blogButton);
        callStaffBtn = rootView.findViewById(R.id.callStaffBtn);
        callCarBtn = rootView.findViewById(R.id.callCarBtn);
        locationBtn = rootView.findViewById(R.id.location);

        String survey = FirebaseRemoteConfig.getInstance().getString("survey");
        Log.i("cba", survey);
        if (survey.equals("na")) {
            locationBtn.setVisibility(View.GONE);
        }else{
            locationBtn.setVisibility(View.VISIBLE);
        }

        Button.OnClickListener onClickListener = new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.youtubeButton:
                        Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/channel/UCW6bF9L0ZK__Tlwl19B0FYQ"));
                        startActivity(intent1);
                        break;
                    case R.id.instButton:
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.instagram.com/cba.sungrak/"));
                        startActivity(intent2);
                        break;
                    case R.id.webButton:
                        Intent intent3 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://cba.sungrak.or.kr/HomePage/Index"));
                        startActivity(intent3);
                        break;
                    case R.id.blogButton:
                        Intent intent4 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://blog.naver.com/thebondofpeace"));
                        startActivity(intent4);
                        break;
                    case R.id.callStaffBtn:
                        Intent intent5 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:01050254375"));
                        startActivity(intent5);
                        break;
                    case R.id.callCarBtn:
                        Intent intent6 = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:01033974842"));
                        startActivity(intent6);
                        break;
                    case R.id.location:
                        Intent intent7 = new Intent(Intent.ACTION_VIEW, Uri.parse("http://naver.me/5CxdC0vS"));
                        //Intent intent7 = new Intent(Intent.ACTION_VIEW, Uri.parse(FirebaseRemoteConfig.getInstance().getString("survey")));
                        startActivity(intent7);
                        break;

                }
            }
        };
        youtubeBtn.setOnClickListener(onClickListener);
        instaBtn.setOnClickListener(onClickListener);
        webBtn.setOnClickListener(onClickListener);
        blogBtn.setOnClickListener(onClickListener);
        callStaffBtn.setOnClickListener(onClickListener);
        callCarBtn.setOnClickListener(onClickListener);
        locationBtn.setOnClickListener(onClickListener);
        return rootView;
    }
}
