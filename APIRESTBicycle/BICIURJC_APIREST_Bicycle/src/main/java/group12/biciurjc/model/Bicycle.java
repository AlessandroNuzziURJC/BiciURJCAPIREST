package group12.biciurjc.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class Bicycle {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String serialNumber;
    private String model;
    private Date date;

    @Enumerated(value = EnumType.STRING)
    private Status status;

    @ManyToOne
    private Station station;

    public Bicycle() {

    }

    public Bicycle(String serialNumber, String model) {
        this.serialNumber = serialNumber;
        this.model = model;
        setDate();
        status = Status.WITHOUT_BASE;
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

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        long miliseconds = System.currentTimeMillis();
        date = new Date(miliseconds);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    private String statusTranslate(Status status){
        return switch (status) {
            case WITHOUT_BASE -> "SIN BASE";
            case IN_BASE -> "EN BASE";
            case RESERVED -> "RESERVADA";
            case WITHDRAWAL -> "BAJA";
        };
    }
}