package com.kasirqu.gui.shared;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ClockPanel extends JPanel {

    private final JLabel timeLabel;
    private final JLabel dateLabel;

    public ClockPanel() {
        setLayout(new GridLayout(2, 1));
        setBorder(BorderFactory.createTitledBorder("Info"));

        timeLabel = new JLabel("", SwingConstants.CENTER);
        dateLabel = new JLabel("", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 18));

        add(timeLabel);
        add(dateLabel);

        startClock();
    }

    private void startClock() {
        Timer timer = new Timer(1000, e -> {
            timeLabel.setText(LocalTime.now().format(DateTimeFormatter.ofPattern("hh:mm:ss a")));
            dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        });
        timer.start();
        timer.getActionListeners()[0].actionPerformed(null);
    }
}