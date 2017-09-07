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
import in.msitprogram.quickmark.models.StudentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;

/**
 * Created by amareshjana on 08/09/16.
 */
public class StudentListAdapter extends BaseAdapter implements Filterable{

    private Context mContext;
    private ArrayList<StudentDetailsModel> mStudentList;
    private ArrayList<StudentDetailsModel> mStudentListFiltered;

    public StudentListAdapter(Context mContext, ArrayList<StudentDetailsModel> list) throws IOException {
        this.mContext = mContext;
        this.mStudentList = list;
        this.mStudentListFiltered = list;
    }

    @Override
    public int getCount() {
        return mStudentListFiltered.size();
    }

    @Override
    public Object getItem(int i) {
        return mStudentListFiltered.get(i);
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
        holder.tvStudentMobileNo.setText(mStudentListFiltered.get(i).getStudentMobileNo());
        holder.tvStudentEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        holder.tvStudentEmail.setLinksClickable(true);
        holder.tvStudentEmail.setText(mStudentListFiltered.get(i).getStudentEmail());
        holder.tvStudentFullName.setText(mStudentListFiltered.get(i).getFullName());
        holder.tvRollNumber.setText(mStudentListFiltered.get(i).getRollNumber());
        Picasso.with(mContext).invalidate(Constants.IMAGE_BASE_URL + mStudentListFiltered.get(i).getStudentImageUrl()+ "?time=" + System.currentTimeMillis());
        Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mStudentListFiltered.get(i).getStudentImageUrl()+ "?time=" + System.currentTimeMillis()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(holder.ivStudentImage);

        //Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mStudentList.get(i).getStudentImageUrl()).fit().centerCrop().into(holder.ivStudentImage);
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
                    results.count = mStudentList.size();
                    results.values = mStudentList;
                } else {//do the search
                    List<StudentDetailsModel> resultsData = new ArrayList<>();
                    String searchStr = constraint.toString().toUpperCase();
                    for (StudentDetailsModel student : mStudentList)
                        if (student.getFullName().toUpperCase().contains(searchStr))
                            resultsData.add(student);
                    results.count = resultsData.size();
                    results.values = resultsData;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mStudentListFiltered = (ArrayList<StudentDetailsModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        TextView tvStudentFullName, tvRollNumber, tvStudentMobileNo, tvStudentEmail;
        ImageView ivStudentImage;
    }
}