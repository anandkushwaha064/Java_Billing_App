package org.billing.pagepojo;


public class ProductPojo {
    private String id;
    private String productName;
    private String quantity;
    private String quantityType;
    private String rate;
    private String gst;
    private String total;

    public ProductPojo() {
        quantity = "0.0";
        rate = "0.0";
        total = "0.0";
    }

    public ProductPojo(String id, String productName, String quantity, String quantityType, String rate,String gst, String total) {
        this.id = id;
        this.productName = productName;
        this.quantity = quantity;
        this.rate = rate;
        this.total = total;
        this.gst = gst;
        this.quantityType = quantityType;
    }
//    public ProductPojo(String id, String productName, String quantity, String rate, String total) {
//        this.id = id;
//        this.productName = productName;
//        this.quantity = quantity;
//        this.rate = rate;
//        this.total = total;
//    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getQuantityType() {
        return quantityType;
    }

    public void setQuantityType(String quantityType) {
        this.quantityType = quantityType;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "{\n" +
                "      \"id\": \"" + id + "\",\n" +
                "      \"name\": \"" + productName + "\",\n" +
                "      \"quantity\": \"" + quantity + "\",\n" +
                "      \"quantityType\": \"" + quantityType + "\",\n" +
                "      \"rate\": \"" + rate + "\",\n" +
                "      \"gst\": \"" + gst + "\",\n" +
                "      \"total\": \"" + total + "\"\n" +
                "    }";
    }
}
