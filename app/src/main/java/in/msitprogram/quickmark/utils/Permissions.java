package in.msitprogram.quickmark.utils;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

/**
 * Created by acer on 9/5/2016.
 */
public class Permissions {
    private Activity activity;
    private static final int ALLPERMISSION_CODE = 0;

    public Permissions(Activity activity) {
        this.activity = activity;
    }

    public void requestPermissionsAllPermissions(String[] permissionList) {
        ActivityCompat.requestPermissions(activity, permissionList, ALLPERMISSION_CODE);
    }

    public void showMessage(String permission){
        if(ActivityCompat.shouldShowRequestPermissionRationale(activity,permission)){
            Toast.makeText(activity,"Please enable "+permission+" in the settings to use this feature", Toast.LENGTH_SHORT).show();
        }
    }
}
