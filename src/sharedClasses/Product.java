package sharedClasses;

import lists.ListOfProducts;

import java.util.UUID;

public class Product {
    private String name;
    private String productID;
    private double inventory;
    private int sellPrice;
    private int buyPrice;
    private String unit;

    public Product(String name, double inventory, int sellPrice, int buyPrice, String unit) {
        this.name = name;
        this.inventory = inventory;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.unit = unit;
        this.productID = uniqueID();
    }

    public Product(String name, String productID, double inventory, int sellPrice, int buyPrice, String unit) {
        this.name = name;
        this.productID = productID;
        this.inventory = inventory;
        this.sellPrice = sellPrice;
        this.buyPrice = buyPrice;
        this.unit = unit;
    }

    public String getName() { return name; }

    public String getProductID() { return productID; }

    public double getInventory() { return inventory; }

    public int getSellPrice() { return sellPrice; }

    public int getBuyPrice() { return buyPrice; }

    public String getUnit() { return unit; }

    private String uniqueID(){
        String uniqueID = (String) UUID.randomUUID().toString().subSequence(0,8);
        return uniqueID;
    }

    public void editProductName(String name){
        this.name = name;
        ListOfProducts.getListInstance().save();
    }

    public void editProductInventory(String inventory){
        boolean deleted = this.inventory <= 0;
        this.inventory = Double.parseDouble(inventory);
        ListOfProducts.getListInstance().save();
        if (deleted)
            ListOfProducts.getListInstance().toAvailable(this);
    }

    public boolean buy(double inventory){
        ListOfProducts list = ListOfProducts.getListInstance();
        if(this.inventory >= inventory) {
            this.inventory -= inventory;
            if (this.inventory <= 0)
                list.deleteProduct(this);
            ListOfProducts.getListInstance().save();
            return true;
        } else
            return false;
    }

    public void delete() {
        this.inventory = 0;
    }

    public boolean editBuyPrice(String text) {
        int buyPrice = Integer.parseInt(text);
        if(buyPrice < this.sellPrice) {
            this.buyPrice = buyPrice;
            ListOfProducts.getListInstance().save();
            return true;
        }
        return false;
    }

    public boolean editSellPrice(String text) {
        int sellPrice = Integer.parseInt(text);
        if(sellPrice > this.buyPrice) {
            this.sellPrice = sellPrice;
            ListOfProducts.getListInstance().save();
            return true;
        }
        return false;
    }
}
