package org.billing.pagepojo;

import org.billing.utility.SystemUtils;
import java.util.ArrayList;
import java.util.List;

public class TransactionsPojo {

    private String billNo;
    private String customerName;
    private String transactionDate;
    private List<ProductPojo> products = new ArrayList<>();
    private List<ScrapPojo> scraps = new ArrayList<>();
    private Double totalOfProduct = 0.0;
    private Double totalOfScrap = 0.0;
    private Double subTotal = 0.0;
    private Double customerAmount = 0.0;
    private Double discount = 0.0;
    private Double oldCredit = 0.0;
    private String mobileNumber;
    private String address;
    private String paid;

    public TransactionsPojo() {

    }

    public TransactionsPojo(String billNo, String customerName, String transactionDate, List<ProductPojo> products, List<ScrapPojo> scraps, Double totalOfProduct, Double totalOfScrap, Double subTotal, Double customerAmount, Double discount, Double oldCredit, String mobileNumber, String address) {
        this.billNo = billNo;
        this.customerName = customerName;
        this.transactionDate = transactionDate;
        this.products = products;
        this.scraps = scraps;
        this.totalOfProduct = totalOfProduct;
        this.totalOfScrap = totalOfScrap;
        this.subTotal = subTotal;
        this.customerAmount = customerAmount;
        this.discount = discount;
        this.oldCredit = oldCredit;
        this.mobileNumber = mobileNumber;
        this.address = address;
    }

    public String getTransactionDateOnly() {
        String[] dateTime = SystemUtils.getDateTime(this.transactionDate);
        return dateTime[0];
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTransactionDateTime() {
        return transactionDate;
    }

    public void setTransactionDateTime(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getProducts() {
        return "{\n" +
                "  \"products\":" + products.toString() + "\n" +
                "}";
    }

    public List<ProductPojo> getProductsAsPojo() {
        return products;
    }

    public void setProducts(List<ProductPojo> products) {
        this.products = products;
    }

    public String getScraps() {
        return "{\n" +
                "  \"scraps\": " + scraps.toString() + "\n" +
                "}";
    }

    public List<ScrapPojo> getScrapsAsPojo() {
        return scraps;
    }

    public void setScraps(List<ScrapPojo> scraps) {
        this.scraps = scraps;
    }

    public Double getTotalOfProduct() {
        return totalOfProduct;
    }

    public void setTotalOfProduct(Double totalOfProduct) {
        this.totalOfProduct = totalOfProduct;
    }

    public Double getTotalOfScrap() {
        return totalOfScrap;
    }

    public void setTotalOfScrap(Double totalOfScrap) {
        this.totalOfScrap = totalOfScrap;
    }

    public Double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Double subTotal) {
        this.subTotal = subTotal;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public Double getCustomerAmount() {
        return customerAmount;
    }

    public void setDeposit(Double customerAmount) {
        this.customerAmount = customerAmount;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Double getOldCredit() {
        return oldCredit;
    }

    public void setOldCredit(Double oldCredit) {
        this.oldCredit = oldCredit;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCustomerAmount(Double customerAmount) {
        this.customerAmount = customerAmount;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    @Override
    public String toString() {
        return "TransactionsPojo{" +
                "billNo='" + billNo + '\'' +
                ", customerName='" + customerName + '\'' +
                ", transactionDate='" + transactionDate + '\'' +
                ", products=" + products +
                ", scraps=" + scraps +
                ", totalOfProduct=" + totalOfProduct +
                ", totalOfScrap=" + totalOfScrap +
                ", subTotal=" + subTotal +
                ", customerAmount=" + customerAmount +
                ", discount=" + discount +
                ", oldCredit=" + oldCredit +
                ", mobileNumber='" + mobileNumber + '\'' +
                ", address='" + address + '\'' +
                ", paid='" + paid + '\'' +
                '}';
    }
}
