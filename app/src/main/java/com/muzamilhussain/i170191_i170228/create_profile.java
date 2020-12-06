package com.muzamilhussain.i170191_i170228;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;

import java.io.InputStream;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class create_profile extends AppCompatActivity {

    EditText firstName, lastName, phone, bio, dob;
    Button male, female, none, save;
    CircleImageView profileImage;
    RelativeLayout selectImage;
    Uri imageUri, checkUri;
    private String gender;

    private boolean profileExists = false;
    private boolean isPictureUploaded = false;
    final String url = "http://192.168.43.173/bistro_chat/get_profile.php";
    final String profileUrl = "http://192.168.43.173/bistro_chat/profile.php";

    SharedPreferences sp;

    Bitmap bitmap;

    String profileId = "";

    //database helper object
    private MyDBHelper db;


    public static final int PROFILE_SYNCED_WITH_SERVER = 1;
    public static final int PROFILE_NOT_SYNCED_WITH_SERVER = 0;

    //a broadcast to know weather the data is synced or not
    public static final String DATA_SAVED_BROADCAST = "net.simplifiedcoding.datasaved";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);


        firstName = findViewById(R.id.first_name_et);
        lastName = findViewById(R.id.last_name_et);
        phone = findViewById(R.id.phone_no_et);
        bio = findViewById(R.id.bio_et);
        dob = findViewById(R.id.dob_et);
        selectImage = findViewById(R.id.select_img_acp);
        profileImage = findViewById(R.id.profile_img_acp);

        male = findViewById(R.id.male_button);
        female = findViewById(R.id.female_button);
        none = findViewById(R.id.none_button);
        save = findViewById(R.id.save_create_profile);


        db = new MyDBHelper(this);

        sp = getSharedPreferences("user", Context.MODE_PRIVATE);

        final int currentUser = sp.getInt("id",0);

        checkUri = Uri.parse("dummy uri");

        MyDBHelper myDBHelper=new MyDBHelper(create_profile.this);
        SQLiteDatabase database=myDBHelper.getWritableDatabase();
        final ContentValues cv= new ContentValues();

        registerReceiver(new NetworkStateChecker(), new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));

//
//        loadNames();
//
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                //loading the names again
//                loadNames();
//            }
//        };
//
//        //registering the broadcast receiver to update sync status
//        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));


        if (currentUser !=0) {

            loadProfile(currentUser);


//            StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url,
//
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            try {
//                                JSONObject res=new JSONObject(response);
//
//                                if (res.getString("response").equals("200")) {
//                                    profileExists = true;
//
//                                    firstName.setText(res.getString("firstName"));
//                                    lastName.setText(res.getString("lastName"));
//                                    phone.setText(res.getString("phoneNumber"));
//                                    dob.setText(res.getString("dob"));
//                                    bio.setText(res.getString("bio"));
//
//                                    bitmap = StringToImage(res.getString("profilePicture"));
//                                    profileImage.setImageBitmap(bitmap);
//
//
//                                    profileId = res.getString("id");
//
//                                    if (res.getString("gender").equals("male")) {
//                                        gender = "male";
//                                        male.setBackgroundResource(R.drawable.gender_rectangle_button_selected);
//                                        male.setTextColor(Color.parseColor("#ffffff"));
//                                    }
//                                    else if (res.getString("gender").equals("female")) {
//                                        gender= "female";
//                                        female.setBackgroundResource(R.drawable.gender_rectangle_button_selected);
//                                        female.setTextColor(Color.parseColor("#ffffff"));
//                                    }
//                                    else {
//                                        gender = "none";
//                                        none.setBackgroundResource(R.drawable.gender_rectangle_button_selected);
//                                        none.setTextColor(Color.parseColor("#ffffff"));
//                                    }
//
//                                }
//
//                            } catch (JSONException e) {
//                                Toast.makeText(create_profile.this,e.toString(),Toast.LENGTH_LONG).show();
//                                e.printStackTrace();
//                            }
//                            Log.d("MyStringRequest",response);
//                        }
//                    },
//                    new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//                            //This code is executed if there is an error.
//                            Toast.makeText(create_profile.this,error.toString(),Toast.LENGTH_LONG).show();
//                        }
//                    }) {
//                protected Map<String, String> getParams() {
//                    Map<String, String> data = new HashMap<String, String>();
//                    data.put("userId", Integer.toString(currentUser));
//                    return data;
//                }
//            };
//            Volley.newRequestQueue(create_profile.this).add(MyStringRequest);
        }


        //loading the names again
        //loadNames();
        //Broadcast receiver to know the sync status
        BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                //loading the names again
                //loadNames();
                loadProfile(currentUser);
            }
        };

        //registering the broadcast receiver to update sync status
        registerReceiver(broadcastReceiver, new IntentFilter(DATA_SAVED_BROADCAST));


        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "Select Image"), 1);
            }
        });

        male.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "male";
                male.setBackgroundResource(R.drawable.gender_rectangle_button_selected);
                male.setTextColor(Color.parseColor("#ffffff"));

                female.setBackgroundResource(R.drawable.gender_rectangle_button);
                female.setTextColor(Color.parseColor("#00d664"));
                none.setBackgroundResource(R.drawable.gender_rectangle_button);
                none.setTextColor(Color.parseColor("#00d664"));
            }
        });
        female.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "female";
                female.setBackgroundResource(R.drawable.gender_rectangle_button_selected);
                female.setTextColor(Color.parseColor("#ffffff"));

                male.setBackgroundResource(R.drawable.gender_rectangle_button);
                male.setTextColor(Color.parseColor("#00d664"));
                none.setBackgroundResource(R.drawable.gender_rectangle_button);
                none.setTextColor(Color.parseColor("#00d664"));
            }
        });
        none.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gender = "none";
                none.setBackgroundResource(R.drawable.gender_rectangle_button_selected);
                none.setTextColor(Color.parseColor("#ffffff"));

                female.setBackgroundResource(R.drawable.gender_rectangle_button);
                female.setTextColor(Color.parseColor("#00d664"));

                male.setBackgroundResource(R.drawable.gender_rectangle_button);
                male.setTextColor(Color.parseColor("#00d664"));
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest MyStringRequest = new StringRequest(Request.Method.POST, profileUrl,

                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject res=new JSONObject(response);

                                    if (res.getString("response").equals("201")) {

                                        if (profileExists) {
                                            updateProfileLocally(Integer.parseInt(profileId),
                                                    PROFILE_SYNCED_WITH_SERVER,
                                                    firstName.getText().toString().trim(),
                                                    lastName.getText().toString().trim(),
                                                    bio.getText().toString().trim(),
                                                    dob.getText().toString().trim(),
                                                    gender,
                                                    "true",
                                                    phone.getText().toString(),
                                                    imageToString(bitmap)
                                            );

                                        } else {
                                            saveProfileLocally(firstName.getText().toString().trim(),
                                                    lastName.getText().toString().trim(),
                                                    bio.getText().toString().trim(),
                                                    dob.getText().toString().trim(),
                                                    gender,
                                                    "true",
                                                    phone.getText().toString(),
                                                    imageToString(bitmap),
                                                    currentUser,
                                                    PROFILE_SYNCED_WITH_SERVER);
                                        }


                                        Intent ProfileSuccessfulIntent = new Intent(create_profile.this,users_chat_list.class);
                                        startActivity(ProfileSuccessfulIntent);
                                        Toast.makeText(create_profile.this,"Profile Updated Successfully",Toast.LENGTH_LONG).show();
                                        finish();
                                    } else {
                                        if (profileExists) {
                                            updateProfileLocally(Integer.parseInt(profileId),
                                                    PROFILE_NOT_SYNCED_WITH_SERVER,
                                                    firstName.getText().toString().trim(),
                                                    lastName.getText().toString().trim(),
                                                    bio.getText().toString().trim(),
                                                    dob.getText().toString().trim(),
                                                    gender,
                                                    "true",
                                                    phone.getText().toString(),
                                                    imageToString(bitmap)
                                            );
                                        } else {
                                            saveProfileLocally(firstName.getText().toString().trim(),
                                                    lastName.getText().toString().trim(),
                                                    bio.getText().toString().trim(),
                                                    dob.getText().toString().trim(),
                                                    gender,
                                                    "true",
                                                    phone.getText().toString(),
                                                    imageToString(bitmap),
                                                    currentUser,
                                                    PROFILE_NOT_SYNCED_WITH_SERVER);
                                        }
                                        Intent ProfileSuccessfulIntent = new Intent(create_profile.this,users_chat_list.class);
                                        startActivity(ProfileSuccessfulIntent);
                                        Toast.makeText(create_profile.this,"Profile Updated Successfully",Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                } catch (JSONException e) {
//                                    Toast.makeText(create_profile.this,e.toString(),Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                                Log.d("MyStringRequest",response);
                            }
                        },
                        new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //This code is executed if there is an error.
                                if (profileExists) {
                                    updateProfileLocally(Integer.parseInt(profileId),
                                            PROFILE_NOT_SYNCED_WITH_SERVER,
                                            firstName.getText().toString().trim(),
                                            lastName.getText().toString().trim(),
                                            bio.getText().toString().trim(),
                                            dob.getText().toString().trim(),
                                            gender,
                                            "true",
                                            phone.getText().toString(),
                                            imageToString(bitmap)
                                    );
                                } else {
                                    saveProfileLocally(firstName.getText().toString().trim(),
                                            lastName.getText().toString().trim(),
                                            bio.getText().toString().trim(),
                                            dob.getText().toString().trim(),
                                            gender,
                                            "true",
                                            phone.getText().toString(),
                                            imageToString(bitmap),
                                            currentUser,
                                            PROFILE_NOT_SYNCED_WITH_SERVER);
                                }
                                Intent ProfileSuccessfulIntent = new Intent(create_profile.this,users_chat_list.class);
                                startActivity(ProfileSuccessfulIntent);
                                Toast.makeText(create_profile.this,"Profile Updated Successfully",Toast.LENGTH_LONG).show();
                                finish();
                            }
                        }) {
                    protected Map<String, String> getParams() {
                        Map<String, String> data = new HashMap<String, String>();
                        data.put("firstName", firstName.getText().toString().trim());
                        data.put("lastName", lastName.getText().toString().trim());
                        data.put("dob", dob.getText().toString().trim());
                        data.put("gender", gender);
                        data.put("bio", bio.getText().toString().trim());
                        data.put("userId", Integer.toString(currentUser));
                        data.put("phoneNumber", phone.getText().toString());
                        data.put("profilePicture",imageToString(bitmap));
                        if (profileExists) {
                            data.put("id",profileId);
                        }
                        return data;
                    }
                };
                Volley.newRequestQueue(create_profile.this).add(MyStringRequest);
            }
        });



    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            imageUri = data.getData();
            if (imageUri != null) {

                try {
                    InputStream inputStream = getContentResolver().openInputStream(imageUri);
                    bitmap  = BitmapFactory.decodeStream(inputStream);
                    profileImage.setImageBitmap(bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
            else {
                Toast.makeText(create_profile.this,
                        "Please Select Profile Image",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String imageToString (Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream);
        byte[] imageBytes = outputStream.toByteArray();

        String encodedImage  = Base64.encodeToString(imageBytes,Base64.DEFAULT);
        return encodedImage;
    }

    private Bitmap StringToImage (String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    private void saveProfileLocally(String firstName, String lastName, String bio, String dob,
                                    String gender, String isOnline, String phoneNumber
                                    ,String profilePicture, int user, int status) {

        db.addUserProfile(firstName,
                lastName,
                bio,
                dob,
                gender,
                isOnline,
                phoneNumber,
                profilePicture,
                user,
                status);
    }

    private void updateProfileLocally(int id, int status,String firstName, String lastName,
                                      String bio, String dob,String gender, String isOnline,
                                      String phoneNumber,String profilePicture) {

        db.updateUserProfile(id,
                status,
                firstName,
                lastName,
                bio,
                dob,
                gender,
                isOnline,
                phoneNumber,
                profilePicture);
    }

    private void loadProfile(int currentUserId) {


        if (db.checkProfileExists(currentUserId)) {
            userProfile currentUserProfile = db.getUserProfile(currentUserId);
            profileExists = true;


            firstName.setText(currentUserProfile.getFirstName());
            lastName.setText(currentUserProfile.getLastName());
            phone.setText(currentUserProfile.getPhoneNumber());
            dob.setText(currentUserProfile.getDob());
            bio.setText(currentUserProfile.getBio());

            bitmap = StringToImage(currentUserProfile.getProfilePicture());
            profileImage.setImageBitmap(bitmap);


            profileId = currentUserProfile.getId();


            if (currentUserProfile.getGender().equals("male")) {
                gender = "male";
                male.setBackgroundResource(R.drawable.gender_rectangle_button_selected);
                male.setTextColor(Color.parseColor("#ffffff"));
            }
            else if (currentUserProfile.getGender().equals("female")) {
                gender= "female";
                female.setBackgroundResource(R.drawable.gender_rectangle_button_selected);
                female.setTextColor(Color.parseColor("#ffffff"));
            }
            else {
                gender = "none";
                none.setBackgroundResource(R.drawable.gender_rectangle_button_selected);
                none.setTextColor(Color.parseColor("#ffffff"));
            }
        }
    }
}
