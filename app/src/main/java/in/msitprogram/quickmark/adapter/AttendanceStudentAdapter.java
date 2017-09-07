package in.msitprogram.quickmark.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import in.msitprogram.quickmark.R;
import in.msitprogram.quickmark.models.StudentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;

/**
 * Created by amareshjana on 29/05/17.
 */

public class AttendanceStudentAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<StudentDetailsModel> mStudentList;

    public AttendanceStudentAdapter(Context mContext, ArrayList<StudentDetailsModel> mStudentList) {
        this.mContext = mContext;
        this.mStudentList = mStudentList;
    }

    /**
     * How many items are in the data set represented by this Adapter.
     *
     * @return Count of items.
     */
    @Override
    public int getCount() {
        return mStudentList.size();
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
        return mStudentList.get(position);
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
        holder.tvStudentFullName.setText(mStudentList.get(position).getFullName());
        holder.tvRollNumber.setText(mStudentList.get(position).getRollNumber());
        Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mStudentList.get(position).getStudentImageUrl()).fit().centerCrop().into(holder.ivStudentImage);
        if (mStudentList.get(position).getStudentAttendance().equals("3")) {
            holder.mAttendance.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
            holder.mAttendance.setText("Present");
        } else if (mStudentList.get(position).getStudentAttendance().equals("2")) {
            holder.mAttendance.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
            holder.mAttendance.setText("Half Day");
        } else if (mStudentList.get(position).getStudentAttendance().equals("1")) {
            holder.mAttendance.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
            holder.mAttendance.setText("Late");
        } else if (mStudentList.get(position).getStudentAttendance().equals("0")) {
            holder.mAttendance.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            holder.mAttendance.setText("Absent");
        }
        return convertView;
    }

    // view holder to hold the values of a list item
    private class ViewHolder {
        private TextView tvStudentFullName, tvRollNumber;
        private ImageView ivStudentImage;
        private Button mAttendance;
    }


}