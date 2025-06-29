package view.components;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * SpeechBubble class to display a speech bubble with a message.
 */
public class SpeechBubble extends JPanel {
  private final int bubbleWidth = 150;
  private final int bubbleHeight = 80;
  private String message;
  private int duration = 5000; // Display duration in ms
  private Timer displayTimer;
  private Point targetPoint; // Point indicating where the bubble should point to

  /**
   * Constructor for SpeechBubble.
   */
  public SpeechBubble() {
    setOpaque(false);
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(bubbleWidth, bubbleHeight));
    setVisible(false);
    targetPoint = new Point(bubbleWidth - 30, bubbleHeight); // Default pointing down-right
  }

  /**
   * Show message at the current panel position.
   *
   * @param message  The message to display
   * @param duration to display in milliseconds
   */
  public void showMessage(String message, int duration) {
    showMessage(message, duration, targetPoint);
  }

  /**
   * Show message with the bubble pointing to the specified location.
   *
   * @param message     The message to display
   * @param duration    Duration to display in milliseconds
   * @param targetPoint Point that the bubble should point toward (relative to this panel)
   */
  public void showMessage(String message, int duration, Point targetPoint) {
    this.message = message;
    this.duration = duration;
    this.targetPoint = targetPoint;

    // Cancel existing timer if running
    if (displayTimer != null && displayTimer.isRunning()) {
      displayTimer.stop();
    }

    // Show bubble
    setVisible(true);
    repaint();

    // Start timer to hide bubble
    displayTimer = new Timer(duration, e -> {
      setVisible(false);
      repaint();
    });
    displayTimer.setRepeats(false);
    displayTimer.start();
  }

  /**
   * Sets the message to be displayed.
   *
   * @param g the <code>Graphics</code> object to protect
   */
  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    if (!isVisible() || message == null) {
      return;
    }

    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    // Draw bubble background
    g2d.setColor(new Color(255, 255, 255, 230));
    RoundRectangle2D bubble = new RoundRectangle2D.Float(
        0, 0, bubbleWidth - 20, bubbleHeight - 30, 20, 20);
    g2d.fill(bubble);

    // Draw border
    g2d.setColor(new Color(200, 200, 200));
    g2d.draw(bubble);

    // Calculate pointer position based on targetPoint
    int[] xcoordinates = {targetPoint.x - 10, targetPoint.x, targetPoint.x - 20};
    int[] ycoordinates = {bubbleHeight - 30, bubbleHeight - 10, bubbleHeight - 10};

    // Draw pointer triangle
    g2d.setColor(new Color(255, 255, 255, 230));
    g2d.fillPolygon(xcoordinates, ycoordinates, 3);
    g2d.setColor(new Color(200, 200, 200));
    g2d.drawPolygon(xcoordinates, ycoordinates, 3);

    // Draw message text
    g2d.setColor(new Color(80, 80, 80));
    g2d.setFont(new Font("Arial", Font.BOLD, 14));

    // Handle multiline text
    FontMetrics fm = g2d.getFontMetrics();
    int lineHeight = fm.getHeight();
    String[] lines = message.split("\n");

    for (int i = 0; i < lines.length; i++) {
      int x = 10;
      int y = 20 + (i * lineHeight);
      g2d.drawString(lines[i], x, y);
    }

    g2d.dispose();
  }
}