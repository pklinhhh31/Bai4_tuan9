package com.practice.maven;

import static org.junit.jupiter.api.Assertions.*;

import com.bank.system.*;
import org.junit.jupiter.api.Test;

public class BankTest {
    @Test
    void testAccountCreation() {
        Account acc = new CheckingAccount(123, 1000.0);
        assertEquals(1000.0, acc.getBalance());
    }
}