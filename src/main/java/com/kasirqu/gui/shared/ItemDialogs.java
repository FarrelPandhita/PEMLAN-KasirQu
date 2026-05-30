package com.kasirqu.gui.shared;

import com.kasirqu.facade.InventoryFacade;
import com.kasirqu.models.Barang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.sql.SQLException;

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
    public static void showAddDialog(JFrame parent, InventoryFacade facade, Runnable onSuccess) {
        JDialog dialog = new JDialog(parent, "Add New Item", true);
        dialog.setSize(420, 380);
        dialog.setLocationRelativeTo(parent);

        JPanel p   = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = defaultGbc();

        String[] labels = {"Item Number:", "Description:", "Kategori:", "Price:", "Qty / Stok:", "Min. Stok:"};
        JTextField fKode = new JTextField(20);
        JTextField fNama = new JTextField(20);
        JComboBox<String> fKategori = new JComboBox<>(new String[]{"Pakaian", "Minuman", "Makanan", "Sepatu", "Lainnya"});
        JTextField fHarga = new JTextField(20);
        JTextField fStok = new JTextField(20);
        JTextField fMinStok = new JTextField(20);

        JComponent[] fields = {fKode, fNama, fKategori, fHarga, fStok, fMinStok};

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
        save.addActionListener(e -> {
            try {
                Barang b = new Barang();
                b.setKodeBarang(fKode.getText());
                b.setNamaBarang(fNama.getText());
                // Dummy mapping for category based on index
                b.setIdKategori(fKategori.getSelectedIndex() + 1);
                b.setHarga(new BigDecimal(fHarga.getText()));
                b.setStok(Integer.parseInt(fStok.getText()));
                b.setMinimalStok(Integer.parseInt(fMinStok.getText()));
                b.setStatus("aktif");
                
                facade.createProduct(b);
                JOptionPane.showMessageDialog(dialog, "Item berhasil ditambahkan!");
                if (onSuccess != null) onSuccess.run();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

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
    public static void showEditDialog(JFrame parent, DefaultTableModel model, int row, InventoryFacade facade, Runnable onSuccess) {
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
        Barang b = facade.getProductByCode(kode);
        if (b == null) {
            JOptionPane.showMessageDialog(parent, "Barang tidak ditemukan di database!");
            return;
        }

        JPanel p   = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = defaultGbc();

        String[]     lbls = {"Item Number:", "Description:", "Price:", "Qty:"};
        JTextField fKode = new JTextField(b.getKodeBarang(), 20);
        fKode.setEditable(false);
        JTextField fNama = new JTextField(b.getNamaBarang(), 20);
        JTextField fHarga = new JTextField(b.getHarga().toString(), 20);
        JTextField fQty = new JTextField(String.valueOf(b.getStok()), 20);

        JTextField[] flds = {fKode, fNama, fHarga, fQty};

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
        save.addActionListener(e -> {
            try {
                b.setNamaBarang(fNama.getText());
                b.setHarga(new BigDecimal(fHarga.getText()));
                b.setStok(Integer.parseInt(fQty.getText()));
                facade.updateProduct(b);
                JOptionPane.showMessageDialog(dialog, "Item berhasil diupdate!");
                if (onSuccess != null) onSuccess.run();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

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
    public static void showRemoveDialog(JFrame parent, DefaultTableModel model, int row, InventoryFacade facade, Runnable onSuccess) {
        if (row < 0) {
            JOptionPane.showMessageDialog(parent,
                "Pilih baris terlebih dahulu sebelum menghapus.",
                "Tidak Ada Item Dipilih", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String kode = model.getValueAt(row, 0).toString();
        Barang b = facade.getProductByCode(kode);
        if (b == null) {
            JOptionPane.showMessageDialog(parent, "Barang tidak ditemukan di database!");
            return;
        }

        JDialog dialog = new JDialog(parent, "Hapus Item", true);
        dialog.setSize(360, 200);
        dialog.setLocationRelativeTo(parent);
        dialog.setLayout(new BorderLayout());

        JPanel body = new JPanel(new GridLayout(3, 1, 0, 6));
        body.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel title = new JLabel("Hapus item ini?", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 15));

        JLabel sub1 = new JLabel(b.getNamaBarang(), SwingConstants.CENTER);
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
        del.addActionListener(e -> {
            try {
                facade.deleteProduct(b.getIdBarang());
                JOptionPane.showMessageDialog(dialog, "Item berhasil dihapus!");
                if (onSuccess != null) onSuccess.run();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

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