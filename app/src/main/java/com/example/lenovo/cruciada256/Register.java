package com.example.lenovo.cruciada256;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Register extends AppCompatActivity {

    EditText emailRegister, passRegister, username, passConfirm;
    TextView CreateAccount;
    ImageView pP;

    ProgressDialog mProgressDialog;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    private StorageReference storageReference;
    private static final int SELECT_FILE = 1;
    private Uri uri=null;
    private DatabaseReference mDatabaseUsers, databaseReference;;
    private FirebaseUser mCurrentUser;
    boolean photo=false;

    boolean ChangeState=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        emailRegister = (EditText) findViewById(R.id.mail);
        passRegister = (EditText) findViewById(R.id.pass);
        username = (EditText) findViewById(R.id.user);
        passConfirm = (EditText) findViewById(R.id.confirmPass);
        pP = (ImageView) findViewById(R.id.profilePic);

        pP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pPSelection();
            }
        });

        CreateAccount = (TextView) findViewById(R.id.register);
        mProgressDialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        //mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mCurrentUser.getUid());
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        storageReference = FirebaseStorage.getInstance().getReference();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user= firebaseAuth.getCurrentUser();
                if(user!=null)
                {
                    mProgressDialog.dismiss();
                    Intent intent = new Intent(Register.this,BottomNavigation.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    //Sterg toate activitatile din stiva de activitati
                    startActivity(intent);
                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mProgressDialog.setTitle("Se creeaza contul");
                mProgressDialog.setMessage("Va rugam așteptați...");
                mProgressDialog.show();
                mProgressDialog.setCancelable(false);
                mProgressDialog.setCanceledOnTouchOutside(false);
                try {
                    createUserAccount();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK)
        {
            uri = data.getData();
            pP = (ImageView) findViewById(R.id.profilePic);

            Picasso
                    .with(getApplicationContext())
                    .load(uri)
                    .fit()
                    .centerInside()
                    .into(pP);
            photo = true;
        }
    }

    private void pPSelection() {

        final CharSequence[] items = {"Alege din Galerie", "Anulează"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
        builder.setTitle("Adaugă o poză");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Alege din Galerie")) {
                    galleryIntent();
                } else if (items[item].equals("Anulează")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {

        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);
    }

    private void createUserAccount() throws NoSuchAlgorithmException {

        final String emailUser, passUser, user, passConf;
        emailUser = emailRegister.getText().toString().trim();
        passUser = passRegister.getText().toString().trim();
        user = username.getText().toString().trim();
        passConf = passConfirm.getText().toString().trim();


        if( !TextUtils.isEmpty(emailUser) && !TextUtils.isEmpty(passUser) && !TextUtils.isEmpty(user) && passConf.compareTo(passUser)==0 && passConf.length()>=6 && photo)
        {
            mProgressDialog.setTitle("Se creează contul");
            mProgressDialog.setMessage("Vă rugăm așteptați...");
            mProgressDialog.show();
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);

            mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull final Task<AuthResult> task) {
                    if( task.isSuccessful())
                    {
                        final StorageReference filePath = storageReference.child("pP").child(uri.getLastPathSegment());

                        filePath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                final Task<Uri> downloadurl = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String userid = mAuth.getCurrentUser().getUid();
                                        DatabaseReference current_user_db = databaseReference.child(userid);

                                        current_user_db.child("Name").setValue(user);
                                        current_user_db.child("image").setValue(uri.toString());
                                        current_user_db.child("reads").setValue("0");
                                        current_user_db.child("followers").setValue("0");
                                        current_user_db.child("following").setValue("0");
                                        //current_user_db.child("read").setValue("");
                                        //current_user_db.child("friends").setValue("");
                                        //current_user_db.child("read").setValue("");
                                        current_user_db.child("id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                                Toast.makeText(Register.this, "Contul a fost creat cu succes", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                });

                            }
                        });
                    }
                    else
                    {
                        Toast.makeText(Register.this, "Email invalid", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        }

        else if( !TextUtils.isEmpty(emailUser) && !TextUtils.isEmpty(passUser) && !TextUtils.isEmpty(user) && passConf.compareTo(passUser)==0 && passConf.length()>=6)
        {
            mProgressDialog.setTitle("Se creează contul");
            mProgressDialog.setMessage("Vă rugăm așteptați...");
            mProgressDialog.show();
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);

            mAuth.createUserWithEmailAndPassword(emailUser, passUser).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull final Task<AuthResult> task) {
                    if( task.isSuccessful())
                    {

                                        String userid = mAuth.getCurrentUser().getUid();
                                        DatabaseReference current_user_db = databaseReference.child(userid);

                                        current_user_db.child("Name").setValue(user);
                                        current_user_db.child("image").setValue("https://firebasestorage.googleapis.com/v0/b/cruciada256.appspot.com/o/readingrn.png?alt=media&token=14cd90bc-b9ac-4131-b56e-9418d0e2502f");
                                        current_user_db.child("reads").setValue("0");
                                        current_user_db.child("followers").setValue("0");
                                        current_user_db.child("following").setValue("0");
                                        //current_user_db.child("read").setValue("");
                                        //current_user_db.child("friends").setValue("");
                                        //current_user_db.child("read").setValue("");
                                        current_user_db.child("id").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {


                                                Toast.makeText(Register.this, "Contul a fost creat cu succes", Toast.LENGTH_SHORT).show();

                                            }
                                        });



                    }
                    else
                    {
                        Toast.makeText(Register.this, "Email invalid", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();
                    }
                }
            });
        }

        else
        {
            if(TextUtils.isEmpty(emailUser)||TextUtils.isEmpty(passUser)||TextUtils.isEmpty(user)||TextUtils.isEmpty(passConf))
            {
                Toast.makeText(Register.this, "Completează toate spațiile", Toast.LENGTH_SHORT).show();
            }
            else if(passConf.compareTo(passUser)!=0)
            {
                Toast.makeText(Register.this, "Cele doua parole sunt diferite", Toast.LENGTH_SHORT).show();
            }
            else if(passConf.compareTo(passUser)==0 && passConf.length()<6)
            {
                Toast.makeText(Register.this, "Parola trebuie să conțină cel puțin 6 caractere", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(Register.this, "Contul nu a putut fi creat", Toast.LENGTH_SHORT).show();
            }
            mProgressDialog.dismiss();
        }
    }

}