package kr.or.sungrak.cba.cba_retreat;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.gson.Gson;

import kr.or.sungrak.cba.cba_retreat.Dialog.LoginDialog;
import kr.or.sungrak.cba.cba_retreat.fragment.ImageViewFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.InfoFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.PostListFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.SwipeImageFragment;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseAuth mAuth;
    Context mContext;


    @Override
    protected void onResume() {
        super.onResume();
        updateSignInButton();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        FirebaseMessaging.getInstance().subscribeToTopic("2019winter");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        fetchRemoteConfig();
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, new InfoFragment()).commit();
        navigation.setSelectedItemId(R.id.info);

        updateSignInButton();
    }

    public void updateSignInButton() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Button logInBtn = headerView.findViewById(R.id.loginBtn);
        Button logOutBtn = headerView.findViewById(R.id.logOutBtn);
        ImageButton backBtn = headerView.findViewById(R.id.back);
        TextView logintext = headerView.findViewById(R.id.logintextView);

        if (mAuth.getCurrentUser() == null) {
            //logOut
            logOutBtn.setVisibility(View.GONE);
            logInBtn.setVisibility(View.VISIBLE);
            logintext.setText("로그인 해주세요");
        } else {
            //logIn
            logInBtn.setVisibility(View.GONE);
            logOutBtn.setVisibility(View.VISIBLE);
            if(loadMyInfo()!=null) {
                logintext.setText(loadMyInfo().getName() + "님 로그인 되었습니다.");
            }
        }
        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() == null) {
                    showLoginDialog(false);
                }
            }
        });
        logOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                    updateSignInButton();
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.info:
                    replaceFragment(new InfoFragment());
                    return true;
                case R.id.notification:
                    replaceFragment(new PostListFragment());
                    return true;
                case R.id.time_table:
                    replaceFragment(new ImageViewFragment("timetable.png"));
                    return true;
            }
            return false;
        }
    };

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.lecture) {
            replaceFragment(new ImageViewFragment("lecture.png"));
        } else if (id == R.id.menu) {
            replaceFragment(new ImageViewFragment("menu.png"));
        } else if (id == R.id.mealwork) {
            replaceFragment(new ImageViewFragment("mealwork.png"));
        } else if (id == R.id.cleaning) {
            replaceFragment(new ImageViewFragment("cleaning.png"));
        } else if (id == R.id.room) {
            replaceFragment(new SwipeImageFragment("room", new String[]{"1층", "2층(형제)", "3층(자매)"}));
        } else if (id == R.id.campus_place) {
            replaceFragment(new ImageViewFragment("campus_place1.png"));
        } else if (id == R.id.gbs_place) {
            replaceFragment(new SwipeImageFragment("gbs_place", new String[]{"본당(C,E)", "식당(F,J)", "1층(EN,OJ)", "2층(OJ)", "3층(OJ)"}));
        } else if (id == R.id.gbs_info) {
            if (mAuth.getCurrentUser() == null) {
                showLoginDialog(true);
                return true;
            } else {
                replaceFragment(new ImageViewFragment("campus_place1.png"));
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void fetchRemoteConfig() {
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        long cacheExpiration = 3600; // 1 hour in seconds.
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("cba", "Firebase Remote config succes");
                            //연결이 성공적으로 되었다면,
                            //mFirebaseRemoteConfig 에 매개변수 값이 들어온다.
                            mFirebaseRemoteConfig.activateFetched();

                        } else {
                            Log.i("cba", "Firebase Remote config succes");
                        }
                    }
                });
    }

    private void showLoginDialog(boolean needToChangeFrament) {
        final LoginDialog loginDialog = new LoginDialog(this, needToChangeFrament);
        loginDialog.show();
        loginDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                updateSignInButton();

                if (loginDialog.changeFrament()) {
                    replaceFragment(new ImageViewFragment("campus_place1.png"));
                }
            }
        });
    }
    public MyInfo loadMyInfo(){
        Gson gson = new Gson();
        SharedPreferences pref = getSharedPreferences("MyInfo", Activity.MODE_PRIVATE);
        String json = pref.getString("MyInfo", "");
        if(TextUtils.isEmpty(json)){
            return null;
        }
        Log.i("Login", "/// " + json);
        return gson.fromJson(json, MyInfo.class);
    }
}

