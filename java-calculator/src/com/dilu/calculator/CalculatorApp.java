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
        else if("+-*/".contains(command)){
            handleOperator(command);
        }
        else if(command.equals("C")){
            handleClear();
        }
        else if(command.equals("=")){
            handleEquals();
        }
    }

    private void handleNumber(String digit){
        if(startNew || display.getText().equals("0")){
            display.setText(digit);
            startNew = false;
        }else{
            display.setText(display.getText() + digit);
        }
    }

    private void handleOperator(String op){
        num1 = Double.parseDouble(display.getText());
        operator = op;
        startNew = true;
    }

    private void handleClear(){
        display.setText("0");
        num1 = 0;
        operator = "";
        startNew = true;
    }

    private void handleEquals(){
        double num2 = Double.parseDouble(display.getText());
        double result = calculate(num1, num2, operator);
        String formatted = removeTrailingZeros(result);

        display.setText(formatted);
        startNew = true;
    }

    private double calculate(double a,double b, String op){
        return switch(op){
            case "+" -> a+b;
            case "-" -> a-b;
            case "*" -> a*b;
            case "/" -> b == 0 ? 0 : a/b;
            default -> 0;
        };
    }

    private String removeTrailingZeros(double value){
        if(value == (long) value){
            return String.format("%d", (long) value);
        }else{
            return String.valueOf(value);
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            CalculatorApp app = new CalculatorApp();
            app.setVisible(true);
        });
    }
}
