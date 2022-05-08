package group12.biciurjc.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String serialNumber;
    private double longitude;
    private double latitude;
    private int bicycleCapacity;
    private boolean active;
    private Date date;

    @OneToMany(mappedBy = "station")
    private List<Bicycle> bicycles = new ArrayList<>();

    public Station() {

    }

    public Station(String serialNumber, double longitude, double latitude, int bicycleCapacity) {
        this.serialNumber = serialNumber;
        this.longitude = longitude;
        this.latitude = latitude;
        this.bicycleCapacity = bicycleCapacity;
        active = true;
        setDate();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getBicycleCapacity() {
        return bicycleCapacity;
    }

    public void setBicycleCapacity(int bicycleCapacity) {
        this.bicycleCapacity = bicycleCapacity;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        long miliseconds = System.currentTimeMillis();
        date = new Date(miliseconds);
    }

    public List<Bicycle> getBicycles() {
        return bicycles;
    }

    public boolean addBicycle(Bicycle bicycle) {
        if (bicycles.size() + 1 <= bicycleCapacity) {
            bicycles.add(bicycle);
            bicycle.setStatus(Status.IN_BASE);
            bicycle.setStation(this);

            return true;
        }

        return false;
    }

    public void deleteBicycle(Bicycle bicycle) {
        bicycles.remove(bicycle);
        bicycle.setStatus(Status.WITHOUT_BASE);
        bicycle.setStation(null);
    }
}