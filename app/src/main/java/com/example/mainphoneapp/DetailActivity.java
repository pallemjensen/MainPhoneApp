package com.example.mainphoneapp;

import android.Manifest;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.graphics.Point;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.widget.TextView;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.mainphoneapp.DB.DataAccessFactory;
import com.example.mainphoneapp.Model.BEFriend;
import com.example.mainphoneapp.DB.IDataAccess;

public class DetailActivity extends AppCompatActivity {

    private final static String LOGTAG = "Camtag";
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    File mFile;
    ImageView mImage;
    BEFriend friend;
    String TAG = MainActivity.TAG;
    Double lng;
    Double lat;




    //SQL
    IDataAccess mData;


    EditText m_etMail;
    EditText m_etName;
    EditText m_etPhone;
    EditText m_etWeb;
    EditText m_etBirthday;
    EditText m_etAddress;


    //GPS LOCATION
    private Button btnUpdateCoords;
    private TextView txtShowUpdatingCoords;
    private LocationManager locationManager;
    private LocationListener listener;
    private long thisid;

    private final static long DEFAULT_LAT = 0;
    private final static long DEFAULT_LON = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, " Detail activity is running");

        //SQL
        mData = DataAccessFactory.getInstance();

        //GPS

        txtShowUpdatingCoords = (TextView) findViewById(R.id.txtViewNewCoords);
        btnUpdateCoords = (Button) findViewById(R.id.btnUpdateLocation);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);


        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                txtShowUpdatingCoords.setText("\n " + location.getLongitude() + " " + location.getLatitude());

                lng = location.getLongitude();
                lat = location.getLatitude();

                friend.setLat(lat);
                friend.setLng(lng);
                Toast.makeText(DetailActivity.this, "Location updated", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

                Intent i = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(i);
            }
        };

        configure_button();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 10:
                configure_button();
                break;
            default:
                break;
        }
    }

    void configure_button() {
        // first check for permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}
                        , 10);
            }
            return;
        }
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        btnUpdateCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });

         ImageButton smsBtn = findViewById(R.id.btnSMS);
         ImageButton callBtn = findViewById(R.id.btnCALL);
         ImageButton emailBtn = findViewById(R.id.btnEMAIL);
         ImageButton browserBtn = findViewById(R.id.btnBrowser);
         ImageButton btnMap = findViewById(R.id.btnMap);

         mImage = (ImageView) findViewById(R.id.imgView);
         m_etName = findViewById(R.id.etName);
         m_etPhone = findViewById(R.id.etPhone);
         m_etMail = findViewById(R.id.etMail);
         m_etWeb = findViewById(R.id.etWebsite);
         m_etAddress = findViewById(R.id.etAddress);
         m_etBirthday = findViewById(R.id.etBirthday);


         m_etWeb.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startBrowser();
             }
         });

         m_etMail.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sendEmail();
             }
         });

         mImage.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onClickTakePics();
             }
         });

         requestPermissionsInGeneral();
         setGui();


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
                 startBrowser();

             }});

         //Go to Map Activity
         btnMap.setOnClickListener(new View.OnClickListener(){
             public void onClick(View v){
                clickMapButton(friend);
             }
         });

         findViewById(R.id.btnTakePics).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onClickTakePics();
             }
         });
     }

    private void clickMapButton(BEFriend friend) {
         Intent mapIntent = new Intent(this, MapActivity.class );
         mapIntent.putExtra("friend", friend);
         startActivity(mapIntent);
    }

    private void setGui(){

        if (getIntent().hasExtra("id")) {

         long thisid = getIntent().getLongExtra("id",1);
         friend = (BEFriend) mData.getById(thisid);   // getIntent().getSerializableExtra("friend");
         m_etName.setText(friend.getName());
         m_etPhone.setText(friend.getPhone());
         m_etBirthday.setText(friend.getBirthday());
         m_etAddress.setText(friend.getBirthday());
         m_etWeb.setText(friend.getWebsite());
         m_etMail.setText(friend.getMail());
         m_etAddress.setText(friend.getAddress());
        }
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.deleteFriend:
                Toast.makeText(this, "Friend is deleted.", Toast.LENGTH_SHORT)
                        .show();
                break;

            case R.id.updateFriend:
                Toast.makeText(this, "Friend is updated.", Toast.LENGTH_SHORT)
                        .show();
                break;
            case R.id.saveNewFriend:
                Toast.makeText(this, "Friend is created.", Toast.LENGTH_SHORT)
                        .show();
                addFriend();

                goBackToMainView();
                break;

        }
        return true;

    }


    //Go backup to main view after creating a new friend.
    public void goBackToMainView(){
        Intent goBackToMainIntent = new Intent(DetailActivity.this, MainActivity.class);

        startActivity(goBackToMainIntent);

    }

    //Gets the input friends, and creates a new friend.
    public void addFriend() {
        String dBName = m_etName.getText().toString();
        String dBPhone = m_etPhone.getText().toString();
        String dBMail = m_etMail.getText().toString();
        String dBWeb = m_etWeb.getText().toString();
        String dBAddress = m_etAddress.getText().toString();
        String dBBirthday = m_etBirthday.getText().toString();
        //double lat = friend.getLat();
        //double lon = friend.getLon();
        double lat = DEFAULT_LAT;
        double lon = DEFAULT_LON;

        Log.d(TAG, "db data test");
        mData.insert(new BEFriend(dBName, dBPhone, lat, lon, dBMail, dBWeb, "picture", dBBirthday, dBAddress));
        Log.d(TAG, "mData insert has run without crashing");



    }

    //Creates the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
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
        m.sendTextMessage(m_etPhone.getText().toString(), null, text, null, null);
    }

    //Starts SMS app
    //Takes the text, and creates a new message + sends.
    private void startSMSActivity()
    {

        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + m_etPhone.getText().toString()));
        sendIntent.putExtra("sms_body", "Hi, it goes well on the android course...");
        startActivity(sendIntent);
    }

    //Opens phone built in app.
    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + m_etPhone.getText().toString()));
        startActivity(intent);
    }

    //Opens built in mail app.
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

    //Opens a browser, with the website assigned.
    private void startBrowser()
    {
        String url = m_etWeb.getText().toString();
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


    //Opens the phones camera.
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

    //Askes for permission to open apps.
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

    //Views the picture that has been taken, in a bitmap.
    private void showPictureTaken(File f, Bitmap bitmap) {
        mImage.setImageBitmap(bitmap);
        //mImage.setImageURI(Uri.fromFile(f));
        //mImage.setBackgroundColor(Color.RED);
        //mImage.setRotation(90);
    }

    //Shows logs
    void log(String s)
    { Log.d(LOGTAG, s); }

    //Scaling the image that has been captured by the camera.
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