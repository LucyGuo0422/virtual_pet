package view.components;

import controller.PetController;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Panel for displaying pet's health status.
 * Redesigned to follow strict MVC pattern - only accesses model through controller
 */
public class HealthPanel extends JPanel {
  // Colors for the health bars
  private static final Color HUNGER_COLOR = new Color(255, 179, 138); // Orange
  private static final Color HYGIENE_COLOR = new Color(138, 198, 255); // Blue
  private static final Color SOCIAL_COLOR = new Color(187, 162, 255);  // Purple
  private static final Color SLEEP_COLOR = new Color(162, 203, 255);   // Light blue
  private static final Color BAR_BACKGROUND = new Color(232, 236, 255); // Light gray-blue
  private static final Color TEXT_COLOR = new Color(75, 75, 75);       // Dark gray
  private static final Color VALUE_COLOR = new Color(25, 30, 120);     // Dark blue for values

  // Controller reference
  private PetController controller;

  // UI Components
  private JProgressBar hungerBar;
  private JProgressBar hygieneBar;
  private JProgressBar socialBar;
  private JProgressBar sleepBar;
  private JLabel hungerValue;
  private JLabel hygieneValue;
  private JLabel socialValue;
  private JLabel sleepValue;

  /**
   * Creates a health panel for the pet.
   *
   * @param controller The pet controller
   */
  public HealthPanel(PetController controller) {
    this.controller = controller;
    setupUi();
    if (controller != null) {
      updateHealthStatus();
    }
  }

  /**
   * Sets the controller for this panel.
   *
   * @param controller the controller to set
   */
  public void setController(PetController controller) {
    this.controller = controller;
    updateHealthStatus();
  }

  /**
   * Sets up the UI components.
   */
  private void setupUi() {
    setLayout(null); // Use null layout for precise positioning
    setBackground(new Color(255, 245, 245));
    setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    // Title
    JLabel titleLabel = new JLabel("Health Status");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
    titleLabel.setForeground(TEXT_COLOR);
    titleLabel.setBounds(10, 5, 200, 30);
    add(titleLabel);

    // Spacing values
    final int startY = 45;
    final int rowHeight = 45;
    final int labelWidth = 100;
    final int barWidth = 150;
    final int valueWidth = 60;
    final int leftPadding = 10;
    final int barHeight = 15;

    // Hunger Row
    JLabel hungerLabel = new JLabel("🍊 Hunger");
    hungerLabel.setFont(new Font("Arial", Font.BOLD, 14));
    hungerLabel.setForeground(HUNGER_COLOR);
    hungerLabel.setBounds(leftPadding, startY, labelWidth, rowHeight);
    add(hungerLabel);

    hungerBar = createProgressBar(HUNGER_COLOR);
    hungerBar.setBounds(labelWidth + leftPadding, startY + 10, barWidth, barHeight);
    add(hungerBar);

    hungerValue = new JLabel("50pts");
    hungerValue.setFont(new Font("Arial", Font.BOLD, 14));
    hungerValue.setForeground(VALUE_COLOR);
    hungerValue.setBounds(labelWidth + barWidth + leftPadding + 5, startY, valueWidth, rowHeight);
    add(hungerValue);

    // Hygiene Row
    JLabel hygieneLabel = new JLabel("💧 Hygiene");
    hygieneLabel.setFont(new Font("Arial", Font.BOLD, 14));
    hygieneLabel.setForeground(HYGIENE_COLOR);
    hygieneLabel.setBounds(leftPadding, startY + rowHeight, labelWidth, rowHeight);
    add(hygieneLabel);

    hygieneBar = createProgressBar(HYGIENE_COLOR);
    hygieneBar.setBounds(labelWidth + leftPadding, startY + rowHeight + 10, barWidth, barHeight);
    add(hygieneBar);

    hygieneValue = new JLabel("50pts");
    hygieneValue.setFont(new Font("Arial", Font.BOLD, 14));
    hygieneValue.setForeground(VALUE_COLOR);
    hygieneValue.setBounds(labelWidth + barWidth + leftPadding + 5, startY + rowHeight, valueWidth,
        rowHeight);
    add(hygieneValue);

    // Social Row
    JLabel socialLabel = new JLabel("🔮 Social");
    socialLabel.setFont(new Font("Arial", Font.BOLD, 14));
    socialLabel.setForeground(SOCIAL_COLOR);
    socialLabel.setBounds(leftPadding, startY + rowHeight * 2, labelWidth, rowHeight);
    add(socialLabel);

    socialBar = createProgressBar(SOCIAL_COLOR);
    socialBar.setBounds(labelWidth + leftPadding, startY + rowHeight * 2 + 10, barWidth, barHeight);
    add(socialBar);

    socialValue = new JLabel("50pts");
    socialValue.setFont(new Font("Arial", Font.BOLD, 14));
    socialValue.setForeground(VALUE_COLOR);
    socialValue.setBounds(labelWidth + barWidth + leftPadding + 5, startY + rowHeight * 2,
        valueWidth, rowHeight);
    add(socialValue);

    // Sleep Row
    JLabel sleepLabel = new JLabel("🌙 Sleep");
    sleepLabel.setFont(new Font("Arial", Font.BOLD, 14));
    sleepLabel.setForeground(SLEEP_COLOR);
    sleepLabel.setBounds(leftPadding, startY + rowHeight * 3, labelWidth, rowHeight);
    add(sleepLabel);

    sleepBar = createProgressBar(SLEEP_COLOR);
    sleepBar.setBounds(labelWidth + leftPadding, startY + rowHeight * 3 + 10, barWidth, barHeight);
    add(sleepBar);

    sleepValue = new JLabel("50pts");
    sleepValue.setFont(new Font("Arial", Font.BOLD, 14));
    sleepValue.setForeground(VALUE_COLOR);
    sleepValue.setBounds(labelWidth + barWidth + leftPadding + 5, startY + rowHeight * 3,
        valueWidth, rowHeight);
    add(sleepValue);
  }

  /**
   * Creates a styled progress bar.
   *
   * @param barColor The color of the progress bar
   * @return The created progress bar
   */
  private JProgressBar createProgressBar(Color barColor) {
    JProgressBar bar = new JProgressBar(0, 100) {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();

        // Draw background
        g2d.setColor(BAR_BACKGROUND);
        RoundRectangle2D background =
            new RoundRectangle2D.Float(0, 0, width, height, height, height);
        g2d.fill(background);

        // Draw progress
        int progressWidth = (int) (width * ((double) getValue() / getMaximum()));
        if (progressWidth > 0) {
          g2d.setColor(getForeground());
          RoundRectangle2D progress =
              new RoundRectangle2D.Float(0, 0, progressWidth, height, height, height);
          g2d.fill(progress);
        }

        g2d.dispose();
      }

      @Override
      public void paint(Graphics g) {
        paintComponent(g);
      }
    };

    bar.setOpaque(false);
    bar.setBorderPainted(false);
    bar.setStringPainted(false);
    bar.setBackground(BAR_BACKGROUND);
    bar.setForeground(barColor);

    return bar;
  }

  /**
   * Updates the health bars based on current pet health.
   * Uses controller to access model data
   */
  public void updateHealthStatus() {
    if (controller == null) {
      return;
    }

    // Get values from controller
    final int hungerVal = controller.getPetHealth().getHunger();
    final int hygieneVal = controller.getPetHealth().getHygiene();
    final int socialVal = controller.getPetHealth().getSocial();
    final int sleepVal = controller.getPetHealth().getSleep();

    // Hunger
    hungerBar.setValue(hungerVal);
    hungerValue.setText(hungerVal + "pts");


    if (hungerVal > 70) {
      hungerBar.setForeground(new Color(255, 102, 102)); // Red for high hunger
    } else if (hungerVal > 30) {
      hungerBar.setForeground(HUNGER_COLOR); // Normal color for medium
    } else {
      hungerBar.setForeground(new Color(102, 204, 102)); // Green for low hunger
    }

    // Hygiene
    hygieneBar.setValue(hygieneVal);
    hygieneValue.setText(hygieneVal + "pts");
    updateBarColor(hygieneBar, HYGIENE_COLOR, hygieneVal);

    // Social
    socialBar.setValue(socialVal);
    socialValue.setText(socialVal + "pts");
    updateBarColor(socialBar, SOCIAL_COLOR, socialVal);

    // Sleep
    sleepBar.setValue(sleepVal);
    sleepValue.setText(sleepVal + "pts");
    updateBarColor(sleepBar, SLEEP_COLOR, sleepVal);
  }

  /**
   * Updates the color of a bar based on its value.
   *
   * @param bar         The progress bar to update
   * @param normalColor The normal color for the bar
   * @param value       The current value
   */
  private void updateBarColor(JProgressBar bar, Color normalColor, int value) {
    if (value < 30) {
      bar.setForeground(new Color(255, 102, 102));
    } else if (value < 70) {
      bar.setForeground(normalColor);
    } else {
      bar.setForeground(new Color(102, 204, 102));
    }
  }

  @Override
  public Dimension getPreferredSize() {
    return new Dimension(300, 240);
  }
}