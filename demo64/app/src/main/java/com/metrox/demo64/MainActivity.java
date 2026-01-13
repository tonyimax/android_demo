package com.metrox.demo64;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.metrox.demo64.R;
import com.metrox.demo64.fragments.DashboardFragment;
import com.metrox.demo64.fragments.DetailFragmentWithCallback;
import com.metrox.demo64.fragments.HomeFragment;
import com.metrox.demo64.fragments.ProfileFragment;

public class MainActivity extends AppCompatActivity implements DetailFragmentWithCallback.OnDataPassListener {

    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        setupBottomNavigation();

        // 默认显示HomeFragment
        if (savedInstanceState == null) {
            bottomNavigation.setSelectedItemId(R.id.nav_home);
        }
    }

    private void initializeViews() {
        bottomNavigation = findViewById(R.id.bottom_navigation);
    }

    private void setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                loadFragment(HomeFragment.newInstance());
                return true;
            } else if (itemId == R.id.nav_dashboard) {
                loadFragment(new DashboardFragment());
                return true;
            } else if (itemId == R.id.nav_profile) {
                loadFragment(new ProfileFragment());
                return true;
            }
            return false;
        });
    }

    private void loadFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    @Override
    public void onBackPressed() {
        // 处理Fragment返回栈
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDataPass(String data) {
        // 处理从Fragment传回的数据
        Toast.makeText(this, "Received: " + data, Toast.LENGTH_SHORT).show();
    }

    private void openDetailFragment() {
        DetailFragmentWithCallback fragment = new DetailFragmentWithCallback();
        fragment.setOnDataPassListener(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit();
    }
}