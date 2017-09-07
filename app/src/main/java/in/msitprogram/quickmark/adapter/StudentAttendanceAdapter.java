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
import in.msitprogram.quickmark.models.StudentAttendanceModel;

/**
 * Created by amareshjana on 31/05/17.
 */

public class StudentAttendanceAdapter extends BaseAdapter {

    private ArrayList<StudentAttendanceModel> mStudentAttendance;
    private Context mContext;

    public StudentAttendanceAdapter(ArrayList<StudentAttendanceModel> mStudentAttendance, Context mContext) {
        this.mStudentAttendance = mStudentAttendance;
        this.mContext = mContext;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mStudentAttendance.size();
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
        return mStudentAttendance.get(position);
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
        holder.mReason.setText(mStudentAttendance.get(position).getCourseName());
        holder.mFromDate.setText(mStudentAttendance.get(position).getTakenLeaveOn());
        if (mStudentAttendance.get(position).getAttendanceTypeName().equals("Absent")) {
            holder.mStatus.setText("Absent");
            holder.mStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
        } else if (mStudentAttendance.get(position).getAttendanceTypeName().equals("Late")) {
            holder.mStatus.setText("Late");
            holder.mStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
        } else if (mStudentAttendance.get(position).getAttendanceTypeName().equals("Half Day")) {
            holder.mStatus.setText("Half Day");
            holder.mStatus.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
        }

        return convertView;
    }

    private class ViewHolder {
        TextView mReason, mFromDate, mStatus;

    }
}
