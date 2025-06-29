package view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * Panel for displaying the activity log.
 * Redesigned with the Clear Log button next to the title
 */
public class ActivityLogPanel extends JPanel {
  private static final int MAX_MESSAGES = 100;
  private static final Color BACKGROUND_COLOR = new Color(252, 247, 245);
  private static final Color TEXT_COLOR = new Color(90, 85, 80);
  private static final Color TITLE_COLOR = new Color(75, 70, 65);
  private static final Color BUTTON_COLOR = new Color(252, 245, 240);
  private static final Color BUTTON_BORDER_COLOR = new Color(230, 220, 215);

  private JTextArea logArea;
  private JButton clearButton;
  private List<String> logMessages;

  /**
   * Creates a new activity log panel.
   */
  public ActivityLogPanel() {
    this.logMessages = new ArrayList<>();
    setupUi();
  }

  /**
   * Sets up the UI components.
   */
  private void setupUi() {
    setLayout(null); // Use null layout for precise positioning
    setBackground(new Color(255, 245, 245)); // Light pink background
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Title panel to hold title and clear button
    JPanel titlePanel = new JPanel(new BorderLayout());
    titlePanel.setOpaque(false);
    titlePanel.setBounds(10, 5, 280, 35);

    // Title
    JLabel titleLabel = new JLabel("Activity Log");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
    titleLabel.setForeground(TITLE_COLOR);
    titlePanel.add(titleLabel, BorderLayout.WEST);

    // Create clear button with rounded corners
    clearButton = new JButton("Clear Log") {
      @Override
      protected void paintComponent(Graphics g) {
        if (!isOpaque()) {
          Graphics2D g2 = (Graphics2D) g.create();
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setColor(getBackground());
          g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
          g2.setColor(BUTTON_BORDER_COLOR);
          g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 15, 15));
          g2.dispose();
        }
        super.paintComponent(g);
      }
    };

    clearButton.setFont(new Font("Arial", Font.BOLD, 12));  // Smaller font for the button
    clearButton.setForeground(TEXT_COLOR);
    clearButton.setBackground(BUTTON_COLOR);
    clearButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    clearButton.setFocusPainted(false);
    clearButton.setOpaque(false);
    clearButton.setContentAreaFilled(false);
    clearButton.addActionListener(e -> clearLog());
    clearButton.setPreferredSize(new Dimension(90, 30));  // Make the button smaller
    titlePanel.add(clearButton, BorderLayout.EAST);

    add(titlePanel);

    // Create log text area with rounded corners
    logArea = new JTextArea() {
      @Override
      protected void paintComponent(Graphics g) {
        if (!isOpaque()) {
          Graphics2D g2 = (Graphics2D) g.create();
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setColor(BACKGROUND_COLOR);
          g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
          g2.dispose();
        }
        super.paintComponent(g);
      }
    };

    logArea.setEditable(false);
    logArea.setLineWrap(true);
    logArea.setWrapStyleWord(true);
    logArea.setFont(new Font("Arial", Font.PLAIN, 14));
    logArea.setBackground(BACKGROUND_COLOR);
    logArea.setForeground(TEXT_COLOR);
    logArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    logArea.setOpaque(false);

    // Create scroll pane
    JScrollPane scrollPane = new JScrollPane(logArea) {
      @Override
      protected void paintComponent(Graphics g) {
        if (!isOpaque()) {
          Graphics2D g2 = (Graphics2D) g.create();
          g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
          g2.setColor(BACKGROUND_COLOR);
          g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 15, 15));
          g2.dispose();
        }
        super.paintComponent(g);
      }
    };
    scrollPane.setBorder(BorderFactory.createEmptyBorder());
    scrollPane.setOpaque(false);
    scrollPane.getViewport().setOpaque(false);
    scrollPane.setBounds(10, 45, 280,
        195);  // Extend height to fill the space previously used by the button
    add(scrollPane);
  }

  /**
   * Adds a message to the activity log.
   *
   * @param message The message to add
   */
  public void addLogMessage(String message) {
    // Add message to list without timestamp
    logMessages.add(message);

    // Trim list if it exceeds maximum size
    if (logMessages.size() > MAX_MESSAGES) {
      logMessages.remove(0);
    }

    // Update the text area
    updateLogArea();
  }

  /**
   * Updates the log area text from the message list.
   */
  private void updateLogArea() {
    StringBuilder sb = new StringBuilder();

    // Build text from messages (newest first)
    for (int i = logMessages.size() - 1; i >= 0; i--) {
      sb.append(logMessages.get(i)).append("\n");
    }

    // Update text area
    logArea.setText(sb.toString());

    // Scroll to top
    logArea.setCaretPosition(0);
  }

  /**
   * Clears all log messages.
   */
  private void clearLog() {
    logMessages.clear();
    logArea.setText("");
  }

  /**
   * Returns the preferred size of the panel.
   *
   * @return The preferred size of the panel
   */
  @Override
  public Dimension getPreferredSize() {
    return new Dimension(300, 250);
  }
}