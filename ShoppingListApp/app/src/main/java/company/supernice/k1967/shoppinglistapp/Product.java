package company.supernice.k1967.shoppinglistapp;

/**
 * Created by Valtteri on 9.9.2017.
 */

public class Product {

    private String Name;
    private int Quantity;
    private double Price;

    public Product(String name, int quantity, double price) {
        Name = name;
        Quantity = quantity;
        Price = price;
    }

    public String getName(){
        return Name;
    }

    public int getQuantity(){
        return Quantity;
    }

    public double getPrice(){
        return  Price;
    }

}
