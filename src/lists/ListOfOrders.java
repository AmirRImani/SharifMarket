package lists;

import customers.Customer;
import sharedClasses.FileOperator;
import sharedClasses.Order;
import sharedClasses.Product;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListOfOrders {
    private ArrayList<Order> notCheckedOrders;
    private ArrayList<Order> historyOfOrders;
    private FileOperator fileOperator;

    private static ListOfOrders orderInstance;
    public static ListOfOrders getListInstance(){
        if(orderInstance == null)
            orderInstance = new ListOfOrders();
        return orderInstance;
    }

    private ListOfOrders(){
        notCheckedOrders = new ArrayList<>();
        fileOperator = new FileOperator("notCheckedOrders.json");
        ArrayList<String> arrayList1 = fileOperator.loadFile(fileOperator.getFile(), 0);
        if(!arrayList1.isEmpty()) notCheckedOrders = typeChanger(arrayList1);

        historyOfOrders = new ArrayList<>();
        fileOperator = new FileOperator("historyOfOrders.json");
        ArrayList<String> arrayList2 = fileOperator.loadFile(fileOperator.getFile(), 0);
        if(!arrayList2.isEmpty()) historyOfOrders = typeChanger(arrayList2);
    }

    public HashSet<Order> getOrders(Customer customer) {
        HashSet<Order> orders = new HashSet<>();
        if (notCheckedOrders != null) {
            for (Order order : notCheckedOrders) {
                if (order.getUsername().equals(customer.getName()))
                    orders.add(order);
            }
        }
        return orders;
    }

    public ArrayList<Order> getHistoryOrders() {
        return historyOfOrders;
    }

    public void save(){
        fileOperator = new FileOperator("notCheckedOrders.json");
        try {
            fileOperator.saveFile(fileOperator.getFile(), notCheckedOrders);
        } catch (IOException e) {
            e.printStackTrace();
        }

        fileOperator = new FileOperator("historyOfOrders.json");
        if (historyOfOrders == null)
            historyOfOrders = new ArrayList<>();
        try {
            fileOperator.saveFile(fileOperator.getFile(), historyOfOrders);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void addOrder(Order order) {
        if(notCheckedOrders == null)
            notCheckedOrders = new ArrayList<>();
        notCheckedOrders.add(order);
        save();
        ListOfProducts listOfProducts = ListOfProducts.getListInstance();
        listOfProducts.save();
    }

    public void deleteOrder(Order order){
        if(notCheckedOrders == null)
            notCheckedOrders = new ArrayList<>();
        notCheckedOrders.remove(order);
        save();
        ListOfProducts listOfProducts = ListOfProducts.getListInstance();
        listOfProducts.save();
    }

    private ArrayList<Order> typeChanger(ArrayList<String> arrayList){
        ArrayList<Order> arrayList1 = new ArrayList<>();

        Pattern pattern = Pattern.compile("(\\{[^\\}]+\\})");
        Matcher matcher;
        Pattern patternUsername = Pattern.compile("username=([^,]+)");
        Matcher matcherUsername;
        Pattern patternDate = Pattern.compile("dateOfOrder=([^,]+)");
        Matcher matcherDate;
        Pattern patternProductID = Pattern.compile("productID=([a-z|0-9]{8})");
        Matcher matcherProductID;
        Pattern patternInventory = Pattern.compile("inventory=([^,]+)");
        Matcher matcherInventory;
        Pattern patternOrderID = Pattern.compile("orderID=([a-z|0-9]{8}+)");
        Matcher matcherOrderID;
        Pattern patternSell = Pattern.compile("sell=(\\d+)");
        Matcher matcherSell;
        Pattern patternProfit = Pattern.compile("profit=(\\d+)");
        Matcher matcherProfit;

        String[] split = arrayList.toString().split("},");

        for (String s : split) {
            matcher = pattern.matcher(s+"}");
            matcher.find();
            for (int i = 0; i < matcher.groupCount(); i++) {
                matcherUsername = patternUsername.matcher(matcher.group(i));
                matcherDate = patternDate.matcher(matcher.group(i));
                matcherProductID = patternProductID.matcher(matcher.group(i));
                matcherInventory = patternInventory.matcher(matcher.group(i));
                matcherOrderID = patternOrderID.matcher(matcher.group(i));
                matcherSell = patternSell.matcher(matcher.group(i));
                matcherProfit = patternProfit.matcher(matcher.group(i));
                matcherUsername.find();
                matcherDate.find();
                matcherProductID.find();
                matcherInventory.find();
                matcherOrderID.find();
                matcherSell.find();
                matcherProfit.find();

                System.out.println(matcherProductID.group(1));

                arrayList1.add(new Order(matcherUsername.group(1),
                        matcherDate.group(1), matcherProductID.group(1), matcherInventory.group(1), matcherOrderID.group(1),
                        Integer.parseInt(matcherSell.group(1)), Integer.parseInt(matcherProfit.group(1))));
            }
        }
        return arrayList1;
    }

    public double orderCounter(String productID){
        int counter = 0;
        if(historyOfOrders != null) {
            for (Order order : historyOfOrders)
                if (order.getProductID().equals(productID)) counter += Double.valueOf(order.getInventory());
        }
        return counter;
    }

    public void buy(Customer customer) {
        ArrayList<Order> orders = new ArrayList<>(notCheckedOrders);
        for (Order order : notCheckedOrders) {
            if (order.getUsername().equals(customer.getName())) {
                orders.remove(order);
                historyOfOrders.add(order);
            }
        }
        notCheckedOrders = new ArrayList<>(orders);
        save();
    }

    public int totalSell(Product product) {
        int sell = 0;
        for (Order historyOfOrder : historyOfOrders) {
            if(historyOfOrder.getProductName().equals(product.getName()))
                sell += historyOfOrder.getSell();
        }
        return sell;
    }

    public int totalProfit(Product product) {
        int profit = 0;
        for (Order historyOfOrder : historyOfOrders) {
            if(historyOfOrder.getProductName().equals(product.getName()))
                profit += historyOfOrder.getProfit();
        }
        return profit;
    }
}
