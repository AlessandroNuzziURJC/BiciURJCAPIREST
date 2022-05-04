package group12.biciurjc.model;

import javax.persistence.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(value = EnumType.STRING)
    private List<Status> statusTransitions = new ArrayList<>();

    public Bicycle() {

    }

    public Bicycle(String serialNumber, String model) {
        this.serialNumber = serialNumber;
        this.model = model;
        setDate();
        setStatus(Status.WITHOUT_BASE);
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
        statusTransitions.add(status);
    }

    public List<Status> getStatusTransitions() {
        return statusTransitions;
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

    public String getStatusString(){
        return statusTranslate(status);
    }

    public List<String> getStatusStringList(){
        List<String> list = new ArrayList<>(statusTransitions.size());

        int i = 1;
        for (Status status : statusTransitions) {
            list.add(i++ + ".    " + statusTranslate(status));
        }
        Collections.reverse(list);

        return list;
    }
}