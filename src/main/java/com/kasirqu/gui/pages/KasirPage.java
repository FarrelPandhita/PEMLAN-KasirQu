package com.kasirqu.gui.pages;

import com.kasirqu.facade.CartFacade;
import com.kasirqu.facade.InventoryFacade;
import com.kasirqu.facade.TransactionFacade;
import com.kasirqu.gui.shared.ActionButton;
import com.kasirqu.gui.shared.ClockPanel;
import com.kasirqu.gui.shared.TotalBox;
import com.kasirqu.models.Barang;
import com.kasirqu.models.CartItem;
import com.kasirqu.models.Transaksi;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class KasirPage extends JPanel {

    private final InventoryFacade inventoryFacade;
    private final CartFacade cartFacade;
    private final TransactionFacade transactionFacade;

    private JTable kasirTable;
    private DefaultTableModel kasirModel;

    private TotalBox subtotalBox;
    private TotalBox discountBox;
    private TotalBox taxBox;
    private TotalBox totalBox;

    public KasirPage() {
        this.inventoryFacade = new InventoryFacade();
        this.cartFacade = new CartFacade();
        this.transactionFacade = new TransactionFacade();

        setLayout(new BorderLayout());
        add(buildTopPanel(),    BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
        
        refreshCartTable();
    }

    // ── TOP: Store Info + Clock ───────────────────────────────
    private JPanel buildTopPanel() {
        JPanel top = new JPanel(new GridLayout(1, 2, 10, 10));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel storePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        storePanel.setBorder(BorderFactory.createTitledBorder("Store Info"));
        storePanel.add(new JLabel("Nama Kasir"));
        storePanel.add(new JTextField("Admin Kasir"));
        storePanel.add(new JLabel("Status"));
        storePanel.add(new JTextField("Ready"));

        top.add(storePanel);
        top.add(new ClockPanel());
        return top;
    }

    // ── CENTER: Table + Right Buttons ────────────────────────
    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout());
        center.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        center.add(buildTablePanel(), BorderLayout.CENTER);
        center.add(buildRightPanel(), BorderLayout.EAST);
        return center;
    }

    private JScrollPane buildTablePanel() {
        String[] cols = {"ID", "Kode", "Description", "Price", "Qty", "Ext Price"};
        
        kasirModel = new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        kasirTable = new JTable(kasirModel);
        kasirTable.setRowHeight(30);
        kasirTable.setFont(new Font("Arial", Font.PLAIN, 13));
        kasirTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        kasirTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        return new JScrollPane(kasirTable);
    }

    private JPanel buildRightPanel() {
        JPanel right = new JPanel(new GridLayout(9, 1, 5, 5));
        right.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 10));

        JLabel itemActionsLabel = new JLabel("Cart Actions");
        itemActionsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        itemActionsLabel.setForeground(Color.GRAY);

        ActionButton btnAdd    = new ActionButton("Add to Cart", new Color(0x1D9E75));
        ActionButton btnEdit   = new ActionButton("Edit Qty",    new Color(0x185FA5));
        ActionButton btnRemove = new ActionButton("Remove Item", new Color(0xA32D2D));
        ActionButton btnClear  = new ActionButton("Clear Cart",  new Color(0x854F0B));

        btnAdd.addActionListener(e -> handleAddToCart());
        btnEdit.addActionListener(e -> handleEditQty());
        btnRemove.addActionListener(e -> handleRemoveItem());
        btnClear.addActionListener(e -> {
            cartFacade.clearCart();
            refreshCartTable();
        });

        JButton btnCheckout = new JButton("Checkout / Print");
        btnCheckout.setFont(new Font("Arial", Font.BOLD, 13));
        btnCheckout.setBackground(new Color(0x1D9E75));
        btnCheckout.setForeground(Color.WHITE);
        btnCheckout.addActionListener(e -> handleCheckout());

        right.add(itemActionsLabel);
        right.add(btnAdd);
        right.add(btnEdit);
        right.add(btnRemove);
        right.add(btnClear);
        right.add(new JSeparator());
        right.add(new JLabel());
        right.add(new JLabel());
        right.add(btnCheckout);
        return right;
    }

    // ── BOTTOM: Totals ────────────────────────────────────────
    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new GridLayout(1, 4, 10, 10));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        subtotalBox = new TotalBox("Sub Total", false);
        discountBox = new TotalBox("Discount (0%)",  false);
        taxBox      = new TotalBox("Tax (11%)", false);
        totalBox    = new TotalBox("Total",     true);

        bottom.add(subtotalBox);
        bottom.add(discountBox);
        bottom.add(taxBox);
        bottom.add(totalBox);
        return bottom;
    }

    // ── LOGIC ─────────────────────────────────────────────────
    private void refreshCartTable() {
        kasirModel.setRowCount(0);
        List<CartItem> items = cartFacade.getCartItems();
        
        for (CartItem item : items) {
            Barang b = item.getBarang();
            kasirModel.addRow(new Object[]{
                b.getIdBarang(),
                b.getKodeBarang(),
                b.getNamaBarang(),
                b.getHarga(),
                item.getQty(),
                item.getSubtotal()
            });
        }
        calculateTotal();
    }

    private void calculateTotal() {
        BigDecimal subtotal = cartFacade.calculateSubtotal();
        // Diskon 0% for now
        BigDecimal discount = BigDecimal.ZERO; 
        // Pajak 11%
        BigDecimal tax = subtotal.multiply(new BigDecimal("0.11"));
        BigDecimal grandTotal = subtotal.subtract(discount).add(tax);

        subtotalBox.setValue(String.format("Rp %.2f", subtotal));
        discountBox.setValue(String.format("Rp %.2f", discount));
        taxBox.setValue(String.format("Rp %.2f", tax));
        totalBox.setValue(String.format("Rp %.2f", grandTotal));
    }

    private void handleAddToCart() {
        String kode = JOptionPane.showInputDialog(this, "Masukkan Kode Barang:");
        if (kode == null || kode.trim().isEmpty()) return;

        Barang b = inventoryFacade.getProductByCode(kode);
        if (b == null) {
            JOptionPane.showMessageDialog(this, "Barang tidak ditemukan!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String qtyStr = JOptionPane.showInputDialog(this, "Qty:", "1");
        if (qtyStr == null) return;

        try {
            int qty = Integer.parseInt(qtyStr);
            if (qty <= 0) throw new NumberFormatException();
            cartFacade.addItem(b, qty);
            refreshCartTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Qty tidak valid atau stok tidak cukup: " + ex.getMessage());
        }
    }

    private void handleEditQty() {
        int row = kasirTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih item di cart terlebih dahulu.");
            return;
        }

        int idBarang = (int) kasirModel.getValueAt(row, 0);
        String currentQty = kasirModel.getValueAt(row, 4).toString();

        String qtyStr = JOptionPane.showInputDialog(this, "Update Qty:", currentQty);
        if (qtyStr == null) return;

        try {
            int newQty = Integer.parseInt(qtyStr);
            if (newQty <= 0) throw new NumberFormatException();
            cartFacade.updateItemQty(idBarang, newQty);
            refreshCartTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Qty tidak valid: " + ex.getMessage());
        }
    }

    private void handleRemoveItem() {
        int row = kasirTable.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "Pilih item di cart terlebih dahulu.");
            return;
        }

        int idBarang = (int) kasirModel.getValueAt(row, 0);
        cartFacade.removeItem(idBarang);
        refreshCartTable();
    }

    private void handleCheckout() {
        if (cartFacade.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Cart masih kosong!");
            return;
        }

        try {
            BigDecimal subtotal = cartFacade.calculateSubtotal();
            BigDecimal diskon = BigDecimal.ZERO;
            BigDecimal pajak = subtotal.multiply(new BigDecimal("0.11"));

            var result = transactionFacade.checkout(
                cartFacade.getCartItems(),
                "Admin Kasir",
                diskon,
                pajak,
                "cash",
                "Pembelian Langsung"
            );
            
            JOptionPane.showMessageDialog(this, 
                "Checkout Berhasil!\nNo Invoice: " + result.getKodeTransaksi() +
                "\nTotal: Rp " + result.getGrandTotal(), 
                "Success", JOptionPane.INFORMATION_MESSAGE);
                
            cartFacade.clearCart();
            refreshCartTable();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Checkout Gagal: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ── Utility ───────────────────────────────────────────────
    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
}