package com.dilu.calculator;

import javax.swing.*;
import java.awt.*;

public class CalculatorApp extends JFrame {

    private final JTextField display;

    // Calculator state
    private double currentValue = 0;      // accumulated value (result so far)
    private String currentOperator = null; // last operator pressed (+, -, *, /)
    private boolean startNew = true;      // are we starting a new number?

    public CalculatorApp() {
        setTitle("Leo Calculator v1.1.0");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        display = new JTextField("0");
        display.setEditable(false);
        display.setFont(new Font("Arial", Font.BOLD, 40));
        display.setHorizontalAlignment(SwingConstants.RIGHT);
        display.setPreferredSize(new Dimension(300, 80));
        display.setBorder(BorderFactory.createEmptyBorder(20, 15, 20, 15));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 5, 5));

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "*",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };

        for (String b : buttons) {
            JButton button = new JButton(b);
            button.setFont(new Font("Arial", Font.BOLD, 24));
            button.addActionListener(e -> onButtonClick(e.getActionCommand()));
            panel.add(button);
        }

        add(display, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    private void onButtonClick(String command) {
        if ("0123456789".contains(command)) {
            handleNumber(command);
        } else if ("+-*/".contains(command)) {
            handleOperator(command);
        } else if ("C".equals(command)) {
            handleClear();
        } else if ("=".equals(command)) {
            handleEquals();
        }
    }

    // ---------- Input handlers ----------

    private void handleNumber(String digit) {
        if (startNew || display.getText().equals("0")) {
            display.setText(digit);
            startNew = false;
        } else {
            display.setText(display.getText() + digit);
        }
    }

    /**
     * Pressing an operator:
     *  - if this is the first operator: store current number in currentValue
     *  - if there is already an operator: calculate currentValue (op) currentNumber
     *  - then store the new operator
     */
    private void handleOperator(String op) {
        double numberOnScreen = Double.parseDouble(display.getText());

        if (currentOperator == null || startNew) {
            // First operator, or user pressed operator twice
            currentValue = numberOnScreen;
        } else {
            // We already had an operator -> apply it first
            currentValue = calculate(currentValue, numberOnScreen, currentOperator);
        }

        display.setText(removeTrailingZeros(currentValue));
        currentOperator = op;
        startNew = true;
    }

    private void handleClear() {
        display.setText("0");
        currentValue = 0;
        currentOperator = null;
        startNew = true;
    }

    /**
     * When '=' is pressed, apply the last operator once more and finish.
     */
    private void handleEquals() {
        if (currentOperator == null) {
            // Nothing to do, just keep the same number
            return;
        }

        double numberOnScreen = Double.parseDouble(display.getText());
        double result = calculate(currentValue, numberOnScreen, currentOperator);

        display.setText(removeTrailingZeros(result));

        // After '=', we show final result and reset operator
        currentValue = result;
        currentOperator = null;
        startNew = true;
    }

    // ---------- Logic helpers ----------

    private double calculate(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "*" -> a * b;
            case "/" -> b == 0 ? 0 : a / b;
            default -> b; // just return last number if no operator
        };
    }

    private String removeTrailingZeros(double value) {
        if (value == (long) value) {
            return String.format("%d", (long) value);
        } else {
            return String.valueOf(value);
        }
    }

    static void main() {
        SwingUtilities.invokeLater(() -> {
            CalculatorApp app = new CalculatorApp();
            app.setVisible(true);
        });
    }
}
