package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.BookReader;
import entities.Transaction;
import services.Logger;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionRepository {

    private static ArrayList<Transaction> Transactions;

    public static void initTransactions() {
        Transactions = new ArrayList<Transaction>();
    }

    public void initializeTransactionsFromCSV() {
        try {

            // Create an object of filereader
            // class with CSV file as a parameter.
            FileReader filereader = new FileReader("data/transactions.csv");

            // create csvReader object passing
            // file reader as a parameter
            // create csvReader object and skip first Line
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withSkipLines(1)
                    .build();

            List<Transaction> csvObjectList = csvReader.readAll().stream().map(data-> {
                Transaction trans = new Transaction();
                trans.setId(Integer.parseInt(data[0].trim()));
                trans.setMoneySum(new BigDecimal(data[1].trim()));
                trans.setBuyer(data[2].trim());
                trans.setSeller(data[3].trim());
                trans.setReason(data[4].trim());
                return trans;
            }).collect(Collectors.toList());
            Transactions.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            Logger.logOperation("Initialized transactions from csv file. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Initialized transactions from csv file. - SUCCESS");
    }

    public Transaction getTransaction(Integer id){
        for (Transaction transaction: Transactions) {
            if (transaction.getId().equals(id))
                return transaction;
        }
        return null;
    }

    public Integer addTransaction(Transaction x) {
        List<Integer> ids = Transactions.stream().map(Transaction::getId).sorted(Comparator.comparing(Integer::valueOf)).
                collect(Collectors.toList());
        for (Integer i = 0; i < ids.get(ids.size() - 1) + 1; i += 1) {
            if (!i.equals(ids.get(i))) {
                x.setId(i);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/transactions.csv", true);
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[]{x.getId().toString(), x.getMoneySum().toString(),
                    x.getBuyer(), x.getSeller(), x.getReason()});
        } catch (IOException e) {
            Logger.logOperation("New transaction added in csv file. - FAILED");
            e.printStackTrace();
        }
        Transactions.add(x);
        Logger.logOperation("New transaction added in csv file. - SUCCESS");
        return x.getId();
    }

    public void deleteTransaction(Integer id){
        Transactions.removeIf(transaction -> transaction.getId().equals(id));
        try {
            FileWriter filewriter = new FileWriter("data/transactions.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "MoneySum", "Buyer", "Seller", "Reason"});
            for (Transaction transaction: Transactions) {
                writer.writeNext(new String[]{transaction.getId().toString(), transaction.getMoneySum().toString(),
                        transaction.getBuyer(), transaction.getSeller(), transaction.getReason()});
            }
        } catch (IOException e) {
            Logger.logOperation("Transaction removed from csv file. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Transaction removed from csv file. - SUCCESS");
    }

    public Transaction aboutTransaction(String buyer, String seller){
        for (Transaction transaction : Transactions)
            if (transaction.getBuyer().equals(buyer) && transaction.getSeller().equals(seller))
                return transaction;
        return null;
    }

    public static ArrayList<Transaction> getTransactions() {
        return Transactions;
    }

    public void deleteTransactions(){
        Transactions.clear();
        try {
            FileWriter filewriter = new FileWriter("data/transactions.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "MoneySum", "Buyer", "Seller", "Reason"});
        } catch (IOException e) {
            Logger.logOperation("Deleted all transactions. - FAILED");
            e.printStackTrace();
        }
        Logger.logOperation("Deleted all transactions. - SUCCESS");
    }

    public void updateTransaction(Integer id, Transaction x){
        x.setId(id);
        for (int i=0;i<Transactions.size();i++)
        {
            if(Transactions.get(i).getId().equals(id)) {
                Transactions.set(i, x);
                break;
            }
        }
        try {
            FileWriter filewriter = new FileWriter("data/transactions.csv");
            CSVWriter writer = new CSVWriter(filewriter);
            writer.writeNext(new String[] {"Id", "MoneySum", "Buyer", "Seller", "Reason"});
            for (Transaction trans: Transactions) {
                writer.writeNext(new String[]{trans.getId().toString(), trans.getMoneySum().toPlainString(),
                        trans.getBuyer(), trans.getSeller(), trans.getReason()});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortTransactions()
    {
        Collections.sort(Transactions);
    }
}
