package in.msitprogram.quickmark.models;

import java.io.Serializable;

/**
 * Created by amareshjana on 24/05/17.
 */

public class EventDetailsModel implements Serializable {

    private String eventId;
    private String title;
    private String createdBy;
    private String description;
    private String eventDate;
    private String postedBy;

    public EventDetailsModel(String eventId, String title, String createdBy, String description, String eventDate, String postedBy) {
        this.eventId = eventId;
        this.title = title;
        this.createdBy = createdBy;
        this.description = description;
        this.eventDate = eventDate;
        this.postedBy = postedBy;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(String postedBy) {
        this.postedBy = postedBy;
    }
}
