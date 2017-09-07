package in.msitprogram.quickmark.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.models.LeaveStatusModel;

/**
 * Created by amareshjana on 29/05/17.
 */

public class LeaveStatusAdapter extends BaseAdapter {

    private ArrayList<LeaveStatusModel> mLeaveHistoryList;
    private Context mContext;

    public LeaveStatusAdapter(ArrayList<LeaveStatusModel> mLeaveHistoryList, Context mContext) {
        this.mLeaveHistoryList = mLeaveHistoryList;
        this.mContext = mContext;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mLeaveHistoryList.size();
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
        return mLeaveHistoryList.get(position);
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
     * @param position    The position of the item within the adapter's data set of the item whose view
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_student_leave_history, parent, false);
            holder = new ViewHolder();
            holder.mReason = (TextView) convertView.findViewById(R.id.course_name);
            holder.mFromDate = (TextView) convertView.findViewById(R.id.leave_date);
            holder.mStatus = (TextView) convertView.findViewById(R.id.status);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mReason.setText(mLeaveHistoryList.get(position).getStudentName());
        holder.mFromDate.setText(mLeaveHistoryList.get(position).getmFromDate());
        if (mLeaveHistoryList.get(position).getmStatus().equals("1")) {
            holder.mStatus.setText("Pending");
            holder.mStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
        } else if (mLeaveHistoryList.get(position).getmStatus().equals("0")) {
            holder.mStatus.setText("Approved");
            holder.mStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        } else if (mLeaveHistoryList.get(position).getmStatus().equals("2")) {
            holder.mStatus.setText("Cancel");
            holder.mStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
        }
        return convertView;
    }

    private class ViewHolder {
        TextView mReason, mFromDate, mStatus;

    }
}
