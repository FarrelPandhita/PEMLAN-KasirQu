package com.kasirqu.gui.pages;

import com.kasirqu.gui.shared.ActionButton;
import com.kasirqu.gui.shared.ClockPanel;
import com.kasirqu.gui.shared.ItemDialogs;
import com.kasirqu.gui.shared.TotalBox;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class KasirPage extends JPanel {

    private JTable kasirTable;
    private DefaultTableModel kasirModel;

    private TotalBox subtotalBox;
    private TotalBox discountBox;
    private TotalBox taxBox;
    private TotalBox totalBox;

    public KasirPage() {
        setLayout(new BorderLayout());
        add(buildTopPanel(),    BorderLayout.NORTH);
        add(buildCenterPanel(), BorderLayout.CENTER);
        add(buildBottomPanel(), BorderLayout.SOUTH);
        calculateTotal();
    }

    // ── TOP: Store Info + Clock ───────────────────────────────
    private JPanel buildTopPanel() {
        JPanel top = new JPanel(new GridLayout(1, 2, 10, 10));
        top.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JPanel storePanel = new JPanel(new GridLayout(2, 2, 5, 5));
        storePanel.setBorder(BorderFactory.createTitledBorder("Store Info"));
        storePanel.add(new JLabel("Nama Kasir"));
        storePanel.add(new JTextField("Muhammad Qalbi Al Arsyad"));
        storePanel.add(new JLabel("Receipt Num"));
        storePanel.add(new JTextField("1773"));

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
        String[] cols = {"Item Number", "Description", "Price", "Qty", "Disc %", "Ext Price"};
        Object[][] data = {
            {"5562", "Megan T Shirt",  "15.29", "1", "10", "15.29"},
            {"5563", "Bottle Drink",   "2.96",  "1", "10", "2.96"},
            {"5564", "Snack Food",     "2.96",  "1", "10", "2.96"},
            {"0001", "Running Shoes",  "45.00", "1", "0",  "45.00"},
        };

        kasirModel = new DefaultTableModel(data, cols) {
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

        JLabel itemActionsLabel = new JLabel("Item Actions");
        itemActionsLabel.setFont(new Font("Arial", Font.PLAIN, 11));
        itemActionsLabel.setForeground(Color.GRAY);

        ActionButton btnAdd    = new ActionButton("Add Item",    new Color(0x1D9E75));
        ActionButton btnEdit   = new ActionButton("Edit Item",   new Color(0x185FA5));
        ActionButton btnRemove = new ActionButton("Remove Item", new Color(0xA32D2D));
        ActionButton btnDetail = new ActionButton("Detail Item", new Color(0x854F0B));

        btnAdd   .addActionListener(e -> ItemDialogs.showAddDialog(getParentFrame()));
        btnEdit  .addActionListener(e -> ItemDialogs.showEditDialog(getParentFrame(), kasirModel, kasirTable.getSelectedRow()));
        btnRemove.addActionListener(e -> ItemDialogs.showRemoveDialog(getParentFrame(), kasirModel, kasirTable.getSelectedRow()));
        btnDetail.addActionListener(e -> ItemDialogs.showDetailDialog(getParentFrame(), kasirModel, kasirTable.getSelectedRow()));

        JButton btnList  = new JButton("Item List");
        JButton btnPrint = new JButton("Print");
        for (JButton b : new JButton[]{btnList, btnPrint}) {
            b.setFont(new Font("Arial", Font.PLAIN, 13));
        }

        right.add(itemActionsLabel);
        right.add(btnAdd);
        right.add(btnEdit);
        right.add(btnRemove);
        right.add(btnDetail);
        right.add(new JSeparator());
        right.add(new JLabel());
        right.add(btnList);
        right.add(btnPrint);
        return right;
    }

    // ── BOTTOM: Totals ────────────────────────────────────────
    private JPanel buildBottomPanel() {
        JPanel bottom = new JPanel(new GridLayout(1, 4, 10, 10));
        bottom.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        subtotalBox = new TotalBox("Sub Total", false);
        discountBox = new TotalBox("Discount",  false);
        taxBox      = new TotalBox("Tax (11%)", false);
        totalBox    = new TotalBox("Total",     true);

        bottom.add(subtotalBox);
        bottom.add(discountBox);
        bottom.add(taxBox);
        bottom.add(totalBox);
        return bottom;
    }

    // ── Calculate ─────────────────────────────────────────────
    private void calculateTotal() {
        double sub = 0;
        for (int i = 0; i < kasirModel.getRowCount(); i++) {
            sub += Double.parseDouble(kasirModel.getValueAt(i, 5).toString());
        }
        double disc  = sub * 0.05;
        double tax   = sub * 0.11;
        double grand = sub - disc + tax;

        subtotalBox.setValue(String.format("%.2f", sub));
        discountBox.setValue(String.format("%.2f", disc));
        taxBox     .setValue(String.format("%.2f", tax));
        totalBox   .setValue(String.format("%.2f", grand));
    }

    // ── Utility ───────────────────────────────────────────────
    private JFrame getParentFrame() {
        return (JFrame) SwingUtilities.getWindowAncestor(this);
    }
}