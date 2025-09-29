package com.example.Telemedi.ui;

import com.example.Telemedi.TelemediApplication;
import com.example.Telemedi.dto.QueryRequest;
import com.example.Telemedi.dto.BotResponse;
import com.example.Telemedi.service.ChatBotService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ChatWindow extends JFrame {

    // Colors - Medical Theme
    private static final Color PRIMARY_BLUE = new Color(0, 123, 255);
    private static final Color PRIMARY_DARK = new Color(0, 86, 179);
    private static final Color SUCCESS_GREEN = new Color(40, 167, 69);
    private static final Color LIGHT_GRAY = new Color(248, 249, 250);
    private static final Color DARK_GRAY = new Color(52, 58, 64);
    private static final Color BORDER_GRAY = new Color(222, 226, 230);
    private static final Color CHAT_BG = new Color(255, 255, 255);
    private static final Color USER_BUBBLE = new Color(0, 123, 255);
    private static final Color BOT_BUBBLE = new Color(248, 249, 250);
    private static final Color REMEDY_GREEN = new Color(25, 135, 84);

    // Components
    private JPanel chatPanel;
    private JTextField messageField;
    private JButton sendButton;
    private JScrollPane scrollPane;
    private JLabel typingLabel;
    private JPanel headerPanel;
    private JLabel userNameLabel;
    private JLabel onlineStatusLabel;

    // Services
    private ChatBotService chatBotService;
    private String currentSessionId;
    private String currentUser = "Samarth"; // Will be dynamic from login

    public ChatWindow() {
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        startConversation();
    }

    private void initializeComponents() {
        // Get Spring service
        chatBotService = TelemediApplication.getSpringContext().getBean(ChatBotService.class);

        // Window setup with custom close behavior
        setTitle("TeleMedi - Advanced Medical Assistant");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setResizable(true);

        // Custom window icon (you can add actual icon file)
        try {
            // setIconImage(Toolkit.getDefaultToolkit().getImage("medical-icon.png"));
        } catch (Exception e) {
            // Default icon
        }

        // Main chat panel with custom background
        chatPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Subtle gradient background
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(255, 255, 255),
                        0, getHeight(), new Color(250, 252, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));
        chatPanel.setBorder(new EmptyBorder(30, 40, 30, 40));

        // Custom scrollpane with modern scrollbar
        scrollPane = new JScrollPane(chatPanel) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        // Custom scrollbar styling
        scrollPane.getVerticalScrollBar().setUI(new ModernScrollBarUI());

        // Modern input field with rounded corners
        messageField = new ModernTextField("Type your symptoms here...");
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        messageField.setPreferredSize(new Dimension(0, 50));

        // Modern send button
        sendButton = new ModernButton("Send", PRIMARY_BLUE);
        sendButton.setPreferredSize(new Dimension(100, 50));

        // Typing indicator
        typingLabel = new JLabel("  ");
        typingLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        typingLabel.setForeground(new Color(108, 117, 125));
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        // Professional Header with gradient
        headerPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Medical gradient header
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(0, 123, 255),
                        getWidth(), 0, new Color(32, 201, 151)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Subtle medical cross pattern
                g2d.setColor(new Color(255, 255, 255, 30));
                for (int i = 0; i < getWidth(); i += 100) {
                    for (int j = 0; j < getHeight(); j += 50) {
                        g2d.fillRect(i + 20, j + 15, 15, 3);
                        g2d.fillRect(i + 26, j + 9, 3, 15);
                    }
                }
            }
        };
        headerPanel.setPreferredSize(new Dimension(0, 120));
        headerPanel.setLayout(new BorderLayout());

        // Left side of header - App info
        JPanel leftHeaderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 30, 20));
        leftHeaderPanel.setOpaque(false);

        JLabel appIconLabel = new JLabel("🏥");
        appIconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 40));

        JPanel appInfoPanel = new JPanel();
        appInfoPanel.setLayout(new BoxLayout(appInfoPanel, BoxLayout.Y_AXIS));
        appInfoPanel.setOpaque(false);

        JLabel titleLabel = new JLabel("TeleMedi Pro");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("AI-Powered Medical Symptom Analysis");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(220, 235, 255));
        subtitleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        appInfoPanel.add(titleLabel);
        appInfoPanel.add(Box.createVerticalStrut(5));
        appInfoPanel.add(subtitleLabel);

        leftHeaderPanel.add(appIconLabel);
        leftHeaderPanel.add(appInfoPanel);

        // Right side of header - User info
        JPanel rightHeaderPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 30, 20));
        rightHeaderPanel.setOpaque(false);

        JPanel userInfoPanel = new JPanel();
        userInfoPanel.setLayout(new BoxLayout(userInfoPanel, BoxLayout.Y_AXIS));
        userInfoPanel.setOpaque(false);

        userNameLabel = new JLabel("Welcome, " + currentUser);
        userNameLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        userNameLabel.setForeground(Color.WHITE);
        userNameLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        onlineStatusLabel = new JLabel("🟢 Online • Secure Session");
        onlineStatusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        onlineStatusLabel.setForeground(new Color(220, 255, 220));
        onlineStatusLabel.setAlignmentX(Component.RIGHT_ALIGNMENT);

        JButton profileButton = new ModernButton("Profile", new Color(255, 255, 255, 30));
        profileButton.setForeground(Color.WHITE);
        profileButton.setPreferredSize(new Dimension(80, 35));
        profileButton.setAlignmentX(Component.RIGHT_ALIGNMENT);

        userInfoPanel.add(userNameLabel);
        userInfoPanel.add(Box.createVerticalStrut(3));
        userInfoPanel.add(onlineStatusLabel);
        userInfoPanel.add(Box.createVerticalStrut(5));
        userInfoPanel.add(profileButton);

        rightHeaderPanel.add(userInfoPanel);

        headerPanel.add(leftHeaderPanel, BorderLayout.WEST);
        headerPanel.add(rightHeaderPanel, BorderLayout.EAST);

        // Chat area with better styling
        JPanel chatAreaPanel = new JPanel(new BorderLayout());
        chatAreaPanel.add(scrollPane, BorderLayout.CENTER);

        // Input area with modern design
        JPanel inputAreaPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Subtle gradient for input area
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(248, 249, 250),
                        0, getHeight(), new Color(255, 255, 255)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Top border
                g2d.setColor(BORDER_GRAY);
                g2d.drawLine(0, 0, getWidth(), 0);
            }
        };
        inputAreaPanel.setBorder(new EmptyBorder(25, 40, 25, 40));
        inputAreaPanel.setLayout(new BorderLayout(20, 10));

        // Typing indicator panel
        JPanel typingPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        typingPanel.setOpaque(false);
        typingPanel.add(typingLabel);

        // Input field container
        JPanel inputContainer = new JPanel(new BorderLayout(15, 0));
        inputContainer.setOpaque(false);
        inputContainer.add(messageField, BorderLayout.CENTER);
        inputContainer.add(sendButton, BorderLayout.EAST);

        // Disclaimer
        JLabel disclaimerLabel = new JLabel(
                "<html><center>⚠️ <b>Medical Disclaimer:</b> This AI assistant provides general information only. " +
                        "Always consult qualified healthcare professionals for medical advice, diagnosis, or treatment.</center></html>"
        );
        disclaimerLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        disclaimerLabel.setForeground(new Color(108, 117, 125));
        disclaimerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        inputAreaPanel.add(typingPanel, BorderLayout.NORTH);
        inputAreaPanel.add(inputContainer, BorderLayout.CENTER);
        inputAreaPanel.add(disclaimerLabel, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(chatAreaPanel, BorderLayout.CENTER);
        add(inputAreaPanel, BorderLayout.SOUTH);
    }

    private void setupEventHandlers() {
        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        // Enhanced button interactions
        sendButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                sendButton.setBackground(PRIMARY_DARK);
                setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                sendButton.setBackground(PRIMARY_BLUE);
                setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }
        });
    }

    private void startConversation() {
        // Add welcome message with animation
        addWelcomeMessage();

        QueryRequest initialRequest = new QueryRequest(null, "");
        BotResponse response = chatBotService.processQuery(initialRequest);
        currentSessionId = response.getSessionId();

        // Delayed bot response for natural feel
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    hideTypingIndicator();
                    addBotMessage(response.getMessage());

                    if (response.getSuggestions() != null && !response.getSuggestions().isEmpty()) {
                        addSuggestionButtons(response.getSuggestions());
                    }
                });
            }
        }, 1500);

        showTypingIndicator();
    }

    private void addWelcomeMessage() {
        JPanel welcomePanel = new JPanel();
        welcomePanel.setLayout(new BoxLayout(welcomePanel, BoxLayout.Y_AXIS));
        welcomePanel.setOpaque(false);
        welcomePanel.setBorder(new EmptyBorder(20, 0, 30, 0));

        JLabel welcomeIcon = new JLabel("👨‍⚕️", SwingConstants.CENTER);
        welcomeIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 48));
        welcomeIcon.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeText = new JLabel("Hello " + currentUser + "! I'm Dr. AI", SwingConstants.CENTER);
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 24));
        welcomeText.setForeground(DARK_GRAY);
        welcomeText.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel welcomeSubtext = new JLabel("Your intelligent medical symptom assistant", SwingConstants.CENTER);
        welcomeSubtext.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcomeSubtext.setForeground(new Color(108, 117, 125));
        welcomeSubtext.setAlignmentX(Component.CENTER_ALIGNMENT);

        welcomePanel.add(welcomeIcon);
        welcomePanel.add(Box.createVerticalStrut(15));
        welcomePanel.add(welcomeText);
        welcomePanel.add(Box.createVerticalStrut(8));
        welcomePanel.add(welcomeSubtext);

        chatPanel.add(welcomePanel);
        refreshChat();
    }

    private void showTypingIndicator() {
        typingLabel.setText("🤖 Dr. AI is analyzing...");
        typingLabel.setVisible(true);
    }

    private void hideTypingIndicator() {
        typingLabel.setText("  ");
        typingLabel.setVisible(false);
    }

    private void sendMessage() {
        String userMessage = messageField.getText().trim();
        if (userMessage.isEmpty()) return;

        // Add user message with animation
        addUserMessage(userMessage);
        messageField.setText("");
        sendButton.setEnabled(false);

        // Show typing indicator
        showTypingIndicator();

        // Process with chatbot (simulate network delay)
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                QueryRequest request = new QueryRequest(currentSessionId, userMessage);
                BotResponse response = chatBotService.processQuery(request);

                SwingUtilities.invokeLater(() -> {
                    hideTypingIndicator();

                    // Add bot response
                    addBotMessage(response.getMessage());

                    // Add remedy if present
                    if (response.getRemedy() != null && !response.getRemedy().isEmpty()) {
                        Timer remedyTimer = new Timer();
                        remedyTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(() -> {
                                    addRemedyMessage(response.getRemedy());
                                });
                            }
                        }, 800);
                    }

                    // Add suggestion buttons
                    if (response.getSuggestions() != null && !response.getSuggestions().isEmpty()) {
                        Timer suggestionTimer = new Timer();
                        suggestionTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(() -> {
                                    addSuggestionButtons(response.getSuggestions());
                                });
                            }
                        }, 1200);
                    }

                    // Check if conversation ended
                    if (response.isConversationEnd()) {
                        Timer endTimer = new Timer();
                        endTimer.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                SwingUtilities.invokeLater(() -> {
                                    addSystemMessage("Consultation completed successfully! 🎉");
                                    addNewChatButton();
                                });
                            }
                        }, 1500);
                    }

                    sendButton.setEnabled(true);
                });
            }
        }, 1000 + (int)(Math.random() * 1000)); // 1-2 second delay for realism
    }

    private void addUserMessage(String message) {
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        messagePanel.setOpaque(false);

        ModernChatBubble messageLabel = new ModernChatBubble(message, USER_BUBBLE, Color.WHITE, true);
        messageLabel.setMaximumWidth(450);

        messagePanel.add(messageLabel);
        chatPanel.add(messagePanel);
        chatPanel.add(Box.createVerticalStrut(15));

        refreshChat();
    }

    private void addBotMessage(String message) {
        JPanel messagePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        messagePanel.setOpaque(false);

        // Bot avatar
        JLabel avatarLabel = new JLabel("🤖");
        avatarLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        avatarLabel.setVerticalAlignment(SwingConstants.TOP);

        ModernChatBubble messageLabel = new ModernChatBubble("Dr. AI: " + message, BOT_BUBBLE, DARK_GRAY, false);
        messageLabel.setMaximumWidth(500);

        messagePanel.add(avatarLabel);
        messagePanel.add(Box.createHorizontalStrut(8));
        messagePanel.add(messageLabel);
        chatPanel.add(messagePanel);
        chatPanel.add(Box.createVerticalStrut(15));

        refreshChat();
    }

    private void addRemedyMessage(String remedy) {
        JPanel remedyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        remedyPanel.setOpaque(false);

        JLabel remedyIcon = new JLabel("💊");
        remedyIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        remedyIcon.setVerticalAlignment(SwingConstants.TOP);

        ModernChatBubble remedyLabel = new ModernChatBubble(
                "💊 Treatment Recommendation:\n\n" + remedy,
                REMEDY_GREEN,
                Color.WHITE,
                false
        );
        remedyLabel.setMaximumWidth(550);

        remedyPanel.add(remedyIcon);
        remedyPanel.add(Box.createHorizontalStrut(8));
        remedyPanel.add(remedyLabel);
        chatPanel.add(remedyPanel);
        chatPanel.add(Box.createVerticalStrut(20));

        refreshChat();
    }

    private void addSuggestionButtons(List<String> suggestions) {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(0, 48, 0, 20));

        JLabel suggestionLabel = new JLabel("💡 Quick Options:");
        suggestionLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        suggestionLabel.setForeground(new Color(108, 117, 125));

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        labelPanel.setOpaque(false);
        labelPanel.add(suggestionLabel);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 5));
        buttonsPanel.setOpaque(false);

        for (String suggestion : suggestions) {
            ModernButton suggestionButton = new ModernButton(suggestion, new Color(108, 117, 125));
            suggestionButton.setPreferredSize(new Dimension(
                    suggestionButton.getFontMetrics(suggestionButton.getFont()).stringWidth(suggestion) + 30,
                    35
            ));

            suggestionButton.addActionListener(e -> {
                messageField.setText(suggestion);
                sendMessage();
            });

            buttonsPanel.add(suggestionButton);
        }

        buttonPanel.add(labelPanel);
        buttonPanel.add(buttonsPanel);

        chatPanel.add(buttonPanel);
        chatPanel.add(Box.createVerticalStrut(25));

        refreshChat();
    }

    private void addSystemMessage(String message) {
        JPanel systemPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 15));
        systemPanel.setOpaque(false);

        JLabel systemLabel = new JLabel(message);
        systemLabel.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        systemLabel.setForeground(new Color(108, 117, 125));
        systemLabel.setHorizontalAlignment(SwingConstants.CENTER);

        systemPanel.add(systemLabel);
        chatPanel.add(systemPanel);
        chatPanel.add(Box.createVerticalStrut(15));

        refreshChat();
    }

    private void addNewChatButton() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 20));
        buttonPanel.setOpaque(false);

        ModernButton newChatButton = new ModernButton("🆕 Start New Consultation", SUCCESS_GREEN);
        newChatButton.setPreferredSize(new Dimension(250, 45));
        newChatButton.setFont(new Font("Segoe UI", Font.BOLD, 14));

        newChatButton.addActionListener(e -> {
            chatPanel.removeAll();
            startConversation();
        });

        buttonPanel.add(newChatButton);
        chatPanel.add(buttonPanel);

        refreshChat();
    }

    private void refreshChat() {
        chatPanel.revalidate();
        chatPanel.repaint();

        SwingUtilities.invokeLater(() -> {
            JScrollBar vertical = scrollPane.getVerticalScrollBar();
            vertical.setValue(vertical.getMaximum());
        });
    }
}

// Custom Components for Modern UI

class ModernTextField extends JTextField {
    public ModernTextField(String placeholder) {
        super(placeholder);
        setFont(new Font("Segoe UI", Font.PLAIN, 16));
        setBorder(BorderFactory.createCompoundBorder(
                new RoundedBorder(25, new Color(222, 226, 230)),
                new EmptyBorder(15, 20, 15, 20)
        ));
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Rounded background
        g2d.setColor(getBackground());
        g2d.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);

        g2d.dispose();
        super.paintComponent(g);
    }
}

class ModernButton extends JButton {
    private Color backgroundColor;

    public ModernButton(String text, Color bgColor) {
        super(text);
        this.backgroundColor = bgColor;
        setFont(new Font("Segoe UI", Font.BOLD, 14));
        setForeground(Color.WHITE);
        setBorder(null);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Button background
        g2d.setColor(backgroundColor);
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 25, 25);

        g2d.dispose();
        super.paintComponent(g);
    }

    @Override
    public void setBackground(Color bg) {
        this.backgroundColor = bg;
        repaint();
    }
}

class ModernChatBubble extends JLabel {
    private Color bubbleColor;
    private boolean isUser;
    private int maxWidth;

    public ModernChatBubble(String text, Color bgColor, Color textColor, boolean isUser) {
        super("<html><div style='width: 300px; padding: 5px;'>" + text + "</div></html>");
        this.bubbleColor = bgColor;
        this.isUser = isUser;
        this.maxWidth = 400;

        setFont(new Font("Segoe UI", Font.PLAIN, 15));
        setForeground(textColor);
        setBorder(new EmptyBorder(15, 20, 15, 20));
        setOpaque(false);
    }

    public void setMaximumWidth(int width) {
        this.maxWidth = width;
        setText("<html><div style='width: " + (width-40) + "px; padding: 5px;'>" +
                getText().replaceAll("<html><div style='width: \\d+px; padding: 5px;'>", "")
                        .replaceAll("</div></html>", "") + "</div></html>");
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Chat bubble with shadow
        g2d.setColor(new Color(0, 0, 0, 10));
        g2d.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 20, 20);

        g2d.setColor(bubbleColor);
        g2d.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 20, 20);

        // Border
        g2d.setColor(new Color(222, 226, 230));
        g2d.drawRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 20, 20);

        g2d.dispose();
        super.paintComponent(g);
    }
}

class RoundedBorder implements javax.swing.border.Border {
    private int radius;
    private Color color;

    public RoundedBorder(int radius, Color color) {
        this.radius = radius;
        this.color = color;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(1, 1, 1, 1);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(color);
        g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
        g2d.dispose();
    }
}

class ModernScrollBarUI extends BasicScrollBarUI {
    @Override
    protected void configureScrollBarColors() {
        this.thumbColor = new Color(200, 200, 200);
        this.trackColor = new Color(240, 240, 240);
    }

    @Override
    protected JButton createDecreaseButton(int orientation) {
        return createZeroButton();
    }

    @Override
    protected JButton createIncreaseButton(int orientation) {
        return createZeroButton();
    }

    private JButton createZeroButton() {
        JButton button = new JButton();
        button.setPreferredSize(new Dimension(0, 0));
        button.setMinimumSize(new Dimension(0, 0));
        button.setMaximumSize(new Dimension(0, 0));
        return button;
    }

    @Override
    protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(thumbColor);
        g2d.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2,
                thumbBounds.width - 4, thumbBounds.height - 4, 10, 10);
        g2d.dispose();
    }
}
