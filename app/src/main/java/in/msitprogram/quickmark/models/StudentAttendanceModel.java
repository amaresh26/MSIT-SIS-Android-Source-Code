package in.msitprogram.quickmark.models;

/**
 * Created by amareshjana on 31/05/17.
 */

public class StudentAttendanceModel {
    private String courseName;
    private String takenLeaveOn;
    private String attendanceTypeName;

    public StudentAttendanceModel(String courseName, String takenLeaveOn, String attendanceTypeName) {
        this.courseName = courseName;
        this.takenLeaveOn = takenLeaveOn;
        this.attendanceTypeName = attendanceTypeName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getTakenLeaveOn() {
        return takenLeaveOn;
    }

    public void setTakenLeaveOn(String takenLeaveOn) {
        this.takenLeaveOn = takenLeaveOn;
    }

    public String getAttendanceTypeName() {
        return attendanceTypeName;
    }

    public void setAttendanceTypeName(String attendanceTypeName) {
        this.attendanceTypeName = attendanceTypeName;
    }
}
