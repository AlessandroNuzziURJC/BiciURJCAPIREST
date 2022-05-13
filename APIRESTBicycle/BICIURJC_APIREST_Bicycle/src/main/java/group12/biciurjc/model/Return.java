package group12.biciurjc.model;

import group12.biciurjc.service.BicycleService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Return {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private Station station;

    @OneToOne
    private Bicycle bicycle;

    @OneToOne
    private Booking book;

    private long userId;
    private String userName;
    private Date date;

    public Return() {

    }

    public Return (Station station, Bicycle bicycle, long userId, String userName) {
        this.station = station;
        this.bicycle = bicycle;
        this.userId = userId;
        this.userName = userName;
        setDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public Bicycle getBicycle() {
        return bicycle;
    }

    public void setBicycle(Bicycle bicycle) {
        this.bicycle = bicycle;
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

    public void setDate() {
        long miliseconds = System.currentTimeMillis();
        date = new Date(miliseconds);
    }

}
