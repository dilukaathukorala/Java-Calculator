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

        }

}
