package kr.or.sungrak.cba.cba_retreat;

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;

import kr.or.sungrak.cba.cba_retreat.fragment.ImageViewFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.InfoFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.LoginFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.PostListFragment;
import kr.or.sungrak.cba.cba_retreat.fragment.SwipeImageFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

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

    @Override
    protected void onResume() {
        super.onResume();
        updateSignInButton();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseMessaging.getInstance().subscribeToTopic("2018-summer-retreat");
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        fragmentTransaction.add(R.id.fragment_container, new PostListFragment()).commit();
        navigation.setSelectedItemId(R.id.notification);

        updateSignInButton();
    }
    
    public void updateSignInButton() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        RelativeLayout loginOffLayout = headerView.findViewById(R.id.loginOffLayout);
        LinearLayout loginOnLayout = headerView.findViewById(R.id.loginOnLayout);
        Button loginBtn = headerView.findViewById(R.id.loginBtn);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            //logOut
            loginOnLayout.setVisibility(View.GONE);
            loginOffLayout.setVisibility(View.VISIBLE);
            loginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.loginBtn:
                            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                                replaceFragment(new LoginFragment());
                            }
                            DrawerLayout drawer = findViewById(R.id.drawer_layout);
                            drawer.closeDrawer(GravityCompat.START);
                            break;
                        default:
                            break;
                    }
                }
            });
        } else {
            //logIn
            loginOffLayout.setVisibility(View.GONE);
            loginOnLayout.setVisibility(View.VISIBLE);
        }
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

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
            replaceFragment(new SwipeImageFragment("room", new String[]{"1층","2층(형제)","3층(자매)"}));
        } else if (id == R.id.campus_place) {
            replaceFragment(new ImageViewFragment("campus_place1.png"));
        } else if (id == R.id.gbs_place) {
            replaceFragment(new SwipeImageFragment("gbs_place",new String[]{"본당(C,E)","식당(F,J)","1층(EN,OJ)","2층(OJ)","3층(OJ)"}));
        }else if (id == R.id.gbs_info) {
            replaceFragment(new LoginFragment());
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

