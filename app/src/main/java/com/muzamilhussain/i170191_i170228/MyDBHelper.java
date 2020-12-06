package com.muzamilhussain.i170191_i170228;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MyDBHelper extends SQLiteOpenHelper {
    String CREATE_USERS_TABLE="CREATE TABLE "+
            contractClass.User.tableName+" ("+
            contractClass.User._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            contractClass.User._email +" TEXT NOT NULL, "+
            contractClass.User._password +" TEXT );";



    String CREATE_USER_PROFILE_TABLE="CREATE TABLE "+
            contractClass.UserProfile.tableName+" ("+
            contractClass.UserProfile._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            contractClass.UserProfile._status +" TINYINT NOT NULL, "+
            contractClass.UserProfile._firstName +" TEXT NOT NULL, "+
            contractClass.UserProfile._lastName +" TEXT NOT NULL, "+
            contractClass.UserProfile._bio +" TEXT NOT NULL, "+
            contractClass.UserProfile._dob +" TEXT NOT NULL, "+
            contractClass.UserProfile._gender +" TEXT NOT NULL, "+
            contractClass.UserProfile._isOnline +" TEXT NOT NULL, "+
            contractClass.UserProfile._phoneNumber +" TEXT NOT NULL, "+
            contractClass.UserProfile._profilePicture +" TEXT NOT NULL, "+
            contractClass.UserProfile._user +" INTEGER NOT NULL );";



    String CREATE_USER_CHAT_TABLE="CREATE TABLE "+
            contractClass.UserChat.tableName+" ("+
            contractClass.UserChat._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "+
            contractClass.UserChat._status +" TINYINT NOT NULL, "+
            contractClass.UserChat._senderId +" INTEGER NOT NULL, "+
            contractClass.UserChat._receiverId +" INTEGER NOT NULL, "+
            contractClass.UserChat._chatId +" TEXT NOT NULL, "+
            contractClass.UserChat._message +" TEXT NOT NULL, "+
            contractClass.UserChat._date +" DATE NOT NULL, "+
            contractClass.UserChat._isFav +" TEXT NOT NULL, "+
            contractClass.UserChat._isSeen +" TEXT NOT NULL, "+
            contractClass.UserChat._isLast +" TEXT NOT NULL );";

    String DROP_USERS_TABLE="DROP TABLE IF EXISTS "+contractClass.User.tableName;
    String DROP_USER_CHAT_TABLE="DROP TABLE IF EXISTS "+contractClass.UserChat.tableName;
    String DROP_USER_PROFILE_TABLE="DROP TABLE IF EXISTS "+contractClass.UserProfile.tableName;

    public MyDBHelper(@Nullable Context context) {
        super(context, contractClass.DB_NAME, null, contractClass.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_USER_CHAT_TABLE);
        db.execSQL(CREATE_USER_PROFILE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_USERS_TABLE);
        db.execSQL(DROP_USER_CHAT_TABLE);
        db.execSQL(DROP_USER_PROFILE_TABLE);
        onCreate(db);
    }


    public boolean addUserProfile(String firstName, String lastName, String bio, String dob,String gender, String isOnline, String phoneNumber
                                    ,String profilePicture, int user, int status) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(contractClass.UserProfile._firstName,firstName);
        contentValues.put(contractClass.UserProfile._lastName,lastName);
        contentValues.put(contractClass.UserProfile._bio,bio);
        contentValues.put(contractClass.UserProfile._dob,dob);
        contentValues.put(contractClass.UserProfile._gender,gender);
        contentValues.put(contractClass.UserProfile._isOnline,isOnline);
        contentValues.put(contractClass.UserProfile._phoneNumber,phoneNumber);
        contentValues.put(contractClass.UserProfile._profilePicture,profilePicture);
        contentValues.put(contractClass.UserProfile._user,user);
        contentValues.put(contractClass.UserProfile._status,status);


        db.insert(contractClass.UserProfile.tableName,null,contentValues);
        db.close();
        return true;
    }

    public boolean updateUserProfileStatus (int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(contractClass.UserProfile._status, status);
        db.update(contractClass.UserProfile.tableName, contentValues, contractClass.UserProfile._ID + "=" + id, null);
        db.close();
        return true;
    }

    public boolean updateUserProfile (int id, int status, String firstName, String lastName, String bio,
                                      String dob, String gender, String isOnline, String phoneNumber,
                                      String profilePicture) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(contractClass.UserProfile._status, status);
        contentValues.put(contractClass.UserProfile._firstName,firstName);
        contentValues.put(contractClass.UserProfile._lastName,lastName);
        contentValues.put(contractClass.UserProfile._bio,bio);
        contentValues.put(contractClass.UserProfile._dob,dob);
        contentValues.put(contractClass.UserProfile._gender,gender);
        contentValues.put(contractClass.UserProfile._isOnline,isOnline);
        contentValues.put(contractClass.UserProfile._phoneNumber,phoneNumber);
        contentValues.put(contractClass.UserProfile._profilePicture,profilePicture);


        db.update(contractClass.UserProfile.tableName, contentValues, contractClass.UserProfile._ID + "=" + id, null);
        db.close();
        return true;
    }


    public userProfile getUserProfile(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + contractClass.UserProfile.tableName + " WHERE " + contractClass.UserProfile._user + " = " + id;
        @SuppressLint("Recycle") Cursor c = db.rawQuery(sql, null);

        userProfile userProfile = new userProfile();

        if (c.moveToFirst()) {
            do {
                userProfile.setFirstName(c.getString(c.getColumnIndex(contractClass.UserProfile._firstName)));
                userProfile.setLastName(c.getString(c.getColumnIndex(contractClass.UserProfile._lastName)));
                userProfile.setGender(c.getString(c.getColumnIndex(contractClass.UserProfile._gender)));
                userProfile.setBio(c.getString(c.getColumnIndex(contractClass.UserProfile._bio)));
                userProfile.setDob(c.getString(c.getColumnIndex(contractClass.UserProfile._dob)));
                userProfile.setId(c.getString(c.getColumnIndex(contractClass.UserProfile._ID)));
                userProfile.setIsOnline(c.getString(c.getColumnIndex(contractClass.UserProfile._isOnline)));
                userProfile.setPhoneNumber(c.getString(c.getColumnIndex(contractClass.UserProfile._phoneNumber)));
                userProfile.setProfilePicture(c.getString(c.getColumnIndex(contractClass.UserProfile._profilePicture)));
            } while (c.moveToNext());
        }

        return userProfile;
    }


    public Cursor getAllUserProfiles() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + contractClass.UserProfile.tableName;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public void syncUserProfiles(@Nullable final Context context) {
        final String url = "http://192.168.43.173/bistro_chat/get_all_profiles.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url,

        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject res=new JSONObject(response);

                    if (res.getString("response").equals("200")) {
                        JSONArray userProfiles = res.getJSONArray("userProfiles");
                        for (int i=0;i<userProfiles.length();i++) {
                            JSONObject userProfileJsonObject = userProfiles.getJSONObject(i);
                            final userProfile userProfileData = new userProfile();
                            userProfileData.setFirstName(userProfileJsonObject.getString("firstName"));
                            userProfileData.setLastName(userProfileJsonObject.getString("lastName"));
                            userProfileData.setDob(userProfileJsonObject.getString("dob"));
                            userProfileData.setIsOnline(userProfileJsonObject.getString("isOnline"));
                            userProfileData.setGender(userProfileJsonObject.getString("gender"));
                            userProfileData.setBio(userProfileJsonObject.getString("bio"));
                            userProfileData.setId(userProfileJsonObject.getString("user"));
                            userProfileData.setPhoneNumber(userProfileJsonObject.getString("phoneNumber"));
                            userProfileData.setProfilePicture(userProfileJsonObject.getString("profilePicture"));

                            if (!checkProfileExists(Integer.parseInt(userProfileJsonObject.getString("id")))) {
                                addUserProfile(userProfileData.getFirstName(),userProfileData.getLastName(),
                                        userProfileData.getBio(),userProfileData.getDob(),userProfileData.getGender(),
                                        userProfileData.getIsOnline(),userProfileData.getPhoneNumber(),userProfileData.getProfilePicture(),
                                        Integer.parseInt(userProfileData.getId()),1);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
        new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            } //Create an error listener to handle errors appropriately.
        });
        Volley.newRequestQueue(context).add(MyStringRequest);
    }

    public Cursor getUnsyncedUserProfiles() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + contractClass.UserProfile.tableName + " WHERE " + contractClass.UserProfile._status + " = 0;";
        Cursor c = db.rawQuery(sql, null);

        return c;
    }


    public boolean checkProfileExists(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + contractClass.UserProfile.tableName + " WHERE " + contractClass.UserProfile._user + "=" + id;
        Cursor c = db.rawQuery(sql, null);

        boolean profileExists = false;
        if (c.getCount() > 0) {
            profileExists = true;
        }
        return profileExists;
    }



    public boolean sendMessage (String senderId, String receiverId, String chatId,
                                String message, Date date, String isLast, String isSeen, String isFav) {

        SQLiteDatabase db = this.getWritableDatabase();

        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        date = new Date();
        ContentValues contentValues = new ContentValues();

        contentValues.put(contractClass.UserChat._senderId,senderId);
        contentValues.put(contractClass.UserChat._receiverId,receiverId);
        contentValues.put(contractClass.UserChat._chatId,chatId);
        contentValues.put(contractClass.UserChat._message,message);
        contentValues.put(contractClass.UserChat._date,dateFormat.format(date));
        contentValues.put(contractClass.UserChat._isLast,isLast);
        contentValues.put(contractClass.UserChat._isFav,isFav);
        contentValues.put(contractClass.UserChat._isSeen,isSeen);

        db.insert(contractClass.UserChat.tableName,null,contentValues);
        db.close();
        return true;
    }

    public boolean updateMessageStatus (int id, int status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(contractClass.UserChat._status, status);
        db.update(contractClass.UserChat.tableName, contentValues, contractClass.UserChat._ID + "=" + id, null);
        db.close();
        return true;
    }


    public Cursor getUnsyncedMessages() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + contractClass.UserChat.tableName + " WHERE " + contractClass.UserChat._status + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }
}