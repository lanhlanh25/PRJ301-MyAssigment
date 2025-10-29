package model;

import java.sql.Date;
import java.sql.Timestamp;

// CẦN BỔ SUNG "extends BaseModel"
public class RequestForLeave extends BaseModel { 
    private Employee createdBy;
    private Timestamp createdTime;
    private Date fromDate;
    private Date toDate;
    private String reason;
    private int status; 
    private Employee processedBy;
    private Timestamp processedTime;
    private String reasonForAction;
    
    public Employee getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Employee createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Employee getProcessedBy() {
        return processedBy;
    }

    public void setProcessedBy(Employee processedBy) {
        this.processedBy = processedBy;
    }

    public Timestamp getProcessedTime() {
        return processedTime;
    }

    public void setProcessedTime(Timestamp processedTime) {
        this.processedTime = processedTime;
    }

    public String getReasonForAction() {
        return reasonForAction;
    }

    public void setReasonForAction(String reasonForAction) {
        this.reasonForAction = reasonForAction;
    }

    public String getStatusString() {
        switch (status) {
            case 1:
                return "In progress";
            case 2:
                return "Approved";
            case 3:
                return "Rejected";
            default:
                return "Unknown";
        }
    }
}