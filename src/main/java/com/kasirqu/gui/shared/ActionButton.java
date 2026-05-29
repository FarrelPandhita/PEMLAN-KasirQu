package com.kasirqu.gui.shared;

import javax.swing.*;
import java.awt.*;

public class ActionButton extends JButton {

    public ActionButton(String text, Color foregroundColor) {
        super(text);
        setFont(new Font("Arial", Font.BOLD, 13));
        setForeground(foregroundColor);
    }

    public ActionButton(String text, Color foregroundColor, Color backgroundColor) {
        this(text, foregroundColor);
        setBackground(backgroundColor);
        setOpaque(true);
        setBorderPainted(false);
    }
}