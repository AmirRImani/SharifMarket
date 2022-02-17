package customers;

import lists.ListOfCustomers;
import lists.ListOfProducts;
import sharedClasses.Order;
import sharedClasses.Product;

import java.util.HashMap;

public class Customer {
    private String name;
    private String password;
    private String email;
    private int cash;
    private final int INITIAL_CASH = 1000000;
    private HashMap<String,Double> orders;

    public void setInventory(Product product, double inventory) {
        orders.replace(product.getProductID(), inventory);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() { return name; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public int getCash() { return cash; }

    public Customer(String name, String password, String email) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.cash = INITIAL_CASH;
        this.orders = new HashMap<>();
    }

    public Customer(String name, String password, String email, int cash, HashMap<String, Double> orders) {
        this.name = name;
        this.password = password;
        this.email = email;
        this.cash = cash;
        this.orders = orders;
    }

    public void addOrder(Order order) {
        String productID = order.getProductID();

        if(orders.containsKey(productID))
            orders.replace(productID, orders.get(productID) + Double.parseDouble(order.getInventory()));
        else
            orders.put(productID, Double.parseDouble(order.getInventory()));
        ListOfCustomers.getListInstance().save();
    }

    public void deleteOrder(Order order) {
        String productID = order.getProductID();
        orders.remove(productID);
        ListOfCustomers.getListInstance().save();
    }

    private void deleteOrder(Product product) {
        orders.remove(product);
    }

    public void buy(double totalPrice) {
        for (String productID : orders.keySet()) {
            Product product = ListOfProducts.getListInstance().getProduct(productID);
            product.buy(orders.get(productID));
            deleteOrder(product);
        }
        orders.clear();
        this.cash -= totalPrice;
        ListOfCustomers.getListInstance().save();
    }
}
