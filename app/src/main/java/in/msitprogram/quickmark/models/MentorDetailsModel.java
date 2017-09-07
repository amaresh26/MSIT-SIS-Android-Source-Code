package in.msitprogram.quickmark.models;

/**
 * Created by amareshjana on 19/05/17.
 */

public class MentorDetailsModel {

    private String fullName = "";
    private String mentorImageUrl = "";
    private String mentorMobileNo = "";
    private String mentorEmail = "";
    private String mentorTypeId = "";

    public MentorDetailsModel(String fullName, String mentorImageUrl, String mentorMobileNo, String mentorEmail, String mentorTypeId) {
        this.fullName = fullName;
        this.mentorImageUrl = mentorImageUrl;
        this.mentorMobileNo = mentorMobileNo;
        this.mentorEmail = mentorEmail;
        this.mentorTypeId = mentorTypeId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getMentorImageUrl() {
        return mentorImageUrl;
    }

    public void setMentorImageUrl(String mentorImageUrl) {
        this.mentorImageUrl = mentorImageUrl;
    }

    public String getMentorMobileNo() {
        return mentorMobileNo;
    }

    public void setMentorMobileNo(String mentorMobileNo) {
        this.mentorMobileNo = mentorMobileNo;
    }

    public String getMentorEmail() {
        return mentorEmail;
    }

    public void setMentorEmail(String mentorEmail) {
        this.mentorEmail = mentorEmail;
    }

    public String getMentorTypeId() {
        return mentorTypeId;
    }

    public void setMentorTypeId(String mentorTypeId) {
        this.mentorTypeId = mentorTypeId;
    }
}
