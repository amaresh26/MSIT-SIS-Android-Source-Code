package in.msitprogram.quickmark.models;

import java.io.Serializable;

/**
 * Created by amareshjana on 29/05/17.
 */

public class LeaveStatusModel implements Serializable{
    private String mResaon;
    private String mFromDate;
    private String mToDate;
    private String lid;
    private String mStatus;
    private String cid;
    private String studentName;

    public LeaveStatusModel(String mResaon, String mFromDate, String mToDate, String lid, String mStatus, String cid, String studentName) {
        this.mResaon = mResaon;
        this.mFromDate = mFromDate;
        this.mToDate = mToDate;
        this.lid = lid;
        this.mStatus = mStatus;
        this.cid = cid;
        this.studentName = studentName;
    }

    public String getmResaon() {
        return mResaon;
    }

    public void setmResaon(String mResaon) {
        this.mResaon = mResaon;
    }

    public String getmFromDate() {
        return mFromDate;
    }

    public void setmFromDate(String mFromDate) {
        this.mFromDate = mFromDate;
    }

    public String getmToDate() {
        return mToDate;
    }

    public void setmToDate(String mToDate) {
        this.mToDate = mToDate;
    }

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
