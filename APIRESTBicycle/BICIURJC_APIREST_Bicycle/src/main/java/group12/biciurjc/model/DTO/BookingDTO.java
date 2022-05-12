package group12.biciurjc.model.DTO;

import java.sql.Date;

public class BookingDTO {

    private long bookingId;
    private long stationId;
    private long bicycleId;
    private long userId;
    private String userName;
    private Date date;
    private int price;

    public BookingDTO(long bookingId, long stationId, long bicycleId, long userId, String userName, Date date, int price) {
        this.bookingId = bookingId;
        this.stationId = stationId;
        this.bicycleId = bicycleId;
        this.userId = userId;
        this.userName = userName;
        this.date = date;
        this.price = price;
    }

    public long getBookingId() {
        return bookingId;
    }

    public void setBookingId(long bookingId) {
        this.bookingId = bookingId;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}