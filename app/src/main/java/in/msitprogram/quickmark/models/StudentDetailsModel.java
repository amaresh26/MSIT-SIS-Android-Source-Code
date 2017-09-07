package in.msitprogram.quickmark.models;

/**
 * Created by amareshjana on 19/05/17.
 */

public class StudentDetailsModel {

    private String fullName = "";
    private String rollNumber = "";
    private String studentImageUrl = "";
    private String studentMobileNo = "";
    private String studentEmail = "";
    private String studentAttendance = "";
    private String uid = "";

    public StudentDetailsModel(String fullName, String rollNumber, String studentImageUrl, String studentMobileNo, String studentEmail, String studentAttendance, String uid) {
        this.fullName = fullName;
        this.rollNumber = rollNumber;
        this.studentImageUrl = studentImageUrl;
        this.studentMobileNo = studentMobileNo;
        this.studentEmail = studentEmail;
        this.studentAttendance = studentAttendance;
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

    public String getStudentAttendance() {
        return studentAttendance;
    }

    public void setStudentAttendance(String studentAttendance) {
        this.studentAttendance = studentAttendance;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
