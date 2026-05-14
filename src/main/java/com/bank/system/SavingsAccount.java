package com.bank.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Lớp tài khoản tiết kiệm với các quy định về số dư tối thiểu và hạn mức rút tiền.
 */
public class SavingsAccount extends Account {

    private static final Logger logger = LoggerFactory.getLogger(SavingsAccount.class);

    // Khai báo hằng số để tránh Magic Numbers
    public static final double MAX_WITHDRAW_LIMIT = 1000.0;
    public static final double MIN_BALANCE_REQUIRED = 5000.0;

    public SavingsAccount(long accountNumber, double balance) {
        super(accountNumber, balance);
    }

    @Override
    public void deposit(double amount) {
        double initialBalance = getBalance();
        try {
            doDepositing(amount);
            double finalBalance = getBalance();

            // Sử dụng hằng số từ lớp Transaction thay vì con số 3
            Transaction t = new Transaction(Transaction.TYPE_DEPOSIT_SAVINGS, amount,
                    initialBalance, finalBalance);
            addTransaction(t);

            logger.info("Nạp tiền vào tài khoản tiết kiệm {} thành công: +{}",
                    getAccountNumber(), amount);

        } catch (InvalidFundingAmountException e) {
            logger.warn("Yêu cầu nạp tiền không hợp lệ cho tài khoản {}: {}",
                    getAccountNumber(), e.getMessage());
        }
    }

    @Override
    public void withdraw(double amount) {
        double initialBalance = getBalance();
        try {
            // Kiểm tra hạn mức rút tiền
            if (amount > MAX_WITHDRAW_LIMIT) {
                logger.warn("Tài khoản {}: Vượt hạn mức rút tiền cho phép ({})",
                        getAccountNumber(), MAX_WITHDRAW_LIMIT);
                throw new InvalidFundingAmountException(amount);
            }

            // Kiểm tra số dư tối thiểu sau khi rút
            if (initialBalance - amount < MIN_BALANCE_REQUIRED) {
                logger.warn("Tài khoản {}: Không đảm bảo số dư tối thiểu {} sau khi rút",
                        getAccountNumber(), MIN_BALANCE_REQUIRED);
                throw new InsufficientFundsException(amount);
            }

            doWithdrawing(amount);
            double finalBalance = getBalance();

            // Sử dụng hằng số thay vì con số 4
            Transaction t = new Transaction(Transaction.TYPE_WITHDRAW_SAVINGS, amount,
                    initialBalance, finalBalance);
            addTransaction(t);

            logger.info("Tài khoản tiết kiệm {}: Rút thành công {}. Số dư mới: {}",
                    getAccountNumber(), amount, finalBalance);

        } catch (InvalidFundingAmountException | InsufficientFundsException e) {
            logger.error("Giao dịch rút tiền thất bại tại tài khoản {}: {}",
                    getAccountNumber(), e.getMessage());
        } catch (Exception e) {
            // Bắt các lỗi hệ thống không lường trước
            logger.error("Lỗi hệ thống nghiêm trọng khi rút tiền tại tài khoản {}: {}",
                    getAccountNumber(), e.getMessage());
        }
    }
}