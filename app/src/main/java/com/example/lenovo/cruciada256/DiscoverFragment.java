package com.example.lenovo.cruciada256;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;

public class DiscoverFragment extends Fragment {



    RecyclerView mCarteList;
    DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    int E;
    //EditText SearchText;
    //Button SearchButton;

    GlobalClass globalClass;

    public String userID;

    private FloatingActionButton floatingActionButton;
    private boolean isSent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover,container,false);

        mCarteList = (RecyclerView)view.findViewById(R.id.carteList);
        mCarteList.setHasFixedSize(true);
        mCarteList.setLayoutManager(new LinearLayoutManager(getActivity()));

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fabSuggestBook);

        globalClass =(GlobalClass) getContext().getApplicationContext();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();



        //SearchButton = (Button)view.findViewById(R.id.SearchButton);
        //SearchText = (EditText)view.findViewById(R.id.SearchView) ;

        mDatabase = FirebaseDatabase.getInstance().getReference().child("books");

        /**SearchButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        String text = SearchText.getText().toString();

        FirebaseSearch(text);

        }
        });*/

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                View mView = getLayoutInflater().inflate(R.layout.dialog_suggest,null);

                final EditText Titlu = (EditText) mView.findViewById(R.id.sugestieTitlu);
                final EditText Autor = (EditText) mView.findViewById(R.id.sugestieAutor);
                final EditText Desc  = (EditText) mView.findViewById(R.id.sugestieDescriere);
                final TextView send = (TextView) mView.findViewById(R.id.sugestieTrimite);

                Titlu.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                        String sap = Titlu.getText().toString().trim();
                        if(TextUtils.isEmpty(sap))
                            send.setTextColor(getResources().getColor(R.color.default_text));
                        else
                            send.setTextColor(getResources().getColor(R.color.negru));

                    }
                });

                mBuilder.setView(mView);
                final AlertDialog dialog = mBuilder.create();
                dialog.show();

                send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final String titlu = Titlu.getText().toString().trim();
                        final String autor = Autor.getText().toString().trim();
                        final String desc = Desc.getText().toString().trim();

                        if(!TextUtils.isEmpty(titlu))
                        {

                            isSent = true;

                            FirebaseDatabase.getInstance().getReference().child("Sugestii").addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    if(isSent)//functia se autoapeleaza cand se face o schimbare in baza de date
                                    {         //asa ca folosim variabila isSent

                                        isSent = false;

                                        String CntSugestiiString = dataSnapshot.child("cnt").getValue().toString();
                                        //Extrag numarul de sugestii sub forma de string il convertesc la int si apoi il incrementez

                                        int CntSugestii=0,p=1;

                                        for(int i=CntSugestiiString.length()-1;i>=0;--i)
                                        {
                                            CntSugestii = CntSugestii + p*(CntSugestiiString.charAt(i)-'0');
                                            p*=10;
                                        }

                                        CntSugestii = CntSugestii+1;
                                        String n =""+CntSugestii;

                                        //adaug sugestia in baza de date
                                        FirebaseDatabase.getInstance().getReference().child("Sugestii").child(n).child("Titlu").setValue(titlu);
                                        if(!TextUtils.isEmpty(autor))FirebaseDatabase.getInstance().getReference().child("Sugestii").child(n).child("Autor").setValue(autor);
                                        if(!TextUtils.isEmpty(desc))FirebaseDatabase.getInstance().getReference().child("Sugestii").child(n).child("Descriere").setValue(desc);
                                        FirebaseDatabase.getInstance().getReference().child("Sugestii").child("cnt").setValue(n);
                                        dialog.dismiss();



                                        Toast.makeText(getContext(),"Sugestia a fost trimisă. Mulțumim!",Toast.LENGTH_SHORT).show();

                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }

                    }
                });

            }
        });

        return view;
    }

    /***/

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Carte,CarteViewHolder> FBRA = new FirebaseRecyclerAdapter<Carte, CarteViewHolder>(
                Carte.class, // clasa care extrage valori din baza de date
                R.layout.carte_row, // interfata xml a unei carti
                CarteViewHolder.class, // clasa statica cu functii pentru asezarea informatiilor in cardView
                mDatabase // adresa informatiilor preluate din baza de date
        ) {
            @Override
            protected void populateViewHolder(CarteViewHolder viewHolder, Carte model, final int position) {

                final String post_key = getRef(position).getKey();

                viewHolder.setNume(model.getNume());
                viewHolder.setAutor(model.getAutor());
                viewHolder.setImage(getContext(), model.getImage());
                viewHolder.setProgress(post_key);
                viewHolder.setStele(getContext(),post_key);

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        globalClass.setCurrentBookID(post_key);
                        Fragment newFragment = new CarteFragment();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container,newFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                    }
                });

            }
        };

        mCarteList.setAdapter(FBRA);

    }

    public static class CarteViewHolder extends RecyclerView.ViewHolder{

        View mView;

        DatabaseReference mDatabaseUser;
        DatabaseReference mDatabaseBook;
        ProgressBar mProgressBar;

        ImageView stea1,stea2,stea3,stea4,stea5;
        TextView numarRecenzii;

        public CarteViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;

            mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("readed");
            mDatabaseBook = FirebaseDatabase.getInstance().getReference().child("books");

            mProgressBar = (ProgressBar) mView.findViewById(R.id.progresListaCarti);

            stea1 = (ImageView) mView.findViewById(R.id.steacartecard1);
            stea2 = (ImageView) mView.findViewById(R.id.steacartecard2);
            stea3 = (ImageView) mView.findViewById(R.id.steacartecard3);
            stea4 = (ImageView) mView.findViewById(R.id.steacartecard4);
            stea5 = (ImageView) mView.findViewById(R.id.steacartecard5);
            numarRecenzii = (TextView) mView.findViewById(R.id.numar_recenzii_carte_card);

        }

        public void setStele(final Context ctx,final String key)
        {

            mDatabaseBook.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String totalRecenziiString,totalSteleString;
                    int totalRecenzii,totalStele;

                    if(dataSnapshot.hasChild("totalRec"))
                        totalRecenziiString = dataSnapshot.child("totalRec").getValue().toString();
                    else
                        totalRecenziiString = "0";

                    if(dataSnapshot.hasChild("totalStele"))
                        totalSteleString = dataSnapshot.child("totalStele").getValue().toString();
                    else
                        totalSteleString = "0";

                    totalRecenzii = StringToInt(totalRecenziiString);
                    totalStele = StringToInt(totalSteleString);

                    if(totalRecenzii==0)
                    {

                        Picasso.with(ctx).load(R.drawable.steagoala).into(stea1);
                        Picasso.with(ctx).load(R.drawable.steagoala).into(stea2);
                        Picasso.with(ctx).load(R.drawable.steagoala).into(stea3);
                        Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                        Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);

                        numarRecenzii.setText("(?)");

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

                        numarRecenzii.setText("("+out2+")");

                        if(rating>4.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea5);
                        }
                        else if(rating>4.2)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steajuma).into(stea5);
                        }
                        else if(rating>3.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
                        }
                        else if(rating>3.2)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steajuma).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
                        }
                        else if(rating>2.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
                        }
                        else if(rating>2.2)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steajuma).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
                        }
                        else if(rating>1.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
                        }
                        else if(rating>1.2)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steajuma).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
                        }
                        else if(rating>0.7)
                        {
                            Picasso.with(ctx).load(R.drawable.steaplina).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
                        }
                        else if(rating>0.2)
                        {
                            Picasso.with(ctx).load(R.drawable.steajuma).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
                        }
                        else
                        {
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea1);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea2);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea3);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea4);
                            Picasso.with(ctx).load(R.drawable.steagoala).into(stea5);
                        }

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void setProgress(final String key)
        {

            mDatabaseUser.child(key).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    String s;

                    if(dataSnapshot.hasChild("capReaded"))
                        s = dataSnapshot.child("capReaded").getValue().toString();
                    else
                        s = "0";

                    int x=0,p=1;

                    for(int i=s.length()-1;i>=0;--i)
                    {
                        x = x + p*(s.charAt(i)-'0');
                        p*=10;
                    }

                    final int x1=x;

                    mDatabaseBook.child(key).child("cap").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            String s2 = dataSnapshot.getValue().toString();

                            int x2=0,p2=1;

                            for(int i=s2.length()-1;i>=0;--i)
                            {
                                x2 = x2 + p2*(s2.charAt(i)-'0');
                                p2*=10;
                            }

                            mProgressBar.setProgress(x1*100/x2);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void setNume(String nume)
        {
            TextView carte_nume = (TextView)mView.findViewById(R.id.numecarteview);
            carte_nume.setText(nume);
        }

        public void setAutor(String autor)
        {
            TextView carte_autor = (TextView)mView.findViewById(R.id.autorcarteview);
            carte_autor.setText(autor);
        }

        /**public void setCap(String cap)
        {
            TextView carte_cap = (TextView)mView.findViewById(R.id.capitol_total);
            carte_cap.setText(cap);
        }

        public void setCap_progres(String cap_progres)
        {
            TextView carte_cap_progres = (TextView)mView.findViewById(R.id.capitol_progres);
            carte_cap_progres.setText(cap_progres);
        }
*/

        public void setImage(Context ctx, String image)
        {
            ImageView carte_img = (ImageView) mView.findViewById(R.id.copertacarteview);
            Picasso.with(ctx).load(image).into(carte_img);

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

}
