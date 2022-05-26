package group12.biciurjc.model.DTO;

import java.sql.Date;

public class UserDTO {

    private long userId;
    private String login;
    private String name;
    private Date date;
    private boolean active;
    private int balance;

    public UserDTO(long userId, String login, String name, Date date, boolean active, int balance) {
        this.userId = userId;
        this.login = login;
        this.name = name;
        this.date = date;
        this.active = active;
        this.balance = balance;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}