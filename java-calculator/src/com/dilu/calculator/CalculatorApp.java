package com.dilu.calculator;

import javax.swing.*;
import java.awt.*;

public class CalculatorApp extends JFrame {

    // Top line – shows the operation chain: 2 + 3 + 4 =
    private final JTextField expressionDisplay;
    // Bottom line – shows current input / result
    private final JTextField resultDisplay;

    // Calculator state
    private double currentValue = 0;        // accumulated value
    private String currentOperator = null;  // last operator pressed
    private boolean startNew = true;        // are we starting a new number?

    public CalculatorApp() {
        setTitle("Leo Calculator v1.2.0");
        setSize(320, 420);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ---------- Displays (top: expression, bottom: result) ----------
        expressionDisplay = new JTextField("");
        expressionDisplay.setEditable(false);
        expressionDisplay.setFont(new Font("Arial", Font.PLAIN, 18));
        expressionDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        expressionDisplay.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));

        resultDisplay = new JTextField("0");
        resultDisplay.setEditable(false);
        resultDisplay.setFont(new Font("Arial", Font.BOLD, 40));
        resultDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        resultDisplay.setBorder(BorderFactory.createEmptyBorder(5, 10, 15, 10));

        JPanel displayPanel = new JPanel(new GridLayout(2, 1));
        displayPanel.add(expressionDisplay);
        displayPanel.add(resultDisplay);

        // ---------- Buttons ----------
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

        add(displayPanel, BorderLayout.NORTH);
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
        if (startNew || resultDisplay.getText().equals("0")) {
            resultDisplay.setText(digit);
            startNew = false;
        } else {
            resultDisplay.setText(resultDisplay.getText() + digit);
        }
    }

    /**
     * When an operator is pressed:
     *  - If it's the first operator, store current number as currentValue.
     *  - If there was already an operator, apply it first (left-to-right).
     *  - Update the expression (top line).
     */
    private void handleOperator(String op) {
        double numberOnScreen = Double.parseDouble(resultDisplay.getText());

        if (currentOperator == null || startNew) {
            // First operator or repeated operator press
            currentValue = numberOnScreen;
            expressionDisplay.setText(removeTrailingZeros(numberOnScreen) + " " + op + " ");
        } else {
            // Apply the previous operator first
            currentValue = calculate(currentValue, numberOnScreen, currentOperator);
            expressionDisplay.setText(
                    expressionDisplay.getText()
                            + removeTrailingZeros(numberOnScreen) + " " + op + " "
            );
        }

        resultDisplay.setText(removeTrailingZeros(currentValue));
        currentOperator = op;
        startNew = true;
    }

    private void handleClear() {
        resultDisplay.setText("0");
        expressionDisplay.setText("");
        currentValue = 0;
        currentOperator = null;
        startNew = true;
    }

    /**
     * When '=' is pressed:
     *  - Apply the last operator
     *  - Show expression on first line with '='
     *  - Show final result on second line
     */
    private void handleEquals() {
        if (currentOperator == null) {
            // Nothing to compute
            return;
        }

        double numberOnScreen = Double.parseDouble(resultDisplay.getText());
        double result = calculate(currentValue, numberOnScreen, currentOperator);

        // Finish the expression line: "... lastNumber ="
        expressionDisplay.setText(
                expressionDisplay.getText()
                        + removeTrailingZeros(numberOnScreen) + " ="
        );

        resultDisplay.setText(removeTrailingZeros(result));

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
            default -> b;
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
