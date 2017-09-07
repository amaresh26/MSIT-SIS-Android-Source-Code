package in.msitprogram.quickmark.models;

import java.io.Serializable;

/**
 * Created by amareshjana on 20/05/17.
 */

public class CourseModel implements Serializable{

    private String course_id ;
    private String course_name ;
    private String no_of_leaves ;
    private String from_date ;
    private String to_date ;

    public CourseModel(String course_id, String course_name, String no_of_leaves, String from_date, String to_date) {
        this.course_id = course_id;
        this.course_name = course_name;
        this.no_of_leaves = no_of_leaves;
        this.from_date = from_date;
        this.to_date = to_date;
    }

    public String getCourse_id() {
        return course_id;
    }

    public void setCourse_id(String course_id) {
        this.course_id = course_id;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getNo_of_leaves() {
        return no_of_leaves;
    }

    public void setNo_of_leaves(String no_of_leaves) {
        this.no_of_leaves = no_of_leaves;
    }

    public String getFrom_date() {
        return from_date;
    }

    public void setFrom_date(String from_date) {
        this.from_date = from_date;
    }

    public String getTo_date() {
        return to_date;
    }

    public void setTo_date(String to_date) {
        this.to_date = to_date;
    }

    @Override
    public String toString() {
        return "CourseModel{" +
                "course_id='" + course_id + '\'' +
                ", course_name='" + course_name + '\'' +
                ", no_of_leaves='" + no_of_leaves + '\'' +
                ", from_date='" + from_date + '\'' +
                ", to_date='" + to_date + '\'' +
                '}';
    }
}
