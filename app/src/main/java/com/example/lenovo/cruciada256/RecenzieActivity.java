package com.example.lenovo.cruciada256;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RecenzieActivity extends AppCompatActivity {

    private ImageView persRecenzieStea1,persRecenzieStea2,persRecenzieStea3,persRecenzieStea4,persRecenzieStea5;
    private EditText persRecenzieParere;

    GlobalClass globalClass;

    private RelativeLayout relativeLayoutRecenzie;


    String bookID;

    private String prevRating,CrtBookSteleString,CrtBookRecString;

    private int CrtBookStele,CrtBookRec;

    private DatabaseReference mDatabase,mDatabaseUser,mDatabaseBook;
    private FirebaseAuth mAuth;

    private boolean mProcessPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recenzie);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        persRecenzieStea1 = (ImageView) findViewById(R.id.recenziestea1);
        persRecenzieStea2 = (ImageView) findViewById(R.id.recenziestea2);
        persRecenzieStea3 = (ImageView) findViewById(R.id.recenziestea3);
        persRecenzieStea4 = (ImageView) findViewById(R.id.recenziestea4);
        persRecenzieStea5 = (ImageView) findViewById(R.id.recenziestea5);

        relativeLayoutRecenzie = (RelativeLayout) findViewById(R.id.recenzieLayoutMare);

        persRecenzieParere = (EditText) findViewById(R.id.recenzieparere);

        globalClass = (GlobalClass) getApplicationContext();

        persRecenzieParere.setText(globalClass.getCurrentBookParere());

        prevRating = globalClass.getCurrentBookRecStar();
        bookID = globalClass.getCurrentBookID();

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Reviews").child(bookID).child(mAuth.getCurrentUser().getUid());
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mDatabaseBook = FirebaseDatabase.getInstance().getReference().child("books").child(bookID);


        if(prevRating.compareTo("1")==0)
            updateStar1();
        else if(prevRating.compareTo("2")==0)
            updateStar2();
        else if(prevRating.compareTo("3")==0)
        updateStar3();
        else if(prevRating.compareTo("4")==0)
            updateStar4();
        else if(prevRating.compareTo("5")==0)
            updateStar5();

        mDatabaseBook.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild("totalRec"))
                    CrtBookRecString = dataSnapshot.child("totalRec").getValue().toString();
                else
                    CrtBookRecString = "0";
                if(dataSnapshot.hasChild("totalStele"))CrtBookSteleString = dataSnapshot.child("totalStele").getValue().toString();
                else
                    CrtBookSteleString = "0";


                CrtBookRec = StringToInt(CrtBookRecString);
                CrtBookStele = StringToInt(CrtBookSteleString);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        persRecenzieStea1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateStar1();

            }
        });

        persRecenzieStea2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateStar2();

            }
        });

        persRecenzieStea3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateStar3();

            }
        });

        persRecenzieStea4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateStar4();

            }
        });

        persRecenzieStea5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateStar5();

            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.recenzie_menu,menu);



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id =item.getItemId();

        if(id==R.id.recenziepost)
        {
            if(globalClass.getCurrentBookRecStar()=="0")
            {
                Toast.makeText(RecenzieActivity.this,"Nu uita să acorzi cărții o notă",Toast.LENGTH_SHORT).show();
            }
            else
            {
                mProcessPost = true;

                mDatabaseUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(mProcessPost) {

                            if (dataSnapshot.child("ExistaRecenzie").hasChild(bookID)) {

                                mProcessPost = false;

                                mDatabase.child("image").setValue(dataSnapshot.child("image").getValue().toString());
                                mDatabase.child("Nume").setValue(dataSnapshot.child("Name").getValue().toString());
                                mDatabase.child("Nota").setValue(globalClass.getCurrentBookRecStar());

                                final String parere = persRecenzieParere.getText().toString().trim();

                                if (!TextUtils.isEmpty(parere))
                                    mDatabase.child("Parere").setValue(persRecenzieParere.getText().toString());
                                else mDatabase.child("Parere").setValue(" ");
                                mDatabase.child("Puncte").setValue("500");

                                mDatabase.child("Voturi").removeValue();


                            } else {


                                mProcessPost = false;

                                mDatabaseBook.child("totalRec").setValue(IntToString(CrtBookRec+1));
                                mDatabaseBook.child("totalStele").setValue(IntToString(CrtBookStele+StringToInt(globalClass.getCurrentBookRecStar())));

                                mDatabase.child("image").setValue(dataSnapshot.child("image").getValue().toString());
                                mDatabase.child("Nume").setValue(dataSnapshot.child("Name").getValue().toString());
                                mDatabase.child("Nota").setValue(globalClass.getCurrentBookRecStar());

                                final String parere = persRecenzieParere.getText().toString().trim();

                                if (!TextUtils.isEmpty(parere))
                                    mDatabase.child("Parere").setValue(persRecenzieParere.getText().toString());
                                else mDatabase.child("Parere").setValue(" ");
                                mDatabase.child("Puncte").setValue("500");
                                mDatabaseUser.child("ExistaRecenzie").child(bookID).setValue("RandomValue");

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });




                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                mDatabase.child("Date").setValue(dateFormat.format(Calendar.getInstance().getTime()));

                Toast.makeText(this,"Recenzia a fost postată",Toast.LENGTH_SHORT).show();
                finish();



            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void updateStar1()
    {

        globalClass.setCurrentBookRecStar("1");
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea1);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea2);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea3);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea4);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea5);

    }

    public void updateStar2()
    {

        globalClass.setCurrentBookRecStar("2");
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea1);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea2);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea3);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea4);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea5);

    }

    public void updateStar3()
    {

        globalClass.setCurrentBookRecStar("3");
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea1);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea2);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea3);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea4);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea5);

    }

    public void updateStar4()
    {

        globalClass.setCurrentBookRecStar("4");
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea1);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea2);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea3);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea4);
        Picasso.with(getApplicationContext()).load(R.drawable.stealinie).into(persRecenzieStea5);

    }

    public void updateStar5()
    {

        globalClass.setCurrentBookRecStar("5");
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea1);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea2);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea3);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea4);
        Picasso.with(getApplicationContext()).load(R.drawable.steaplina).into(persRecenzieStea5);

    }

    public int StringToInt(String s)
    {

        int x=0,p=1;

        for(int i=s.length()-1;i>=0;--i)
        {
            x = x + p*(s.charAt(i)-'0');
            p*=10;
        }

        return x;

    }

    public String IntToString(int n)
    {

        String s="";

        int k=-1;

        int[] v = new int[5];

        do
        {
            k++;
            v[k]=n%10;
            n/=10;
        }while(n!=0);

        for(int i=k;i>=0;--i)
            s = s + v[i];

        return s;

    }

}
