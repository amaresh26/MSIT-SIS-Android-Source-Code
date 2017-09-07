package in.msitprogram.quickmark.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by amareshjana on 18/05/17.
 */

public class SessionManager {

    private String AAP_SHARED_PREF_NAME = "in.apptozee.quickcomm";
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mSharedPreferenceEditor;
    private static final String FULL_NAME = "full_name";
    private static final String USER_ID = "userId";
    private static final String PASSWORD = "password";
    private static final String GUID = "guid";
    private static final String FCMID = "fcm_id";
    private static final String EMAIL = "email";
    private static final String USER_IMG = "user_img";
    private static final String ROLL_NUMBER = "roll_number";
    private static final String MOBILE = "mobile";
    private static final String PARENT_NAME = "parent_name";
    private static final String PARENT_MOBILE = "parent_mobile";
    private static final String PARENT_EMAIL = "parent_email";
    private static final String USER_TYPE_ID = "user_type_id";


    public SessionManager(Context context) {
        this.mSharedPreferences = context.getSharedPreferences(AAP_SHARED_PREF_NAME,
                Activity.MODE_PRIVATE);
        this.mSharedPreferenceEditor = mSharedPreferences.edit();
    }

    /*
    * clearing the shared preference
    * */
    public void clearPreference(){
        mSharedPreferenceEditor.clear().commit();
    }

    /*
    * GUID
    * */
    public void saveGUID(String guid) {
        mSharedPreferenceEditor.putString(GUID, guid);
        mSharedPreferenceEditor.commit();
    }

    public String getGUID(){
        return mSharedPreferences.getString(GUID,"");
    }

    /*
    * Full Name
    * */
    public void saveFullName(String username) {
        mSharedPreferenceEditor.putString(FULL_NAME, username);
        mSharedPreferenceEditor.commit();
    }

    public String getFullName() {
        return mSharedPreferences.getString(FULL_NAME, "");
    }


    /*
    * User id
    * */
    public String getUserId() {
        return mSharedPreferences.getString(USER_ID, "");
    }

    public void saveUserId(String username) {
        mSharedPreferenceEditor.putString(USER_ID, username);
        mSharedPreferenceEditor.commit();
    }

    /*
     * Roll Number
     * */
    public void saveRollNumber(String rollNumber) {
        mSharedPreferenceEditor.putString(ROLL_NUMBER, rollNumber);
        mSharedPreferenceEditor.commit();
    }

    public String getRollNumber() {
        return mSharedPreferences.getString(ROLL_NUMBER, "");
    }

    /*
    * Email
    * */
    public void saveEmail(String Email) {
        mSharedPreferenceEditor.putString(EMAIL, Email);
        mSharedPreferenceEditor.commit();
    }

    public String getEmail() {
        return mSharedPreferences.getString(EMAIL, "");
    }

    /*
    * Mobile Number
    * */
    public void saveMobileNo(String mMobile) {
        mSharedPreferenceEditor.putString(MOBILE, mMobile);
        mSharedPreferenceEditor.commit();
    }

    public String getMobileNo() {
        return mSharedPreferences.getString(MOBILE, "");
    }

    /*
    * Parent Name
    * */
    public void saveParentName(String parentName) {
        mSharedPreferenceEditor.putString(PARENT_NAME, parentName);
        mSharedPreferenceEditor.commit();
    }

    public String getParentName() {
        return mSharedPreferences.getString(PARENT_NAME, "");
    }

    /*
    * Parent Mobile Number
    * */
    public void saveParentMobileNo(String parentMobileNo) {
        mSharedPreferenceEditor.putString(PARENT_MOBILE, parentMobileNo);
        mSharedPreferenceEditor.commit();
    }

    public String getParentMobileNo() {
        return mSharedPreferences.getString(PARENT_MOBILE, "");
    }

    /*
    * Parent Email
    * */
    public void saveParentEmail(String parentEmail) {
        mSharedPreferenceEditor.putString(PARENT_EMAIL, parentEmail);
        mSharedPreferenceEditor.commit();
    }

    public String getParentEmail() {
        return mSharedPreferences.getString(PARENT_EMAIL, "");
    }

    /*
    * User Image
    * */
    public void saveUserImg(String userImg) {
        mSharedPreferenceEditor.putString(USER_IMG, userImg);
        mSharedPreferenceEditor.commit();
    }

    public String getUserImg() {
        return mSharedPreferences.getString(USER_IMG, "");
    }

    /*
    * User type Id
    * */

    public void saveUserType(String userType){
        mSharedPreferenceEditor.putString(USER_TYPE_ID, userType);
        mSharedPreferenceEditor.commit();
    }

    public String getUserType() {
        return mSharedPreferences.getString(USER_TYPE_ID, "");
    }

    /*
    * FCM ID
    * */

    public void saveFcmId(String fcmId){
        mSharedPreferenceEditor.putString(FCMID, fcmId);
        mSharedPreferenceEditor.commit();
    }

    public String getFcmId() {
        return mSharedPreferences.getString(FCMID, "");
    }

}
