package com.example.lenovo.cruciada256;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import org.w3c.dom.Text;

public class ListaEseuriActivity extends AppCompatActivity {

    GlobalClass globalClass;

    private RecyclerView list;
    private TextView Toate;

    private DatabaseReference mDatabase;

    boolean mProcessLike,mProcessDownload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_eseuri);

        list = (RecyclerView) findViewById(R.id.listaEseuri);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this));
        Toate = (TextView) findViewById(R.id.tipeseu);

        globalClass = (GlobalClass) getApplicationContext();

        if(globalClass.getCurrentEseuTip().compareTo("1")==0)
        {
            Toate.setText("Eseuri - tema și viziunea în operă");
        }
        else if(globalClass.getCurrentEseuTip().compareTo("2")==0)
        {
            Toate.setText("Eseuri - caracterizarea personajului");
        }
        else if(globalClass.getCurrentEseuTip().compareTo("3")==0)
        {
            Toate.setText("Eseuri - relația dintre personaje");
        }

        mDatabase = FirebaseDatabase.getInstance().getReference().child("Eseuri").child(globalClass.getCurrentBookID()).child(globalClass.getCurrentEseuTip());

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Eseu,EseuViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Eseu, EseuViewHolder>(
                Eseu.class,
                R.layout.eseu_row,
                EseuViewHolder.class,
                mDatabase
        ) {
            @Override
            protected void populateViewHolder(EseuViewHolder viewHolder, Eseu model, int position) {

                final String post_key = getRef(position).getKey();
                final int like = StringToInt(model.getLikes());
                final int down = StringToInt(model.getDowns());

                viewHolder.setIcon(getApplicationContext(),globalClass.getCurrentEseuTip());
                viewHolder.setDowns(model.getDowns());
                viewHolder.setUps(model.getLikes());
                viewHolder.setPDFtitle(model.getTitlu());
                viewHolder.setStates(globalClass.getCurrentBookID(),globalClass.getCurrentEseuTip(),post_key);
                viewHolder.setRatio(like,down);

                viewHolder.star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        mProcessLike = true;

                        mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(mProcessLike)
                                {

                                    mProcessLike = false;

                                    if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                    {
                                        mDatabase.child(post_key).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).removeValue();
                                        mDatabase.child(post_key).child("likes").setValue(IntToString(like+1));
                                    }
                                    else
                                    {
                                        mDatabase.child(post_key).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("RandomValue");
                                        mDatabase.child(post_key).child("likes").setValue(IntToString(like-1));

                                    }

                                }

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                });

                viewHolder.cutie.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(ListaEseuriActivity.this);

                        View mView = getLayoutInflater().inflate(R.layout.dialog_descarca,null);

                        final TextView DA,NU;

                        DA = (TextView) mView.findViewById(R.id.dialogeseuDA);
                        NU = (TextView) mView.findViewById(R.id.dialogeseuNU);

                        mBuilder.setView(mView);
                        final AlertDialog dialog = mBuilder.create();
                        dialog.show();

                        NU.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                dialog.dismiss();

                            }
                        });

                        DA.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                mProcessDownload = true;

                                mDatabase.child(post_key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        if(mProcessDownload)
                                        {

                                            mProcessDownload = false;

                                            if(!dataSnapshot.child("downers").hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                                            {
                                                mDatabase.child(post_key).child("downs").setValue(IntToString(down-1));
                                                mDatabase.child(post_key).child("downers").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue("RandomValue");
                                            }

                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(dataSnapshot.child("pdfdown").getValue().toString())));
                                            Toast.makeText(ListaEseuriActivity.this,"Se descarcă eseul...",Toast.LENGTH_SHORT).show();
                                            dialog.dismiss();

                                        }

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                            }
                        });

                    }
                });

            }
        };

        list.setAdapter(firebaseRecyclerAdapter);

    }

    public static class EseuViewHolder extends RecyclerView.ViewHolder{

        View mView;

        private ImageView star,cutie;

        private boolean mProcess;

        public EseuViewHolder(View itemView)
        {

            super(itemView);
            mView = itemView;

            star = (ImageView) mView.findViewById(R.id.actualEseuStea);
            cutie = (ImageView) mView.findViewById(R.id.actualEseuCutie);

        }

        public void setIcon(Context ctx,String type)
        {

            ImageView icon = (ImageView)mView.findViewById(R.id.actualEseuImagine);

            if(type.compareTo("1")==0)
            {
                Picasso.with(ctx).load(R.drawable.eseutip1).into(icon);
            }
            else if(type.compareTo("2")==0)
            {
                Picasso.with(ctx).load(R.drawable.eseutip2).into(icon);
            }
            else if(type.compareTo("3")==0)
            {
                Picasso.with(ctx).load(R.drawable.eseutip3).into(icon);
            }


        }

        public void setPDFtitle(String title)
        {

            TextView titlu = (TextView) mView.findViewById(R.id.actualEseuTitlu);
            titlu.setText(title);

        }

        public void setUps(String scor)
        {
            TextView ups = (TextView) mView.findViewById(R.id.actualEseuSteaCnt);
            if(scor.charAt(0)=='-')ups.setText(scor.substring(1)+" aprecieri");
            else if(scor.charAt(0)=='0')ups.setText(scor+" aprecieri");
            else ups.setText("-"+scor+" aprecieri");
        }

        public void setDowns(String scor)
        {
            TextView ups = (TextView) mView.findViewById(R.id.actualEseuDownCnt);
            if(scor.charAt(0)=='-')ups.setText(scor.substring(1)+" descărcări");
            else if(scor.charAt(0)=='0')ups.setText(scor+" descărcări");
            else ups.setText("-"+scor+" descărcări");
        }

        public void setStates(String bookID,String tip,String post_id)
        {
            mProcess = true;
            FirebaseDatabase.getInstance().getReference().child("Eseuri").child(bookID).child(tip).child(post_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(mProcess)
                    {

                        mProcess = false;

                        if(dataSnapshot.hasChild(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                            star.setImageResource(R.drawable.steaplina);
                        else
                            star.setImageResource(R.drawable.stealinie);

                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

        public void setRatio(int a,int b)
        {

            ProgressBar progressBar = (ProgressBar) mView.findViewById(R.id.actualEseuRatio);

            if(a==b && b==0)
            {
                progressBar.setProgress(50);
            }else {

                a = a * (-1);
                b = b * (-1);

                if (b == 0)
                    b = 1;

                progressBar.setProgress(a * 100 / b);
            }
        }

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

}
