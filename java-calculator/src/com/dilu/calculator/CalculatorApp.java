package com.dilu.calculator;

import javax.swing.*;
import java.awt.*;

public class CalculatorApp extends JFrame {

    private final JTextField expressionDisplay;
    private final JTextField resultDisplay;

    private double currentValue = 0;
    private String currentOperator = null;
    private boolean startNew = true;

    public CalculatorApp() {
        setTitle("DA Calculator");
        setSize(330, 480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // ===== BACKGROUND BLACK =====
        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

        // --------- Displays ---------
        expressionDisplay = new JTextField("");
        expressionDisplay.setEditable(false);
        expressionDisplay.setFont(new Font("Arial", Font.PLAIN, 20));
        expressionDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        expressionDisplay.setBorder(BorderFactory.createEmptyBorder(5, 10, 0, 10));
        expressionDisplay.setBackground(Color.BLACK);
        expressionDisplay.setForeground(Color.LIGHT_GRAY);

        resultDisplay = new JTextField("0");
        resultDisplay.setEditable(false);
        resultDisplay.setFont(new Font("Arial", Font.BOLD, 48));
        resultDisplay.setHorizontalAlignment(SwingConstants.RIGHT);
        resultDisplay.setBorder(BorderFactory.createEmptyBorder(10, 10, 20, 10));
        resultDisplay.setBackground(Color.BLACK);
        resultDisplay.setForeground(Color.WHITE);

        JPanel displayPanel = new JPanel(new GridLayout(2, 1));
        displayPanel.setBackground(Color.BLACK);
        displayPanel.add(expressionDisplay);
        displayPanel.add(resultDisplay);

        // --------- Buttons ---------
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        panel.setBackground(Color.BLACK);

        String[] buttons = {
                "7", "8", "9", "/",
                "4", "5", "6", "x",
                "1", "2", "3", "-",
                "0", "C", "=", "+"
        };

        for (String b : buttons) {

            RoundedButton button;

            // =========== YOUR COLOR THEME ===========
            if ("+-x/".contains(b)) {
                button = new RoundedButton(b, new Color(255, 149, 0)); // orange
            } else if (b.equals("C")) {
                button = new RoundedButton(b, new Color(158, 158, 158)); // ash gray
            } else if (b.equals("=")) {
                button = new RoundedButton(b, new Color(255, 149, 0)); // green (you can change)
            } else {
                button = new RoundedButton(b, new Color(40, 40, 40)); // light black
            }

            button.addActionListener(e -> onButtonClick(e.getActionCommand()));
            panel.add(button);
        }

        add(displayPanel, BorderLayout.NORTH);
        add(panel, BorderLayout.CENTER);
    }

    // ===================== BUTTON HANDLERS ======================

    private void onButtonClick(String command) {
        if ("0123456789".contains(command)) {
            handleNumber(command);
        } else if ("+-x/".contains(command)) {
            handleOperator(command);
        } else if ("C".equals(command)) {
            handleClear();
        } else if ("=".equals(command)) {
            handleEquals();
        }
    }

    private void handleNumber(String digit) {
        if (startNew || resultDisplay.getText().equals("0")) {
            resultDisplay.setText(digit);
            startNew = false;
        } else {
            resultDisplay.setText(resultDisplay.getText() + digit);
        }
    }

    private void handleOperator(String op) {
        double numberOnScreen = Double.parseDouble(resultDisplay.getText());

        if (currentOperator == null || startNew) {
            currentValue = numberOnScreen;
            expressionDisplay.setText(removeTrailingZeros(numberOnScreen) + " " + op + " ");
        } else {
            currentValue = calculate(currentValue, numberOnScreen, currentOperator);
            expressionDisplay.setText(expressionDisplay.getText() + removeTrailingZeros(numberOnScreen) + " " + op + " ");
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

    private void handleEquals() {
        if (currentOperator == null) return;

        double numberOnScreen = Double.parseDouble(resultDisplay.getText());
        double result = calculate(currentValue, numberOnScreen, currentOperator);

        expressionDisplay.setText(expressionDisplay.getText() + removeTrailingZeros(numberOnScreen) + " =");
        resultDisplay.setText(removeTrailingZeros(result));

        currentValue = result;
        currentOperator = null;
        startNew = true;
    }

    private double calculate(double a, double b, String op) {
        return switch (op) {
            case "+" -> a + b;
            case "-" -> a - b;
            case "x" -> a * b;
            case "/" -> b == 0 ? 0 : a / b;
            default -> b;
        };
    }

    private String removeTrailingZeros(double value) {
        if (value == (long) value) return String.format("%d", (long) value);
        return String.valueOf(value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorApp().setVisible(true));
    }
}


// ============================
//   Rounded Button Class
// ============================
class RoundedButton extends JButton {

    private final Color backgroundColor;

    public RoundedButton(String text, Color bgColor) {
        super(text);
        this.backgroundColor = bgColor;
        setForeground(Color.WHITE);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setFont(new Font("Arial", Font.BOLD, 22));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(backgroundColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        // No border
    }
}
