package com.dilu.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class CalculatorApp extends JFrame {

    private final JTextField expressionDisplay;
    private final JTextField resultDisplay;

    private double currentValue = 0;
    private String currentOperator = null;
    private boolean startNew = true;

    public CalculatorApp() {
        setTitle("DA Calculator");
        setSize(330, 530);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(Color.BLACK);
        setLayout(new BorderLayout());

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

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 4, 10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 0, 15));
        mainPanel.setBackground(Color.BLACK);

        String[] gridButtons = {
                "7", "8", "9", "+",
                "4", "5", "6", "-",
                "1", "2", "3", "x",
                "0", ".", "C", "/"
        };

        for (String b : gridButtons) {
            mainPanel.add(makeButton(b));
        }

        JPanel bottomPanel = new JPanel(new GridBagLayout());
        bottomPanel.setBackground(Color.BLACK);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 0, 10);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.weightx = 3;
        bottomPanel.add(makeButton("="), gbc);

        gbc.gridx = 3;
        gbc.gridwidth = 1;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        bottomPanel.add(makeButton("←"), gbc);

        add(displayPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setupKeyBindings(getRootPane());
    }

    private RoundedButton makeButton(String b) {
        Color bg;
        if ("+-/x".contains(b)) bg = new Color(255, 149, 0);
        else if (b.equals("C") ) bg = new Color(158, 158, 158);
        else if (b.equals("⬅") ) bg = new Color(100, 100, 150);
        else if (b.equals("=")) bg = new Color(158, 158, 158);
        else bg = new Color(40, 40, 40);

        RoundedButton button = new RoundedButton(b, bg);
        button.addActionListener(e -> onButtonClick(e.getActionCommand()));
        return button;
    }

    private void onButtonClick(String command) {
        if ("0123456789".contains(command)) handleNumber(command);
        else if (command.equals(".")) handleDecimal();
        else if ("+-/x".contains(command)) handleOperator(command);
        else if (command.equals("C")) handleClear();
        else if (command.equals("=")) handleEquals();
        else if (command.equals("←")) handleBackspace();
    }

    private void handleNumber(String digit) {
        if (startNew || resultDisplay.getText().equals("0")) {
            resultDisplay.setText(digit);
            startNew = false;
        } else {
            resultDisplay.setText(resultDisplay.getText() + digit);
        }
    }

    private void handleDecimal() {
        String text = resultDisplay.getText();
        if (startNew) {
            resultDisplay.setText("0.");
            startNew = false;
        } else if (!text.contains(".")) {
            resultDisplay.setText(text + ".");
        }
    }

    private void handleOperator(String op) {
        String actualOp = op.equals("x") ? "*" : op;
        double number = Double.parseDouble(resultDisplay.getText());

        if (currentOperator == null || startNew) {
            currentValue = number;
            expressionDisplay.setText(removeTrailingZeros(number) + " " + op + " ");
        } else {
            currentValue = calculate(currentValue, number, currentOperator);
            expressionDisplay.setText(expressionDisplay.getText() +
                    removeTrailingZeros(number) + " " + op + " ");
        }

        resultDisplay.setText(removeTrailingZeros(currentValue));
        currentOperator = actualOp;
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

        double number = Double.parseDouble(resultDisplay.getText());
        double result = calculate(currentValue, number, currentOperator);

        expressionDisplay.setText(expressionDisplay.getText() +
                removeTrailingZeros(number) + " =");
        resultDisplay.setText(removeTrailingZeros(result));

        currentValue = result;
        currentOperator = null;
        startNew = true;
    }

    private void handleBackspace() {
        String text = resultDisplay.getText();
        if (startNew) return;

        if (text.length() <= 1) {
            resultDisplay.setText("0");
            startNew = true;
        } else {
            resultDisplay.setText(text.substring(0, text.length() - 1));
        }
    }

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
        if (value == (long) value) return String.valueOf((long) value);
        return String.valueOf(value);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CalculatorApp().setVisible(true));
    }

    private void setupKeyBindings(JComponent root) {
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        bind(im, am, KeyStroke.getKeyStroke('0'), "0");
        bind(im, am, KeyStroke.getKeyStroke('1'), "1");
        bind(im, am, KeyStroke.getKeyStroke('2'), "2");
        bind(im, am, KeyStroke.getKeyStroke('3'), "3");
        bind(im, am, KeyStroke.getKeyStroke('4'), "4");
        bind(im, am, KeyStroke.getKeyStroke('5'), "5");
        bind(im, am, KeyStroke.getKeyStroke('6'), "6");
        bind(im, am, KeyStroke.getKeyStroke('7'), "7");
        bind(im, am, KeyStroke.getKeyStroke('8'), "8");
        bind(im, am, KeyStroke.getKeyStroke('9'), "9");

        bind(im, am, KeyStroke.getKeyStroke('.'), ".");
        bind(im, am, KeyStroke.getKeyStroke('+'), "+");
        bind(im, am, KeyStroke.getKeyStroke('-'), "-");
        bind(im, am, KeyStroke.getKeyStroke('*'), "x");
        bind(im, am, KeyStroke.getKeyStroke('/'), "/");

        bind(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "←");
        bind(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "C");
        bind(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "C");

        bind(im, am, KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "=");
    }

    private void bind(InputMap im, ActionMap am, KeyStroke key, String cmd) {
        im.put(key, cmd);
        am.put(cmd, new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                onButtonClick(cmd);
            }
        });
    }
}

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
    protected void paintBorder(Graphics g) {}
}
