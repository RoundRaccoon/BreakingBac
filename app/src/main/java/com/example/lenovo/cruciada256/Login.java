package com.example.lenovo.cruciada256;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.security.NoSuchAlgorithmException;

public class Login extends AppCompatActivity{

    EditText userEmailEdit, userPassEdit;
    TextView loginBtn;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEmailEdit = (EditText) findViewById(R.id.username);
        userPassEdit = (EditText) findViewById(R.id.password);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        loginBtn=(TextView)findViewById(R.id.login);

        mAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(this);

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if( user!=null )
                {

                    Intent intent = new Intent(Login.this,BottomNavigation.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            }
        };

        mAuth.addAuthStateListener(mAuthListener);


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Autentificare în curs");
                mProgressDialog.setMessage("Vă rugăm așteptați...");
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
                mProgressDialog.setCanceledOnTouchOutside(false);
                try {
                    loginUser();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();

        mAuth.removeAuthStateListener(mAuthListener);
    }

    //Funcția care se apelează când utilizatorul apasă butoul de ”AUTENTIFICARE”
    private void loginUser() throws NoSuchAlgorithmException {

        //Preluăm datele (email și parolă) scrise de utilizator
        String userEmail, userPass;
        userEmail = userEmailEdit.getText().toString().trim();
        userPass = userPassEdit.getText().toString().trim();
        //Se verifică dacă s-au introdus un email și o parolă
        if( !TextUtils.isEmpty(userEmail) && !TextUtils.isEmpty(userPass))
        {
            //Verificăm dacă în baza de date există o combinație email-parolă ca cea introdusă
            mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    //Dacă input-ul este corect, se deschide pagina principală a aplicației
                    if(task.isSuccessful())
                    {
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(Login.this,BottomNavigation.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                    else   //Altfel, o casetă de dialog apare cu mesajul de mai jos
                    {
                        Toast.makeText(Login.this, "Date de conectare invalide", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        }else //Dacă nu au fost introduse un email/parolă, apare mesajul următor
        {
            Toast.makeText(Login.this, "Introdu email si parola", Toast.LENGTH_LONG).show();
            mProgressDialog.dismiss();
        }
    }
}