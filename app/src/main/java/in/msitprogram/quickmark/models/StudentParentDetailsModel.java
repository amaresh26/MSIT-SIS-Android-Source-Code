package in.msitprogram.quickmark.models;

import java.io.Serializable;

/**
 * Created by amareshjana on 05/06/17.
 */

public class StudentParentDetailsModel implements Serializable {

    private String fullName = "";
    private String rollNumber = "";
    private String studentImageUrl = "";
    private String studentMobileNo = "";
    private String studentEmail = "";
    private String parentName = "";
    private String parentMobile = "";
    private String parentEmail = "";
    private String uid = "";

    public StudentParentDetailsModel(String fullName, String rollNumber, String studentImageUrl,
                                     String studentMobileNo, String studentEmail,
                                     String parentName, String parentMobile, String parentEmail, String uid) {
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.studentImageUrl = studentImageUrl;
        this.studentMobileNo = studentMobileNo;
        this.studentEmail = studentEmail;
        this.parentName = parentName;
        this.parentMobile = parentMobile;
        this.parentEmail = parentEmail;
        this.uid = uid;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRollNumber() {
        return rollNumber;
    }

    public void setRollNumber(String rollNumber) {
        this.rollNumber = rollNumber;
    }

    public String getStudentImageUrl() {
        return studentImageUrl;
    }

    public void setStudentImageUrl(String studentImageUrl) {
        this.studentImageUrl = studentImageUrl;
    }

    public String getStudentMobileNo() {
        return studentMobileNo;
    }

    public void setStudentMobileNo(String studentMobileNo) {
        this.studentMobileNo = studentMobileNo;
    }

    public String getStudentEmail() {
        return studentEmail;
    }

    public void setStudentEmail(String studentEmail) {
        this.studentEmail = studentEmail;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getParentMobile() {
        return parentMobile;
    }

    public void setParentMobile(String parentMobile) {
        this.parentMobile = parentMobile;
    }

    public String getParentEmail() {
        return parentEmail;
    }

    public void setParentEmail(String parentEmail) {
        this.parentEmail = parentEmail;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
