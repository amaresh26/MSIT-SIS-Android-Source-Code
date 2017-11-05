package in.msitprogram.quickmark.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
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
import in.msitprogram.quickmark.models.StudentDetailsModel;
import in.msitprogram.quickmark.utils.Constants;

/**
 * Created by amareshjana on 29/05/17.
 */

public class AttendanceStudentAdapter extends BaseAdapter implements Filterable{

    private Context mContext;
    private ArrayList<StudentDetailsModel> mStudentList;
    private ArrayList<StudentDetailsModel> mStudentListFiltered;

    public AttendanceStudentAdapter(Context mContext, ArrayList<StudentDetailsModel> mStudentList) {
        this.mContext = mContext;
        this.mStudentList = mStudentList;
        this.mStudentListFiltered = mStudentList;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(mContext);
                builderSingle.setTitle("Select One Option");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(mContext, android.R.layout.select_dialog_item);
                arrayAdapter.add("Absent");
                arrayAdapter.add("Half Day");
                arrayAdapter.add("Late");
                //arrayAdapter.add("Present");

                builderSingle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (strName.equals("Absent")) {
                            mStudentListFiltered.get(position).setStudentAttendance("0");
                        } else if (strName.equals("Half Day")) {
                            mStudentListFiltered.get(position).setStudentAttendance("2");
                        } else if (strName.equals("Late")) {
                            mStudentListFiltered.get(position).setStudentAttendance("1");
                        } else if (strName.equals("Present")) {
                            mStudentListFiltered.get(position).setStudentAttendance("3");
                        }
                        notifyDataSetChanged();
                    }
                });
                builderSingle.show();
            }
        });

        holder.tvStudentFullName.setText(mStudentListFiltered.get(position).getFullName());
        holder.tvRollNumber.setText(mStudentListFiltered.get(position).getRollNumber());
//        Picasso.with(mContext).invalidate(Constants.IMAGE_BASE_URL + mStudentListFiltered.get(position).getStudentImageUrl()+ "?time=" + System.currentTimeMillis());
        Picasso.with(mContext).load(Constants.IMAGE_BASE_URL + mStudentListFiltered.get(position).getStudentImageUrl()+ "?time=" + System.currentTimeMillis()).networkPolicy(NetworkPolicy.NO_CACHE).memoryPolicy(MemoryPolicy.NO_CACHE).fit().centerCrop().into(holder.ivStudentImage);
        if (mStudentListFiltered.get(position).getStudentAttendance().equals("3")) {
            holder.mAttendance.setBackgroundColor(ContextCompat.getColor(mContext, R.color.green));
            holder.mAttendance.setText("Present");
        } else if (mStudentListFiltered.get(position).getStudentAttendance().equals("2")) {
            holder.mAttendance.setBackgroundColor(ContextCompat.getColor(mContext, R.color.gray));
            holder.mAttendance.setText("Half Day");
        } else if (mStudentListFiltered.get(position).getStudentAttendance().equals("1")) {
            holder.mAttendance.setBackgroundColor(ContextCompat.getColor(mContext, R.color.orange));
            holder.mAttendance.setText("Late");
        } else if (mStudentListFiltered.get(position).getStudentAttendance().equals("0")) {
            holder.mAttendance.setBackgroundColor(ContextCompat.getColor(mContext, R.color.red));
            holder.mAttendance.setText("Absent");
        }
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

    // view holder to hold the values of a list item
    private class ViewHolder {
        private TextView tvStudentFullName, tvRollNumber;
        private ImageView ivStudentImage;
        private Button mAttendance;
    }


}