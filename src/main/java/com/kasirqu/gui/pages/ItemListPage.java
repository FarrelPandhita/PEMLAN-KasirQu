package com.kasirqu.gui.pages;

import com.kasirqu.gui.shared.ActionButton;
import com.kasirqu.gui.shared.ItemDialogs;
import com.kasirqu.gui.shared.StatCard;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItemListPage extends JPanel {

    // ── Sample data ───────────────────────────────────────────
    private final Object[][] storeItems = {
        {"5562", "Megan T Shirt",   "Pakaian",  "15.29", "24", "10"},
        {"5563", "Bottle Drink",    "Minuman",  "2.96",  "52", "10"},
        {"5564", "Snack Food",      "Makanan",  "2.96",  "3",  "10"},
        {"0001", "Running Shoes",   "Sepatu",   "45.00", "8",  "0"},
        {"0002", "Polo Shirt",      "Pakaian",  "18.50", "15", "5"},
        {"0003", "Mineral Water",   "Minuman",  "1.20",  "0",  "0"},
        {"0004", "Chocolate Bar",   "Makanan",  "3.50",  "40", "0"},
        {"0005", "Sport Sandals",   "Sepatu",   "22.00", "4",  "15"},
        {"0006", "Hoodie Jacket",   "Pakaian",  "35.00", "9",  "0"},
        {"0007", "Energy Drink",    "Minuman",  "4.50",  "28", "0"},
        {"0008", "Instant Noodle",  "Makanan",  "1.80",  "67", "0"},
        {"0009", "Casual Sneakers", "Sepatu",   "55.00", "6",  "10"},
    };

    // ── Components ────────────────────────────────────────────
    private JTable itemTable;
    private DefaultTableModel itemModel;

    private JTextField  searchField;
    private JComboBox<String> categoryFilter;
    private JComboBox<String> stockFilter;

    private StatCard totalItemsCard;
    private StatCard totalCategoriesCard;
    private StatCard lowStockCard;
    private StatCard emptyStockCard;

    private JLabel showingLabel;

    // ──────────────────────────────────────────────────────────
    public ItemListPage() {
        setLayout(new BorderLayout(0, 0));

        JPanel northStack = new JPanel(new BorderLayout());
        northStack.add(buildHeaderPanel(), BorderLayout.NORTH);
        northStack.add(buildStatsPanel(),  BorderLayout.CENTER);
        northStack.add(buildToolbar(),     BorderLayout.SOUTH);

        add(northStack,          BorderLayout.NORTH);
        add(buildCenterPanel(),  BorderLayout.CENTER);
        add(buildFooter(),       BorderLayout.SOUTH);

        populateTable(storeItems);
    }

    // ── HEADER ────────────────────────────────────────────────
    private JPanel buildHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel title = new JLabel("Daftar Item Toko");
        title.setFont(new Font("Arial", Font.BOLD, 16));

        ActionButton btnTambah = new ActionButton("+ Tambah Item", Color.WHITE, new Color(0x1D9E75));
        btnTambah.addActionListener(e -> ItemDialogs.showAddDialog(getParentFrame()));

        header.add(title,     BorderLayout.WEST);
        header.add(btnTambah, BorderLayout.EAST);
        return header;
    }

    // ── STATS ─────────────────────────────────────────────────
    private JPanel buildStatsPanel() {
        JPanel stats = new JPanel(new GridLayout(1, 4, 8, 0));
        stats.setBorder(BorderFactory.createEmptyBorder(0, 10, 8, 10));

        totalItemsCard      = new StatCard("Total Item",   null);
        totalCategoriesCard = new StatCard("Kategori",     null);
        lowStockCard        = new StatCard("Stok Menipis", new Color(0x854F0B));
        emptyStockCard      = new StatCard("Stok Habis",   new Color(0xA32D2D));

        stats.add(totalItemsCard);
        stats.add(totalCategoriesCard);
        stats.add(lowStockCard);
        stats.add(emptyStockCard);
        return stats;
    }

    // ── TOOLBAR ───────────────────────────────────────────────
    private JPanel buildToolbar() {
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        toolbar.setBorder(BorderFactory.createEmptyBorder(0, 10, 6, 10));

        searchField    = new JTextField(20);
        categoryFilter = new JComboBox<>(new String[]{"Semua Kategori", "Pakaian", "Minuman", "Makanan", "Sepatu"});
        stockFilter    = new JComboBox<>(new String[]{"Semua Stok", "Stok Cukup", "Stok Menipis", "Stok Habis"});

        searchField.setFont(new Font("Arial", Font.PLAIN, 13));
        categoryFilter.setFont(new Font("Arial", Font.PLAIN, 13));
        stockFilter.setFont(new Font("Arial", Font.PLAIN, 13));

        JButton btnSearch = new JButton("Cari");
        JButton btnReset  = new JButton("Reset");
        btnSearch.setFont(new Font("Arial", Font.PLAIN, 13));
        btnReset .setFont(new Font("Arial", Font.PLAIN, 13));

        btnSearch.addActionListener(e -> applyFilter());
        btnReset .addActionListener(e -> {
            searchField   .setText("");
            categoryFilter.setSelectedIndex(0);
            stockFilter   .setSelectedIndex(0);
            applyFilter();
        });
        searchField.addActionListener(e -> applyFilter());

        toolbar.add(new JLabel("Cari:"));
        toolbar.add(searchField);
        toolbar.add(new JLabel("Kategori:"));
        toolbar.add(categoryFilter);
        toolbar.add(new JLabel("Stok:"));
        toolbar.add(stockFilter);
        toolbar.add(btnSearch);
        toolbar.add(btnReset);
        return toolbar;
    }

    // ── CENTER: Row actions + Table ───────────────────────────
    private JPanel buildCenterPanel() {
        JPanel center = new JPanel(new BorderLayout());
        center.add(buildRowActions(), BorderLayout.NORTH);
        center.add(buildTable(),      BorderLayout.CENTER);
        return center;
    }

    private JPanel buildRowActions() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 4));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        ActionButton btnEdit   = new ActionButton("Edit Item",   new Color(0x185FA5));
        ActionButton btnDelete = new ActionButton("Hapus Item",  new Color(0xA32D2D));
        ActionButton btnDetail = new ActionButton("Detail Item", new Color(0x854F0B));

        btnEdit  .addActionListener(e -> ItemDialogs.showEditDialog  (getParentFrame(), itemModel, itemTable.getSelectedRow()));
        btnDelete.addActionListener(e -> ItemDialogs.showRemoveDialog (getParentFrame(), itemModel, itemTable.getSelectedRow()));
        btnDetail.addActionListener(e -> ItemDialogs.showDetailDialog (getParentFrame(), itemModel, itemTable.getSelectedRow()));

        panel.add(btnEdit);
        panel.add(btnDelete);
        panel.add(btnDetail);
        return panel;
    }

    private JScrollPane buildTable() {
        String[] cols = {"Kode", "Nama Item", "Kategori", "Harga", "Stok", "Disc %"};
        itemModel = new DefaultTableModel(null, cols) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };

        itemTable = new JTable(itemModel);
        itemTable.setRowHeight(28);
        itemTable.setFont(new Font("Arial", Font.PLAIN, 13));
        itemTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        itemTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Color stok column
        itemTable.getColumnModel().getColumn(4).setCellRenderer((tbl, val, sel, foc, row, col) -> {
            JLabel lbl = new JLabel(val == null ? "" : val.toString(), SwingConstants.CENTER);
            lbl.setOpaque(true);
            String v = val == null ? "" : val.toString();
            if      (v.equals("Habis"))   { lbl.setForeground(new Color(0xA32D2D)); lbl.setFont(new Font("Arial", Font.BOLD, 13)); }
            else if (v.equals("Menipis")) { lbl.setForeground(new Color(0x854F0B)); lbl.setFont(new Font("Arial", Font.BOLD, 13)); }
            else                          { lbl.setForeground(new Color(0x0F6E56)); lbl.setFont(new Font("Arial", Font.BOLD, 13)); }
            lbl.setBackground(sel ? itemTable.getSelectionBackground() : itemTable.getBackground());
            return lbl;
        });

        return new JScrollPane(itemTable);
    }

    // ── FOOTER ────────────────────────────────────────────────
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setBorder(BorderFactory.createEmptyBorder(5, 10, 8, 10));

        showingLabel = new JLabel("Menampilkan 0 dari 0 item");
        showingLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        showingLabel.setForeground(Color.GRAY);

        JPanel footerBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        JButton btnExport  = new JButton("Export");
        JButton btnRefresh = new JButton("Refresh");
        btnExport .setFont(new Font("Arial", Font.PLAIN, 12));
        btnRefresh.setFont(new Font("Arial", Font.PLAIN, 12));
        btnRefresh.addActionListener(e -> populateTable(storeItems));
        footerBtns.add(btnExport);
        footerBtns.add(btnRefresh);

        footer.add(showingLabel, BorderLayout.WEST);
        footer.add(footerBtns,  BorderLayout.EAST);
        return footer;
    }

    // ── POPULATE & FILTER ─────────────────────────────────────
    private void populateTable(Object[][] data) {
        itemModel.setRowCount(0);
        int low = 0, empty = 0;

        for (Object[] row : data) {
            int    stok      = Integer.parseInt(row[4].toString());
            String stokLabel = stok == 0 ? "Habis" : stok <= 5 ? "Menipis" : String.valueOf(stok);
            if      (stok == 0)  empty++;
            else if (stok <= 5)  low++;
            itemModel.addRow(new Object[]{row[0], row[1], row[2], "$" + row[3], stokLabel, row[5] + "%"});
        }

        int total = data.length;
        showingLabel        .setText("Menampilkan " + total + " dari " + storeItems.length + " item");
        totalItemsCard      .setValue(String.valueOf(storeItems.length));
        totalCategoriesCard .setValue("4");
        lowStockCard        .setValue(String.valueOf(low));
        emptyStockCard      .setValue(String.valueOf(empty));
    }

    private void applyFilter() {
        String q       = searchField.getText().toLowerCase().trim();
        String cat     = categoryFilter.getSelectedIndex() == 0 ? "" : categoryFilter.getSelectedItem().toString();
        int    stockSel = stockFilter.getSelectedIndex();

        List<Object[]> filtered = new ArrayList<>();
        for (Object[] row : storeItems) {
            String nama = row[1].toString().toLowerCase();
            String kode = row[0].toString().toLowerCase();
            String kat  = row[2].toString();
            int    stok = Integer.parseInt(row[4].toString());

            boolean matchQ   = q.isEmpty() || nama.contains(q) || kode.contains(q);
            boolean matchCat = cat.isEmpty() || kat.equals(cat);
            boolean matchStk = stockSel == 0
                || (stockSel == 1 && stok > 5)
                || (stockSel == 2 && stok > 0 && stok <= 5)
                || (stockSel == 3 && stok == 0);

            if (matchQ && matchCat && matchStk) filtered.add(row);
        }

        itemModel.setRowCount(0);
        int low = 0, empty = 0;
        for (Object[] row : filtered) {
            int    stok      = Integer.parseInt(row[4].toString());
            String stokLabel = stok == 0 ? "Habis" : stok <= 5 ? "Menipis" : String.valueOf(stok);
            if      (stok == 0)  empty++;
            else if (stok <= 5)  low++;
            itemModel.addRow(new Object[]{row[0], row[1], row[2], "$" + row[3], stokLabel, row[5] + "%"});
        }

        showingLabel  .setText("Menampilkan " + filtered.size() + " dari " + storeItems.length + " item");
        lowStockCard  .setValue(String.valueOf(low));
        emptyStockCard.setValue(String.valueOf(empty));
    }

    // ── Utility ───────────────────────────────────────────────
    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
}