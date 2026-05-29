package com.kasirqu.gui;

import com.kasirqu.gui.pages.ItemListPage;
import com.kasirqu.gui.pages.KasirPage;

import javax.swing.*;
import java.awt.*;

public class GuiKasir extends JFrame {

    public GuiKasir() {
        setTitle("KasirQu");
        setSize(1200, 720);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Arial", Font.PLAIN, 13));

        tabbedPane.addTab("Kasir",     new KasirPage());
        tabbedPane.addTab("Item List", new ItemListPage());

        add(tabbedPane);
    }
}