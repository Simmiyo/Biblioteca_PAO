package entities;

import java.math.BigDecimal;
import java.util.Objects;

public class Transaction {
    private BigDecimal MoneySum;
    private String Buyer; //cont IBAN
    private String Seller; //cont IBAN
    private String Reason;

    public Transaction(BigDecimal sum, String buyer, String seller, String reason){
        MoneySum = sum;
        Buyer = buyer;
        Seller = seller;
        Reason = reason;
    }

    public BigDecimal getMoneySum() { return MoneySum; }

    public String getBuyer() { return Buyer; }

    public String getSeller() { return Seller; }

    public String getReason() { return Reason; }

    public void setMoneySum(BigDecimal moneySum) { MoneySum = moneySum; }

    public void setBuyer(String buyer) { Buyer = buyer; }

    public void setSeller(String seller) { Seller = seller; }

    public void setReason(String reason) { Reason = reason; }

    @Override
    public String toString() {
        return "Transaction{" +
                "MoneySum=" + MoneySum +
                ", Buyer='" + Buyer + '\'' +
                ", Seller='" + Seller + '\'' +
                ", Reason='" + Reason + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(getMoneySum(), that.getMoneySum()) && Objects.equals(getBuyer(), that.getBuyer()) && Objects.equals(getSeller(), that.getSeller()) && Objects.equals(getReason(), that.getReason());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMoneySum(), getBuyer(), getSeller(), getReason());
    }
}
