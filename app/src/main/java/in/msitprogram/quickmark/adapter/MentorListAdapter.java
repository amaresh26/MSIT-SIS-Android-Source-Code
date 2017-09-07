package in.msitprogram.quickmark.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.models.MentorDetailsModel;
import in.msitprogram.quickmark.utils.Constants;

/**
 * Created by amareshjana on 08/09/16.
 */
public class MentorListAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<MentorDetailsModel> mMentorList;
    private Date holderDate;

    public MentorListAdapter(Context mContext, ArrayList<MentorDetailsModel> list) throws IOException {
        this.mContext = mContext;
        this.mMentorList = list;
    }

    @Override
    public int getCount() {
        return mMentorList.size();
    }

    @Override
    public Object getItem(int i) {
        return mMentorList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mentor_details, parent, false);
            holder = new ViewHolder();
            holder.tvMentorFullName = (TextView) convertView.findViewById(R.id.mentor_full_name_list);
            holder.tvMentorMobileNo = (TextView) convertView.findViewById(R.id.mentor_mobile_no);
            Linkify.addLinks(holder.tvMentorMobileNo, Linkify.PHONE_NUMBERS);
            holder.tvMentorEmail = (TextView) convertView.findViewById(R.id.mentor_email);
            Linkify.addLinks(holder.tvMentorEmail, Linkify.EMAIL_ADDRESSES);
            holder.ivMentorImage = (ImageView) convertView.findViewById(R.id.mentor_image);
            holder.tvMentorType = (TextView) convertView.findViewById(R.id.mentor_type);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvMentorMobileNo.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        holder.tvMentorMobileNo.setLinksClickable(true);
        holder.tvMentorMobileNo.setText(mMentorList.get(i).getMentorMobileNo());
        holder.tvMentorEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        holder.tvMentorEmail.setLinksClickable(true);
        holder.tvMentorEmail.setText(mMentorList.get(i).getMentorEmail());
        holder.tvMentorFullName.setText(mMentorList.get(i).getFullName());
        Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mMentorList.get(i).getMentorImageUrl()).fit().centerCrop().into(holder.ivMentorImage);
        if (mMentorList.get(i).getMentorTypeId().equals("3"))
            holder.tvMentorType.setText("Non-Teaching Staff");
        else
            holder.tvMentorType.setText(" ");
        return convertView;
    }

    private class ViewHolder {
        TextView tvMentorFullName, tvMentorMobileNo, tvMentorEmail,tvMentorType;
        ImageView ivMentorImage;
    }
}