package kr.or.sungrak.cba.cba_retreat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kr.or.sungrak.cba.cba_retreat.common.CBAUtil;
import kr.or.sungrak.cba.cba_retreat.common.Tag;
import kr.or.sungrak.cba.cba_retreat.dialog.LoginDialog;
import kr.or.sungrak.cba.cba_retreat.dialog.MyProgessDialog;
import kr.or.sungrak.cba.cba_retreat.dialog.SelectDialog;
import kr.or.sungrak.cba.cba_retreat.fragment.CampMemberListFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.CampRegistFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.GBSFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.InfoFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.QAListFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.SRNotiFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.SwipeImageFragment;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    private DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    Context mContext;
    MenuItem mCheckAttMenu;
    MyProgessDialog myDialog;

    private static int sHiddenCode[] = {
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_DOWN};

    private int hiddenCodeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        myDialog = new MyProgessDialog(this);
        myDialog.showProgressDialog();
        setContentView(R.layout.activity_main);
        if (TextUtils.isEmpty(CBAUtil.getRetreat(this))) {
            CBAUtil.setRetreat(this, Tag.RETREAT_SUNGRAK);
//            showSelectDialog();
            initialActivity();
        } else {
            initialActivity();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MyInfo myInfo = CBAUtil.loadMyInfo(this);
        if (mCheckAttMenu != null) {
            mCheckAttMenu.setVisible(true);
            if (myInfo != null) {
                if (myInfo.isLeader()) {
                    mCheckAttMenu.setVisible(true);
                }
            }
            if (mAuth.getCurrentUser() == null) {
                CBAUtil.removeAllPreferences(this);
            }
            updateNavHeader();
        }
    }

    public void initialActivity() {
        mAuth = FirebaseAuth.getInstance();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mainTitle = findViewById(R.id.main_title);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().clear();
        ImageView bannerImage = navigationView.getHeaderView(0).findViewById(R.id.bannerImage);
        LinearLayout logInLayout = navigationView.getHeaderView(0).findViewById(R.id.loginLayout);
        switch (CBAUtil.getRetreat(this)) {
            case Tag.RETREAT_CBA:
                navigationView.inflateMenu(R.menu.activity_main_drawer);
                mainTitle.setText(Tag.CBA_TITLE);
                bannerImage.setImageResource(R.drawable.banner);
                FirebaseMessaging.getInstance().subscribeToTopic(Tag.RETREAT_CBA);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Tag.RETREAT_SUNGRAK);
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_CBA);
                logInLayout.setVisibility(View.VISIBLE);
                if (CBAUtil.isAdmin(this)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Tag.CBA_ADMIN);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Tag.SR_ADMIN);
                }
                break;
            case Tag.RETREAT_SUNGRAK:
                navigationView.inflateMenu(R.menu.sungrak_drawer_menu);
                mainTitle.setText(Tag.SR_TITLE);
                bannerImage.setImageResource(R.drawable.sr_banner);
                logInLayout.setVisibility(View.GONE);

                FirebaseMessaging.getInstance().subscribeToTopic(Tag.RETREAT_SUNGRAK);
                FirebaseMessaging.getInstance().unsubscribeFromTopic(Tag.RETREAT_CBA);
                mDatabase = FirebaseDatabase.getInstance().getReference(Tag.RETREAT_SUNGRAK);
                if (CBAUtil.isAdmin(this)) {
                    navigationView.getMenu().findItem(R.id.sr_member_list).setVisible(true);
                    FirebaseMessaging.getInstance().subscribeToTopic(Tag.SR_ADMIN);
                    FirebaseMessaging.getInstance().unsubscribeFromTopic(Tag.CBA_ADMIN);
                }
                break;
        }

        mDatabase.child(Tag.SETTING).child(Tag.YOUTUBE).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        CBAUtil.setPref(MainActivity.this, Tag.YOUTUBE, (String) dataSnapshot.getValue());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }

                });

        mDatabase.child(Tag.IMAGES).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.e("Count ", "" + snapshot.getChildrenCount());
                for (DataSnapshot ds : snapshot.getChildren()) {
                    String key = ds.getKey();
                    Map<String, String> value = (HashMap<String, String>) ds.getValue();
                    saveImage(key, value);
                    Log.e("TAG", key + "/" + value);
                }
                myDialog.hideProgressDialog();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                myDialog.hideProgressDialog();
            }
        });

        mainTitle.setOnClickListener(v -> {
            replaceFragment(new InfoFragment());
        });
        replaceFragment(new InfoFragment());

        updateNavHeader();

    }

    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageButton logInBtn = headerView.findViewById(R.id.loginBtn);
        ImageButton logOutBtn = headerView.findViewById(R.id.logOutBtn);
        TextView loginText = headerView.findViewById(R.id.logintextView);
        LinearLayout selectedTitleLayOut = headerView.findViewById(R.id.selectedTitleLayOut);

        if (mAuth.getCurrentUser() == null) {
            //logOut
            logOutBtn.setVisibility(View.GONE);
            logInBtn.setVisibility(View.VISIBLE);
            loginText.setText("홍길동      |      OJ조      |      조원");
        } else {
            //logIn
            logInBtn.setVisibility(View.GONE);
            logOutBtn.setVisibility(View.VISIBLE);
            if (CBAUtil.loadMyInfo(this) != null) {
                loginText.setText(CBAUtil.loadMyInfo(this).getName() + "/" + CBAUtil.loadMyInfo(this).getGbsLevel());
            }
        }
        logInBtn.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() == null) {
                showLoginDialog(false);
            }
        });
        logOutBtn.setOnClickListener(v -> {
            if (mAuth.getCurrentUser() != null) {
                CBAUtil.signOut(getApplicationContext());
                updateNavHeader();
                closeDrawer();
                replaceFragment(new InfoFragment());
            }
        });

        selectedTitleLayOut.setOnClickListener((v) -> showSelectDialog());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
        closeDrawer();
    }

    public void addFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).addToBackStack(null).commit();
        closeDrawer();
    }

    private void closeDrawer() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            return;
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof InfoFragment) {
            super.onBackPressed();
            return;

        }
        replaceFragment(new InfoFragment());
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.lecture:
                replaceFragment(new SwipeImageFragment("lecture"));
                break;
            case R.id.menu:
                replaceFragment(new SwipeImageFragment("menu"));
                break;
            case R.id.mealwork:
                replaceFragment(new SwipeImageFragment("mealwork"));
                break;
            case R.id.cleaning:
                replaceFragment(new SwipeImageFragment("cleaning"));
                break;
            case R.id.room:
                replaceFragment(new SwipeImageFragment("room"));
                break;
            case R.id.campus_place:
                replaceFragment(new SwipeImageFragment("campus_place"));
                break;
            case R.id.gbs_place:
                replaceFragment(new SwipeImageFragment("gbs_place"));
                break;
//            case R.id.check_attendance:
//                replaceFragment(new AttendCampusFragment());
//                break;
//            case R.id.statistic_attendance:
//                replaceFragment(new DateStatisticFragment());
//                break;
            case R.id.sr_welcom:
                replaceFragment(new SwipeImageFragment("c1"));
                break;
            case R.id.sr_welcom2:
                replaceFragment(new SwipeImageFragment("c2"));
                break;
            case R.id.sr_noti:
                replaceFragment(new SRNotiFragment());
                break;
            case R.id.sr_program:
                replaceFragment(new SwipeImageFragment("c4"));
                break;
            case R.id.sr_place:
                replaceFragment(new SwipeImageFragment("c5"));
                break;
            case R.id.sr_safe:
                replaceFragment(new SwipeImageFragment("c6"));
                break;
            case R.id.sr_trable_insurance:
                replaceFragment(new SwipeImageFragment("c7"));
                break;
            case R.id.sr_sponsor:
                replaceFragment(new SwipeImageFragment("c8"));
                break;
            case R.id.sr_member_list:
                replaceFragment(new CampMemberListFragment());
//                srImageDBmake();
            default:
                break;
        }
        return true;
    }

    private void cbaImageDBmake() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> campus_place = new HashMap<>();
        Map<String, Object> menu = new HashMap<>();
        Map<String, Object> lecture = new HashMap<>();
        Map<String, Object> gbs_place = new HashMap<>();
        Map<String, Object> mealwork = new HashMap<>();
        Map<String, Object> cleaning = new HashMap<>();
        Map<String, Object> room = new HashMap<>();
        menu.put("메뉴", "menu.png");
        lecture.put("강의", "lecture.png");
        gbs_place.put("1층", "gbs_place1.png");
        gbs_place.put("2층", "gbs_place2.png");
        mealwork.put("식사_간식봉사", "mealwork.png");
        cleaning.put("청소구역", "cleang.png");
        room.put("1층", "room1.png");
        room.put("1층", "room2.png");


        campus_place.put("3층", "campus_place1.png");
        campus_place.put("4층", "campus_place2.png");


        map.put("menu", menu);
        map.put("campus_place", campus_place);
        map.put("lecture", lecture);
        map.put("gbs_place", gbs_place);
        map.put("mealwork", mealwork);
        map.put("cleaning", cleaning);
        map.put("room", room);
//and os on
        mDatabase.child("images").updateChildren(map);
    }

    private void srImageDBmake() {
        Map<String, Object> map = new HashMap<>();
        Map<String, Object> c1 = new HashMap<>();
        Map<String, Object> c2 = new HashMap<>();
        Map<String, Object> c3 = new HashMap<>();
        Map<String, Object> c4 = new HashMap<>();
        Map<String, Object> c5 = new HashMap<>();
        Map<String, Object> c6 = new HashMap<>();
        Map<String, Object> c7 = new HashMap<>();
        Map<String, Object> c8 = new HashMap<>();
        Map<String, Object> m5 = new HashMap<>();
        c1.put("초청의 글1", "c1-01.png");
        c1.put("초청의 글2", "c1-02.png");
        c1.put("초청의 글3", "c1-03.png");

        c2.put("환영의 글1", "c2-01.png");
        c2.put("환영의 글2", "c2-02.png");
        c2.put("환영의 글3", "c2-03.png");

        c3.put("제4차 특별 신유 집회 안내", "c3-01.png");
        c3.put("오전 신유성회 안내", "c3-02.png");
        c3.put("새벽기도 안내", "c3-03.png");
        c3.put("선택식 강의 안내", "c3-04.png");
        c3.put("성락인의 고백 안내", "c3-05.png");
        c3.put("성락원 공동숙소 안내", "c3-06.png");
        c3.put("센터 기도실 사용수칙 안내", "c3-07.png");
        c3.put("센터 편의시설 장소 안내", "c3-08.png");
        c3.put("어린이 예배 안내", "c3-09.png");
        c3.put("새가족 교육 및 침례 안내", "c3-10.png");
        c3.put("시무언성락역사전시관 안내", "c3-11.png");
        c3.put("예배통역 안내", "c3-12.png");
        c3.put("월산재단 봉사인증활동 안내", "c3-13.png");
        c3.put("의료 서비스 안내", "c3-14.png");
        c3.put("전문인 상담 안내", "c3-15.png");
        c3.put("성라교회 기관별 수련회 안내", "c3-16.png");

        c4.put("몽산포성락원", "c4-01.png");
        c4.put("세계센터", "c4-02.png");

        c5.put("몽산포성락원 안내도", "c5-01.png");
        c5.put("몽산포성락원 안내도", "c5-02.png");
        c5.put("공동숙소", "c5-03.png");


        c6.put("안전사고 지원1", "c6-01.png");
        c6.put("안전사고 지원1", "c6-02.png");
        c6.put("안전사고 지원1", "c6-03.png");

        c7.put("여행자보험 안내1", "c7-01.png");
        c7.put("여행자보험 안내1", "c7-02.png");
        c7.put("여행자보험 안내1", "c7-02.png");

        c8.put("교회기관1", "c8-01.png");
        c8.put("교회기관2", "c8-02.png");
        c8.put("유관기관1", "c8-03.png");
        c8.put("유관기관2", "c8-04.png");
        c8.put("기업후원1", "c8-05.png");
        c8.put("기업후원2", "c8-06.png");

        m5.put("오시는길", "m5-map.png");


        map.put("c1", c1);
        map.put("c2", c2);
        map.put("c3", c3);
        map.put("c4", c4);
        map.put("c5", c5);
        map.put("c6", c6);
        map.put("c7", c7);
        map.put("c8", c8);
//        map.put("m3", m3);
//        map.put("c4", c4);
//        map.put("c5", c5);
//        map.put("c6", c6);
//        map.put("m5", m5);
        FirebaseDatabase.getInstance().getReference(Tag.RETREAT_SUNGRAK).child("images").updateChildren(map);
    }

    private void fetchRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        long cacheExpiration = 3600; // 1 hour in seconds.
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.i("cba", "Firebase Remote config succes");
                        //연결이 성공적으로 되었다면,
                        //mFirebaseRemoteConfig 에 매개변수 값이 들어온다.
                        mFirebaseRemoteConfig.activateFetched();

                    } else {
                        Log.i("cba", "Firebase Remote config succes");
                    }
                });
    }

    public void showLoginDialog(boolean needToChangeFrament) {
        final LoginDialog loginDialog = new LoginDialog(this, needToChangeFrament);

        loginDialog.show();

        loginDialog.setOnDismissListener(dialog -> {
            Log.i(TAG, "onDismiss");
            updateNavHeader();
            if (loginDialog.changeFrament()) {
                closeDrawer();
                replaceFragment(new GBSFragment());
            }
        });
    }

    private void showSelectDialog() {
        final SelectDialog selectDialog = new SelectDialog(this);
        selectDialog.setCancelable(false);
        selectDialog.show();
        selectDialog.setOnDismissListener(dialog -> {
            initialActivity();
            closeDrawer();
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (checkKeyDown(keyCode)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean checkKeyDown(int keyCode) {
        if (keyCode == sHiddenCode[hiddenCodeIndex]) {
            if (hiddenCodeIndex == sHiddenCode.length - 1) {
                hiddenCodeIndex = 0;
                EditText et = new EditText(MainActivity.this);
                mDatabase.child("setting").child("adminpw").addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setTitle("코드를 입력해주세요")
                                        .setView(et)
                                        .setPositiveButton("ok",
                                                (dialog, which) -> {
                                                    if (et.getText().toString().equals(dataSnapshot.getValue())) {
                                                        Toast.makeText(getApplication(), "관리자 모드가 되셨습니다.", Toast.LENGTH_LONG).show();
                                                        CBAUtil.setAdmin(getApplication(), true);
                                                        initialActivity();
                                                    }
                                                })
                                        .show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }

                        });

            } else {
                hiddenCodeIndex++;
            }
        } else {
            hiddenCodeIndex = 0;
        }
        return false;
    }

    private void saveImage(String key, Map<String, String> td) {
        Gson gson = new Gson();
        String myInfo = gson.toJson(td);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, myInfo);
        Log.e(TAG, "saveimgae //" + myInfo);
        editor.commit();
    }

    public Map<String, String> loadImage(String key) {
        Gson gson = new Gson();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String json = pref.getString(key, "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }

        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
        }.getType();
        Map retValue = sortByValue(gson.fromJson(json, type));
        Log.e(TAG, "loadimage " + retValue);
        return retValue;
    }

    private static <K, V> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new LinkedList<>(map.entrySet());
        Collections.sort(list, (Comparator<Object>) (o1, o2) -> ((Comparable<V>) ((Map.Entry<K, V>) (o1)).getValue()).compareTo(((Map.Entry<K, V>) (o2)).getValue()));

        Map<K, V> result = new LinkedHashMap<>();
        for (Iterator<Map.Entry<K, V>> it = list.iterator(); it.hasNext(); ) {
            Map.Entry<K, V> entry = it.next();
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults[0] == 0) {
            if (requestCode == 10) {
                replaceFragment(new QAListFragment());
            } else if (requestCode == 20) {
                replaceFragment(new CampRegistFragment());
            }
            Log.e(TAG, "승인");
        } else {
            Log.e(TAG, "거절");
        }


    }
}





