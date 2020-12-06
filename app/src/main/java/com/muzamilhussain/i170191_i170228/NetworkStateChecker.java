package com.muzamilhussain.i170191_i170228;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NetworkStateChecker extends BroadcastReceiver {

    //context and database helper object
    private Context context;
    private MyDBHelper db;

    @Override
    public void onReceive(Context context, Intent intent) {

        this.context = context;

        db = new MyDBHelper(context);

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        //if there is a network
        if (activeNetwork != null) {
            //if connected to wifi or mobile data plan
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {

                Cursor cursor = db.getUnsyncedUserProfiles();

                if (cursor.moveToFirst()) {
                    do {
                        //calling the method to save the unsynced profile to MySQL
                        saveUserProfiles(
                                cursor.getInt(cursor.getColumnIndex(contractClass.UserProfile._ID)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserProfile._firstName)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserProfile._lastName)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserProfile._bio)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserProfile._dob)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserProfile._gender)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserProfile._phoneNumber)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserProfile._isOnline)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserProfile._profilePicture)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserProfile._user))
                        );
                    } while (cursor.moveToNext());
                }

                cursor = db.getUnsyncedMessages();

                if (cursor.moveToFirst()) {
                    do {
                        saveMessages(
                                cursor.getInt(cursor.getColumnIndex(contractClass.UserChat._ID)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserChat._senderId)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserChat._receiverId)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserChat._chatId)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserChat._isFav)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserChat._isLast)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserChat._isSeen)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserChat._message)),
                                cursor.getString(cursor.getColumnIndex(contractClass.UserChat._date))
                        );
                    } while (cursor.moveToNext());
                }

            }
        }
    }

    /*
     * method taking two arguments
     * name that is to be saved and id of the name from SQLite
     * if the name is successfully sent
     * we will update the status as synced in SQLite
     * */
    private void saveUserProfiles(final int id, final String firstName, final String lastName,final String bio,
                                  final String dob, final String gender, final String phoneNumber,
                                  final String isOnline,final String profilePicture,final String user) {


        final String profileUrl = "http://192.168.43.173/bistro_chat/profile.php";
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, profileUrl,

                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject res=new JSONObject(response);

                            if (res.getString("response").equals("201")) {
                                db.updateUserProfileStatus(id, create_profile.PROFILE_SYNCED_WITH_SERVER);

                                //sending the broadcast to refresh the list
                                context.sendBroadcast(new Intent(create_profile.DATA_SAVED_BROADCAST));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.d("MyStringRequest",response);
                    }
                },
                new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //This code is executed if there is an error.
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> data = new HashMap<String, String>();
                data.put("firstName", firstName);
                data.put("lastName", lastName);
                data.put("dob", dob);
                data.put("gender", gender);
                data.put("bio", bio);
                data.put("userId", user);
                data.put("phoneNumber", phoneNumber);
                data.put("profilePicture",profilePicture);
                data.put("id",Integer.toString(id));
                return data;
            }
        };
        Volley.newRequestQueue(context).add(MyStringRequest);
    }

    private void saveMessages(final int id, final String senderId, final String receiverId
                            , final String chatId, final String isFav,
                              final String isLast, final String isSeen, final String message,
                              final String date) {


    }
}
