package org.example;

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class FastFoodDashboard {
    private JFrame frame;
    private JLabel totalBillLabel;
    private JTextArea orderSummaryArea;
    private final Map<String, Integer> stock = new HashMap<>();
    private final Map<String, Integer> prices = new HashMap<>();
    private final Map<String, JSpinner> spinnerMap = new HashMap<>();
    private final Map<String, JLabel> stockLabels = new HashMap<>();
    private final ArrayList<String> transactionHistory = new ArrayList<>();

    public FastFoodDashboard() {
        initializeStockAndPrices();
        initializeGUI();
        updateOrderSummary(); // Display initial order summary
    }

    private void initializeStockAndPrices() {
        // Data untuk makanan
        stock.put("Burger", 50);
        stock.put("Pizza", 50);
        stock.put("Hot Dog", 50);
        stock.put("French Fries", 50);
        prices.put("Burger", 30000);
        prices.put("Pizza", 40000);
        prices.put("Hot Dog", 25000);
        prices.put("French Fries", 18000);

        // Data untuk minuman
        stock.put("Pepsi", 50);
        stock.put("Es Teh", 50);
        stock.put("Iced Coffee", 50);
        stock.put("Fanta", 50);
        prices.put("Pepsi", 15000);
        prices.put("Es Teh", 10000);
        prices.put("Iced Coffee", 20000);
        prices.put("Fanta", 15000);
    }

    private void initializeGUI() {
        frame = new JFrame("Dasbor Fast Food");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 600);

        // Set a light pink background color for the main frame
        frame.getContentPane().setBackground(new Color(255, 182, 193)); // Light Pink

        frame.setLayout(new BorderLayout());

        // Sidebar untuk kategori
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(new Color(255, 105, 180)); // Hot Pink

        JLabel titleLabel = new JLabel("MC BOY", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 24));

        sidebar.add(titleLabel, BorderLayout.NORTH);

        JList<String> categoryList = new JList<>(new String[]{"Makanan", "Minuman"});
        categoryList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        categoryList.setSelectedIndex(0);

        sidebar.add(new JScrollPane(categoryList), BorderLayout.CENTER);

        JButton adminButton = new JButton("Login Admin");
        adminButton.addActionListener(e -> showAdminLogin());

        // Customize button colors
        adminButton.setBackground(Color.BLUE); // Blue background
        adminButton.setForeground(Color.WHITE); // White text

        sidebar.add(adminButton, BorderLayout.SOUTH);

        frame.add(sidebar, BorderLayout.WEST);

        // Panel utama untuk makanan dan minuman
        JPanel foodPanel = createFoodPanel();
        JPanel drinkPanel = createDrinkPanel();

        JPanel mainPanel = new JPanel(new CardLayout());
        mainPanel.add(foodPanel, "Makanan");
        mainPanel.add(drinkPanel, "Minuman");

        frame.add(mainPanel, BorderLayout.CENTER);

        // Panel kanan untuk ringkasan pesanan
        JPanel orderSummaryPanel = new JPanel(new BorderLayout());
        orderSummaryPanel.setBorder(BorderFactory.createTitledBorder("Ringkasan Pesanan"));

        orderSummaryArea = new JTextArea();
        orderSummaryArea.setEditable(false);

        orderSummaryArea.setFont(new Font("Monospaced", Font.PLAIN, 12));

        JScrollPane scrollPane = new JScrollPane(orderSummaryArea);

        orderSummaryPanel.add(scrollPane, BorderLayout.CENTER);

        frame.add(orderSummaryPanel, BorderLayout.EAST);

        // Total dan tombol pembayaran
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalBillLabel = new JLabel("Total: Rp0,00");
        totalBillLabel.setFont(new Font("Serif", Font.BOLD, 16));

        bottomPanel.add(totalBillLabel);

        JButton proceedButton = new JButton("Lanjutkan Pembayaran");
        proceedButton.addActionListener(e -> showPaymentOptions());

        // Customize button colors
        proceedButton.setBackground(Color.GREEN); // Green background
        proceedButton.setForeground(Color.BLACK); // Black text

        bottomPanel.add(proceedButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);

        // Listener untuk kategori
        categoryList.addListSelectionListener(e -> {
            if (categoryList.getSelectedValue() != null) {
                CardLayout cl = (CardLayout) mainPanel.getLayout();
                cl.show(mainPanel, categoryList.getSelectedValue());
            }
        });

        frame.setVisible(true);
    }

    private JPanel createFoodPanel() {
        return createItemPanel(new String[]{"Burger", "Pizza", "Hot Dog", "French Fries"});
    }

    private JPanel createDrinkPanel() {
        return createItemPanel(new String[]{"Pepsi", "Es Teh", "Iced Coffee", "Fanta"});
    }

    private JPanel createItemPanel(String[] items) {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        for (String item : items) {
            JPanel itemPanel = new JPanel(new BorderLayout());
            itemPanel.setBorder(BorderFactory.createLineBorder(Color.RED));

            JLabel nameLabel = new JLabel(item, SwingConstants.CENTER);
            nameLabel.setFont(new Font("Arial", Font.BOLD, 16));

            JLabel priceLabel = new JLabel(formatRupiah(prices.get(item)), SwingConstants.CENTER);
            priceLabel.setFont(new Font("Arial", Font.PLAIN, 14));

            int maxStock = stock.getOrDefault(item, 0);
            if (maxStock < 0) maxStock = 0;

            JLabel stockLabel = new JLabel(maxStock + " barang tersedia", SwingConstants.CENTER);
            stockLabels.put(item, stockLabel);

            JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, maxStock, 1));
            quantitySpinner.addChangeListener(e -> updateOrderSummary());

            spinnerMap.put(item, quantitySpinner);


            JLabel imageLabel = getjLabel(item);

            JPanel bottomPanel = new JPanel(new BorderLayout());
            bottomPanel.add(stockLabel, BorderLayout.NORTH);
            bottomPanel.add(quantitySpinner, BorderLayout.CENTER);

            itemPanel.add(imageLabel, BorderLayout.NORTH);
            itemPanel.add(priceLabel, BorderLayout.CENTER);
            itemPanel.add(bottomPanel, BorderLayout.SOUTH);

            panel.add(itemPanel);
        }
        return panel;
    }

    private JLabel getjLabel(String item) {
        String imagePath = "images/" + item.toLowerCase().replaceAll(" ", "") + ".png";
        ImageIcon itemImage = new ImageIcon(imagePath);

        // Resize the image to a fixed size (e.g., width: 100px and height: 100px)
        Image img = itemImage.getImage();
        Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        itemImage = new ImageIcon(scaledImg);

        JLabel imageLabel = new JLabel(itemImage);
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        return imageLabel;
    }

    private void updateOrderSummary() {
        StringBuilder summary = new StringBuilder();
        int totalBill = 0;

        for (String item : spinnerMap.keySet()) {
            JSpinner spinner = spinnerMap.get(item);
            int quantity = (Integer) spinner.getValue();

            if (quantity > 0) {
                int price = prices.get(item);
                totalBill += quantity * price;
                summary.append(String.format("%s x%d = %s\n",
                        item,
                        quantity,
                        formatRupiah(quantity * price)));
            }
        }

        if (summary.isEmpty()) {
            summary.append("Tidak ada barang yang dipilih.");
        }

        orderSummaryArea.setText(summary.toString());
        totalBillLabel.setText("Total: " + formatRupiah(totalBill));
    }

    private void showPaymentOptions() {
        if (orderSummaryArea.getText().isEmpty() || orderSummaryArea.getText().equals("Tidak ada barang yang dipilih.")) {
            JOptionPane.showMessageDialog(frame, "Tidak ada barang yang dipilih!");
            return;
        }

        JPanel paymentPanel = new JPanel(new GridLayout(0, 1));
        JRadioButton cashButton = new JRadioButton("Tunai");
        JRadioButton transferButton = new JRadioButton("Transfer Bank");
        JRadioButton qrisButton = new JRadioButton("QRIS");

        ButtonGroup paymentGroup = new ButtonGroup();
        paymentGroup.add(cashButton);
        paymentGroup.add(transferButton);
        paymentGroup.add(qrisButton);

        paymentPanel.add(cashButton);
        paymentPanel.add(transferButton);
        paymentPanel.add(qrisButton);

        int result = JOptionPane.showConfirmDialog(frame,
                paymentPanel,
                "Pilih Metode Pembayaran",
                JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            String paymentMethod;

            if (cashButton.isSelected()) {
                printReceipt();
                paymentMethod = "Tunai";
                JOptionPane.showMessageDialog(frame,
                        "Terima kasih! Pembayaran tunai berhasil.");
                processOrder(paymentMethod);
            } else if (transferButton.isSelected()) {
                String[] banks =
                        {"Bank Mandiri - 1234567890",
                                "BCA - 0987654321",
                                "BNI - 1122334455"};
                String selectedBank =
                        (String) JOptionPane.showInputDialog(frame,
                                "Pilih bank:",
                                "Transfer Bank",
                                JOptionPane.QUESTION_MESSAGE,
                                null,
                                banks,
                                banks[0]);
                if (selectedBank != null) {
                    paymentMethod = selectedBank;
                    JOptionPane.showMessageDialog(frame,
                            "Gunakan Virtual Account ini untuk pembayaran:\n" + selectedBank +
                                    "\nTerima kasih! Pembayaran berhasil.");
                    processOrder(paymentMethod);
                }
            } else if (qrisButton.isSelected()) {
                // Load and display the QRIS image with reduced size
                ImageIcon qrCodeImageIcon = new ImageIcon("images/qris.png");
                Image qrCodeImageResized = qrCodeImageIcon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH); // Resize to smaller size
                qrCodeImageIcon = new ImageIcon(qrCodeImageResized);

                JLabel qrCodeLabel = new JLabel(qrCodeImageIcon);
                qrCodeLabel.setHorizontalAlignment(SwingConstants.CENTER);

                // Show QRIS code in a dialog
                JOptionPane.showMessageDialog(frame,
                        qrCodeLabel,
                        "Scan Kode QRIS",
                        JOptionPane.PLAIN_MESSAGE);

                paymentMethod = "QRIS";
                JOptionPane.showMessageDialog(frame,
                        "Terima kasih! Pembayaran QRIS berhasil.");
                processOrder(paymentMethod);
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Silakan pilih metode pembayaran.");
            }
        }
    }

    private void printReceipt() {
        StringBuilder receipt =
                new StringBuilder();
        receipt.append("Struk Pembayaran\n");
        receipt.append("-----------------\n");

        for (String item : spinnerMap.keySet()) {
            JSpinner spinner =
                    spinnerMap.get(item);
            int quantity =
                    (Integer) spinner.getValue();

            if (quantity > 0) {
                int price =
                        prices.get(item);
                receipt.append(String.format("%s x%d = %s\n",
                        item,
                        quantity,
                        formatRupiah(quantity * price)));
            }
        }

        receipt.append("-----------------\n");
        receipt.append("Total: ").append(formatRupiah(calculateTotal()));

        JTextArea receiptArea =
                new JTextArea(receipt.toString());
        receiptArea.setEditable(false);

        JOptionPane.showMessageDialog(frame,
                new JScrollPane(receiptArea),
                "Struk Pembayaran",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void processOrder(String paymentMethod) {
        int totalBill =
                calculateTotal();

        StringBuilder transactionDetails =
                new StringBuilder();

        transactionDetails.append(
                String.format("Metode Pembayaran: %s\n", paymentMethod));

        for (String item : spinnerMap.keySet()) {
            JSpinner spinner =
                    spinnerMap.get(item);
            int quantity =
                    (Integer) spinner.getValue();

            if (quantity > 0) {
                stock.put(item,
                        stock.get(item) - quantity);
                stockLabels.get(item).setText(stock.get(item) +
                        " barang tersedia");
                spinner.setValue(0);

                transactionDetails.append(
                        String.format("%s x%d\n",
                                item,
                                quantity));
            }
        }

        transactionHistory.add(transactionDetails +
                String.format("Total: %s\n--------------------\n",
                        formatRupiah(totalBill)));

        totalBillLabel.setText(
                "Total: Rp0,00");
        updateOrderSummary();

        JOptionPane.showMessageDialog(frame,
                "Pesanan diproses! Total: "
                        + formatRupiah(totalBill));
    }

    private void showAdminLogin() {
        JTextField usernameField =
                new JTextField(15);
        JPasswordField passwordField =
                new JPasswordField(15);

        JPanel loginPanel =
                new JPanel();
        loginPanel.setLayout(
                new GridLayout(2,2));
        loginPanel.add(new JLabel(
                "Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel(
                "Password:"));
        loginPanel.add(passwordField);

        int result =
                JOptionPane.showConfirmDialog(frame,
                        loginPanel,
                        "Login Admin",
                        JOptionPane.OK_CANCEL_OPTION);

        if(result == JOptionPane.OK_OPTION){
            String username =
                    usernameField.getText();
            String password =
                    new String(passwordField.getPassword());

            if ("admin".equals(username) &&
                    "password".equals(password)) {
                showAdminMenu();
            } else {
                JOptionPane.showMessageDialog(frame,
                        "Username atau password salah!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showAdminMenu() {
        String[] options =
                {"Tambah Stok Makanan/Minuman","Lihat Riwayat Transaksi"};
        int choice =
                JOptionPane.showOptionDialog(frame,
                        "Menu Admin",
                        "Admin Menu",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        options,
                        options[0]);

        switch(choice){
            case 0:
                addStockMenu();
                break;
            case 1:
                showTransactionHistory();
                break;
            default:
                break;
        }
    }

    private void addStockMenu() {
        String[] itemsToAddStock =
                {"Burger","Pizza","Hot Dog","French Fries",
                        "Pepsi","Es Teh","Iced Coffee","Fanta"};

        String selectedItem =
                (String)JOptionPane.showInputDialog(frame,
                        "Pilih item untuk menambah stok:",
                        "Tambah Stok",
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        itemsToAddStock,
                        itemsToAddStock[0]);

        String inputQuantity =
                JOptionPane.showInputDialog(frame,"Masukkan jumlah stok yang ingin ditambahkan:");

        try {
            int quantityToAdd =
                    Integer.parseInt(inputQuantity.trim());

            if(quantityToAdd >0 && selectedItem != null){
                stock.put(selectedItem,
                        stock.get(selectedItem)+quantityToAdd);

                stockLabels.get(selectedItem).setText(stock.get(selectedItem)+
                        " barang tersedia");

                JOptionPane.showMessageDialog(frame,"Stok telah ditambahkan untuk "
                        + selectedItem + ".");
            } else {
                JOptionPane.showMessageDialog(frame,"Jumlah stok tidak valid!");
            }

        } catch(NumberFormatException e){
            JOptionPane.showMessageDialog(frame,"Input tidak valid!");
        }
    }

    private void showTransactionHistory() {
        StringBuilder historyBuilder =
                new StringBuilder();

        historyBuilder.append("Riwayat Transaksi:\n");
        historyBuilder.append("--------------------\n");

        for(String transaction : transactionHistory){
            historyBuilder.append(transaction).append("\n");
        }

        JTextArea historyArea =
                new JTextArea(historyBuilder.toString());
        historyArea.setEditable(false);

        JOptionPane.showMessageDialog(frame,new JScrollPane(historyArea),"Riwayat Transaksi",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private int calculateTotal() {
        int totalBill=0;

        for(String item : spinnerMap.keySet()){
            JSpinner spinner=
                    spinnerMap.get(item);

            int quantity=
                    (Integer)spinner.getValue();

            if(quantity >0){
                int price=
                        prices.get(item);

                totalBill+=quantity*price;
            }
        }

        return totalBill;
    }

    private String formatRupiah(int amount) {
        NumberFormat formatter;
        formatter = NumberFormat.getCurrencyInstance(
                new Locale("id","ID"));

        return formatter.format(amount);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FastFoodDashboard::new);
    }
}
