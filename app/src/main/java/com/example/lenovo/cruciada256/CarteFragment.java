package com.example.lenovo.cruciada256;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CarteFragment extends Fragment {

    private String mPostKey;

    private ImageView Cover;
    private TextView Title;
    private TextView Author;
    private TextView Readers;
    private TextView ReadersRN;
    //private EditText AddReview;
    private TextView button1,buttonR;
    private ProgressBar progressBar;
    private TextView Chapter;

    private DatabaseReference mDatabase;
    DatabaseReference mDatabaseRecenzii;
    private FirebaseAuth mAuth;


    private int capCrt,capTotal;
    private String capCrtString,capTotalString;

    private String userID;

    private String readCNTstring,readingCNTstring;
    private int readCNT,readingCNT;

    private ImageView stea1,stea2,stea3,stea4,stea5;
    private TextView nrRecenzii;

    private String totalSteleString,totalRecenziiString;
    private int totalStele,totalRecenzii;

    private TextView scrieRecenzieText;

    GlobalClass globalClass;

    private ImageView startRecStea1,startRecStea2,startRecStea3,startRecStea4,startRecStea5;

    private final FirebaseDatabase database = FirebaseDatabase.getInstance();

    private NestedScrollView scrol;

    private LinearLayout CuRecenziePers,FaraRecenziePers;

    private ImageView carteRecenzieProfilePic,carteRecenzieStea1,carteRecenzieStea2,carteRecenzieStea3,carteRecenzieStea4,carteRecenzieStea5;
    private TextView carteRecenzieData,carteRecenzieName,carteRecenzieParere,editatiRecenzia,stergetiRecenzia;

    private TextView numarDeRecenzii,veziRecenzii;
    private String notaCrt;

    private boolean wasPosted,mProcessAdd1,mProcessFull,mProcessAdd2,mProcessDownload,mProcessUpdateFull,mProcessUpdateAdd,mProcessUpdateEmpty;

    //private ImageView downloadBook;

    private TextView butonEseu;

    private ImageView eseutip1,eseutip2,eseutip3;
    private TextView texteseu1,texteseu2,texteseu3;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.carte_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.downloadPdf:

                mProcessDownload = true;

                FirebaseDatabase.getInstance().getReference().child("books").child(mPostKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(mProcessDownload)
                        {

                            mProcessDownload = false;

                            if(dataSnapshot.hasChild("download"))
                            {

                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dataSnapshot.child("download").getValue().toString())));
                                Toast.makeText(getContext(),"Se descarcă cartea",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {

                                Toast.makeText(getContext(),"Ne pare rău. Nu avem încă această carte. Încercăm să o adăugăm cât mai curând.",Toast.LENGTH_SHORT).show();

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_carte,container,false);

        globalClass = (GlobalClass) getContext().getApplicationContext();

        //downloadBook = (ImageView) view.findViewById(R.id.downloadPdf);

        mPostKey = globalClass.getCurrentBookID();

        texteseu1 = (TextView) view.findViewById(R.id.texteseu1);
        texteseu2 = (TextView) view.findViewById(R.id.texteseu2);
        texteseu3 = (TextView) view.findViewById(R.id.texteseu3);

        Cover = (ImageView) view.findViewById(R.id.bookcover);
        Title = (TextView) view.findViewById(R.id.booktitle);
        Author = (TextView) view.findViewById(R.id.bokauthor);
        Readers = (TextView) view.findViewById(R.id.bookreads);
        ReadersRN = (TextView) view.findViewById(R.id.bookreadsrn);
        Chapter = (TextView) view.findViewById(R.id.numar_capitole_citit);
        scrieRecenzieText = (TextView) view.findViewById(R.id.scrietiorecenzie);
        butonEseu = (TextView) view.findViewById(R.id.butontrimiteeseu);

        CuRecenziePers = (LinearLayout) view.findViewById(R.id.amdejarecenzie);
        FaraRecenziePers = (LinearLayout) view.findViewById(R.id.namrecenzie);

        carteRecenzieData = (TextView) view.findViewById(R.id.carteRecenzieData);
        carteRecenzieName = (TextView) view.findViewById(R.id.carteRecenzieUserName);
        carteRecenzieParere = (TextView) view.findViewById(R.id.carteRecenzieLunga);
        editatiRecenzia = (TextView) view.findViewById(R.id.editatirecenzia);
        stergetiRecenzia = (TextView) view.findViewById(R.id.stergetirecenzie);

        numarDeRecenzii = (TextView) view.findViewById(R.id.bookrecenzi);
        veziRecenzii = (TextView) view.findViewById(R.id.bookvezirecenzii);

        carteRecenzieProfilePic = (ImageView) view.findViewById(R.id.carteRecenzieProfilePic);
        carteRecenzieStea1 = (ImageView) view.findViewById(R.id.carteRecenzieStea1);
        carteRecenzieStea2 = (ImageView) view.findViewById(R.id.carteRecenzieStea2);
        carteRecenzieStea3 = (ImageView) view.findViewById(R.id.carteRecenzieStea3);
        carteRecenzieStea4 = (ImageView) view.findViewById(R.id.carteRecenzieStea4);
        carteRecenzieStea5 = (ImageView) view.findViewById(R.id.carteRecenzieStea5);

        scrol = (NestedScrollView) view.findViewById(R.id.scrollviewcareaparemaitarziuchill);

        //AddReview = (EditText) view.findViewById(R.id.review_adaugat);
        progressBar = (ProgressBar) view.findViewById(R.id.trueprogressbar);

        stea1 = (ImageView) view.findViewById(R.id.steacarte1);
        stea2 = (ImageView) view.findViewById(R.id.steacarte2);
        stea3 = (ImageView) view.findViewById(R.id.steacarte3);
        stea4 = (ImageView) view.findViewById(R.id.steacarte4);
        stea5 = (ImageView) view.findViewById(R.id.steacarte5);
        nrRecenzii = (TextView) view.findViewById(R.id.numar_recenzii_carte);

        startRecStea1 = (ImageView) view.findViewById(R.id.startrecenziestea1);
        startRecStea2 = (ImageView) view.findViewById(R.id.startrecenziestea2);
        startRecStea3 = (ImageView) view.findViewById(R.id.startrecenziestea3);
        startRecStea4 = (ImageView) view.findViewById(R.id.startrecenziestea4);
        startRecStea5 = (ImageView) view.findViewById(R.id.startrecenziestea5);

        eseutip1 = (ImageView) view.findViewById(R.id.eseutip1);
        eseutip2 = (ImageView) view.findViewById(R.id.eseutip2);
        eseutip3 = (ImageView) view.findViewById(R.id.eseutip3);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        userID = mAuth.getCurrentUser().getUid();

        try {
            mDatabaseRecenzii = FirebaseDatabase.getInstance().getReference().child("Reviews").child(mPostKey);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        button1 = (TextView) view.findViewById(R.id.buton1);
        buttonR = (TextView) view.findViewById(R.id.butonR);

        mDatabaseRecenzii.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(userID))
                {
                    CuRecenziePers.setVisibility(View.VISIBLE);
                    FaraRecenziePers.setVisibility(View.GONE);
                    wasPosted = true;

                    String n;

                    if(dataSnapshot.child(userID).hasChild("image"))Picasso.with(getContext()).load(dataSnapshot.child(userID).child("image").getValue().toString()).into(carteRecenzieProfilePic);
                    if(dataSnapshot.child(userID).hasChild("Nume"))carteRecenzieName.setText(dataSnapshot.child(userID).child("Nume").getValue().toString());
                    if(dataSnapshot.child(userID).hasChild("Nota"))
                    {
                        n = dataSnapshot.child(userID).child("Nota").getValue().toString();

                        notaCrt = dataSnapshot.child(userID).child("Nota").getValue().toString();

                        if(n.compareTo("1")==0)
                        {
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea1);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea2);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea3);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea4);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea5);
                        }
                        else if(n.compareTo("2")==0)
                        {
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea1);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea2);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea3);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea4);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea5);
                        }
                        else if(n.compareTo("3")==0)
                        {
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea1);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea2);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea3);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea4);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea5);
                        }
                        else if(n.compareTo("4")==0)
                        {
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea1);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea2);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea3);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea4);
                            Picasso.with(getContext()).load(R.drawable.steagoala).into(carteRecenzieStea5);
                        }
                        else if(n.compareTo("5")==0)
                        {
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea1);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea2);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea3);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea4);
                            Picasso.with(getContext()).load(R.drawable.steaplina).into(carteRecenzieStea5);
                        }

                    }
                    if(dataSnapshot.child(userID).hasChild("Parere"))
                    {
                        if(dataSnapshot.child(userID).child("Parere").getValue().toString().compareTo(" ")==0)
                        {
                            carteRecenzieParere.setVisibility(View.GONE);
                        }
                        else
                        {
                            carteRecenzieParere.setText(dataSnapshot.child(userID).child("Parere").getValue().toString());
                            carteRecenzieParere.setVisibility(View.VISIBLE);
                        }
                    }



                    if(dataSnapshot.child(userID).hasChild("Date"))carteRecenzieData.setText(dataSnapshot.child(userID).child("Date").getValue().toString());


                }
                else
                {
                    CuRecenziePers.setVisibility(View.GONE);
                    FaraRecenziePers.setVisibility(View.VISIBLE);
                    wasPosted = false;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("books").child(mPostKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                Title.setText(dataSnapshot.child("nume").getValue().toString());
                Author.setText(dataSnapshot.child("autor").getValue().toString());

                 readCNTstring = dataSnapshot.child("readCnt").getValue().toString();
                 readCNT = StringToInt(readCNTstring);

                if(readCNTstring.charAt(0)=='0')
                    Readers.setText("Nimeni nu a citit această carte :(");
                else if(readCNTstring.charAt(0)=='1'&& readCNTstring.length()==1)
                    Readers.setText("O persoană a citit această carte!");
                else
                    Readers.setText(readCNTstring +" persoane au citit această carte!");

                 readingCNTstring = dataSnapshot.child("readingCnt").getValue().toString();
                 readingCNT = StringToInt(readingCNTstring);

                if(readingCNTstring.charAt(0)=='0')
                    ReadersRN.setText("Nimeni nu citește momentan");
                else if(readingCNTstring.charAt(0)=='1'&& readingCNTstring.length()==1)
                    ReadersRN.setText("O persoană citește acum!");
                else
                    ReadersRN.setText(readingCNTstring +" persoane citesc acum");

                Picasso.with(getContext()).load(dataSnapshot.child("image").getValue().toString()).into(Cover);

                capTotalString = dataSnapshot.child("cap").getValue().toString();
                capTotal = StringToInt(capTotalString);

                if(dataSnapshot.hasChild("totalRec"))
                    totalRecenziiString = dataSnapshot.child("totalRec").getValue().toString();
                else
                    totalRecenziiString = "0";

                if(dataSnapshot.hasChild("totalRec"))
                    totalSteleString = dataSnapshot.child("totalStele").getValue().toString();
                else
                    totalSteleString = "0";

                totalRecenzii = StringToInt(totalRecenziiString);

                if(totalRecenzii!=1)
                    numarDeRecenzii.setText(" "+totalRecenzii + " recenzii ale comunității");
                else
                    numarDeRecenzii.setText(" 1 recenzie ale comunității");

                totalStele = StringToInt(totalSteleString);

                if(totalRecenzii==0)
                {

                    Picasso.with(getContext()).load(R.drawable.steagoala).into(stea1);
                    Picasso.with(getContext()).load(R.drawable.steagoala).into(stea2);
                    Picasso.with(getContext()).load(R.drawable.steagoala).into(stea3);
                    Picasso.with(getContext()).load(R.drawable.steagoala).into(stea4);
                    Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);

                    nrRecenzii.setText("(?)");

                }
                else
                {

                    float rating = (float)totalStele/totalRecenzii;

                    NumberFormat format = NumberFormat.getNumberInstance();

                    format.setMinimumFractionDigits(1);
                    format.setMaximumFractionDigits(1);

                    String out = format.format(rating);

                    StringBuilder out2 = new StringBuilder(out);
                    out2.setCharAt(1,'.');

                    nrRecenzii.setText("("+out2+")");

                    if(rating>4.7)
                    {
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea5);
                    }
                    else if(rating>4.2)
                    {
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steajuma).into(stea5);
                    }
                    else if(rating>3.7)
                    {
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);
                    }
                    else if(rating>3.2)
                    {
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steajuma).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);
                    }
                    else if(rating>2.7)
                    {
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);
                    }
                    else if(rating>2.2)
                    {
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steajuma).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);
                    }
                    else if(rating>1.7)
                    {
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);
                    }
                    else if(rating>1.2)
                    {
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steajuma).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);
                    }
                    else if(rating>0.7)
                    {
                        Picasso.with(getContext()).load(R.drawable.steaplina).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);
                    }
                    else if(rating>0.2)
                    {
                        Picasso.with(getContext()).load(R.drawable.steajuma).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);
                    }
                    else
                    {
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea1);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea2);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea3);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea4);
                        Picasso.with(getContext()).load(R.drawable.steagoala).into(stea5);
                    }

                }


                        mDatabase.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.child("readed").hasChild(mPostKey))
                        {

                            capCrtString = dataSnapshot.child("readed").child(mPostKey).child("capReaded").getValue().toString();
                            capCrt = StringToInt(capCrtString);


                            if(capCrt==0)
                            {
                                Chapter.setText("Carte neîncepută");
                                progressBar.setProgress(0);
                                button1.setVisibility(View.VISIBLE);
                                buttonR.setBackground(getResources().getDrawable(R.drawable.btn_style_1));
                                buttonR.setTextColor(getResources().getColor(R.color.colorPrimary));

                            }
                            else if(capCrt==1)
                            {

                                Chapter.setText("Ai citit 1 capitol din "+capTotal);
                                progressBar.setProgress(100/capTotal);
                                button1.setVisibility(View.VISIBLE);
                                buttonR.setBackground(getResources().getDrawable(R.drawable.btn_style_1));
                                buttonR.setTextColor(getResources().getColor(R.color.colorPrimary));

                            }
                            else if(capCrt!=capTotal)
                            {
                                Chapter.setText("Ai citit "+capCrt+" capitole din "+capTotal);
                                progressBar.setProgress(capCrt*100/capTotal);
                                button1.setVisibility(View.VISIBLE);

                                buttonR.setBackground(getResources().getDrawable(R.drawable.btn_style_1));
                                buttonR.setTextColor(getResources().getColor(R.color.colorPrimary));


                            }
                            else {
                                Chapter.setText("Ai citit toate capitolele !");
                                progressBar.setProgress(100);
                                button1.setVisibility(View.GONE);

                                buttonR.setBackground(getResources().getDrawable(R.drawable.btn_style_2));
                                buttonR.setTextColor(getResources().getColor(R.color.alb));

                            }

                        }
                        else
                        {

                            capCrt = 0;
                            capCrtString="0";

                            Chapter.setText("Carte neînceputa");
                            progressBar.setProgress(0);

                        }

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                })  ;


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Eseuri").child(globalClass.getCurrentBookID()).child("1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()!=1)texteseu1.setText(dataSnapshot.getChildrenCount()+" eseuri despre tema și viziunea operei");
                else texteseu1.setText(dataSnapshot.getChildrenCount()+" eseu despre tema și viziunea operei");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Eseuri").child(globalClass.getCurrentBookID()).child("2").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()!=1)texteseu2.setText(dataSnapshot.getChildrenCount()+" eseuri despre caracterizarea personajului");
                else texteseu2.setText(dataSnapshot.getChildrenCount()+" eseu despre caracterizarea personajului");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("Eseuri").child(globalClass.getCurrentBookID()).child("3").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getChildrenCount()!=1)texteseu3.setText(dataSnapshot.getChildrenCount()+" eseuri despre relația dintre personaje");
                else texteseu3.setText(dataSnapshot.getChildrenCount()+" eseu despre relația dintre personaje");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        eseutip1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalClass.setCurrentEseuTip("1");
                Intent intent = new Intent(getActivity(),ListaEseuriActivity.class);
                startActivity(intent);

            }
        });

        eseutip2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalClass.setCurrentEseuTip("2");
                Intent intent = new Intent(getActivity(),ListaEseuriActivity.class);
                startActivity(intent);

            }
        });

        eseutip3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalClass.setCurrentEseuTip("3");
                Intent intent = new Intent(getActivity(),ListaEseuriActivity.class);
                startActivity(intent);

            }
        });

        progressBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                emptyProgressLmao(v);

            }
        });

        buttonR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(capCrt==capTotal)
                    emptyProgressLmao(v);
                else
                fullProgressLmao(v);

            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addProgressLmao(v);

            }
        });

        scrieRecenzieText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalClass.setCurrentBookRecStar("0");
                globalClass.setCurrentBookParere("");
                Intent intent = new Intent(getActivity(),RecenzieActivity.class);
                startActivity(intent);

            }
        });

        startRecStea1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalClass.setCurrentBookRecStar("1");
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea1);
                Intent intent = new Intent(getActivity(),RecenzieActivity.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea1);

                    }
                }, 500);

            }
        });

        startRecStea2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalClass.setCurrentBookRecStar("2");
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea1);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea2);
                Intent intent = new Intent(getActivity(),RecenzieActivity.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea1);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea2);

                    }
                }, 500);

            }
        });

        startRecStea3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalClass.setCurrentBookRecStar("3");
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea1);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea2);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea3);
                Intent intent = new Intent(getActivity(),RecenzieActivity.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea1);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea2);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea3);

                    }
                }, 500);

            }
        });

        startRecStea4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalClass.setCurrentBookRecStar("4");
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea1);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea2);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea3);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea4);
                Intent intent = new Intent(getActivity(),RecenzieActivity.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea1);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea2);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea3);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea4);

                    }
                }, 500);

            }
        });

        startRecStea5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                globalClass.setCurrentBookRecStar("5");
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea1);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea2);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea3);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea4);
                Picasso.with(getContext()).load(R.drawable.steaplina).into(startRecStea5);
                Intent intent = new Intent(getActivity(),RecenzieActivity.class);
                startActivity(intent);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea1);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea2);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea3);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea4);
                        Picasso.with(getContext()).load(R.drawable.stealinie).into(startRecStea5);

                    }
                }, 500);

            }
        });

        editatiRecenzia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabaseRecenzii.child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(dataSnapshot.hasChild("Nota"))globalClass.setCurrentBookRecStar(dataSnapshot.child("Nota").getValue().toString());
                        if(dataSnapshot.hasChild("Parere"))globalClass.setCurrentBookParere(dataSnapshot.child("Parere").getValue().toString());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                Intent intent = new Intent(getActivity(),RecenzieActivity.class);
                startActivity(intent);

            }
        });

        stergetiRecenzia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child("books").child(mPostKey).child("totalRec").setValue(IntToString(totalRecenzii-1));
                mDatabase.child("books").child(mPostKey).child("totalStele").setValue(IntToString(totalStele-StringToInt(notaCrt)));
                mDatabaseRecenzii.child(userID).removeValue();

                mDatabase.child("Users").child(userID).child("ExistaRecenzie").child(mPostKey).removeValue();

            }
        });

        veziRecenzii.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                moveToRecenzii(v);

            }
        });

        /*downloadBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://firebasestorage.googleapis.com/v0/b/cruciada256.appspot.com/o/calinescu_george_enigma_otiliei.pdf?alt=media&token=1c5bb90c-0c53-4d4f-916a-48ebf3e8132f")));

                mProcessDownload = true;

                FirebaseDatabase.getInstance().getReference().child("books").child(mPostKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(mProcessDownload)
                        {

                            mProcessDownload = false;

                            if(dataSnapshot.hasChild("download"))
                            {

                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dataSnapshot.child("download").getValue().toString())));
                                Toast.makeText(getContext(),"Se descarcă cartea",Toast.LENGTH_SHORT).show();

                            }
                            else
                            {

                                Toast.makeText(getContext(),"Ne pare rău. Nu avem încă această carte. Încercăm să o adăugăm cât mai curând.",Toast.LENGTH_SHORT).show();

                            }

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });*/

        butonEseu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(),AdaugaEseuActivity.class);
                startActivity(intent);

            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public int StringToInt(String s)
    {

        if(s.charAt(0)=='-')
        {

            int x = 0, p = 1;

            for (int i = s.length() - 1; i > 0; --i) {
                x = x + p * (s.charAt(i) - '0');
                p *= 10;
            }

            x = x * (-1);
            return x;
        }
        else {
            int x = 0, p = 1;

            for (int i = s.length() - 1; i >= 0; --i) {
                x = x + p * (s.charAt(i) - '0');
                p *= 10;
            }
            return x;
        }


    }

    public String IntToString(int n) {

        String s = "";

        if (n >= 0) {
            int k = -1;

            int[] v = new int[5];

            do {
                k++;
                v[k] = n % 10;
                n /= 10;
            } while (n != 0);

            for (int i = k; i >= 0; --i)
                s = s + v[i];

            return s;

        }

        else
        {
            s = "-";
            n = n * (-1);

            int k = -1;

            int[] v = new int[5];

            do {
                k++;
                v[k] = n % 10;
                n /= 10;
            } while (n != 0);
            for (int i = k; i >= 0; --i)
                s = s + v[i];

            return s;
        }

    }

    public void fullProgressLmao(View view)
    {
        if(capCrt>0)
        {

            readingCNTstring = IntToString(readingCNT-1);
            mDatabase.child("books").child(mPostKey).child("readingCnt").setValue(readingCNTstring);


        }
        readCNTstring = IntToString(readCNT+1);
        mDatabase.child("Users").child(userID).child("readed").child(mPostKey).child("capReaded").setValue(capTotalString);
        mDatabase.child("books").child(mPostKey).child("readCnt").setValue(readCNTstring);
        mDatabase.child("books").child(mPostKey).child(userID).setValue("read");

        mProcessFull = true;

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mProcessFull)
                {
                    mProcessFull = false;

                    int CntNews = StringToInt(dataSnapshot.child("NewsCnt").getValue().toString());

                    CntNews = CntNews - 1;

                    String CntNewsString = IntToString(CntNews);

                    mDatabase.child("NewsCnt").setValue(CntNewsString);
                    mDatabase.child("News").child(CntNewsString).child("tip").setValue("gata");
                    mDatabase.child("News").child(CntNewsString).child("bookID").setValue(mPostKey);
                    mDatabase.child("News").child(CntNewsString).child("userID").setValue(userID);

                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                    mDatabase.child("News").child(CntNewsString).child("date").setValue(dateFormat.format(Calendar.getInstance().getTime()));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mProcessUpdateFull = true;

        mDatabase.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(mProcessUpdateFull)
                {

                    mProcessUpdateFull = false;

                    int Cnt = StringToInt(dataSnapshot.child("reads").getValue().toString());

                    Cnt = Cnt+1;

                    mDatabase.child("Users").child(userID).child("reads").setValue(IntToString(Cnt));

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addProgressLmao(View view)
    {

        if(capCrt==0)
        {
            readingCNTstring = IntToString(readingCNT+1);
            mDatabase.child("books").child(mPostKey).child("readingCnt").setValue(readingCNTstring);
            mDatabase.child("books").child(mPostKey).child(userID).setValue("reading");

            mDatabase.child("Users").child(userID).child("readed").child(mPostKey).child("capReaded").setValue("1");

            mProcessAdd2 = true;

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(mProcessAdd2)
                    {
                        mProcessAdd2 = false;

                        int CntNews = StringToInt(dataSnapshot.child("NewsCnt").getValue().toString());

                        CntNews = CntNews - 1;

                        String CntNewsString = IntToString(CntNews);

                        mDatabase.child("NewsCnt").setValue(CntNewsString);
                        mDatabase.child("News").child(CntNewsString).child("tip").setValue("start");
                        mDatabase.child("News").child(CntNewsString).child("bookID").setValue(mPostKey);
                        mDatabase.child("News").child(CntNewsString).child("userID").setValue(userID);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        mDatabase.child("News").child(CntNewsString).child("date").setValue(dateFormat.format(Calendar.getInstance().getTime()));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
        else if(capCrt==capTotal-1)
        {
            readingCNTstring = IntToString(readingCNT-1);
            mDatabase.child("books").child(mPostKey).child("readingCnt").setValue(readingCNTstring);

            readCNTstring = IntToString(readCNT+1);
            mDatabase.child("books").child(mPostKey).child("readCnt").setValue(readCNTstring);

            capCrtString = IntToString(capCrt+1);
            mDatabase.child("Users").child(userID).child("readed").child(mPostKey).child("capReaded").setValue(capCrtString);

            mDatabase.child("books").child(mPostKey).child(userID).setValue("read");

            mProcessAdd1 = true;

            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(mProcessAdd1)
                    {
                        mProcessAdd1 = false;

                        int CntNews = StringToInt(dataSnapshot.child("NewsCnt").getValue().toString());

                        CntNews = CntNews - 1;

                        String CntNewsString = IntToString(CntNews);

                        mDatabase.child("NewsCnt").setValue(CntNewsString);
                        mDatabase.child("News").child(CntNewsString).child("tip").setValue("gata");
                        mDatabase.child("News").child(CntNewsString).child("bookID").setValue(mPostKey);
                        mDatabase.child("News").child(CntNewsString).child("userID").setValue(userID);

                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
                        mDatabase.child("News").child(CntNewsString).child("date").setValue(dateFormat.format(Calendar.getInstance().getTime()));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            mProcessUpdateAdd = true;

            mDatabase.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(mProcessUpdateAdd)
                    {

                        mProcessUpdateAdd = false;

                        int Cnt = StringToInt(dataSnapshot.child("reads").getValue().toString());

                        Cnt = Cnt+1;

                        mDatabase.child("Users").child(userID).child("reads").setValue(IntToString(Cnt));

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }
        else
        {

            capCrtString = IntToString(capCrt+1);
            mDatabase.child("Users").child(userID).child("readed").child(mPostKey).child("capReaded").setValue(capCrtString);

        }

    }

    public void emptyProgressLmao(View view)
    {

        if(capCrt!=0)
        {

            if(capCrt<capTotal)
            {

                readingCNTstring = IntToString(readingCNT-1);
                mDatabase.child("books").child(mPostKey).child("readingCnt").setValue(readingCNTstring);

            }
            else
            {

                readCNTstring = IntToString(readCNT-1);
                mDatabase.child("books").child(mPostKey).child("readCnt").setValue(readCNTstring);

                mProcessUpdateEmpty = true;

                mDatabase.child("Users").child(userID).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if(mProcessUpdateEmpty)
                        {

                            mProcessUpdateEmpty = false;

                            int Cnt = StringToInt(dataSnapshot.child("reads").getValue().toString());

                            Cnt = Cnt-1;

                            mDatabase.child("Users").child(userID).child("reads").setValue(IntToString(Cnt));

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
            mDatabase.child("Users").child(userID).child("readed").child(mPostKey).child("capReaded").setValue("0");
            mDatabase.child("books").child(mPostKey).child(userID).removeValue();

        }

    }

    public void moveToRecenzii(View v)
    {

        Intent intent = new Intent(getActivity(),ReviewListActivity.class);
        startActivity(intent);

    }

}

