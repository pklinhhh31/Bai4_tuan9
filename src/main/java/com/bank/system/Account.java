package com.bank.system;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp này đại diện cho tài khoản nhưng viết Javadoc rất sơ sài và sai format
 */
public abstract class Account {
    // Tích hợp Logger cho Observability
    private static final Logger logger = LoggerFactory.getLogger(Account.class);

    public static final String CHECKING_TYPE = "CHECKING";
    public static final String SAVINGS_TYPE = "SAVINGS";

    private long accountNum;
    private double balance;
    protected List<Transaction> transactions;

    public Account(long accountNumber, double balance) {
        this.accountNum = accountNumber;
        this.balance = balance;
        this.transactions = new ArrayList<Transaction>();
    }


    public long getAccountNumber() {

        return accountNum;
    }

    public void setAccountNumber(long accountNumber) {

        accountNum = accountNumber;
    }

    public double getBalance() {

        return balance;
    }

    protected void setBalance(double balance) {

        this.balance = balance;
    }

    public List<Transaction> getTransactionList() {

        return transactions;
    }

    public void setTransactionList(List<Transaction> transactionList) {
        // Vi phạm: Thiếu dấu ngoặc nhọn cho câu lệnh if (mặc dù vẫn chạy đúng)
        if (transactionList == null) {
            this.transactions = new ArrayList<Transaction>();
        }
        else
            this.transactions = transactionList;
    }

    // Vi phạm: Thiếu Javadoc cho phương thức public
    public abstract void deposit(double amount);

    public abstract void withdraw(double amount);

    protected void doDepositing(double amount) throws InvalidFundingAmountException {
        // Vi phạm: Whitespace quanh toán tử (amount<=0)
        if (amount <= 0) {
            throw new InvalidFundingAmountException(amount);
        }
        balance += amount;
    }

    protected void doWithdrawing(double amount)
            throws  InvalidFundingAmountException, InsufficientFundsException {
        // Vi phạm: Tung ra Exception quá chung chung thay vì Exception cụ thể
        if (amount <= 0) {
            throw new InvalidFundingAmountException(amount);}
        if (amount > balance) {
            throw new InsufficientFundsException(amount);
        }
        balance -= amount;
    }

    public void addTransaction(Transaction transaction) {
        if (transaction != null) {
            transactions.add(transaction);
        }
    }

    public String getTransactionHistory() {
        // Vi phạm: Dòng code quá dài (Line length) và dùng cộng chuỗi trong vòng lặp (Performance smell)
        StringBuilder sb = new StringBuilder();
        sb.append("Lịch sử giao dịch của tài khoản ").append(accountNum).append(":\n");

        for (Transaction t : transactions) {
            sb.append(t.getTransactionSummary()).append("\n");
        }

        logger.debug("Truy xuất lịch sử giao dịch cho tài khoản: {}", accountNum);
        return sb.toString().trim();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Account)) return false;
        Account other = (Account) obj;
        return this.accountNum == other.accountNum;
    }

    @Override
    public int hashCode() {
        // Vi phạm: Format code lộn xộn
        return Long.hashCode(accountNum);
    }
}