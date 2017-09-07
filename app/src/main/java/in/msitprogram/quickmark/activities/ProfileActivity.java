package in.msitprogram.quickmark.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;
import in.msitprogram.quickmark.utils.VolleyTask;

/**
 * Created by amareshjana on 19/05/17.
 */

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private ImageView userImg;
    private AppCompatEditText inputFullName;
    private AppCompatEditText inputRollNumber;
    private AppCompatEditText inputEmail;
    private AppCompatEditText inputMobile;
    private AppCompatEditText inputParentName;
    private AppCompatEditText inputParentMobileNumber;
    private AppCompatEditText inputParentEmail;
    private Button mProfieUpdateBtn;
    private TextView mEditprofile;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        //setting toolbar
        setToolbar("Profile");
        //finding the view in the layout
        findViews();
        //setting the Profile data
        setProfileData();
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        userImg = (ImageView) findViewById(R.id.user_img);
        inputFullName = (AppCompatEditText) findViewById(R.id.input_full_name);
        inputRollNumber = (AppCompatEditText) findViewById(R.id.input_roll_number);
        inputEmail = (AppCompatEditText) findViewById(R.id.input_email);
        inputMobile = (AppCompatEditText) findViewById(R.id.input_mobile);
        inputParentName = (AppCompatEditText) findViewById(R.id.input_parent_name);
        inputParentMobileNumber = (AppCompatEditText) findViewById(R.id.input_parent_mobile_number);
        inputParentEmail = (AppCompatEditText) findViewById(R.id.input_patent_email);
        mProfieUpdateBtn = (Button) findViewById(R.id.update_profile_btn);
        mEditprofile = (TextView) findViewById(R.id.edit_profile);

        //method in base activity to close keypad
        closeKeypad();

        userImg.setOnClickListener(this);
        mProfieUpdateBtn.setOnClickListener(this);
        mEditprofile.setOnClickListener(this);
    }

    /*
    * setting the profile data from session Manager
    * which is stored while login is successful
    * */
    private void setProfileData() {
        setData(inputFullName, mSessionManager.getFullName());
        inputFullName.setEnabled(FALSE);
        setData(inputRollNumber, mSessionManager.getRollNumber());
        inputRollNumber.setEnabled(FALSE);
        setData(inputEmail, mSessionManager.getEmail());
        inputEmail.setEnabled(FALSE);
        setData(inputMobile, mSessionManager.getMobileNo());
        inputMobile.setEnabled(FALSE);
//        makeToast(mSessionManager.getUserType(),LONG);
        if (mSessionManager.getUserType().equals("1")){
            inputParentName.setVisibility(View.GONE);
            inputParentMobileNumber.setVisibility(View.GONE);
            inputParentEmail.setVisibility(View.GONE);
        }
        setData(inputParentName, mSessionManager.getParentName());
        inputParentName.setEnabled(FALSE);
        setData(inputParentMobileNumber, mSessionManager.getParentMobileNo());
        inputParentMobileNumber.setEnabled(FALSE);
        setData(inputParentEmail, mSessionManager.getParentEmail());
        inputParentEmail.setEnabled(FALSE);

        //if image is not available
        if (!mSessionManager.getUserImg().equals("") && mSessionManager.getUserImg() != null) {
            Picasso.with(ProfileActivity.this).load(Constants.IMAGE_BASE_URL + mSessionManager.getUserImg()).
                    fit().centerCrop().into(userImg);
        }
        userImg.setClickable(false);
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == userImg) {

            AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
            builder.setItems(new String[]{"Gallery", "Camera"}, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(i, 0);
                    } else if (which == 1) {
                        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, 1);
                    }
                }
            });
            builder.show();
        } else if (v == mProfieUpdateBtn) {
            //calling the api for updating the profile
            updateProfileApi();
        } else if (v == mEditprofile) {
            //used to make the edit text fields editable
            editProfile();
        }
    }

    /*
    * calling update profile api
    * @params to send to the api are
    * @param1 guid
    * @param2 full_name
    * @param3 mobile
    * @param4 email
    * @param5 user_img
    * user can only edit these details in his/her profile
    * */
    private void updateProfileApi() {
        //getting the user image in base64
        Bitmap bitmap = ((BitmapDrawable) userImg.getDrawable()).getBitmap();
        String profilePicBase64 = encodeToBase64(bitmap);

        //sending the data as params using hashMap
        HashMap<String, String> mParams = new HashMap<>();
        mParams.put("guid", mSessionManager.getGUID());
        mParams.put("full_name", getData(inputFullName));
        mParams.put("mobile", getData(inputMobile));
        mParams.put("email", getData(inputEmail));
        mParams.put("user_img", profilePicBase64);

        new VolleyTask(ProfileActivity.this, Constants.UPDATE_PROFILE_URL, mParams, "Please Wait...") {
            @Override
            protected void handleError(VolleyError error) {

            }

            @Override
            protected void handleResponse(String response) {
                try {
                    JSONObject mMainObject = new JSONObject(response);
                    if (mMainObject.getString("result").equals("success")) {
                        //setting the data in session manager
                        mSessionManager.saveFullName(getData(inputFullName));
                        mSessionManager.saveMobileNo(getData(inputMobile));
                        mSessionManager.saveEmail(getData(inputEmail));
                        String user_image = mMainObject.getJSONObject("data").getString("user_img");
                        mSessionManager.saveUserImg(user_image);

                        //setting the data from the session
                        setProfileData();
                        makeToast("Profile Updated Successfully", SHORT);
                    } else {
                        setProfileData();
                        makeToast("Profile Updated Unsuccessfully Please try after some time...", SHORT);
                    }
                } catch (JSONException e) {

                } catch (Exception e) {

                }

            }
        };
    }

    /*
    * used to edit the fields of the profile
    * */
    private void editProfile() {
        inputFullName.setEnabled(TRUE);
        inputEmail.setEnabled(TRUE);
        inputMobile.setEnabled(TRUE);
        //setting update icon visible
        mProfieUpdateBtn.setVisibility(View.VISIBLE);
        userImg.setClickable(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 0 && data != null) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            //scalling the bitmap to the center
            Bitmap bitmap = scaleCenterCrop(BitmapFactory.decodeFile(picturePath),600,600);
            userImg.setImageBitmap(bitmap);
        } else if (resultCode == RESULT_OK && requestCode == 1 && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            userImg.setImageBitmap(photo);
        }
    }

    /**
     * used to change the image file into string which is base64 format
     * */
    private String encodeToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
    /**
     * this is to make the image round shaped which will be used for good look
     * */
    public Bitmap scaleCenterCrop(Bitmap source, int newHeight, int newWidth) {
        int sourceWidth = source.getWidth();
        int sourceHeight = source.getHeight();

        // Compute the scaling factors to fit the new height and width, respectively.
        // To cover the final image, the final scaling will be the bigger
        // of these two.
        float xScale = (float) newWidth / sourceWidth;
        float yScale = (float) newHeight / sourceHeight;
        float scale = Math.max(xScale, yScale);

        // Now get the size of the source bitmap when scaled
        float scaledWidth = scale * sourceWidth;
        float scaledHeight = scale * sourceHeight;

        // Let's find out the upper left coordinates if the scaled bitmap
        // should be centered in the new size give by the parameters
        float left = (newWidth - scaledWidth) / 2;
        float top = (newHeight - scaledHeight) / 2;

        // The target rectangle for the new, scaled version of the source bitmap will now
        // be
        RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);

        // Finally, we create a new bitmap of the specified size and draw our new,
        // scaled bitmap onto it.
        Bitmap dest = Bitmap.createBitmap(newWidth, newHeight, source.getConfig());
        Canvas canvas = new Canvas(dest);
        canvas.drawBitmap(source, null, targetRect, null);

        return dest;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(ProfileActivity.this)) {
            showNoNetworkDialogue(ProfileActivity.this);
        }
    }
}
