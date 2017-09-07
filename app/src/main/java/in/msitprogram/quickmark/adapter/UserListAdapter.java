package in.msitprogram.quickmark.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.models.UserModel;
import in.msitprogram.quickmark.utils.Constants;

/**
 * Created by amareshjana on 08/09/16.
 */
public class UserListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<UserModel> mStudentList;
    private Date holderDate;

    public UserListAdapter(Context mContext, ArrayList<UserModel> list) throws IOException {
        this.mContext = mContext;
        this.mStudentList = list;
    }

    @Override
    public int getCount() {
        return mStudentList.size();
    }

    @Override
    public Object getItem(int i) {
        return mStudentList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_student_list, parent, false);
            holder = new ViewHolder();
            holder.tvStudentFullName = (TextView) convertView.findViewById(R.id.student_full_name_list);
            holder.tvRollNumber = (TextView) convertView.findViewById(R.id.student_roll_no_list);
            holder.ivStudentImage = (ImageView) convertView.findViewById(R.id.student_image);
            holder.mAttendance = (Button) convertView.findViewById(R.id.student_attendance);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvStudentFullName.setText(mStudentList.get(i).getFullName());
        holder.tvRollNumber.setText(mStudentList.get(i).getRollNumber());
        Picasso.with(mContext).invalidate(Constants.IMAGE_BASE_URL + mStudentList.get(i).getUserImageUrl()+ "?time=" + System.currentTimeMillis());
        Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mStudentList.get(i).getUserImageUrl()+ "?time=" + System.currentTimeMillis()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(holder.ivStudentImage);
       // Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mStudentList.get(i).getUserImageUrl()).fit().centerCrop().into(holder.ivStudentImage);
        holder.mAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(mContext)
                        .setMessage("This feature will be available once user app is launched")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                                dialog.dismiss();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
        return convertView;
    }

    private class ViewHolder {
        TextView tvStudentFullName, tvRollNumber;
        ImageView ivStudentImage;
        Button mAttendance;
    }
}