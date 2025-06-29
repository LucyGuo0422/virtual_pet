package view;

import controller.PetController;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import pet.enums.Action;
import view.components.ActivityLogPanel;
import view.components.BackgroundPanel;
import view.components.HealthPanel;
import view.components.PetImagePanel;
import view.components.SpeechBubble;

/**
 * Main GUI implementation for the Virtual Pet Simulator.
 * Provides getter methods for components rather than handling actions internally
 */
public class PetView extends JFrame implements PetViewInterface {
  // Constants
  private static final String TITLE = "Virtual Pet";
  private static final int WINDOW_WIDTH = 1000;
  private static final int WINDOW_HEIGHT = 700;
  private static final Color TEXT_COLOR = new Color(75, 75, 75);
  private static final Color BUTTON_COLOR = new Color(255, 248, 249);
  private static final Color BUTTON_BORDER_COLOR = new Color(240, 230, 230);

  // GUI Components
  private PetImagePanel petImagePanel;
  private HealthPanel healthPanel;
  private ActivityLogPanel activityLogPanel;
  private JLabel moodLabel;
  private JButton feedButton;
  private JButton playButton;
  private JButton cleanButton;
  private JButton sleepButton;
  private JButton stepButton;
  private JButton mysteryBoxButton;
  private JLabel healthLabel;
  private SpeechBubble speechBubble;

  /**
   * Constructor for the GUI.
   */
  public PetView() {
    initialize();
  }

  /**
   * Initializes the view.
   */
  @Override
  public void initialize() {
    // Basic frame setup
    setTitle(TITLE);
    setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    setResizable(false);

    // Try to set the look and feel to the system's default
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      System.err.println("Look and feel class not found: " + e.getMessage());
    } catch (InstantiationException e) {
      System.err.println("Could not instantiate look and feel: " + e.getMessage());
    } catch (IllegalAccessException e) {
      System.err.println("Access exception for look and feel: " + e.getMessage());
    } catch (UnsupportedLookAndFeelException e) {
      System.err.println("Unsupported look and feel: " + e.getMessage());
    }

    // Create a background panel with the background image
    BackgroundPanel backgroundPanel = new BackgroundPanel("res/images/background.png");
    backgroundPanel.setLayout(null);

    // Create the content
    createContent(backgroundPanel);

    // Set main content pane
    setContentPane(backgroundPanel);
  }

  /**
   * Displays the welcome message when the application starts.
   */
  public void displayWelcomeMessage() {
    JOptionPane.showMessageDialog(
        this,
        "Welcome to Virtual Pet!\n\n"
            + "In this game, you will take care of a virtual pet.\n"
            + "Feed, play with, clean, and put your pet to sleep to keep it happy and healthy.\n"
            + "You might also find Mystery Boxes with random effects!",
        "Virtual Pet",
        JOptionPane.INFORMATION_MESSAGE
    );
  }

  /**
   * Prompts the user for a pet name.
   *
   * @return The name entered by the user, or a default name if none is provided
   */
  public String promptForPetName() {
    String name = JOptionPane.showInputDialog(
        this,
        "What would you like to name your pet?",
        "Name Your Pet",
        JOptionPane.QUESTION_MESSAGE
    );

    // Return default name if user cancels or enters empty string
    if (name == null || name.trim().isEmpty()) {
      return "Buddy";
    }

    return name;
  }

  /**
   * Creates all the content components on the background panel.
   *
   * @param panel The panel to add components to
   */
  private void createContent(JPanel panel) {
    // Left side: Health status and activity log
    int leftPanelWidth = 300;

    // Create health panel with status bars
    healthPanel = new HealthPanel(null);
    healthPanel.setBounds(20, 20, leftPanelWidth, 250);
    // Make the panel transparent to show the background
    healthPanel.setOpaque(false);
    panel.add(healthPanel);

    // Activity log panel
    activityLogPanel = new ActivityLogPanel();
    activityLogPanel.setBounds(20, 290, leftPanelWidth, 250);
    // Make the panel semi-transparent to show the background
    activityLogPanel.setOpaque(false);
    panel.add(activityLogPanel);

    // Pet image panel taking the right side
    petImagePanel = new PetImagePanel(null);
    petImagePanel.setBounds(340, 20, 640, 460);
    petImagePanel.setOpaque(false); // Make it transparent
    panel.add(petImagePanel);

    // Add speech bubble
    speechBubble = new SpeechBubble();
    speechBubble.setBounds(400, 150, 150, 80);
    panel.add(speechBubble);


    // Mood status label below the pet
    moodLabel = new JLabel("NEUTRAL", SwingConstants.CENTER);
    moodLabel.setFont(new Font("Arial", Font.BOLD, 24));
    moodLabel.setForeground(Color.DARK_GRAY);
    moodLabel.setBounds(340, 490, 640, 30);
    panel.add(moodLabel);

    // Health label for compatibility
    healthLabel = new JLabel("Health: Loading...", SwingConstants.CENTER);
    healthLabel.setBounds(340, 520, 640, 20);
    panel.add(healthLabel);

    // Action buttons at the bottom
    createActionButtons(panel);
  }

  /**
   * Creates the action buttons.
   */
  private void createActionButtons(JPanel panel) {
    final int buttonWidth = 110;
    final int buttonHeight = 110;
    final int buttonSpacing = 25;
    final int startX = 100;
    final int startY = 550;

    // Feed button
    feedButton = createIconButton("Feed", "res/images/icon_feed.png");
    feedButton.setBounds(startX, startY, buttonWidth, buttonHeight);
    panel.add(feedButton);

    // Play button
    playButton = createIconButton("Play", "res/images/icon_play.png");
    playButton.setBounds(startX + buttonWidth + buttonSpacing, startY, buttonWidth, buttonHeight);
    panel.add(playButton);

    // Clean button
    cleanButton = createIconButton("Clean", "res/images/icon_clean.png");
    cleanButton.setBounds(startX + (buttonWidth + buttonSpacing) * 2, startY, buttonWidth,
        buttonHeight);
    panel.add(cleanButton);

    // Sleep button
    sleepButton = createIconButton("Sleep", "res/images/icon_sleep.png");
    sleepButton.setBounds(startX + (buttonWidth + buttonSpacing) * 3, startY, buttonWidth,
        buttonHeight);
    panel.add(sleepButton);

    // Step button
    stepButton = createIconButton("Step", "res/images/icon_step.png");
    stepButton.setBounds(startX + (buttonWidth + buttonSpacing) * 4, startY, buttonWidth,
        buttonHeight);
    panel.add(stepButton);

    // Mystery Box button
    mysteryBoxButton = createIconButton("Mystery Box", "res/images/icon_box.png");
    mysteryBoxButton.setBounds(startX + (buttonWidth + buttonSpacing) * 5, startY, buttonWidth,
        buttonHeight);
    panel.add(mysteryBoxButton);
  }

  /**
   * Creates a simple icon button with rounded corners.
   *
   * @param actionName The action name (for accessibility)
   * @param iconPath   The path to the icon image
   * @return The created button
   */
  private JButton createIconButton(String actionName, String iconPath) {
    // Create a custom JButton with rounded corners
    JButton button = new JButton() {
      @Override
      protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint rounded rectangle background
        g2.setColor(BUTTON_COLOR);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        // Draw a subtle border
        g2.setColor(BUTTON_BORDER_COLOR);
        g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 20, 20);

        g2.dispose();
        super.paintComponent(g);
      }
    };

    // Make button transparent so our custom painting shows
    button.setContentAreaFilled(false);
    button.setBorderPainted(false);
    button.setFocusPainted(false);
    button.setOpaque(false);

    // Set tooltip for accessibility
    button.setToolTipText(actionName);

    // Center the icon in the button
    button.setLayout(new GridBagLayout());

    try {
      // Load and prepare the icon
      File iconFile = new File(iconPath);
      if (iconFile.exists()) {
        Image img = ImageIO.read(iconFile);

        if (img != null) {
          // Scale the image preserving its original aspect ratio
          int imgWidth = img.getWidth(null);
          int imgHeight = img.getHeight(null);
          double scale = Math.min(80.0 / imgWidth, 80.0 / imgHeight);

          int scaledWidth = (int) (imgWidth * scale);
          int scaledHeight = (int) (imgHeight * scale);

          Image scaledImg = img.getScaledInstance(scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

          ImageIcon icon = new ImageIcon(scaledImg);
          JLabel iconLabel = new JLabel(icon);

          // Add the icon to the button
          button.add(iconLabel);

          System.out.println("Successfully loaded icon: " + iconPath);
        } else {
          System.err.println("Failed to create image from file: " + iconPath);
          addFallbackLabel(button, actionName);
        }
      } else {
        System.err.println("Icon file does not exist: " + iconPath);
        System.err.println("Current directory: " + new File(".").getAbsolutePath());
        addFallbackLabel(button, actionName);
      }
    } catch (IOException e) {
      System.err.println("Error loading icon: " + iconPath + " - " + e.getMessage());
      e.printStackTrace();
      addFallbackLabel(button, actionName);
    }

    return button;
  }

  /**
   * Adds a fallback letter label to a button when the icon can't be loaded.
   */
  private void addFallbackLabel(JButton button, String actionName) {
    JLabel fallback = new JLabel(actionName.substring(0, 1));
    fallback.setFont(new Font("Arial", Font.BOLD, 36));
    fallback.setForeground(new Color(150, 150, 150));
    button.add(fallback);
  }

  /**
   * Shows an action image temporarily and then reverts back to the normal image.
   *
   * @param action The action being performed
   */
  @Override
  public void showActionImage(Action action) {
    petImagePanel.showActionImage(action);
  }

  /**
   * Displays a speech bubble with a message.
   *
   * @param message The message to display
   */
  public void showSpeechBubble(String message) {
    // Create a point that targets the pet's position
    Point targetPoint = new Point(120, 70);
    speechBubble.showMessage(message, 4000, targetPoint);
  }

  /**
   * Clears any active action image.
   */
  public void clearActionImage() {
    if (petImagePanel != null) {
      petImagePanel.clearActionImage();
    }
  }

  /**
   * Updates the mood label with the specified text.
   *
   * @param moodText the text representing the pet's mood
   */
  @Override
  public void updateMood(String moodText) {
    moodLabel.setText(moodText);
  }

  /**
   * Updates the pet's image based on the specified mood.
   *
   * @param mood the mood of the pet, which determines the image to display
   */
  @Override
  public void updateImage(String mood) {
    // This will be forwarded to petImagePanel when needed
    // Let the controller handle this by updating the model
  }

  /**
   * Updates the health label with the specified text.
   *
   * @param healthText the text representing the pet's health status
   */
  @Override
  public void updateHealth(String healthText) {
    healthLabel.setText(healthText);

    // Also update the health panel if needed
    if (healthPanel != null) {
      healthPanel.updateHealthStatus();
    }
  }

  /**
   * Updates the display based on current pet state.
   */
  public void updateDisplay() {
    // This will be called by the controller to update the view
    petImagePanel.updateImage();
    healthPanel.updateHealthStatus();
  }

  /**
   * Returns the feed button for attaching listeners.
   *
   * @return the feed button component
   */
  @Override
  public JButton getFeedButton() {
    return feedButton;
  }

  /**
   * Returns the play button for attaching listeners.
   *
   * @return the play button component
   */
  @Override
  public JButton getPlayButton() {
    return playButton;
  }

  /**
   * Returns the clean button for attaching listeners.
   *
   * @return the clean button component
   */
  @Override
  public JButton getCleanButton() {
    return cleanButton;
  }

  /**
   * Returns the sleep button for attaching listeners.
   *
   * @return the sleep button component
   */
  @Override
  public JButton getSleepButton() {
    return sleepButton;
  }

  /**
   * Returns the step button for attaching listeners.
   *
   * @return the step button component
   */
  @Override
  public JButton getStepButton() {
    return stepButton;
  }

  /**
   * Returns the mystery box button for attaching listeners.
   *
   * @return the mystery box button component
   */
  public JButton getMysteryBoxButton() {
    return mysteryBoxButton;
  }

  @Override
  public JPanel getPetImagePanel() {
    return petImagePanel;
  }

  /**
   * Displays a message to the user.
   *
   * @param message the message to display
   */
  @Override
  public void displayMessage(String message) {
    activityLogPanel.addLogMessage(message);
  }

  /**
   * Displays a dialog to confirm opening a mystery box.
   *
   * @param boxName     the name of the mystery box
   * @param description the description of the mystery box
   * @return true if the user wants to open the box, false otherwise
   */
  public boolean displayMysteryBoxDialog(String boxName, String description) {
    int result = JOptionPane.showConfirmDialog(
        this,
        "Your pet found a " + boxName + "!\n" + description + "\n\nWould you like to open it?",
        "Mystery Box Found",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE
    );
    return result == JOptionPane.YES_OPTION;
  }

  /**
   * Updates the enabled state of action buttons based on the pet's current state.
   *
   * @param feedEnabled  whether the feed button should be enabled
   * @param playEnabled  whether the play button should be enabled
   * @param cleanEnabled whether the clean button should be enabled
   * @param sleepEnabled whether the sleep button should be enabled
   * @param stepEnabled  whether the step button should be enabled
   */
  @Override
  public void updateButtonStates(boolean feedEnabled, boolean playEnabled,
                                 boolean cleanEnabled, boolean sleepEnabled,
                                 boolean stepEnabled) {
    feedButton.setEnabled(feedEnabled);
    playButton.setEnabled(playEnabled);
    cleanButton.setEnabled(cleanEnabled);
    sleepButton.setEnabled(sleepEnabled);
    stepButton.setEnabled(stepEnabled);
    mysteryBoxButton.setEnabled(feedEnabled);
  }

  /**
   * Displays a game over message when the pet dies.
   */
  @Override
  public void displayGameOver() {
    JOptionPane.showMessageDialog(this,
        "Your pet has passed away due to neglect.",
        "Pet Deceased",
        JOptionPane.ERROR_MESSAGE);
  }

  /**
   * Closes the view.
   */
  @Override
  public void close() {
    dispose();
  }

  /**
   * Sets the controller for this view.
   * This allows for circular references between view and controller
   *
   * @param controller the controller to set
   */
  public void setController(PetController controller) {
    // Set controller for child components
    if (petImagePanel != null) {
      petImagePanel.setController(controller);
    }

    if (healthPanel != null) {
      healthPanel.setController(controller);
    }
  }
}