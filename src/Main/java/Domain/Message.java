package Domain;

import java.time.LocalDateTime;

public class Message extends Entity<Integer> {
    Integer fromID;
    Integer toID;
    String message;
    LocalDateTime dateTime;
    Integer replyID;
    public Message(Integer id, Integer fromID, Integer toID, String message, LocalDateTime dateTime, Integer replyID) {
        this.setId(id);
        this.fromID = fromID;
        this.toID = toID;
        this.message = message;
        this.dateTime = dateTime;
        this.replyID = replyID;
    }

    public Message(Integer fromID, Integer toID, String message, LocalDateTime dateTime, Integer replyID) {
        this.fromID = fromID;
        this.toID = toID;
        this.message = message;
        this.dateTime = dateTime;
        this.replyID = replyID;
    }

    public Integer getFromID() {
        return fromID;
    }

    public void setFromID(Integer fromID) {
        this.fromID = fromID;
    }

    public Integer getToID() {
        return toID;
    }

    public void setToID(Integer toID) {
        this.toID = toID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getReplyID() {
        return replyID;
    }

    public void setReplyID(Integer replyID) {
        this.replyID = replyID;
    }
}
