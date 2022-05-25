package group12.biciurjc.model.DTO;

import java.sql.Date;

public class UserDTO {

    private Long id;
    private String name;
    private Date date;
    private boolean active;

    public UserDTO(String name) {
        this.name = name;
        setDate();
        active = true;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSpanishFormat() {
        return date.toString().substring(8, 10)+ "/" + date.toString().substring(5, 7) + "/" + date.toString().substring(0, 4);
    }
}