import org.junit.jupiter.api.Test;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class FastFoodDashboardTest {
    private final HashMap<String, Integer> prices = new HashMap<>();
    private final HashMap<String, Integer> stock = new HashMap<>();

    public FastFoodDashboardTest() {
        // Data untuk pengujian
        prices.put("Burger", 30000);
        prices.put("Pizza", 40000);
        prices.put("Hot Dog", 25000);
        prices.put("French Fries", 18000);

        stock.put("Burger", 50);
        stock.put("Pizza", 50);
        stock.put("Hot Dog", 50);
        stock.put("French Fries", 50);
    }

    private String formatRupiah(int amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(amount);
    }

    private int calculateTotal(HashMap<String, Integer> selectedItems) {
        int total = 0;
        for (String item : selectedItems.keySet()) {
            int quantity = selectedItems.get(item);
            if (quantity > 0 && prices.containsKey(item)) {
                total += quantity * prices.get(item);
            }
        }
        return total;
    }

    @Test
    public void testCalculateTotal() {
        HashMap<String, Integer> selectedItems = new HashMap<>();
        selectedItems.put("Burger", 2); // 2 x 30000
        selectedItems.put("Pizza", 1); // 1 x 40000
        selectedItems.put("Hot Dog", 0); // 0 x 25000

        int expectedTotal = 100000;
        int actualTotal = calculateTotal(selectedItems);

        assertEquals(expectedTotal, actualTotal, "Total biaya harus sesuai.");
    }

    @Test
    public void testFormatRupiah() {
        int amount = 150000;
        String expectedFormat = "Rp150.000,00";
        String actualFormat = formatRupiah(amount);

        assertEquals(expectedFormat, actualFormat, "Format rupiah harus sesuai.");
    }

    @Test
    public void testStockUpdate() {
        // Stok awal
        int initialStockBurger = stock.get("Burger");

        // Simulasi pengurangan stok
        int purchasedQuantity = 3;
        stock.put("Burger", initialStockBurger - purchasedQuantity);

        int expectedStock = initialStockBurger - purchasedQuantity;
        int actualStock = stock.get("Burger");

        assertEquals(expectedStock, actualStock, "Stok barang harus diperbarui dengan benar.");
    }

    @Test
    public void testInvalidStock() {
        // Simulasi pembelian dengan stok tidak cukup
        int initialStockPizza = stock.get("Pizza");
        int requestedQuantity = 60; // Melebihi stok

        boolean isStockSufficient = requestedQuantity <= initialStockPizza;

        assertFalse(isStockSufficient, "Pembelian harus ditolak jika stok tidak mencukupi.");
    }
}
