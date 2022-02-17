package customers;

import lists.ListOfOrders;
import lists.ListOfProducts;
import sharedClasses.Order;
import sharedClasses.Product;
import java.util.Scanner;

public class CustomerProcess {
    private Scanner scanner = new Scanner(System.in);
    private Customer customer;
    private ListOfProducts listOfProducts = ListOfProducts.getListInstance();
    private ListOfOrders listOfOrders = ListOfOrders.getListInstance();

    public CustomerProcess(Customer customer) {
        this.customer = customer;
    }

    public boolean order(Customer customer, Product product, double inventory, int sell, int profit){
        if(enough(product, inventory)) {
            Order order = new Order(customer.getName(), product.getProductID(), Double.toString(inventory), sell, profit);
            listOfOrders.addOrder(order);
            customer.addOrder(order);
            return true;
        }
        return false;
    }

    public boolean enough(Product product, double inventory) {
        return product.getInventory() >= inventory;
    }

    public void deleteOrder(Customer customer, Order order) {
        listOfOrders.deleteOrder(order);
        customer.deleteOrder(order);
    }
}
