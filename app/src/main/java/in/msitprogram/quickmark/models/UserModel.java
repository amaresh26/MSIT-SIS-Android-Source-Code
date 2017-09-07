package in.msitprogram.quickmark.models;

/**
 * Created by amareshjana on 19/05/17.
 */

public class UserModel {

    private String fullName = "";
    private String rollNumber = "";
    private String userImageUrl = "";

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
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
}
