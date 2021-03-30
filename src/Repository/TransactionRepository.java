package Repository;

import Entities.Transaction;

import java.util.ArrayList;
import java.util.Comparator;

public class TransactionRepository {
    ArrayList<Transaction> Transactions = new ArrayList<Transaction>();


    public TransactionRepository(){

    }

    public Transaction getTransaction(int i){
        return this.Transactions.get(i);
    }


    public void addTransaction(Transaction x){
        this.Transactions.add(x);
    }
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

    public void updateTransaction(String buyer, String seller, Transaction x){
        for (int i=0;i<this.Transactions.size();i++)
        {
            if(this.Transactions.get(i).getBuyer()==buyer && this.Transactions.get(i).getSeller()==seller) {
                this.Transactions.set(i, x);
                break;
            }
        }
    }

    public void sortTransactions()
    {
        getTransactions().sort(Comparator.comparing(Transaction::getMoneySum));
    }
}
