package com.example.mainphoneapp;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mainphoneapp.Model.BEFriend;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailActivity extends AppCompatActivity {

    private final static String LOGTAG = "Camtag";
    private final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
    private static final int PICK_IMAGE_REQUEST = 1;


    //Picture upload
    private Button mButtonChooseImage;
    ImageView mImage;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    // Firestore stuff
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_MAIL = "mail";
    private static final String KEY_GEO = "location";
    private static final String KEY_PICTURE_NAME = "picture";

    private FirebaseFirestore fireDb = FirebaseFirestore.getInstance();
    private DocumentReference friendRef;

    File mFile = null;

    BEFriend friend;
    String TAG = MainActivity.TAG;
    Double lng;
    Double lat;

    EditText m_etMail;
    EditText m_etName;
    EditText m_etPhone;
    EditText m_etAddress;

    //GPS LOCATION
    private Button btnUpdateCoords;
    private TextView txtShowUpdatingCoords;
    private LocationManager locationManager;
    private LocationListener listener;
    private long thisid;
    private String friendId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Log.d(TAG, " Detail activity is running");

        if (getIntent().hasExtra("id")) {
            friendId = getIntent().getStringExtra("id");
            friendRef = fireDb.collection("Friends").document(friendId);
        }

        //Picture
        mButtonChooseImage = findViewById(R.id.BtnChooseImage);
        mImage = findViewById(R.id.imgView);

        mStorageRef = FirebaseStorage.getInstance().getReference("friend-pictures");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Friends");

        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        //GPS
        txtShowUpdatingCoords = findViewById(R.id.txtViewNewCoords);
        btnUpdateCoords = findViewById(R.id.btnUpdateLocation);

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


    /*
    Returns the file extension (.jpg ect...)
     */
    private String getFileExtension(Uri uri){
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    /*
    Uploads the picture, and saves the picture with a name consisting of current time + extension..
     */
    private String uploadImage() {
        String imageName = "";
        if (mImageUri != null) {
            imageName = System.currentTimeMillis() + "." + getFileExtension(mImageUri);
            StorageReference fileReference = mStorageRef.child(imageName);
            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(DetailActivity.this, "Picture uploaded", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
           } else if (mFile!= null){
            imageName = System.currentTimeMillis() + "." + getFileExtension(Uri.fromFile(mFile));
            StorageReference fileReference = mStorageRef.child(imageName);
            fileReference.putFile(Uri.fromFile(mFile)).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(DetailActivity.this, "Picture uploaded", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetailActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else
        {
            Toast.makeText(DetailActivity.this, "No picture selected.", Toast.LENGTH_SHORT).show();
            imageName = "default.jpg";
        }

        return imageName;
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    void configure_button() {
        // this code won't execute IF permissions are not allowed, because in the line above there is return statement.
        btnUpdateCoords.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                //noinspection MissingPermission, but works
                locationManager.requestLocationUpdates("gps", 5000, 0, listener);
            }
        });

         ImageButton smsBtn = findViewById(R.id.btnSMS);
         ImageButton callBtn = findViewById(R.id.btnCALL);
         ImageButton emailBtn = findViewById(R.id.btnEMAIL);

         ImageButton btnMap = findViewById(R.id.btnMap);

         m_etName = findViewById(R.id.etName);
         m_etPhone = findViewById(R.id.etPhone);
         m_etMail = findViewById(R.id.etMail);
         m_etAddress = findViewById(R.id.etAddress);

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
            loadFriend();
        }

       /*
         if (friend.getPicture().isEmpty()){
             mImage.setImageResource(R.drawable.mybestfriend_picture);
         } else {
             Bitmap bitmap = BitmapFactory.decodeFile(friend.getPicture());
             mImage.setImageBitmap(bitmap);
         }
         */
        }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deleteFriend:
                Toast.makeText(this, "Friend is deleted.", Toast.LENGTH_SHORT)
                        .show();
                deleteFriend();
                goBackToMainView();
                break;
            case R.id.updateFriend:
                Toast.makeText(this, "Friend is updated.", Toast.LENGTH_SHORT)
                        .show();
                updateFriendInFireStore();
                goBackToMainView();
                break;
            case R.id.saveNewFriend:
                Toast.makeText(this, "Friend is created.", Toast.LENGTH_SHORT)
                        .show();
                saveFriendtoFireStore();
                goBackToMainView();

                break;
        }
        return true;
    }

        public void updateFriendInFireStore(){
        double currentLatitude = friend.getLocation().getLatitudeE6();
        double currentLongtitude = friend.getLocation().getLongitudeE6();

        if (lat!=null)
        {
            currentLatitude = lat;
        }

        if (lng!=null)
        {
            currentLongtitude = lng;
        }

        String picture;
        String newPicture =  uploadImage();
        String currentPicture = friend.getPicture();

        if (newPicture!=currentPicture)
        {
            picture = newPicture;
        }
        else {
            picture = currentPicture;
        }

        String name = m_etName.getText().toString();
        String address = m_etAddress.getText().toString();
        String phone = m_etPhone.getText().toString();
        String mail = m_etMail.getText().toString();

        GeoPoint geoPoint = new GeoPoint(currentLatitude, currentLongtitude);

        Map<String, Object> friend = new HashMap<>();

        friend.put(KEY_NAME, name);
        friend.put(KEY_ADDRESS, address);
        friend.put(KEY_PHONE, phone);
        friend.put(KEY_MAIL,mail);
        friend.put(KEY_GEO,geoPoint);
        friend.put(KEY_PICTURE_NAME, picture);

        friendRef.update(friend)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(DetailActivity.this, "Friend updated", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "Update Error", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    public void loadFriend(){
                    fireDb.collection("Friends").document(friendId).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            friend = documentSnapshot.toObject(BEFriend.class);
                            String pic;
                            if (friend.getPicture() != null)
                            pic = friend.getPicture();
                            else
                            {
                                pic = "default.jpg";
                            }
                            StorageReference sr = mStorageRef.child(pic);
                            sr.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            Picasso.get().load(uri).fit().into(mImage);
                                        }
                                    });
                            m_etName.setText(friend.getName());
                            m_etPhone.setText(friend.getPhone());
                            m_etMail.setText(friend.getMail());
                            m_etAddress.setText(friend.getAddress());
                        } else {
                            Toast.makeText(DetailActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DetailActivity.this, "Document does not exist", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());
                }
                });

    }

    public void saveFriendtoFireStore() {

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

    String name = m_etName.getText().toString();
    String address = m_etAddress.getText().toString();
    String phone = m_etPhone.getText().toString();
    String mail = m_etMail.getText().toString();
    String picture = uploadImage();

    GeoPoint geoPoint = new GeoPoint(latLocation, lngLocation);

    Map<String, Object> friend = new HashMap<>();

    friend.put(KEY_NAME, name);
    friend.put(KEY_ADDRESS, address);
    friend.put(KEY_PHONE, phone);
    friend.put(KEY_MAIL,mail);
    friend.put(KEY_GEO,geoPoint);
    friend.put(KEY_PICTURE_NAME, picture);


        fireDb.collection("Friends").document().set(friend)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(DetailActivity.this, "Friend added", Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(DetailActivity.this, "Error adding friend to Firestore", Toast.LENGTH_SHORT).show();
                }
            });
    }

    //Deletes the whole document, from FireStore. (the entire friend)
    public void deleteFriend(){
        friendRef.delete();
   }


    //Go backup to main view after creating a new friend.
    public void goBackToMainView(){
        Intent goBackToMainIntent = new Intent(DetailActivity.this, MainActivity.class);
        startActivity(goBackToMainIntent);
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
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT,
                "Write your message here");
        startActivity(emailIntent);
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


    //Create a File for saving an image
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
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
                mImageUri = data.getData();
            Picasso.get().load(mImageUri).fit().into(mImage);
        }

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
