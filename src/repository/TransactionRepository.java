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
            List<String[]> allData = csvReader.readAll();

            List<Transaction> csv_objectList = csvReader.readAll().stream().map(data-> {
                Transaction trans = new Transaction();
                trans.setId(Integer.parseInt(data[0]));
                trans.setMoneySum(new BigDecimal(data[1]));
                trans.setBuyer(data[2]);
                trans.setSeller(data[3]);
                trans.setReason(data[4]);
                return trans;
            }).collect(Collectors.toList());
            this.Transactions.addAll(csv_objectList);
        }
        catch (IOException | CsvException e) {
            e.printStackTrace();
        }
    }

    public Transaction getTransaction(int i){
        return this.Transactions.get(i);
    }

    public void addTransaction(Transaction x){
        Boolean exists = Boolean.FALSE;
        ArrayList<Integer> ids = new ArrayList<>();
        for (Transaction trans: Transactions
        ) {
            if (trans.equals(x)) {
                exists = Boolean.TRUE;
                break;
            }
            ids.add(trans.getId());
        }
        if (!exists) {
            ids.sort(Comparator.comparing(Integer::valueOf));
            for (Integer i = 0; i < ids.get(ids.size() - 1) + 1; i += 1) {
                if (i != ids.get(i)) {
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
    }

    //ramas
    public void deleteTransaction(Transaction x){
        this.Transactions.remove(x);
    }

    public Transaction aboutTransaction(String buyer, String seller){
        for (int i=0;i<this.Transactions.size();i++)
            if(this.Transactions.get(i).getBuyer()==buyer && this.Transactions.get(i).getSeller()==seller)
                return this.Transactions.get(i);
        return null;
    }

    public ArrayList<Transaction> getTransactions() {
        return Transactions;
    }

    public void deleteTransactions(){
        while (this.Transactions.isEmpty()!=true)
            this.Transactions.remove(0);
    }

    public void updateTransaction(Integer id, Transaction x){
        x.setId(id);
        for (int i=0;i<this.Transactions.size();i++)
        {
            if(this.Transactions.get(i).getId() == id) {
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
        getTransactions().sort(Comparator.comparing(Transaction::getMoneySum));
    }
}
