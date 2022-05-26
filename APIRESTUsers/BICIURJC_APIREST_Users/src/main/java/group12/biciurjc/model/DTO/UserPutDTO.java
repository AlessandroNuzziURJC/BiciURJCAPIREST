package group12.biciurjc.model.DTO;

public class UserPutDTO {

    private String login;
    private String name;
    private String password;
    private boolean active;
    private int balance;

    public UserPutDTO(String login, String name, String password, boolean active, int balance) {
        this.login = login;
        this.name = name;
        this.password = password;
        this.active = active;
        this.balance = balance;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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