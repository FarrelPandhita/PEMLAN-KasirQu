package com.kasirqu.gui.shared;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ItemDialogs {

    // ── Helper ────────────────────────────────────────────────
    private static GridBagConstraints defaultGbc() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets  = new Insets(5, 5, 5, 5);
        gbc.fill    = GridBagConstraints.HORIZONTAL;
        gbc.anchor  = GridBagConstraints.WEST;
        return gbc;
    }

    // ══════════════════════════════════════════════════════════
    //  ADD
    // ══════════════════════════════════════════════════════════
    public static void showAddDialog(JFrame parent) {
        JDialog dialog = new JDialog(parent, "Add New Item", true);
        dialog.setSize(420, 380);
        dialog.setLocationRelativeTo(parent);

        JPanel p   = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = defaultGbc();

        String[] labels = {"Item Number:", "Description:", "Kategori:", "Price:", "Qty / Stok:", "Disc %:", "Min. Stok:"};
        JComponent[] fields = {
            new JTextField(20),
            new JTextField(20),
            new JComboBox<>(new String[]{"Pakaian", "Minuman", "Makanan", "Sepatu"}),
            new JTextField(20),
            new JTextField(20),
            new JTextField(20),
            new JTextField(20),
        };

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            p.add(new JLabel(labels[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            p.add(fields[i], gbc);
        }

        JPanel btns   = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton("Cancel");
        JButton save   = new ActionButton("Add Item", Color.WHITE, new Color(0x1D9E75));

        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> dialog.dispose()); // TODO: implement save logic

        btns.add(cancel);
        btns.add(save);

        dialog.setLayout(new BorderLayout());
        dialog.add(p,    BorderLayout.CENTER);
        dialog.add(btns, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ══════════════════════════════════════════════════════════
    //  EDIT
    // ══════════════════════════════════════════════════════════
    public static void showEditDialog(JFrame parent, DefaultTableModel model, int row) {
        if (row < 0) {
            JOptionPane.showMessageDialog(parent,
                "Pilih baris terlebih dahulu sebelum melakukan edit.",
                "Tidak Ada Item Dipilih", JOptionPane.WARNING_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(parent, "Edit Item", true);
        dialog.setSize(420, 320);
        dialog.setLocationRelativeTo(parent);

        String kode  = model.getValueAt(row, 0).toString();
        String nama  = model.getValueAt(row, 1).toString();
        String harga = model.getValueAt(row, 2).toString();
        String qty   = model.getValueAt(row, 3).toString();
        String disc  = model.getValueAt(row, 4).toString();

        JPanel p   = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = defaultGbc();

        String[]     lbls = {"Item Number:", "Description:", "Price:", "Qty:", "Disc %:"};
        JTextField[] flds = {
            new JTextField(kode,  20),
            new JTextField(nama,  20),
            new JTextField(harga, 20),
            new JTextField(qty,   20),
            new JTextField(disc,  20),
        };

        for (int i = 0; i < lbls.length; i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            p.add(new JLabel(lbls[i]), gbc);
            gbc.gridx = 1; gbc.weightx = 1;
            p.add(flds[i], gbc);
        }

        JPanel  btns   = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton("Cancel");
        JButton save   = new ActionButton("Save Changes", Color.WHITE, new Color(0x185FA5));

        cancel.addActionListener(e -> dialog.dispose());
        save.addActionListener(e -> dialog.dispose()); // TODO: implement save logic

        btns.add(cancel);
        btns.add(save);

        dialog.setLayout(new BorderLayout());
        dialog.add(p,    BorderLayout.CENTER);
        dialog.add(btns, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ══════════════════════════════════════════════════════════
    //  REMOVE
    // ══════════════════════════════════════════════════════════
    public static void showRemoveDialog(JFrame parent, DefaultTableModel model, int row) {
        if (row < 0) {
            JOptionPane.showMessageDialog(parent,
                "Pilih baris terlebih dahulu sebelum menghapus.",
                "Tidak Ada Item Dipilih", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String nama = model.getValueAt(row, 1).toString();
        String kode = model.getValueAt(row, 0).toString();

        JDialog dialog = new JDialog(parent, "Hapus Item", true);
        dialog.setSize(360, 200);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        JPanel body = new JPanel(new GridLayout(3, 1, 0, 6));
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Hapus item ini?", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel sub1 = new JLabel(nama, SwingConstants.CENTER);
        sub1.setFont(new Font("Arial", Font.BOLD, 14));

        JLabel sub2 = new JLabel("Item #" + kode + " — tindakan ini tidak dapat dibatalkan.", SwingConstants.CENTER);
        sub2.setFont(new Font("Arial", Font.PLAIN, 12));
        sub2.setForeground(Color.GRAY);

        body.add(title);
        body.add(sub1);
        body.add(sub2);

        JPanel  btns   = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton cancel = new JButton("Cancel");
        JButton del    = new ActionButton("Hapus", Color.WHITE, new Color(0xE24B4A));

        cancel.addActionListener(e -> dialog.dispose());
        del.addActionListener(e -> dialog.dispose()); // TODO: implement delete logic

        btns.add(cancel);
        btns.add(del);

        dialog.add(body, BorderLayout.CENTER);
        dialog.add(btns, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // ══════════════════════════════════════════════════════════
    //  DETAIL
    // ══════════════════════════════════════════════════════════
    public static void showDetailDialog(JFrame parent, DefaultTableModel model, int row) {
        if (row < 0) {
            JOptionPane.showMessageDialog(parent,
                "Pilih baris terlebih dahulu untuk melihat detail.",
                "Tidak Ada Item Dipilih", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        JDialog dialog = new JDialog(parent, "Detail Item", true);
        dialog.setSize(360, 300);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        JPanel p   = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = defaultGbc();

        String[] colNames = {"Item Number", "Description", "Harga", "Qty", "Disc %"};
        for (int i = 0; i < Math.min(colNames.length, model.getColumnCount()); i++) {
            gbc.gridx = 0; gbc.gridy = i; gbc.weightx = 0;
            JLabel lbl = new JLabel(colNames[i] + ":");
            lbl.setForeground(Color.GRAY);
            p.add(lbl, gbc);

            gbc.gridx = 1; gbc.weightx = 1;
            JLabel val = new JLabel(model.getValueAt(row, i).toString());
            val.setFont(new Font("Arial", Font.BOLD, 13));
            p.add(val, gbc);
        }

        JPanel  btns  = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton close = new JButton("Tutup");
        close.addActionListener(e -> dialog.dispose());
        btns.add(close);

        dialog.add(p,    BorderLayout.CENTER);
        dialog.add(btns, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }
}