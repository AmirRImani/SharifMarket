package view.seller;

public class CalculatedProduct {
    private String name;
    private double totalSell;
    private double totalProfit;
    private double soldQuantity;
    private String unit;

    public String getName() { return name; }

    public double getTotalSell() { return totalSell; }

    public double getTotalProfit() { return totalProfit; }

    public double getSoldQuantity() { return soldQuantity; }

    public String getUnit() { return unit; }

    public CalculatedProduct(String name, double totalSell, double totalProfit, double soldQuantity, String unit) {
        this.name = name;
        this.totalSell = totalSell;
        this.totalProfit = totalProfit;
        this.soldQuantity = soldQuantity;
        this.unit = unit;
    }
}
