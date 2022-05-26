package group12.biciurjc.model.DTO;

import java.sql.Date;

public class ReturnDTO {

    private long returnId;
    private long stationId;
    private long bicycleId;
    private long userId;
    private String userName;
    private Date date;

    public ReturnDTO(long returnId, long stationId, long bicycleId, long userId, String userName, Date date) {
        this.returnId = returnId;
        this.stationId = stationId;
        this.bicycleId = bicycleId;
        this.userId = userId;
        this.userName = userName;
        this.date = date;
    }

    public long getReturnId() {
        return returnId;
    }

    public void setReturnId(long returnId) {
        this.returnId = returnId;
    }

    public long getStationId() {
        return stationId;
    }

    public void setStationId(long stationId) {
        this.stationId = stationId;
    }

    public long getBicycleId() {
        return bicycleId;
    }

    public void setBicycleId(long bicycleId) {
        this.bicycleId = bicycleId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}