package group12.biciurjc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private String name;

    @NotNull
    private String encondedPassword;

    @NotNull
    private Date date;

    @NotNull
    private boolean active;

    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long login;

    @NotNull
    private double balance;

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

    public User(String name, String encondedPassword, double balance) {
        this.name = name;
        this.encondedPassword = encondedPassword;
        setDate();
        this.active = true;
        this.balance = balance;
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

    public void setNewDate(Date date) {
       this.date = date;
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

    public long getLogin() {
        return login;
    }

    public void setLogin(long login) {
        this.login = login;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }
}