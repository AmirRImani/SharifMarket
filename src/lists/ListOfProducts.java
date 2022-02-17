package lists;

import sharedClasses.FileOperator;
import sharedClasses.Product;

import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ListOfProducts {
    private HashSet<Product> products;
    private HashSet<Product> deletedProducts;
    private FileOperator fileOperator;

    private ListOfProducts(){
        fileOperator = new FileOperator("products.json");
        //TODO
        HashSet<String> hashSet1 = fileOperator.loadFile(fileOperator.getFile());
        if(!hashSet1.isEmpty()) products = typeChanger(hashSet1);
        fileOperator = new FileOperator("deletedProducts.json");
        HashSet<String> hashSet2 = fileOperator.loadFile(fileOperator.getFile());
        if(!hashSet2.isEmpty()) deletedProducts = typeChanger(hashSet2);
    }
    private static ListOfProducts listInstance;
    public static ListOfProducts getListInstance(){
        if(listInstance == null)
            listInstance = new ListOfProducts();
        return listInstance;
    }

    public HashSet<Product> getProducts() {
        if(products != null)
            return products;
        return new HashSet<>();
    }

    public HashSet<Product> getNProducts() {
        if (deletedProducts != null)
            return deletedProducts;
        return new HashSet<>();
    }

    public Product getProduct(String productID){
        if(products != null) {
            for (Product product : products) {
                if (product.getProductID().equals(productID))
                    return product;
            }
        }
        if(deletedProducts != null) {
            for (Product product : deletedProducts) {
                if (product.getProductID().equals(productID))
                    return product;
            }
        }
        return null;
    }

    public void save(){
        System.out.println("save");
        fileOperator = new FileOperator("products.json");
        try {
            fileOperator.saveFile(fileOperator.getFile(),products,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(deletedProducts == null)
            deletedProducts = new HashSet<>();
        fileOperator = new FileOperator("deletedProducts.json");
        try {
            fileOperator.saveFile(fileOperator.getFile(),deletedProducts,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean addProduct(Product product){
        if(products == null)
            products = new HashSet<>();
        products.add(product);
        save();
        return true;
    }

    public void deleteProduct(Product product){
        if(deletedProducts == null)
            deletedProducts = new HashSet<>();
        product.delete();
        deletedProducts.add(product);
        products.remove(product);
        save();
    }

    private HashSet<Product> typeChanger(HashSet<String> hashSet) {
        HashSet<Product> product = new HashSet<>();

        Pattern pattern = Pattern.compile("(\\{[^\\}]+\\})");
        Matcher matcher;
        Pattern patternName = Pattern.compile("name=([^,]+)");
        Matcher matcherName;
        Pattern patternProductID = Pattern.compile("productID=([^,]+)");
        Matcher matcherProductID;
        Pattern patternInventory = Pattern.compile("inventory=([^,]+)");
        Matcher matcherInventory;
        Pattern patternSellPrice = Pattern.compile("sellPrice=([^,|.]+)");
        Matcher matcherSellPrice;
        Pattern patternBuyPrice = Pattern.compile("buyPrice=([^,|.]+)");
        Matcher matcherBuyPrice;
        Pattern patternUnit = Pattern.compile("unit=([^,]+)}");
        Matcher matcherUnit;

        String[] split = hashSet.toString().split("},");

        for (String s : split) {
            matcher = pattern.matcher(s+"}");
            matcher.find();
            for (int i = 0; i < matcher.groupCount(); i++) {
                matcherName = patternName.matcher(matcher.group(i));
                matcherProductID = patternProductID.matcher(matcher.group(i));
                matcherInventory = patternInventory.matcher(matcher.group(i));
                matcherSellPrice = patternSellPrice.matcher(matcher.group(i));
                matcherBuyPrice = patternBuyPrice.matcher(matcher.group(i));
                matcherUnit = patternUnit.matcher(matcher.group(i));
                matcherName.find();
                matcherSellPrice.find();
                matcherBuyPrice.find();
                matcherProductID.find();
                matcherInventory.find();
                matcherUnit.find();
                product.add(new Product(matcherName.group(1),
                        matcherProductID.group(1),
                        Double.valueOf(matcherInventory.group(1)),
                        Integer.parseInt(matcherSellPrice.group(1)),
                        Integer.parseInt(matcherBuyPrice.group(1)),
                        matcherUnit.group(1)));
            }
        }
        return product;
    }

    public boolean productExistence(String name){
        if(products == null)
            products = new HashSet<>();

        for (Product product : products) {
            if(product.getName().equals(name))
                return true;
        }

        if(deletedProducts == null)
            deletedProducts = new HashSet<>();

        for (Product deletedProduct : deletedProducts) {
            if(deletedProduct.getName().equals(name))
                return true;
        }
        return false;
    }

    public void toAvailable(Product product) {
        if (products == null)
            products = new HashSet<>();
        products.add(product);
        deletedProducts.remove(product);
        save();
    }
}
