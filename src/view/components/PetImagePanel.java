package view.components;

import controller.PetController;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import pet.enums.Action;

/**
 * Panel for displaying the pet image based on its current state.
 * with a cozy interior room background
 * Follows MVC pattern
 */
public class PetImagePanel extends JPanel {
  // Updated dimensions for better display
  private static final int IMAGE_WIDTH = 300;
  private static final int IMAGE_HEIGHT = 350;
  private static final int PANEL_PADDING = 20;
  private static final int ACTION_IMAGE_DURATION = 2000;
  // Duration to show action image (2 seconds)

  // Colors for styling
  private static final Color NAME_TAG_COLOR = new Color(255, 159, 127);
  private static final Color NAME_TEXT_COLOR = Color.WHITE;

  // Controller reference
  private PetController controller;

  private Map<String, Image> imageCache;
  private Image currentImage;
  private JPanel nameTagPanel;
  private JLabel nameLabel;

  // Track temporary action state
  private Action currentAction = null;
  private Timer actionTimer = null;

  /**
   * Creates a new pet image panel.
   *
   * @param controller The pet controller
   */
  public PetImagePanel(PetController controller) {
    this.controller = controller;
    this.imageCache = new HashMap<>();

    setLayout(new BorderLayout(0, 10));
    // Remove the border to allow the background to fill the entire panel
    setBorder(BorderFactory.createEmptyBorder(PANEL_PADDING, PANEL_PADDING, PANEL_PADDING,
        PANEL_PADDING));
    setBackground(new Color(255, 250, 245));

    // Create the name tag panel with rounded corners
    createNameTag();

    // Add the name tag to the panel
    add(nameTagPanel, BorderLayout.NORTH);

    // Load initial pet image if controller is available
    if (controller != null) {
      updateImage();
    }
  }

  /**
   * Sets the controller for this panel.
   *
   * @param controller the controller to set
   */
  public void setController(PetController controller) {
    this.controller = controller;

    // Update name label with the pet name
    if (nameLabel != null && controller != null) {
      nameLabel.setText(controller.getPetName());
    }

    // Load initial image
    updateImage();
  }

  /**
   * Creates a smaller stylish name tag panel positioned in the upper left corner.
   */
  private void createNameTag() {
    nameTagPanel = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Create rounded rectangle for name tag
        RoundRectangle2D roundedRectangle = new RoundRectangle2D.Float(
            0, 0, getWidth(), getHeight(), getHeight(), getHeight());

        // Fill with name tag color
        g2d.setColor(NAME_TAG_COLOR);
        g2d.fill(roundedRectangle);

        g2d.dispose();
        super.paintComponent(g);
      }
    };

    nameTagPanel.setOpaque(false);
    nameTagPanel.setPreferredSize(new Dimension(120, 40));
    nameTagPanel.setLayout(new BorderLayout());

    // Create pet name label - get name from controller if available
    String petName = (controller != null) ? controller.getPetName() : "Pet";
    nameLabel = new JLabel(petName, SwingConstants.CENTER);
    nameLabel.setFont(new Font("Arial", Font.BOLD, 20)); // Smaller font
    nameLabel.setForeground(NAME_TEXT_COLOR);
    nameTagPanel.add(nameLabel, BorderLayout.CENTER);
  }

  /**
   * Shows the action image temporarily.
   *
   * @param action The action being performed
   */
  public void showActionImage(Action action) {
    if (action == Action.SLEEP) {
      this.currentAction = action;
      updateImage();
      return;
    }
    // Cancel existing timer if one is running
    if (actionTimer != null) {
      actionTimer.cancel();
    }

    // Set current action
    this.currentAction = action;

    // Update display with action image
    updateImage();

    // Start timer to revert back to normal image
    actionTimer = new Timer();
    actionTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        // Reset the current action and update display
        SwingUtilities.invokeLater(() -> {
          currentAction = null;
          updateImage();
          actionTimer = null;
        });
      }
    }, ACTION_IMAGE_DURATION);
  }

  /**
   * Clears any active action image and shows the normal pet state.
   */
  public void clearActionImage() {
    // Cancel existing timer if one is running
    if (actionTimer != null) {
      actionTimer.cancel();
      actionTimer = null;
    }

    // Clear current action
    this.currentAction = null;

    // Update display to show normal state
    updateImage();
  }

  /**
   * Updates the image based on pet's current state.
   */
  public void updateImage() {
    if (controller == null) {
      return;
    }

    String imageName = getImageName();
    currentImage = getImage(imageName);
    repaint();
  }

  /**
   * Determines the appropriate image name based on pet state.
   * Uses controller to access pet state data
   *
   * @return The image name to use
   */
  private String getImageName() {
    if (controller == null) {
      return "neutral.png"; // Default image if no controller
    }

    // If there's an active action, show that image
    if (currentAction != null) {
      switch (currentAction) {
        case FEED:
          return "feeding.png";
        case PLAY:
          return "playing.png";
        case CLEAN:
          return "cleaning.png";
        case SLEEP:
          return "sleeping.png";
        default:
          return "neutral.png"; // Default image if action is unknown
      }
    }

    // No action or sleep action, show normal state image
    if (!controller.isPetAlive()) {
      return "dead.png";
    }

    if (controller.isPetAsleep()) {
      return "sleeping.png";
    }

    switch (controller.getPetMood()) {
      case HAPPY:
        return "happy.png";
      case SAD:
        return "sad.png";
      default:
        return "neutral.png";
    }
  }

  /**
   * Gets the image from cache or loads it from disk.
   *
   * @param imageName The name of the image to get
   * @return The image
   */
  private Image getImage(String imageName) {
    // Check if the image is already in the cache
    if (imageCache.containsKey(imageName)) {
      return imageCache.get(imageName);
    }

    // Try to load the image
    try {
      File file = new File("res/images/" + imageName);
      if (file.exists()) {
        BufferedImage img = ImageIO.read(file);
        Image scaledImage = getScaledImage(img);
        imageCache.put(imageName, scaledImage);
        return scaledImage;
      } else {
        System.err.println("Image file does not exist: res/images/" + imageName);
      }
    } catch (IOException e) {
      System.err.println("Failed to load image: " + imageName);
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Scales the image to fit the panel while maintaining aspect ratio.
   *
   * @param img The original image
   * @return The scaled image
   */
  private Image getScaledImage(BufferedImage img) {
    double originalWidth = img.getWidth();
    double originalHeight = img.getHeight();
    double ratio = originalWidth / originalHeight;

    int targetWidth = IMAGE_WIDTH;
    int targetHeight = IMAGE_HEIGHT;

    // Adjust dimensions to maintain aspect ratio
    if (ratio > 1) {
      // Width is greater than height
      targetHeight = (int) (IMAGE_WIDTH / ratio);
    } else {
      // Height is greater than or equal to width
      targetWidth = (int) (IMAGE_HEIGHT * ratio);
    }

    // Scale the image
    return img.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
  }


  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g.create();
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    int width = getWidth();
    int height = getHeight();

    if (currentImage != null) {
      // Calculate position to center the image
      int imageWidth = currentImage.getWidth(this);
      int imageHeight = currentImage.getHeight(this);
      int x = (width - imageWidth) / 2;
      int y = (height - imageHeight) / 2;

      // Draw the image
      g2d.drawImage(currentImage, x, y, this);
    }

    // Draw small name tag in top left corner
    if (nameTagPanel != null && !nameTagPanel.isShowing()) {
      // Calculate position
      int tagWidth = 120;
      int tagHeight = 40;
      int tagX = 15;
      int tagY = 15;

      // Draw rounded rectangle background
      g2d.setColor(NAME_TAG_COLOR);
      g2d.fillRoundRect(tagX, tagY, tagWidth, tagHeight, tagHeight, tagHeight);

      // Draw name text
      g2d.setFont(new Font("Arial", Font.BOLD, 20));
      g2d.setColor(NAME_TEXT_COLOR);

      FontMetrics metrics = g2d.getFontMetrics();
      String name = (controller != null) ? controller.getPetName() : "Pet";
      int textWidth = metrics.stringWidth(name);
      int textX = tagX + (tagWidth - textWidth) / 2;
      int textY = tagY + ((tagHeight - metrics.getHeight()) / 2) + metrics.getAscent();

      g2d.drawString(name, textX, textY);
    }

    g2d.dispose();
  }

  @Override
  public Dimension getPreferredSize() {
    // Increase the preferred size to accommodate taller images
    return new Dimension(IMAGE_WIDTH + (PANEL_PADDING * 2),
        IMAGE_HEIGHT + (PANEL_PADDING * 2) + 40);
  }
}