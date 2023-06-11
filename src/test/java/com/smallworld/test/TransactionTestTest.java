package com.smallworld.test;

import com.smallworld.TransactionDataFetcher;
import com.smallworld.data.Transaction;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TransactionTestTest {

    @Test
    public void testTotalTransactionAmount() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertEquals(4371.37, transactionDataFetcher.getTotalTransactionAmount(), 0.0);
    }

    @Test
    public void testMaxTransactionAmount() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertEquals(0.0, transactionDataFetcher.getMaxTransactionAmount(), 0.0);
    }

    @Test
    public void testHasOpenComplianceIssues() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertTrue(transactionDataFetcher.hasOpenComplianceIssues("Tom Shelby"));
    }

    @Test
    public void testCountUniqueClients() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertEquals(10, transactionDataFetcher.countUniqueClients());
    }

    @Test
    public void testUnsolvedIssueIds() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertEquals(0, transactionDataFetcher.getUnsolvedIssueIds());
    }

    @Test
    public void testAllSolvedIssueMessages() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertEquals("", transactionDataFetcher.getAllSolvedIssueMessages());
    }

    @Test
    public void testTransactionsByBeneficiaryName() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertEquals(Transaction.class, transactionDataFetcher.getTransactionsByBeneficiaryName());
    }

    @Test
    public void testTotalTransactionAmountSentBy() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertEquals(Transaction.class, transactionDataFetcher.getTotalTransactionAmountSentBy("Grace Burgess"));
    }

    @Test
    public void testTop3TransactionsByAmount() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertEquals(Transaction.class, transactionDataFetcher.getTop3TransactionsByAmount());
    }

    @Test
    public void testTopSender() {
        TransactionDataFetcher transactionDataFetcher = new TransactionDataFetcher();
        assertEquals(Transaction.class, transactionDataFetcher.getTopSender());
    }

}