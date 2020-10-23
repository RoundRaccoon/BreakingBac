package com.example.lenovo.cruciada256;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    TextView creeazaCont;
    TextView login;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        creeazaCont = (TextView) findViewById(R.id.createaccount);
        login = (TextView) findViewById(R.id.signin);
        //buton = (Button) findViewById(R.id.main);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();
                if( user!=null )
                {
                    Intent intent = new Intent(MainActivity.this,BottomNavigation.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);


        final ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
        final int[] imageArray = {R.drawable.z, R.drawable.y, R.drawable.x};
        final int[] imageArrayRo = {R.drawable.xro,R.drawable.yro,R.drawable.zro};
        final Handler handler = new Handler();

        Runnable runnable = new Runnable() {

            int i=0;

            @Override
            public void run() {
                imageView1.setImageResource(imageArrayRo[i]);
                i++;
                if(i>imageArrayRo.length-1){
                    i=0;
                }
                handler.postDelayed(this,2000);
            }
        };
        handler.postDelayed(runnable,2000);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent logare = new Intent(MainActivity.this, Login.class);
                startActivity(logare);
            }
        });

        creeazaCont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cont = new Intent(MainActivity.this, Register.class);
                startActivity(cont);
            }
        });

    }
}
