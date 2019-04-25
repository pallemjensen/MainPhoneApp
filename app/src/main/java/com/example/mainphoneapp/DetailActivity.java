package com.example.mainphoneapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.TextView;
import java.io.File;;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.mainphoneapp.DB.DataAccessFactory;
import com.example.mainphoneapp.Model.BEFriend;
import com.example.mainphoneapp.DB.IDataAccess;

public class DetailActivity extends AppCompatActivity {

    private final static String LOGTAG = "Camtag";
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;

    File mFile = null;
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

    void configure_button() {
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        btnUpdateCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission, but works
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


         setGui();


         smsBtn.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
                 startSMSActivity();
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
         friend = (BEFriend) mData.getById(thisid);   // load friend from database by id
         m_etName.setText(friend.getName());
         m_etPhone.setText(friend.getPhone());
         m_etBirthday.setText(friend.getBirthday());
         m_etAddress.setText(friend.getBirthday());
         m_etWeb.setText(friend.getWebsite());
         m_etMail.setText(friend.getMail());
         m_etAddress.setText(friend.getAddress());
         if (friend.getPicture().isEmpty()){
             mImage.setImageResource(R.drawable.mybestfriend_picture);
         } else {
             Bitmap bitmap = BitmapFactory.decodeFile(friend.getPicture());
             mImage.setImageBitmap(bitmap);
         }

        }
     }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteFriend:
                Toast.makeText(this, "Friend is deleted.", Toast.LENGTH_SHORT)
                        .show();
                deleteById();
                goBackToMainView();
                break;

            case R.id.updateFriend:
                Toast.makeText(this, "Friend is updated.", Toast.LENGTH_SHORT)
                        .show();
                updateFriend();
                goBackToMainView();
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


    void deleteById(){

        mData.deleteById(friend.getId());

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

        if (!(dBWeb.contains("www")))
        {
            dBWeb = "https://www." + dBWeb;
        }

        if (!(dBWeb.contains("https://")))
        {
            dBWeb = "https://" + dBWeb;
        }
        String dBAddress = m_etAddress.getText().toString();
        String dBBirthday = m_etBirthday.getText().toString();

        double latLocation = 0;
        double lngLocation = 0;

        if (lat!=null)
        {
            latLocation = lat;
        }

        if (lng!=null)
        {
            lngLocation = lng;
        }


        String picPath = "";
        if (mFile != null) {
            picPath = mFile.getPath();
        }

        Log.d(TAG, "db data test");
        mData.insert(new BEFriend(dBName, dBPhone, latLocation, lngLocation, dBMail, dBWeb, picPath, dBBirthday, dBAddress));
        Log.d(TAG, "mData insert has run without crashing");
    }

    public void updateFriend() {
        String dBName = m_etName.getText().toString();
        String dBPhone = m_etPhone.getText().toString();
        String dBMail = m_etMail.getText().toString();
        String dBWeb = m_etWeb.getText().toString();
        String dBAddress = m_etAddress.getText().toString();
        String dBBirthday = m_etBirthday.getText().toString();

        if (!(dBWeb.contains("www")))
        {
            dBWeb = "https://www." + dBWeb;
        }

        if (!(dBWeb.contains("https://")))
        {
            dBWeb = "https://" + dBWeb;
        }

        double latLocation = friend.getLat();
        double lngLocation = friend.getLon();

        if (lat!=null)
        {
            latLocation = lat;
        }

        if (lng!=null)
        {
            lngLocation = lng;
        }

        String picPath;
        if (mFile != null) {
            picPath = mFile.getPath();
        } else {
            picPath = friend.getPicture();
        }

        mData.update(new BEFriend(friend.getId(),dBName, dBPhone, latLocation, lngLocation, dBMail, dBWeb, picPath, dBBirthday, dBAddress));
    }

    //Creates the menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    //Starts SMS app
    private void startSMSActivity()
    {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setData(Uri.parse("sms:" + m_etPhone.getText().toString()));
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

        if (!(url.contains("www")))
        {
            url = "https://www." + url;
        }

        if (!(url.contains("https://")))
        {
            url = "https://" + url;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    //Opens the phones camera.
    private void onClickTakePics()
    {
        // create Intent to take a picture
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (intent.resolveActivity(getPackageManager()) != null) {
            Log.d(LOGTAG, "camera app will be started");
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
        else
            Log.d(LOGTAG, "camera app could NOT be started");

    }

    /** Create a File for saving an image */
    private File getOutputMediaFile(){
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");

                mFile = getOutputMediaFile(); // create a file to save the image
                if (mFile == null)
                {
                    Toast.makeText(this, "Could not create file...", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    FileOutputStream out = new FileOutputStream(mFile);
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                showPictureTaken(mFile);

            } else
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled...", Toast.LENGTH_LONG).show();
                return;

            } else
                Toast.makeText(this, "Picture NOT taken - unknown error...", Toast.LENGTH_LONG).show();
        }
    }

    //Views the picture that has been taken, in a bitmap.
    private void showPictureTaken(File f) {
        mImage.setImageURI(Uri.fromFile(f));
    }

    //Shows logs
    void log(String s)
    { Log.d(LOGTAG, s); }

}