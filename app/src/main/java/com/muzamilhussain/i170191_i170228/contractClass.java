package com.muzamilhussain.i170191_i170228;

import android.provider.BaseColumns;

import java.sql.Date;
import java.sql.Time;

public class contractClass {
    private contractClass(){}
    public static int DB_VERSION=1;
    public static String DB_NAME="bistro_chat.db";

    public static class User implements BaseColumns {
        public static String tableName="users";
        public static String _email="email";
        public static String _password="password";
    }

    public static class UserProfile implements BaseColumns {
        public static String tableName="user_profiles";
        public static String _ID = "id";
        public static String _firstName="firstName";
        public static String _lastName="lastName";
        public static String _dob="dob";
        public static String _gender="gender";
        public static String _isOnline="isOnline";
        public static String _phoneNumber="phoneNumber";
        public static String _bio="bio";
        public static String _user="user";
        public static String _profilePicture="profilePicture";
        public static String _status="status";

    }

    public static class UserChat implements BaseColumns {
        public static String tableName="user_chats";
        public static String _ID = "id";
        public static String _chatId="chatId";
        public static String _senderId="senderId";
        public static String _receiverId="receiverId";
        public static String _message="message";
        public static String _date= "date";
        public static String _isSeen="isSeen";
        public static String _isLast="isLast";
        public static String _isFav="isFav";
        public static String _status="status";
    }
}
