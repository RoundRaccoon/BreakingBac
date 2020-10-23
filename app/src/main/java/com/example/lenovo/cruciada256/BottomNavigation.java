package com.example.lenovo.cruciada256;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class BottomNavigation extends AppCompatActivity {

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);

        mAuth = FirebaseAuth.getInstance();

        Fragment selectedFragment = null;
        selectedFragment = new DiscoverFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        View da4 = (View) findViewById(R.id.navigation_home);
        da4.setBackgroundColor(Color.parseColor("#7fc274"));
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    View da1 = (View) findViewById(R.id.navigation_home);
                    View da2 = (View) findViewById(R.id.navigation_dashboard);
                    View da3 = (View) findViewById(R.id.navigation_notifications);

                    switch (menuItem.getItemId()){
                        case R.id.navigation_home:
                            selectedFragment = new DiscoverFragment();
                            da1.setBackgroundColor(Color.parseColor("#7fc274"));
                            da2.setBackgroundColor(Color.WHITE);
                            da3.setBackgroundColor(Color.WHITE);
                            break;
                        case R.id.navigation_dashboard:
                            selectedFragment = new NewsFragment();
                            da1.setBackgroundColor(Color.WHITE);
                            da2.setBackgroundColor(Color.parseColor("#7fc274"));
                            da3.setBackgroundColor(Color.WHITE);
                            break;
                        case R.id.navigation_notifications:
                            selectedFragment = new MyProfileFragment();
                            da1.setBackgroundColor(Color.WHITE);
                            da2.setBackgroundColor(Color.WHITE);
                            da3.setBackgroundColor(Color.parseColor("#7fc274"));
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,selectedFragment).commit();
                    return false;
                }
            };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.basic_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();
        if(id==R.id.logout)
        {
            mAuth.signOut();
            Intent intent = new Intent(BottomNavigation.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        if(id==R.id.help)
        {
            Intent i=new Intent(BottomNavigation.this, Help.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
