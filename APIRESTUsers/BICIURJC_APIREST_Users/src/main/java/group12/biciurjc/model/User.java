package group12.biciurjc.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.sql.Blob;
import java.sql.Date;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    private String login;

    @NotNull
    private String name;

    @NotNull
    private String encondedPassword;

    @NotNull
    private Date date;

    @NotNull
    private boolean active;

    @NotNull
    private int balance;

    @Lob
    private Blob imageFile;

    public User() {

    }

    public User(String login, String name, String encondedPassword) {
        this.login = login;
        this.name = name;
        this.encondedPassword = encondedPassword;
        setDate();
        active = true;
        balance = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}