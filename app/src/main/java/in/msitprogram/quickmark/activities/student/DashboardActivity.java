package in.msitprogram.quickmark.activities.student;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.activities.BaseActivity;
import in.msitprogram.quickmark.activities.ChangePasswordActivity;
import in.msitprogram.quickmark.activities.CourseListActivity;
import in.msitprogram.quickmark.activities.LoginActivity;
import in.msitprogram.quickmark.activities.ProfileActivity;
import in.msitprogram.quickmark.activities.mentor.MentorDetailsActivity;
import in.msitprogram.quickmark.utils.Constants;
import in.msitprogram.quickmark.utils.Networking;


public class DashboardActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private LinearLayout btnDashboardAttendance;
    private LinearLayout btnDashboardEvents;
    private LinearLayout btnDashboardMarks;
    private LinearLayout btnDashboardApplyLeave;
    private ImageView mNavUserImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Actionbar toggle button
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        //navigation menu
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        //getting the layout used in the navigation view
        LinearLayout lay = (LinearLayout) navigationView.getHeaderView(0);
        //setting the username for navigation view
        TextView navHeaderTitle = (TextView) lay.findViewById(R.id.username_nav);
        navHeaderTitle.setText(mSessionManager.getFullName());
        //setting the email for navigation view
        TextView navHeaderSubTitle = (TextView) lay.findViewById(R.id.user_email_nav);
        navHeaderSubTitle.setText(mSessionManager.getEmail());
        //setting the image to the nav
        mNavUserImage = (ImageView) lay.findViewById(R.id.user_img);
        if (!mSessionManager.getUserImg().equals("") && mSessionManager.getUserImg() != null) {
//            Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mSessionManager.getUserImg()).
//                    fit().centerCrop().into(mNavUserImage);
            Picasso.with(mContext).invalidate(Constants.IMAGE_BASE_URL + mSessionManager.getUserImg()+ "?time=" + System.currentTimeMillis());
            Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mSessionManager.getUserImg()+ "?time=" + System.currentTimeMillis()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(mNavUserImage);
        }
        //finding the views
        findViews();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_rate_us) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } else if (id == R.id.nav_about_us) {
            Uri uri = Uri.parse("http://www.msitprogram.net");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }else if (id == R.id.nav_logout) {
            //clear the session (shared preference)
            String gcm_id = mSessionManager.getFcmId();
            mSessionManager.clearPreference();
            mSessionManager.saveFcmId(gcm_id);
            nextIntent(LoginActivity.class, null, TRUE);
        } else if (id == R.id.nav_share) {
            final String appPackageName = getPackageName();
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                String sAux = "\nThis is an application of MSIT-JNTUH Student Information System\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=" + appPackageName;
                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "choose one"));
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (id == R.id.nav_change_password) {
            nextIntent(ChangePasswordActivity.class, null, FALSE);
        } else if (id == R.id.nav_friends_list) {
            nextIntent(StudentDetailsActivity.class, null, FALSE);
        } else if (id == R.id.nav_mentor_details) {
            nextIntent(MentorDetailsActivity.class, null, FALSE);
        } else if (id == R.id.nav_my_teams) {
            Intent mIntent = new Intent(DashboardActivity.this, CourseListActivity.class);
            mIntent.putExtra("NAV", "MY_TEAM");
            startActivity(mIntent);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        if (v == btnDashboardAttendance) {
            //opening attendance activity
            nextIntent(StudentAttendanceActivity.class, null, FALSE);
        } else if (v == btnDashboardEvents) {
            //seding to Event list SCREEN
            nextIntent(StudentEventListActivity.class, null, FALSE);
        } else if (v == btnDashboardMarks) {
            //sending to course activity
            nextIntent(CourseListActivity.class, null, FALSE);
        } else if (v == btnDashboardApplyLeave) {
            //opening apply leave activity
            nextIntent(StudentLeaveHistoryActivity.class, null, FALSE);
        } else if(v == mNavUserImage){
            nextIntent(ProfileActivity.class, null, FALSE);
        }
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {
        btnDashboardAttendance = (LinearLayout) findViewById(R.id.btn_dashboard_attendance);
        btnDashboardEvents = (LinearLayout) findViewById(R.id.btn_dashboard_events);
        btnDashboardMarks = (LinearLayout) findViewById(R.id.btn_dashboard_marks);
        btnDashboardApplyLeave = (LinearLayout) findViewById(R.id.btn_dashboard_apply_leave);

        //on click actions
        mNavUserImage.setOnClickListener(this);
        btnDashboardAttendance.setOnClickListener(this);
        btnDashboardEvents.setOnClickListener(this);
        btnDashboardMarks.setOnClickListener(this);
        btnDashboardApplyLeave.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Networking.isNetworkAvailable(DashboardActivity.this)){
            showNoNetworkDialogue(DashboardActivity.this);
        }
    }
}
