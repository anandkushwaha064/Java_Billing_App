package org.billing.pagepojo;
import javafx.scene.control.Button;

public class BillPojo {

    private String SeNo;

    private String CustomerName;

    private String date;

    private String time;

    private String productCost;

    private String scrapCost;

    private String totalCost;

    private String paid;

    private String gotAmount;

    private String oldAmount;

    private String discount;

    private String mobileNumber;

    private Button printButton;

    private Button delete;

    public BillPojo(String seNo, String customerName, String date, String time, String productCost, String scrapCost, String totalCost, String gotAmount, String oldAmount,String discount,String paid, String mobileNumber, Button printButton, Button delete) {
        SeNo = seNo;
        CustomerName = customerName;
        this.date = date;
        this.time = time;
        this.productCost = productCost;
        this.scrapCost = scrapCost;
        this.totalCost = totalCost;
        this.gotAmount = gotAmount;
        this.oldAmount = oldAmount;
        this.discount = discount;
        this.paid = paid;
        this.mobileNumber = mobileNumber;
        this.printButton = printButton;
        this.delete = delete;
    }

    public String getSeNo() {
        return SeNo;
    }

    public void setSeNo(String seNo) {
        SeNo = seNo;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getProductCost() {
        return productCost;
    }

    public void setProductCost(String productCost) {
        this.productCost = productCost;
    }

    public String getScrapCost() {
        return scrapCost;
    }

    public void setScrapCost(String scrapCost) {
        this.scrapCost = scrapCost;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public Button getPrintButton() {
        return printButton;
    }

    public void setPrintButton(Button printButton) {
        this.printButton = printButton;
    }

    public String getGotAmount() {
        return gotAmount;
    }

    public void setGotAmount(String gotAmount) {
        this.gotAmount = gotAmount;
    }

    public String getOldAmount() {
        return oldAmount;
    }

    public void setOldAmount(String oldAmount) {
        this.oldAmount = oldAmount;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Button getDelete() {
        return delete;
    }

    public void setDelete(Button delete) {
        this.delete = delete;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }
}
