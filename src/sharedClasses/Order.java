package sharedClasses;

import lists.ListOfOrders;
import lists.ListOfProducts;

import java.util.Date;
import java.util.UUID;

public class Order {
    private String username;
    private String dateOfOrder;
    private String productName;
    private String productID;
    private String inventory;
    private String unit;
    private String orderID;
    private int sell;
    private int profit;

    public Order() { }

    public void setInventory(double inventory) {
        this.inventory = Double.toString(inventory);
        ListOfOrders.getListInstance().save();
    }

    public String getOrderID() { return orderID; }

    public String getProductID() { return productID; }

    public String getProductName() { return productName; }

    public String getInventory() { return inventory; }

    public String getUsername() { return username; }

    public String getUnit() { return unit; }

    public int getSell() { return sell; }

    public int getProfit() { return profit; }

    public String getDateOfOrder() {
        return dateOfOrder;
    }

    public Order(String username, String productID, String inventory, int sell, int profit) {
        this.username = username;
        this.productID = productID;
        this.productName = ListOfProducts.getListInstance().getProduct(productID).getName();
        this.inventory = inventory;
        this.dateOfOrder = (new Date()).toString();
        this.orderID = uniqueID();
        this.unit = ListOfProducts.getListInstance().getProduct(productID).getUnit();
        this.sell = sell;
        this.profit = profit;
    }

    public Order(String username, String dateOfOrder, String productID, String inventory, String orderID, int sell, int profit) {
        this.username = username;
        this.dateOfOrder = dateOfOrder;
        this.productID = productID;
        this.productName = ListOfProducts.getListInstance().getProduct(productID).getName();
        this.inventory = inventory;
        this.orderID = orderID;
        this.unit = ListOfProducts.getListInstance().getProduct(productID).getUnit();
        this.sell = sell;
        this.profit = profit;
    }

    private String uniqueID(){
        String uniqueID = (String) UUID.randomUUID().toString().subSequence(0,8);
        return uniqueID;
    }

}
