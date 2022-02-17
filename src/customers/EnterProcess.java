package customers;

import lists.ListOfCustomers;

public class EnterProcess {
    private ListOfCustomers list = ListOfCustomers.getListInstance();

    public Customer login(String username, String password) {
        Customer customer;
        if (!list.idExistence(username, password)) {
            return null;
        }else {
            customer = list.getCustomer(username);
            return customer;
        }
    }

    public Customer signup(String name, String password, String email){
        Customer customer;
        if(!list.userExistence(name)) {
            customer = new Customer(name, password, email);
            return customer;
        }
        return null;
    }

    public void addCustomer(Customer customer){
        list.addCustomer(customer);
    }

    public boolean validity(String username, String email) {
        if(list.userExistence(username)){
            if(list.getCustomer(username).getEmail().equals(email))
                return true;
        }
        return false;
    }

    public void newPassword(String username, String password) {
        Customer customer = list.getCustomer(username);
        customer.setPassword(password);
    }


}
