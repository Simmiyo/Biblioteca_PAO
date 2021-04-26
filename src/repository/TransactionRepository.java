package repository;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import entities.Section;
import entities.Transaction;
import services.Pair;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionRepository {
    ArrayList<Transaction> Transactions = new ArrayList<Transaction>();


    public TransactionRepository(){

    }

    public void InitializeTransactionsFromCSV() {
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
            this.Transactions.addAll(csvObjectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Transaction getTransaction(Integer id){
        for (Transaction transaction: Transactions) {
            if (transaction.getId().equals(id))
                return transaction;
        }
        return null;
    }

    public void addTransaction(Transaction x) {
        List<Integer> ids = Transactions.stream().map(Transaction::getId).collect(Collectors.toList());
        ids.sort(Comparator.comparing(Integer::valueOf));
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
            e.printStackTrace();
        }
        this.Transactions.add(x);
    }

    //ramas
    public void deleteTransaction(Transaction x){
        this.Transactions.remove(x);
    }

    public Transaction aboutTransaction(String buyer, String seller){
        for (Transaction transaction : this.Transactions)
            if (transaction.getBuyer().equals(buyer) && transaction.getSeller().equals(seller))
                return transaction;
        return null;
    }

    public ArrayList<Transaction> getTransactions() {
        return Transactions;
    }

    public void deleteTransactions(){
        while (!this.Transactions.isEmpty())
            this.Transactions.remove(0);
    }

    public void updateTransaction(Integer id, Transaction x){
        x.setId(id);
        for (int i=0;i<this.Transactions.size();i++)
        {
            if(this.Transactions.get(i).getId().equals(id)) {
                this.Transactions.set(i, x);
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
