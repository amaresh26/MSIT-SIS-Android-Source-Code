package in.msitprogram.quickmark.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.models.MentorDetailsModel;
import in.msitprogram.quickmark.utils.Constants;

/**
 * Created by amareshjana on 08/09/16.
 */
public class MentorListAdapter extends BaseAdapter implements Filterable{

    private Context mContext;
    private ArrayList<MentorDetailsModel> mMentorListFiltered;
    private ArrayList<MentorDetailsModel> mMentorList;

    public MentorListAdapter(Context mContext, ArrayList<MentorDetailsModel> list) throws IOException {
        this.mContext = mContext;
        this.mMentorList = list;
        this.mMentorListFiltered = list;
    }

    @Override
    public int getCount() {
        return mMentorListFiltered.size();
    }

    @Override
    public Object getItem(int i) {
        return mMentorListFiltered.get(i);
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
        holder.tvMentorMobileNo.setText(mMentorListFiltered.get(i).getMentorMobileNo());
        holder.tvMentorEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        holder.tvMentorEmail.setLinksClickable(true);
        holder.tvMentorEmail.setText(mMentorListFiltered.get(i).getMentorEmail());
        holder.tvMentorFullName.setText(mMentorListFiltered.get(i).getFullName());

        Picasso.with(mContext).invalidate(Constants.IMAGE_BASE_URL + mMentorListFiltered.get(i).getMentorImageUrl()+ "?time=" + System.currentTimeMillis());
        Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mMentorListFiltered.get(i).getMentorImageUrl()+ "?time=" + System.currentTimeMillis()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(holder.ivMentorImage);
        //Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mMentorList.get(i).getMentorImageUrl()).fit().centerCrop().into(holder.ivMentorImage);
        if (mMentorList.get(i).getMentorTypeId().equals("3"))
            holder.tvMentorType.setText("Non-Teaching Staff");
        else
            holder.tvMentorType.setText(" ");
        return convertView;
    }

    /**
     * <p>Returns a filter that can be used to constrain data with a filtering
     * pattern.</p>
     * <p>
     * <p>This method is usually implemented by {@link Adapter}
     * classes.</p>
     *
     * @return a filter used to constrain data
     */
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                if (constraint == null || constraint.length() == 0) {
                    //no constraint given, just return all the data. (no search)
                    results.count = mMentorList.size();
                    results.values = mMentorList;
                } else {//do the search
                    List<MentorDetailsModel> resultsData = new ArrayList<>();
                    String searchStr = constraint.toString().toUpperCase();
                    for (MentorDetailsModel student : mMentorList)
                        if (student.getFullName().toUpperCase().contains(searchStr))
                            resultsData.add(student);
                    results.count = resultsData.size();
                    results.values = resultsData;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mMentorListFiltered = (ArrayList<MentorDetailsModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        TextView tvMentorFullName, tvMentorMobileNo, tvMentorEmail,tvMentorType;
        ImageView ivMentorImage;
    }
}