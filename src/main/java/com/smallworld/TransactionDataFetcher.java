package com.smallworld;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smallworld.data.Transaction;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TransactionDataFetcher {

    private byte[] jsonData;
    private List<Transaction> transactionList = new ArrayList<>();

    private void generateTransactionList() throws IOException {
        this.jsonData = Files.readAllBytes(Paths.get("transactions.json"));
        ObjectMapper objectMapper = new ObjectMapper();
        this.setTransactionList(objectMapper.readValue(jsonData,
                objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Transaction.class)));
    }

    /**
     * Returns the sum of the amounts of all transactions
     */
    public double getTotalTransactionAmount() {
        try {
            this.generateTransactionList();
            return transactionList.stream().filter(t -> t.isIssueSolved())
                    .mapToDouble(transaction -> (double) transaction.getAmount()).sum();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the sum of the amounts of all transactions sent by the specified client
     */
    public double getTotalTransactionAmountSentBy(String senderFullName) {
        try {
            this.generateTransactionList();
            return transactionList.stream().filter(t -> t.isIssueSolved() && t.getSenderFullName().equals(senderFullName))
                    .mapToDouble(t -> (double) t.getAmount()).sum();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the highest transaction amount
     */
    public double getMaxTransactionAmount() {
        try {
            this.generateTransactionList();
            return transactionList.stream().filter(t -> t.isIssueSolved())
                    .mapToDouble(transaction -> (double) transaction.getAmount()).max().getAsDouble();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Counts the number of unique clients that sent or received a transaction
     */
    public long countUniqueClients() {
        try {
            this.generateTransactionList();
            return transactionList.stream().filter(t -> t.isIssueSolved()).distinct().count();
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns whether a client (sender or beneficiary) has at least one transaction with a compliance
     * issue that has not been solved
     */
    public boolean hasOpenComplianceIssues(String clientFullName) {
        try {
            this.generateTransactionList();
            Predicate<Transaction> transactionPredicate = t -> t.getSenderFullName().equals(clientFullName);
            return transactionList.stream().filter(t -> t.isIssueSolved()).anyMatch(transactionPredicate);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns all transactions indexed by beneficiary name
     */
    public Map<String, Transaction> getTransactionsByBeneficiaryName() {
        try {
            this.generateTransactionList();
            Map<String, Transaction> transactionMap = new HashMap<>();
            for(Transaction transaction : transactionList) {
                if(transaction.getBeneficiaryFullName() != null) {
                    transactionMap.put(transaction.getBeneficiaryFullName(), transaction);
                }
            }
            return transactionMap;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the identifiers of all open compliance issues
     */
    public Set<Integer> getUnsolvedIssueIds() {
        try {
            this.generateTransactionList();
            Set<Integer> unresolvedIssueIds = new HashSet<>();
            for (Transaction transaction : transactionList) {
                if(!transaction.isIssueSolved()) {
                    unresolvedIssueIds.add(transaction.getIssueId());
                }
            }
            return unresolvedIssueIds;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns a list of all solved issue messages
     */
    public List<String> getAllSolvedIssueMessages() {

        try {
            this.generateTransactionList();
            List<Transaction> solvedIssues = transactionList.stream().filter(t -> t.isIssueSolved()).collect(Collectors.toList());
            List<String> solvedIssueMsg = new ArrayList<>();
            for(Transaction transaction : solvedIssues) {
                if(transaction.getIssueMessage() != null)
                    solvedIssueMsg.add(transaction.getIssueMessage());
            }
            return solvedIssueMsg;
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the 3 transactions with the highest amount sorted by amount descending
     */
    public List<Transaction> getTop3TransactionsByAmount() {
        List<Transaction> top3Transactions = new ArrayList<>();
        try {
            this.generateTransactionList();
            int nextValue = 0;
            for(Transaction transaction : transactionList) {
                nextValue += 1;
                if(nextValue == transactionList.size()) {
                    nextValue = 0;
                    break;
                }
                if(transaction.getAmount() < transactionList.get(nextValue).getAmount()) {
                    if(transaction.isIssueSolved())
                        top3Transactions.add(transaction);
                }
            }
            top3Transactions.sort(Comparator.comparing(Transaction::getSenderFullName, Comparator.reverseOrder()));
        } catch(Exception e) {
            throw new RuntimeException(e);
        }

        return top3Transactions;
    }

    /**
     * Returns the senderFullName of the sender with the most total sent amount
     */
    public Optional<String> getTopSender() {
        Transaction transaction = null;
        try {
            this.generateTransactionList();
            double maxValue = 0;
            for(Transaction trn : transactionList) {
                if(trn.getAmount() > maxValue) {
                    maxValue = trn.getAmount();
                    transaction = trn;
                }
            }
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
        return transaction.getSenderFullName().describeConstable();
    }

    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
    }
}
