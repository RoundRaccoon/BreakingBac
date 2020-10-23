package com.example.lenovo.cruciada256;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class AdaugaEseuActivity extends AppCompatActivity {


    private int tipEseu,cnt;

    private ImageView Imag1,Imag2,Imag3,Imag4;
    private ImageView Del1,Del2,Del3,Del4;
    private Bitmap bitmap1,bitmap2,bitmap3,bitmap4;

    private EditText titlu;

    private static final int SELECT_FILE = 1;

    private Uri uri1=null,uri2=null,uri3=null,uri4=null,UriPDF=null;

    private DatabaseReference databaseReference;

    GlobalClass globalClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adauga_eseu);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        globalClass = (GlobalClass) getApplicationContext();

        Imag1 = (ImageView) findViewById(R.id.eseupoza1);
        Imag2 = (ImageView) findViewById(R.id.eseupoza2);
        Imag3 = (ImageView) findViewById(R.id.eseupoza3);
        Imag4 = (ImageView) findViewById(R.id.eseupoza4);

        Del1 = (ImageView) findViewById(R.id.eseudel1);
        Del2 = (ImageView) findViewById(R.id.eseudel2);
        Del3 = (ImageView) findViewById(R.id.eseudel3);
        Del4 = (ImageView) findViewById(R.id.eseudel4);

        titlu = (EditText) findViewById(R.id.eseutitlu);

        Imag1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cnt==0)selectPhoto();

            }
        });

        Imag2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cnt==1)selectPhoto();

            }
        });

        Imag3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cnt==2)selectPhoto();

            }
        });

        Imag4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cnt==3)selectPhoto();

            }
        });

        Del1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cnt==1)
                {
                    cnt--;
                    Imag2.setVisibility(View.INVISIBLE);
                    Del1.setVisibility(View.GONE);
                    Imag1.setImageDrawable(getResources().getDrawable(R.drawable.adaugapoza));
                }

            }
        });

        Del2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cnt==2)
                {
                    cnt--;
                    Imag3.setVisibility(View.INVISIBLE);
                    Del2.setVisibility(View.GONE);
                    Imag2.setImageDrawable(getResources().getDrawable(R.drawable.adaugapoza));
                    Del1.setVisibility(View.VISIBLE);
                }

            }
        });

        Del3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cnt==3)
                {
                    cnt--;
                    Imag4.setVisibility(View.INVISIBLE);
                    Del3.setVisibility(View.GONE);
                    Imag3.setImageDrawable(getResources().getDrawable(R.drawable.adaugapoza));
                    Del2.setVisibility(View.VISIBLE);
                }

            }
        });

        Del4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(cnt==4)
                {
                    cnt--;
                    //Imag4.setVisibility(View.INVISIBLE);
                    Del4.setVisibility(View.GONE);
                    Imag4.setImageDrawable(getResources().getDrawable(R.drawable.adaugapoza));
                    Del3.setVisibility(View.VISIBLE);
                }

            }
        });

    }

    private void galleryIntent() {

        Log.d("gola", "entered here");
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, SELECT_FILE);


        /**Intent myIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(myIntent,120);*/

    }

    private void cameraIntent(){

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent,0);

    }

    private void selectPhoto() {

        final CharSequence[] items = {"Pornește Camera","Alege din Galerie", "Anulează"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AdaugaEseuActivity.this);
        builder.setTitle("Adaugă o poză");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Alege din Galerie")) {
                    galleryIntent();
                } else if(items[item].equals("Pornește Camera")){
                    cameraIntent();
                } else if (items[item].equals("Anulează")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.eseu_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.eseupost)
        {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent,2);

        }
        else if(id == R.id.eseupdf)
        {

            final String yep = titlu.getText().toString().trim();
            boolean ok=false;

            if(cnt==0)
            {
                Toast.makeText(AdaugaEseuActivity.this,"Ups! Ai uitat să trimiți eseul.",Toast.LENGTH_SHORT).show();
            }
            else if(TextUtils.isEmpty(yep))
            {
                Toast.makeText(AdaugaEseuActivity.this,"Ups! Ai uitat să alegi un titlu",Toast.LENGTH_SHORT).show();
            }
            else
            {

                PdfDocument pdfDocument = new PdfDocument();
                PdfDocument.PageInfo pi = new PdfDocument.PageInfo.Builder(bitmap1.getWidth(),bitmap1.getHeight(),1).create();

                PdfDocument.Page page = pdfDocument.startPage(pi);
                Canvas canvas = page.getCanvas();
                Paint paint = new Paint();
                paint.setColor(getResources().getColor(R.color.alb));
                canvas.drawPaint(paint);

                bitmap1 = Bitmap.createScaledBitmap(bitmap1,bitmap1.getWidth(),bitmap1.getHeight(),true);
                paint.setColor(Color.BLUE);
                canvas.drawBitmap(bitmap1,0,0,null);

                pdfDocument.finishPage(page);

                if(cnt>=2)
                {

                    PdfDocument.PageInfo p2 = new PdfDocument.PageInfo.Builder(bitmap2.getWidth(),bitmap2.getHeight(),2).create();

                    PdfDocument.Page page2 = pdfDocument.startPage(p2);
                    Canvas canvas2 = page2.getCanvas();
                    Paint paint2 = new Paint();
                    paint2.setColor(getResources().getColor(R.color.alb));
                    canvas2.drawPaint(paint2);

                    bitmap2 = Bitmap.createScaledBitmap(bitmap2,bitmap2.getWidth(),bitmap2.getHeight(),true);
                    paint2.setColor(Color.BLUE);
                    canvas2.drawBitmap(bitmap2,0,0,null);

                    pdfDocument.finishPage(page2);

                }

                if(cnt>=3)
                {

                    PdfDocument.PageInfo p3 = new PdfDocument.PageInfo.Builder(bitmap3.getWidth(),bitmap3.getHeight(),3).create();

                    PdfDocument.Page page3 = pdfDocument.startPage(p3);
                    Canvas canvas3 = page3.getCanvas();
                    Paint paint3 = new Paint();
                    paint3.setColor(getResources().getColor(R.color.alb));
                    canvas3.drawPaint(paint3);

                    bitmap3 = Bitmap.createScaledBitmap(bitmap3,bitmap3.getWidth(),bitmap3.getHeight(),true);
                    paint3.setColor(Color.BLUE);
                    canvas3.drawBitmap(bitmap3,0,0,null);

                    pdfDocument.finishPage(page3);

                }

                if(cnt>=4)
                {

                    PdfDocument.PageInfo p4 = new PdfDocument.PageInfo.Builder(bitmap4.getWidth(),bitmap4.getHeight(),4).create();

                    PdfDocument.Page page4 = pdfDocument.startPage(p4);
                    Canvas canvas4 = page4.getCanvas();
                    Paint paint4 = new Paint();
                    paint4.setColor(getResources().getColor(R.color.alb));
                    canvas4.drawPaint(paint4);

                    bitmap4 = Bitmap.createScaledBitmap(bitmap4,bitmap4.getWidth(),bitmap4.getHeight(),true);
                    paint4.setColor(Color.BLUE);
                    canvas4.drawBitmap(bitmap4,0,0,null);

                    pdfDocument.finishPage(page4);

                }

                //Toast.makeText(AdaugaEseuActivity.this,"1",Toast.LENGTH_SHORT).show();

                File folder = new File(Environment.getExternalStorageDirectory(),"Eseuri Cărutrărești");
                if(!folder.exists())
                {
                    ok=true;
                        Toast.makeText(AdaugaEseuActivity.this,"6",Toast.LENGTH_SHORT).show();
                        folder.mkdir();
                    Toast.makeText(AdaugaEseuActivity.this,Environment.getExternalStorageDirectory().toString(),Toast.LENGTH_SHORT).show();
                }

                File myPdf = new File(folder,yep+".pdf");
                if(!myPdf.exists())
                {
                    try{
                        boolean success = myPdf.createNewFile();
                    } catch(IOException e){
                        Toast.makeText(AdaugaEseuActivity.this,"3",Toast.LENGTH_SHORT).show();
                    }

                }

                try{
                    FileOutputStream os = new FileOutputStream(myPdf);
                    pdfDocument.writeTo(os);
                    pdfDocument.close();

                } catch (FileNotFoundException e){
                    Toast.makeText(AdaugaEseuActivity.this,"4",Toast.LENGTH_SHORT).show();
                } catch (IOException e){
                    Toast.makeText(AdaugaEseuActivity.this,"5",Toast.LENGTH_SHORT).show();
                }

                if(!ok)Toast.makeText(AdaugaEseuActivity.this,"Eseul a fost salvat sub forma de PDF in telefonul dvs.",Toast.LENGTH_SHORT).show();
                else Toast.makeText(AdaugaEseuActivity.this,"Eseul nu a putut fi salvat, deoarece aplicatia nu are permisiunea de a modifica in storage.",Toast.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == SELECT_FILE && resultCode == RESULT_OK && data!=null)
        {
            if(cnt==0)
            {
                uri1 = data.getData();
                Picasso
                        .with(getApplicationContext())
                        .load(uri1)
                        .fit()
                        .centerInside()
                        .into(Imag1);


                try {
                    bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri1);
                }  catch (IOException e){
                    e.printStackTrace();
                }

                /**bitmap1 = Bitmap.createScaledBitmap(bitmap1,774,1032,false);

                Matrix matrix = new Matrix();
                matrix.postRotate(90);
                bitmap1 = Bitmap.createBitmap(bitmap1,0,0,bitmap1.getHeight(),bitmap1.getWidth(),matrix,true);

                Toast.makeText(AdaugaEseuActivity.this,""+bitmap1.getWidth(),Toast.LENGTH_SHORT).show();
                Toast.makeText(AdaugaEseuActivity.this,""+bitmap1.getHeight(),Toast.LENGTH_SHORT).show();
                */

                boolean ok = false;
                if(bitmap1.getWidth()>4000)
                    ok = true;

                    if(bitmap1.getWidth()>4){

                        if(ok)bitmap1 = Bitmap.createScaledBitmap(bitmap1,1806,1352,false);
                        else bitmap1 = Bitmap.createScaledBitmap(bitmap1,1352,1806,false);

                        if(ok){Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        bitmap1 = Bitmap.createBitmap(bitmap1,0,0,bitmap1.getWidth(),bitmap1.getHeight(),matrix,true);}

                    }

                //Toast.makeText(AdaugaEseuActivity.this,""+bitmap1.getWidth(),Toast.LENGTH_SHORT).show();
                //oast.makeText(AdaugaEseuActivity.this,""+bitmap1.getHeight(),Toast.LENGTH_SHORT).show();

                cnt++;
                Imag2.setVisibility(View.VISIBLE);
                Del1.setVisibility(View.VISIBLE);
            }
            else if(cnt==1)
            {

                uri2 = data.getData();
                Picasso
                        .with(getApplicationContext())
                        .load(uri2)
                        .fit()
                        .centerInside()
                        .into(Imag2);

                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri2);
                }  catch (IOException e){
                    e.printStackTrace();
                }

                boolean ok = false;
                if(bitmap2.getWidth()>4000)
                    ok = true;

                if(bitmap2.getWidth()>4){

                    if(ok)bitmap2 = Bitmap.createScaledBitmap(bitmap2,1806,1352,false);
                    else bitmap2 = Bitmap.createScaledBitmap(bitmap1,1352,1806,false);

                    if(ok){Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        bitmap2 = Bitmap.createBitmap(bitmap2,0,0,bitmap2.getWidth(),bitmap2.getHeight(),matrix,true);}

                }

                cnt++;
                Imag3.setVisibility(View.VISIBLE);
                Del2.setVisibility(View.VISIBLE);
                Del1.setVisibility(View.GONE);

            }
            else if(cnt==2)
            {

                uri3 = data.getData();
                Picasso
                        .with(getApplicationContext())
                        .load(uri3)
                        .fit()
                        .centerInside()
                        .into(Imag3);
                try {
                    bitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri3);
                }  catch (IOException e){
                    e.printStackTrace();
                }

                boolean ok = false;
                if(bitmap3.getWidth()>4000)
                    ok = true;

                if(bitmap3.getWidth()>4){

                    if(ok)bitmap3 = Bitmap.createScaledBitmap(bitmap3,1806,1352,false);
                    else bitmap3 = Bitmap.createScaledBitmap(bitmap1,1352,1806,false);

                    if(ok){Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        bitmap3 = Bitmap.createBitmap(bitmap3,0,0,bitmap3.getWidth(),bitmap3.getHeight(),matrix,true);}

                }

                cnt++;
                Imag4.setVisibility(View.VISIBLE);
                Del3.setVisibility(View.VISIBLE);
                Del2.setVisibility(View.GONE);

            }
            else if(cnt==3)
            {

                uri4 = data.getData();
                Picasso
                        .with(getApplicationContext())
                        .load(uri4)
                        .fit()
                        .centerInside()
                        .into(Imag4);

                try {
                    bitmap4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri4);
                }  catch (IOException e){
                    e.printStackTrace();
                }

                boolean ok = false;
                if(bitmap4.getWidth()>4000)
                    ok = true;

                if(bitmap4.getWidth()>4){

                    if(ok)bitmap4 = Bitmap.createScaledBitmap(bitmap4,1806,1352,false);
                    else bitmap4 = Bitmap.createScaledBitmap(bitmap1,1352,1806,false);

                    if(ok){Matrix matrix = new Matrix();
                        matrix.postRotate(90);
                        bitmap4 = Bitmap.createBitmap(bitmap4,0,0,bitmap4.getWidth(),bitmap4.getHeight(),matrix,true);}

                }

                cnt++;
                //Imag3.setVisibility(View.VISIBLE);
                Del4.setVisibility(View.VISIBLE);
                Del3.setVisibility(View.GONE);

            }


        }

        if(resultCode == Activity.RESULT_OK && requestCode == 0)
        {
            if(cnt==0)
            {
                String result = data.toURI();
                Picasso
                        .with(getApplicationContext())
                        .load(result)
                        .fit()
                        .centerInside()
                        .into(Imag1);
                cnt++;
                Imag2.setVisibility(View.VISIBLE);
                Del1.setVisibility(View.VISIBLE);
            }
        }

        if(requestCode == 2 && resultCode == RESULT_OK && data!= null)
        {

            UriPDF = data.getData();

            final AlertDialog.Builder mBuilder = new AlertDialog.Builder(AdaugaEseuActivity.this);

            View mView = getLayoutInflater().inflate(R.layout.dialog_trimite,null);

            final TextView tipTema,tipCaract,tipRelatie;

            tipTema = (TextView) mView.findViewById(R.id.eseutema);
            tipCaract = (TextView) mView.findViewById(R.id.eseucaract);
            tipRelatie = (TextView) mView.findViewById(R.id.eseurelatie);

            final TextView Titlu,Trimite;

            Titlu = (TextView) mView.findViewById(R.id.eseupdftitlu);
            Trimite = (TextView) mView.findViewById(R.id.eseuTrimite);

            final LinearLayout linearLayout = (LinearLayout) findViewById(R.id.eseuschimba);

            Titlu.setText(UriPDF.getLastPathSegment().substring(28));

            mBuilder.setView(mView);
            final AlertDialog dialog = mBuilder.create();
            dialog.show();

            tipTema.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tipTema.setTextColor(getResources().getColor(R.color.alb));
                    tipTema.setBackground(getResources().getDrawable(R.drawable.btn_style_2));

                    tipCaract.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tipCaract.setBackground(getResources().getDrawable(R.drawable.btn_style_1));

                    tipRelatie.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tipRelatie.setBackground(getResources().getDrawable(R.drawable.btn_style_1));

                    Trimite.setTextColor(getResources().getColor(R.color.negru));

                    tipEseu = 1;
                }
            });

            tipCaract.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tipTema.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tipTema.setBackground(getResources().getDrawable(R.drawable.btn_style_1));

                    tipCaract.setTextColor(getResources().getColor(R.color.alb));
                    tipCaract.setBackground(getResources().getDrawable(R.drawable.btn_style_2));

                    tipRelatie.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tipRelatie.setBackground(getResources().getDrawable(R.drawable.btn_style_1));

                    Trimite.setTextColor(getResources().getColor(R.color.negru));

                    tipEseu = 2;
                }
            });

            tipRelatie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tipTema.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tipTema.setBackground(getResources().getDrawable(R.drawable.btn_style_1));

                    tipCaract.setTextColor(getResources().getColor(R.color.colorPrimary));
                    tipCaract.setBackground(getResources().getDrawable(R.drawable.btn_style_1));

                    tipRelatie.setTextColor(getResources().getColor(R.color.alb));
                    tipRelatie.setBackground(getResources().getDrawable(R.drawable.btn_style_2));

                    Trimite.setTextColor(getResources().getColor(R.color.negru));

                    tipEseu = 3;
                }
            });

            Trimite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(tipEseu!=0)
                    {

                        Toast.makeText(AdaugaEseuActivity.this,"Se trimite eseul...",Toast.LENGTH_SHORT).show();

                        String tip = ""+tipEseu;

                        final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("eseuri").child(globalClass.getCurrentBookID()).child(tip);
                        databaseReference = FirebaseDatabase.getInstance().getReference().child("Eseuri").child(globalClass.getCurrentBookID()).child(tip).child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        filePath.putFile(UriPDF).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                final Task<Uri> downloadUrl = taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        databaseReference.child("titlu").setValue(UriPDF.getLastPathSegment().substring(28));
                                        databaseReference.child("likes").setValue("0");
                                        databaseReference.child("downs").setValue("0");
                                        databaseReference.child("pdfdown").setValue(uri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                Toast.makeText(AdaugaEseuActivity.this,"Eseul a fost trimis cu succes!",Toast.LENGTH_SHORT).show();
                                                dialog.dismiss();
                                                finish();

                                            }
                                        });

                                    }
                                });

                            }
                        });

                    }

                }
            });

        }

    }
}
