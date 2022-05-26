package group12.biciurjc.model.DTO;

public class BalanceDTO {

    private int balance;

    public BalanceDTO(int balance) {
        this.balance = balance;
    }

    public BalanceDTO() {

    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }
}