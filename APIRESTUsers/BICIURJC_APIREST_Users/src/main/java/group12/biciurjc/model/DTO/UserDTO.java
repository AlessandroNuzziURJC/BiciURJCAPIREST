package group12.biciurjc.model.DTO;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Date;

public class UserDTO {

    private String name;
    private Date date;
    private boolean active;

    @Lob
    private Blob imageFile;

    public UserDTO(String name) {
        this.name = name;
        setDate();
        active = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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