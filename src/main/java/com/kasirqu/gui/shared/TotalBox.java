package com.kasirqu.gui.shared;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class TotalBox extends JPanel {

    private final JLabel valueLabel;

    public TotalBox(String title, boolean grand) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            title,
            TitledBorder.CENTER,
            TitledBorder.TOP,
            new Font("Arial", Font.PLAIN, 11),
            Color.GRAY
        ));

        valueLabel = new JLabel("0.00", SwingConstants.CENTER);
        valueLabel.setFont(new Font("Arial", Font.BOLD, grand ? 36 : 24));
        if (grand) valueLabel.setForeground(new Color(0xA32D2D));

        add(valueLabel, BorderLayout.CENTER);
    }

    public void setValue(String value) {
        valueLabel.setText(value);
    }

    public JLabel getValueLabel() {
        return valueLabel;
    }
}