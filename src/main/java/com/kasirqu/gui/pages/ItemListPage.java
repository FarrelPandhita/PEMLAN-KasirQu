package com.kasirqu.gui.pages;

import com.kasirqu.gui.shared.ActionButton;
import com.kasirqu.gui.shared.ItemDialogs;
import com.kasirqu.gui.shared.StatCard;

import com.kasirqu.facade.InventoryFacade;
import com.kasirqu.models.Barang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ItemListPage extends JPanel {

    private final InventoryFacade inventoryFacade;
    private List<Barang> currentItems = new ArrayList<>();

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
        inventoryFacade = new InventoryFacade();

        setLayout(new BorderLayout(0, 0));

        JPanel northStack = new JPanel(new BorderLayout());
        northStack.add(buildHeaderPanel(), BorderLayout.NORTH);
        northStack.add(buildStatsPanel(),  BorderLayout.CENTER);
        northStack.add(buildToolbar(),     BorderLayout.SOUTH);

        add(northStack,          BorderLayout.NORTH);
        add(buildCenterPanel(),  BorderLayout.CENTER);
        add(buildFooter(),       BorderLayout.SOUTH);

        loadDataFromDB();
    }

    // ── HEADER ────────────────────────────────────────────────
    private JPanel buildHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel title = new JLabel("Daftar Item Toko");
        title.setFont(new Font("Arial", Font.BOLD, 16));

        ActionButton btnTambah = new ActionButton("+ Tambah Item", Color.WHITE, new Color(0x1D9E75));
        btnTambah.addActionListener(e -> ItemDialogs.showAddDialog(getParentFrame(), inventoryFacade, this::loadDataFromDB));

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

        btnEdit  .addActionListener(e -> ItemDialogs.showEditDialog  (getParentFrame(), itemModel, itemTable.getSelectedRow(), inventoryFacade, this::loadDataFromDB));
        btnDelete.addActionListener(e -> ItemDialogs.showRemoveDialog (getParentFrame(), itemModel, itemTable.getSelectedRow(), inventoryFacade, this::loadDataFromDB));
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
        btnRefresh.addActionListener(e -> loadDataFromDB());
        footerBtns.add(btnExport);
        footerBtns.add(btnRefresh);

        footer.add(showingLabel, BorderLayout.WEST);
        footer.add(footerBtns,  BorderLayout.EAST);
        return footer;
    }

    // ── DATA LOADING & FILTER ─────────────────────────────────────
    public void loadDataFromDB() {
        // Load up to 1000 items
        currentItems = inventoryFacade.getProducts(1000, 0);
        populateTable(currentItems);
    }

    private void populateTable(List<Barang> data) {
        itemModel.setRowCount(0);
        int low = 0, empty = 0;
        java.util.Set<String> categories = new java.util.HashSet<>();

        for (Barang b : data) {
            int stok = b.getStok();
            String stokLabel = stok == 0 ? "Habis" : stok <= b.getMinimalStok() ? "Menipis" : String.valueOf(stok);
            if      (stok == 0)  empty++;
            else if (stok <= b.getMinimalStok())  low++;

            String kat = b.getNamaKategori() != null ? b.getNamaKategori() : "-";
            categories.add(kat);
            
            itemModel.addRow(new Object[]{
                b.getKodeBarang(), 
                b.getNamaBarang(), 
                kat, 
                "Rp" + b.getHarga(), 
                stokLabel, 
                "0%"
            });
        }

        int total = data.size();
        showingLabel        .setText("Menampilkan " + total + " dari " + currentItems.size() + " item");
        totalItemsCard      .setValue(String.valueOf(currentItems.size()));
        totalCategoriesCard .setValue(String.valueOf(categories.size()));
        lowStockCard        .setValue(String.valueOf(low));
        emptyStockCard      .setValue(String.valueOf(empty));
    }

    private void applyFilter() {
        String q       = searchField.getText().toLowerCase().trim();
        // Category filter is hardcoded in UI ("Semua Kategori", "Pakaian" etc)
        // Since we only have ID in Barang, filtering by exact category string name is tricky without mapping.
        // For now, we skip category filter or map it if we know the IDs.
        int stockSel = stockFilter.getSelectedIndex();

        List<Barang> filtered = new ArrayList<>();
        for (Barang b : currentItems) {
            String nama = b.getNamaBarang().toLowerCase();
            String kode = b.getKodeBarang().toLowerCase();
            int    stok = b.getStok();
            int minStok = b.getMinimalStok();

            boolean matchQ   = q.isEmpty() || nama.contains(q) || kode.contains(q);
            boolean matchStk = stockSel == 0
                || (stockSel == 1 && stok > minStok)
                || (stockSel == 2 && stok > 0 && stok <= minStok)
                || (stockSel == 3 && stok == 0);

            if (matchQ && matchStk) {
                filtered.add(b);
            }
        }
        
        populateTable(filtered);
    }

    // ── Utility ───────────────────────────────────────────────
    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
}