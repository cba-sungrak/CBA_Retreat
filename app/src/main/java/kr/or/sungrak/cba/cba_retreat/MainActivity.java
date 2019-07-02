package kr.or.sungrak.cba.cba_retreat;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;

import kr.or.sungrak.cba.cba_retreat.common.CBAUtil;
import kr.or.sungrak.cba.cba_retreat.common.Tag;
import kr.or.sungrak.cba.cba_retreat.dialog.LoginDialog;
import kr.or.sungrak.cba.cba_retreat.dialog.SelectDialog;
import kr.or.sungrak.cba.cba_retreat.fragment.AttendCampusFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.DateStatisticFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.GBSFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.ImageViewFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.InfoFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.SwipeImageFragment;
import kr.or.sungrak.cba.cba_retreat.models.MyInfo;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = "MainActivity";
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseAuth mAuth;
    Context mContext;
    MenuItem mCheckAttMenu;
    private static int sHiddenCode[] = {
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_DOWN,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_UP,
            KeyEvent.KEYCODE_VOLUME_UP};
    private int hiddenCodeIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        if (TextUtils.isEmpty(CBAUtil.getRetreatTitle(this))) {
            showSelectDialog();
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
        FirebaseMessaging.getInstance().subscribeToTopic("2019winter");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().clear();

        switch (CBAUtil.getRetreatTitle(this)) {
            case Tag.RETERAT_CBA:
                navigationView.inflateMenu(R.menu.activity_main_drawer);
                Menu menu = navigationView.getMenu();
                mCheckAttMenu = menu.findItem(R.id.check_attendance);
                mCheckAttMenu.setVisible(false);
                break;
            case Tag.RETREAT_SUNGRAK:
            case Tag.RETREAT_SUNGRAK_ADMIN:
                navigationView.inflateMenu(R.menu.sungrak_drawer_menu);
                if (CBAUtil.getRetreatTitle(this).equals(Tag.RETREAT_SUNGRAK_ADMIN)) {
                    Menu menu2 = navigationView.getMenu();
                    mCheckAttMenu = menu2.findItem(R.id.gbs_info);
                    mCheckAttMenu.setTitle("관리자모드입니다.");
                }
                break;
        }

//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        replaceFragment(new InfoFragment());
//        fragmentTransaction.add(R.id.fragment_container, new InfoFragment()).commit();

        updateNavHeader();

    }

    public void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        Button logInBtn = headerView.findViewById(R.id.loginBtn);
        Button logOutBtn = headerView.findViewById(R.id.logOutBtn);
        ImageButton backBtn = headerView.findViewById(R.id.back);
        ImageButton homeBtn = headerView.findViewById(R.id.home);
        TextView loginText = headerView.findViewById(R.id.logintextView);
        TextView selectedTitle = headerView.findViewById(R.id.selectedTitle);
        LinearLayout selectedTitleLayOut = headerView.findViewById(R.id.selectedTitleLayOut);

        if (mAuth.getCurrentUser() == null) {
            //logOut
            logOutBtn.setVisibility(View.GONE);
            logInBtn.setVisibility(View.VISIBLE);
            loginText.setText("로그인 해주세요");
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

        backBtn.setOnClickListener((v) -> closeDrawer());

        homeBtn.setOnClickListener((v) -> replaceFragment(new InfoFragment()));

        switch (CBAUtil.getRetreatTitle(this)) {
            case Tag.RETERAT_CBA:
                selectedTitle.setText("예수로 사는 교회");
                break;
            case Tag.RETREAT_SUNGRAK:
                selectedTitle.setText("내영혼아 교회를 수호 하자");
                break;
            default:
                selectedTitle.setText("수련회를 선택해주세요");
                break;
        }

        selectedTitleLayOut.setOnClickListener((v) -> showSelectDialog());
    }

    public void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment).commit();
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
        } else {
            super.onBackPressed();
        }
    }

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
                break;
            case R.id.statistic_attendance:
                replaceFragment(new DateStatisticFragment());
                break;
            default:
                break;
        }
        return true;
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

    private void showLoginDialog(boolean needToChangeFrament) {
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
        if (checkKeyDown(keyCode, event)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean checkKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == sHiddenCode[hiddenCodeIndex]) {
            if (hiddenCodeIndex == sHiddenCode.length - 1) {
                hiddenCodeIndex = 0;

                EditText et = new EditText(MainActivity.this);

                new AlertDialog.Builder(this)
                        .setTitle("코드를 입력해주세요")
                        .setView(et)
                        .setPositiveButton("ok",
                                (dialog, which) -> {
                                    if ("1111".equals(et.getText().toString())) {
                                        CBAUtil.setRetreatTitle(getApplication(), Tag.RETREAT_SUNGRAK_ADMIN);
                                        Toast.makeText(getApplication(), "일치", Toast.LENGTH_SHORT).show();
                                        initialActivity();
                                    }
                                })
                        .show();
            } else {
                hiddenCodeIndex++;
            }
        } else {
            hiddenCodeIndex = 0;
        }
        return false;
    }
}

