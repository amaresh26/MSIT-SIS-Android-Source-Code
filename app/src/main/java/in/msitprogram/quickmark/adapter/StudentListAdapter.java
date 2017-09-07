package in.msitprogram.quickmark.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.models.StudentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;

/**
 * Created by amareshjana on 08/09/16.
 */
public class StudentListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<StudentDetailsModel> mStudentList;

    public StudentListAdapter(Context mContext, ArrayList<StudentDetailsModel> list) throws IOException {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_student_details, parent, false);
            holder = new ViewHolder();
            holder.tvStudentFullName = (TextView) convertView.findViewById(R.id.student_full_name_list);
            holder.tvRollNumber = (TextView) convertView.findViewById(R.id.student_roll_no_list);
            holder.tvStudentMobileNo = (TextView) convertView.findViewById(R.id.student_mobile_no);
            Linkify.addLinks(holder.tvStudentMobileNo, Linkify.PHONE_NUMBERS);
            holder.tvStudentEmail = (TextView) convertView.findViewById(R.id.student_email);
            Linkify.addLinks(holder.tvStudentEmail, Linkify.EMAIL_ADDRESSES);
            holder.ivStudentImage = (ImageView) convertView.findViewById(R.id.student_image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvStudentMobileNo.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        holder.tvStudentMobileNo.setLinksClickable(true);
        holder.tvStudentMobileNo.setText(mStudentList.get(i).getStudentMobileNo());
        holder.tvStudentEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        holder.tvStudentEmail.setLinksClickable(true);
        holder.tvStudentEmail.setText(mStudentList.get(i).getStudentEmail());
        holder.tvStudentFullName.setText(mStudentList.get(i).getFullName());
        holder.tvRollNumber.setText(mStudentList.get(i).getRollNumber());
        Picasso.with(mContext).invalidate(Constants.IMAGE_BASE_URL + mStudentList.get(i).getStudentImageUrl()+ "?time=" + System.currentTimeMillis());
        Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mStudentList.get(i).getStudentImageUrl()+ "?time=" + System.currentTimeMillis()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(holder.ivStudentImage);

        //Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mStudentList.get(i).getStudentImageUrl()).fit().centerCrop().into(holder.ivStudentImage);
        return convertView;
    }

    private class ViewHolder {
        TextView tvStudentFullName, tvRollNumber, tvStudentMobileNo, tvStudentEmail;
        ImageView ivStudentImage;
    }
}