package group12.biciurjc.model;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;
    private String encondedPassword;
    private Date date;
    private boolean active;

    @Lob
    private Blob imageFile;

    public User() {

    }

    public User(String name, String encondedPassword) {
        this.name = name;
        this.encondedPassword = encondedPassword;
        setDate();
        active = true;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEncondedPassword() {
        return encondedPassword;
    }

    public void setEncondedPassword(String encondedPassword) {
        this.encondedPassword = encondedPassword;
    }

    public Date getDate() {
        return date;
    }

    public void setDate() {
        long miliseconds = System.currentTimeMillis();
        date = new Date(miliseconds);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Blob getImageFile() {
        return imageFile;
    }

    public void setImageFile(Blob imageFile) {
        this.imageFile = imageFile;
    }

    public String getSpanishFormat() {
        return date.toString().substring(8, 10)+ "/" + date.toString().substring(5, 7) + "/" + date.toString().substring(0, 4);
    }
}