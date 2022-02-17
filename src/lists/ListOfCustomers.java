package lists;

import customers.Customer;
import sharedClasses.FileOperator;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListOfCustomers {
    private HashSet<Customer> customers = new HashSet<>();
    private FileOperator fileOperator;

    private static ListOfCustomers listInstance;
    public static ListOfCustomers getListInstance(){
        if(listInstance == null)
            listInstance = new ListOfCustomers();
        return listInstance;
    }

    private ListOfCustomers(){
        fileOperator = new FileOperator("customers.json");
        HashSet<String> hashSet = fileOperator.loadFile(fileOperator.getFile());
        if(!hashSet.isEmpty()) customers = typeChanger(hashSet);
    }

    public Customer getCustomer(String username) {
        for (Customer customer : customers) {
            if(customer.getName().equals(username))
                return customer;
        }
        return null;
    }

    public String restoreUserID(String name, String email){
        for (Customer customer : customers) {
            if (customer.getName().equals(name) && customer.getEmail().equals(email))
                return customer.getPassword();
        }
        return "Not valid";
    }

    public boolean userExistence(String name){
        for (Customer customer : customers) {
            if(customer.getName().equals(name))
                return true;
        }
        return false;
    }

    public boolean idExistence(String username, String password){
        for (Customer customer : customers) {
            if(customer.getName().equals(username) && customer.getPassword().equals(password))
                return true;
        }
        return false;
    }



    public void addCustomer(Customer customer) {
        customers.add(customer);
        save();
    }

    public void save() {
        fileOperator = new FileOperator("customers.json");
        try {
            fileOperator.saveFile(fileOperator.getFile(),customers,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HashSet<Customer> typeChanger(HashSet<String> hashSet){
        HashSet<Customer> customers = new HashSet<>();
        HashMap<String, Double> orders = new HashMap<>();

        Pattern pattern = Pattern.compile("(\\{[^\\}]+\\})");
        Matcher matcher;
        Pattern patternName = Pattern.compile("name=([^,]+)");
        Matcher matcherName;
        Pattern patternPass = Pattern.compile("password=([^,]+)");
        Matcher matcherPass;
        Pattern patternEmail = Pattern.compile("email=([^,]+)");
        Matcher matcherEmail;
        Pattern patternCash = Pattern.compile("cash=(\\d+)");
        Matcher matcherCash;
        Pattern patternOrders = Pattern.compile("orders=\\{([^\\}]+)\\}");
        Matcher matcherOrders;
        Pattern patternProductID = Pattern.compile("([^\\s]{8})=");
        Matcher matcherProductID;
        Pattern patternInventory = Pattern.compile("=(\\d+\\.\\d+)");
        Matcher matcherInventory;

        String[] split = hashSet.toString().split("},");

        for (String s : split) {
            matcher = pattern.matcher(s+"}");
            matcher.find();
            for (int i = 0; i < matcher.groupCount(); i++) {
                matcherName = patternName.matcher(matcher.group(i));
                matcherPass = patternPass.matcher(matcher.group(i));
                matcherEmail = patternEmail.matcher(matcher.group(i));
                matcherCash = patternCash.matcher(matcher.group(i));
                matcherOrders = patternOrders.matcher(matcher.group(i));
                matcherName.find();
                matcherPass.find();
                matcherEmail.find();
                matcherCash.find();

                if(matcherOrders.find()) {
                    String[] splitOrders = matcherOrders.group(1).split(",");
                    for (String splitOrder : splitOrders) {
                        matcherProductID = patternProductID.matcher(splitOrder);
                        matcherInventory = patternInventory.matcher(splitOrder);
                        matcherProductID.find();
                        matcherInventory.find();

                        orders.put(matcherProductID.group(1), Double.parseDouble(matcherInventory.group(1)));

                    }
                }

                customers.add(new Customer(matcherName.group(1), matcherPass.group(1), matcherEmail.group(1)
                , Integer.parseInt(matcherCash.group(1)), orders));
            }
        }
        return customers;
    }

}
