package kr.or.sungrak.cba.cba_retreat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.view.Menu;
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
import kr.or.sungrak.cba.cba_retreat.fragment.AttendCampusFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.GBSFragment;
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
    MenuItem mCheckAttMenu;

    @Override
    protected void onResume() {
        super.onResume();
        MyInfo myInfo = CBAUtil.loadMyInfo(this);
        mCheckAttMenu.setVisible(true);
        if (myInfo != null) {
            if (myInfo.isLeader()) {
                mCheckAttMenu.setVisible(true);
            }
        }
        if (mAuth.getCurrentUser() == null) {
            CBAUtil.removeAllPreferences(this);
        }

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
        Menu menu = navigationView.getMenu();
        mCheckAttMenu = menu.findItem(R.id.check_attendance);
        mCheckAttMenu.setVisible(false);

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
            if (loadMyInfo() != null) {
                logintext.setText(loadMyInfo().getName() + "/" + loadMyInfo().getGbsLevel());
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
                    CBAUtil.signOut(getApplicationContext());
                    updateSignInButton();
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    replaceFragment(new InfoFragment());
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

    public void replaceFragment(Fragment fragment) {
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
        switch (item.getItemId()) {
            case R.id.lecture:
                replaceFragment(new ImageViewFragment("lecture2.png"));
                break;
            case R.id.menu:
                replaceFragment(new ImageViewFragment("menu2.png"));
                break;
            case R.id.mealwork:
                replaceFragment(new ImageViewFragment("mealwork2.png"));
                break;
            case R.id.cleaning:
                replaceFragment(new ImageViewFragment("cleaning2.png"));
                break;
            case R.id.room:
                replaceFragment(new SwipeImageFragment("room", new String[]{"2층", "별관(형제)", "3층(형제/자매)", "4층(자매)"}));
                break;
            case R.id.campus_place:
                replaceFragment(new SwipeImageFragment("campus_place", new String[]{"3층", "4층", "5층", "별관"}));
                break;
            case R.id.gbs_place:
                replaceFragment(new SwipeImageFragment("gbs_place", new String[]{"2층(C/OJ)", "3층(CH)", "4층(CH/A,B)", "5층(E,F,J)"}));
                break;
            case R.id.gbs_info:
                if (mAuth.getCurrentUser() == null) {
                    showLoginDialog(true);
                    return true;
                } else {
                    replaceFragment(new GBSFragment());
                }
                break;
            case R.id.check_attendance:
                replaceFragment(new AttendCampusFragment());
            default:
                break;
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
                Log.i(TAG, "onDismiss");
                updateSignInButton();
                if (loginDialog.changeFrament()) {
                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.closeDrawer(GravityCompat.START);
                    replaceFragment(new GBSFragment());
                }
            }
        });
    }

    public MyInfo loadMyInfo() {
        Gson gson = new Gson();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        String json = pref.getString("MyInfo", "");
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        Log.i(TAG, "/// " + json);
        return gson.fromJson(json, MyInfo.class);
    }
}

