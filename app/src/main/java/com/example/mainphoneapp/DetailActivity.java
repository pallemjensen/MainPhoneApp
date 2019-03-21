package com.example.mainphoneapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.widget.TextView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.mainphoneapp.Model.BEFriend;

public class DetailActivity extends AppCompatActivity {

    private final static String LOGTAG = "Camtag";
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    File mFile;
    ImageView mImage;
    TextView mFilename;

    String TAG = MainActivity.TAG;

    String phoneNumber;

     EditText m_etMail;
     EditText m_etName;
     EditText m_etPhone;
     EditText m_etWeb;
     EditText m_etBirthday;
     EditText m_etAddress;
    private String web;


    @Override
        protected void onCreate(Bundle savedInstanceState){
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_detail);
         Log.d(TAG, " Detail activity is running");

         Button smsBtn = findViewById(R.id.btnSMS);
         Button callBtn = findViewById(R.id.btnCALL);
         Button emailBtn = findViewById(R.id.btnEMAIL);
         Button browserBtn = findViewById(R.id.btnBrowser);
         mImage = (ImageView) findViewById(R.id.imgView);
         mFilename = (TextView) findViewById(R.id.txtFileName);
         mFilename.setBackgroundColor(Color.LTGRAY);
         m_etName = findViewById(R.id.etName);
         m_etPhone = findViewById(R.id.etPhone);
         m_etMail = findViewById(R.id.etMail);
         m_etWeb = findViewById(R.id.etWebsite);
         m_etAddress = findViewById(R.id.etAddress);
         m_etBirthday = findViewById(R.id.etBirthday);

         Button btnMap = findViewById(R.id.btnMap);

         requestPermissionsInGeneral();
        setGui();
        phoneNumber = m_etPhone.getText().toString();
        web = m_etWeb.getText().toString().trim();

         smsBtn.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 showYesNoDialog();
             }
         });
         callBtn.setOnClickListener(new View.OnClickListener() {

             public void onClick(View v) {
                 makeCall();
             }
         });
         emailBtn.setOnClickListener(new View.OnClickListener() {

             public void onClick(View v) {
                 sendEmail();
             }
         });
         browserBtn.setOnClickListener(new View.OnClickListener() {

             public void onClick(View arg0) {
                 startBrowser(web);

             }});

         //Go to Map Activity
         btnMap.setOnClickListener(new View.OnClickListener(){
             public void onClick(View v){
                clickMapButton();
             }
         });

         findViewById(R.id.btnTakePics).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onClickTakePics();
             }
         });


     }

    private void clickMapButton() {
         Intent mapIntent = new Intent(this, MapActivity.class );
         startActivity(mapIntent);
    }

    private void setGui(){
         BEFriend friend = (BEFriend) getIntent().getSerializableExtra("friend");
         m_etName.setText(friend.getName());
         m_etPhone.setText(friend.getPhone());
         m_etBirthday.setText(friend.getBirthday());
         m_etAddress.setText(friend.getBirthday());
         m_etWeb.setText(friend.getWebsite());
         m_etMail.setText(friend.getMail());
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itemAbout:
                Toast.makeText(this, "About will be show - NIY", Toast.LENGTH_SHORT)
                        .show();
                break;

            case R.id.itemHelp:
                Toast.makeText(this, "Help will be shown - NIY", Toast.LENGTH_SHORT)
                        .show();
                break;
        }
        return true;

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }


    static int PERMISSION_REQUEST_CODE = 1;


    private void sendSMS() {
        Toast.makeText(this, "An sms will be send", Toast.LENGTH_LONG)
                .show();



        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.SEND_SMS)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.SEND_SMS};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                return;

            }
            else
                Log.d(TAG, "permission to SEND_SMS granted!");

        }

        SmsManager m = SmsManager.getDefault();
        String text = "Hi, it goes well on the android course...";
        m.sendTextMessage(phoneNumber, null, text, null, null);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode,
                                            String[] permissions,
                                            int[] grantResults)
    {

        Log.d(TAG, "Permission: " + permissions[0] + " - grantResult: " + grantResults[0]);

        if (permissions[0].equals(Manifest.permission.SEND_SMS) && grantResults[0] == PackageManager.PERMISSION_GRANTED)

        {
            SmsManager m = SmsManager.getDefault();

            String text = "Hi, it goes well on the android course...";
            m.sendTextMessage(phoneNumber, null, text, null, null);
        }

    }

    private void startSMSActivity()
    {

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + phoneNumber));
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...");
        startActivity(sendIntent);
    }

    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(intent);
    }

    private void sendEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        String[] receivers = { m_etMail.getText().toString()};
        emailIntent.putExtra(Intent.EXTRA_EMAIL, receivers);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Test");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "Hej, Hope that it is ok, Best Regards android...;-)");
        startActivity(emailIntent);
    }


    private void startBrowser(String web)
    {
        String url = web;
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void showYesNoDialog()
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("SMS Handling");

        alertDialogBuilder
                .setMessage("Click Direct if SMS should be send directly. Click Start to start SMS app...")
                .setCancelable(true)
                .setPositiveButton("Direct",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        DetailActivity.this.sendSMS();
                    }
                })
                .setNegativeButton("Start", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        DetailActivity.this.startSMSActivity();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }




    private void onClickTakePics()
    {

        mFile = getOutputMediaFile(); // create a file to save the image
        if (mFile == null)
        {
            Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show();
            return;
        }
        // create Intent to take a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mFile));

        Log.d(LOGTAG, "file uri = " + Uri.fromFile(mFile).toString());

        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(LOGTAG, "camera app will be started");
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else
            Log.d(LOGTAG, "camera app could NOT be started");

    }

    /** Create a File for saving an image */
    private File getOutputMediaFile(){

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED) {

                Log.d(TAG, "permission denied to SEND_SMS - requesting it");
                String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
                return null;

            }
            else
                Log.d(TAG, "permission to write to ext file granted!");

        }


        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "Camera01");

        // Create the storage directory if it does not exist
        if (! mediaStorageDir.exists()){

            if (! mediaStorageDir.mkdirs()){
                log("failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String postfix = "jpg";
        String prefix = "IMG";

        File mediaFile = new File(mediaStorageDir.getPath() +
                File.separator + prefix +
                "_"+ timeStamp + "." + postfix);

        return mediaFile;
    }

    private void requestPermissionsInGeneral() {
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        requestPermissions(permissions, PERMISSION_REQUEST_CODE);
    }}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                showPictureTaken(mFile, bitmap);


            } else
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show();
                return;

            } else
                Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show();
        }
    }

    private void showPictureTaken(File f, Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
        //mImage.setImageURI(Uri.fromFile(f));
        mImage.setBackgroundColor(Color.RED);
        //mImage.setRotation(90);
        mFilename.setText(f.getAbsolutePath());
    }


    void log(String s)
    { Log.d(LOGTAG, s); }

    private void scaleImage()
    {
        final Display display = getWindowManager().getDefaultDisplay();
        Point p = new Point();
        display.getSize(p);
        final float screenWidth = p.x;
        final float screenHeight = p.y-100;
        mImage.setMaxHeight((int)screenHeight);
        mImage.setMaxWidth((int)screenWidth);
    }

}