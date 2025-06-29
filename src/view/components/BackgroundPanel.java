package view.components;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/**
 * A custom panel that displays a background image for the entire window.
 */
public class BackgroundPanel extends JPanel {
  private static final Color DEFAULT_BG_TOP = new Color(255, 243, 246);
  private static final Color DEFAULT_BG_BOTTOM = new Color(255, 230, 235);
  private Image backgroundImage;

  /**
   * Creates a new background panel.
   *
   * @param imagePath The path to the background image
   */
  public BackgroundPanel(String imagePath) {
    setLayout(null); // Use absolute positioning

    try {
      File imageFile = new File(imagePath);
      if (imageFile.exists()) {
        backgroundImage = ImageIO.read(imageFile);
        System.out.println("Successfully loaded background image: " + imagePath);
      } else {
        System.err.println("Background image not found: " + imagePath);
        backgroundImage = createDefaultBackground(800, 600);
      }
    } catch (IOException e) {
      System.err.println("Failed to load background image: " + e.getMessage());
      backgroundImage = createDefaultBackground(800, 600);
    }
  }

  /**
   * Creates a default gradient background if the image fails to load.
   *
   * @param width  The width of the background
   * @param height The height of the background
   * @return The created background image
   */
  private Image createDefaultBackground(int width, int height) {
    BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
    Graphics2D g2d = image.createGraphics();

    // Create a gradient
    GradientPaint gradient = new GradientPaint(
        0, 0, DEFAULT_BG_TOP,
        0, height, DEFAULT_BG_BOTTOM
    );

    g2d.setPaint(gradient);
    g2d.fillRect(0, 0, width, height);

    // Add some subtle texture (optional)
    g2d.setColor(new Color(255, 255, 255, 10)); // Very transparent white
    for (int i = 0; i < 100; i++) {
      int x = (int) (Math.random() * width);
      int y = (int) (Math.random() * height);
      int size = (int) (Math.random() * 10) + 5;
      g2d.fillOval(x, y, size, size);
    }

    g2d.dispose();

    return image;
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);

    // Draw the background image to fill the entire panel
    if (backgroundImage != null) {
      g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
  }

}