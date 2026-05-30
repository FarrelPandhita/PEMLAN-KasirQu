package com.kasirqu;

import javax.swing.SwingUtilities;

import com.kasirqu.gui.GuiKasir;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting KasirQu POS System...");
        SwingUtilities.invokeLater(() -> new GuiKasir().setVisible(true));
    }
}
