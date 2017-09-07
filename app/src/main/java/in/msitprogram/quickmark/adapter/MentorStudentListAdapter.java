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

import java.util.ArrayList;
import java.util.List;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.models.StudentParentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;

/**
 * Created by amareshjana on 05/06/17.
 */

public class MentorStudentListAdapter extends BaseAdapter implements Filterable{

    private ArrayList<StudentParentDetailsModel> mStudentList;
    private ArrayList<StudentParentDetailsModel> mStudentListFiltered;
    private Context mContext;

    public MentorStudentListAdapter(Context mContext, ArrayList<StudentParentDetailsModel> mStudentList) {
        this.mStudentList = mStudentList;
        this.mStudentListFiltered = mStudentList;
        this.mContext = mContext;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mStudentListFiltered.size();
    }

    /**
     * Get the data item associated with the specified position in the data set.
     *
     * @param position Position of the item whose data we want within the adapter's
     *                 data set.
     * @return The data at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return mStudentListFiltered.get(position);
    }

    /**
     * Get the row id associated with the specified position in the list.
     *
     * @param position The position of the item within the adapter's data set whose row id we want.
     * @return The id of the item at the specified position.
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Get a View that displays the data at the specified position in the data set. You can either
     * create a View manually or inflate it from an XML layout file. When the View is inflated, the
     * parent View (GridView, ListView...) will apply default layout parameters unless you use
     * {@link LayoutInflater#inflate(int, ViewGroup, boolean)}
     * to specify a root view and to prevent attachment to the root.
     *
     * @param i           The position of the item within the adapter's data set of the item whose view
     *                    we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *                    is non-null and of an appropriate type before using. If it is not possible to convert
     *                    this view to display the correct data, this method can create a new view.
     *                    Heterogeneous lists can specify their number of view types, so that this View is
     *                    always of the right type (see {@link #getViewTypeCount()} and
     *                    {@link #getItemViewType(int)}).
     * @param parent      The parent that this view will eventually be attached to
     * @return A View corresponding to the data at the specified position.
     */
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mentor_student_details, parent, false);
            holder = new ViewHolder();
            holder.tvStudentFullName = (TextView) convertView.findViewById(R.id.student_full_name_list);
            holder.tvRollNumber = (TextView) convertView.findViewById(R.id.student_roll_no_list);
            holder.tvStudentMobileNo = (TextView) convertView.findViewById(R.id.student_mobile_no);
            holder.tvStudentEmail = (TextView) convertView.findViewById(R.id.student_email);
            holder.tvParentName = (TextView) convertView.findViewById(R.id.parent_name);
            holder.tvParentEmail = (TextView) convertView.findViewById(R.id.parent_email);
            holder.tvParentMobile = (TextView) convertView.findViewById(R.id.parent_mobile);
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
        holder.tvParentName.setText(mStudentListFiltered.get(i).getParentName());
        holder.tvParentEmail.setAutoLinkMask(Linkify.EMAIL_ADDRESSES);
        holder.tvParentEmail.setLinksClickable(true);
        holder.tvParentEmail.setText(mStudentListFiltered.get(i).getParentEmail());
        holder.tvParentMobile.setAutoLinkMask(Linkify.PHONE_NUMBERS);
        holder.tvParentMobile.setLinksClickable(true);
        holder.tvParentMobile.setText(mStudentListFiltered.get(i).getParentMobile());
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
                    List<StudentParentDetailsModel> resultsData = new ArrayList<>();
                    String searchStr = constraint.toString().toUpperCase();
                    for (StudentParentDetailsModel student : mStudentList)
                        if (student.getFullName().toUpperCase().contains(searchStr))
                            resultsData.add(student);
                    results.count = resultsData.size();
                    results.values = resultsData;
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                mStudentListFiltered = (ArrayList<StudentParentDetailsModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    private class ViewHolder {
        TextView tvStudentFullName, tvRollNumber, tvStudentMobileNo, tvStudentEmail, tvParentName, tvParentEmail, tvParentMobile;
        ImageView ivStudentImage;
    }
}
