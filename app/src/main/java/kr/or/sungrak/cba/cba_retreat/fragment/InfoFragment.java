package kr.or.sungrak.cba.cba_retreat.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kr.or.sungrak.cba.cba_retreat.R;
import kr.or.sungrak.cba.cba_retreat.models.Post;

public class InfoFragment extends Fragment {

    private static final String TAG = "CBA/InfoFragment";
    private ImageButton youtubeBtn;
    private ImageButton instaBtn;
    private ImageButton webBtn;
    private ImageButton blogBtn;
    private ImageButton callStaffBtn;
    private ImageButton callCarBtn;
    private ImageButton locationBtn;
    private TextView noticeTxtView;

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
        noticeTxtView = rootView.findViewById(R.id.noti_textView);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("2019messages");
        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Post post = dataSnapshot.getValue(Post.class);  // chatDat
                if(post.isStaff.contentEquals("공지")){
                    noticeTxtView.setText(post.message);
                    noticeTxtView.setSelected(true);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
