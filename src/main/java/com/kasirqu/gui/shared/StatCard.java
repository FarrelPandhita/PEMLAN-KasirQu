package com.kasirqu.gui.shared;

import javax.swing.*;
import java.awt.*;

public class StatCard extends JPanel {

    private final JLabel valueLabel;

    public StatCard(String label, Color valueColor) {
        setLayout(new BorderLayout(0, 4));
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0xDDDDDD)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        setBackground(new Color(0xF5F5F5));

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.PLAIN, 11));
        lbl.setForeground(Color.GRAY);

        valueLabel = new JLabel("0", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, 22));
        if (valueColor != null) valueLabel.setForeground(valueColor);

        add(lbl,        BorderLayout.NORTH);
        add(valueLabel, BorderLayout.CENTER);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }
}