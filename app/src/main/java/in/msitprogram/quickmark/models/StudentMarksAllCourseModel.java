package in.msitprogram.quickmark.models;

/**
 * Created by amareshjana on 05/06/17.
 */

public class StudentMarksAllCourseModel {

    private String course_name;
    private String percentage;
    private String started_date;
    private String ended_date;

    public StudentMarksAllCourseModel(String course_name, String percentage, String started_date, String ended_date) {
        this.course_name = course_name;
        this.percentage = percentage;
        this.started_date = started_date;
        this.ended_date = ended_date;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getStarted_date() {
        return started_date;
    }

    public void setStarted_date(String started_date) {
        this.started_date = started_date;
    }

    public String getEnded_date() {
        return ended_date;
    }

    public void setEnded_date(String ended_date) {
        this.ended_date = ended_date;
    }
}
