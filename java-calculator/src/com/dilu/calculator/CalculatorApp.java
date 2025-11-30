package com.dilu.calculator;

import javax.swing.*;
import java.awt.*;

public class CalculatorApp extends JFrame{

    private JTextField display;
    private double num1=0;
    private String operator = "";
    private boolean startNew = true;

    public CalculatorApp(){

        setTitle("Calculator");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 28));
        display.setHorizontalAlignment(SwingConstants.RIGHT);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 5, 5));

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+",
        };

        for (String b : buttons){
            JButton button = new JButton(b);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.addActionListener(e -> onButtonClick(e.getActionCommand()));
            panel.add(button);
        }

        add(display, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private void onButtonClick(String command){
        if("0123456789".contains(command)){
            handleNumber(command);
        }
    }

}
