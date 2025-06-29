package view;

import javax.swing.JButton;
import javax.swing.JPanel;
import pet.enums.Action;

/**
 * Interface for the pet view component.
 * Defines the contract for any view implementation in the Virtual Pet application.
 */
public interface PetViewInterface {

  /**
   * Updates the pet's image based on the specified mood.
   *
   * @param mood the mood of the pet, which determines the image to display
   */
  void updateImage(String mood);

  /**
   * Updates the mood label with the specified text.
   *
   * @param moodText the text representing the pet's mood
   */
  void updateMood(String moodText);

  /**
   * Updates the health label with the specified text.
   *
   * @param healthText the text representing the pet's health status
   */
  void updateHealth(String healthText);

  /**
   * Shows an action image temporarily.
   *
   * @param action the action being performed
   */
  void showActionImage(Action action);

  /**
   * Clears any active action image.
   */
  void clearActionImage();

  /**
   * Returns the feed button for attaching listeners.
   *
   * @return the feed button component
   */
  JButton getFeedButton();

  /**
   * Returns the play button for attaching listeners.
   *
   * @return the play button component
   */
  JButton getPlayButton();

  /**
   * Returns the clean button for attaching listeners.
   *
   * @return the clean button component
   */
  JButton getCleanButton();

  /**
   * Returns the sleep button for attaching listeners.
   *
   * @return the sleep button component
   */
  JButton getSleepButton();

  /**
   * Returns the step button for attaching listeners.
   *
   * @return the step button component
   */
  JButton getStepButton();

  /**
   * Returns the mystery box button for attaching listeners.
   *
   * @return the mystery box button component
   */
  JButton getMysteryBoxButton();

  JPanel getPetImagePanel();

  /**
   * Displays a message to the user.
   *
   * @param message the message to display
   */
  void displayMessage(String message);

  /**
   * Updates the enabled state of action buttons based on the pet's current state.
   *
   * @param feedEnabled  whether the feed button should be enabled
   * @param playEnabled  whether the play button should be enabled
   * @param cleanEnabled whether the clean button should be enabled
   * @param sleepEnabled whether the sleep button should be enabled
   * @param stepEnabled  whether the step button should be enabled
   */
  void updateButtonStates(boolean feedEnabled, boolean playEnabled,
                          boolean cleanEnabled, boolean sleepEnabled,
                          boolean stepEnabled);

  /**
   * Displays a game over message when the pet dies.
   */
  void displayGameOver();

  /**
   * Initializes the view.
   */
  void initialize();

  /**
   * Closes the view.
   */
  void close();

}
