package pl.miloszgilga.archiver.scrapper.gui.window;

import lombok.Getter;
import pl.miloszgilga.archiver.scrapper.controller.ConnectToDbController;
import pl.miloszgilga.archiver.scrapper.state.EnvKey;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import javax.swing.*;
import java.awt.*;

@Getter
public class ConnectToDbWindow extends AbstractWindow {
  private final RootState rootState;
  private final ConnectToDbController controller;

  private final JPanel inputFieldsPanel;

  private final JLabel hostLabel;
  private final JTextField hostField;
  private final JLabel portLabel;
  private final JTextField portField;
  private final JLabel usernameLabel;
  private final JTextField usernameField;
  private final JLabel passwordLabel;
  private final JPasswordField passwordField;
  private final JLabel dbNameLabel;
  private final JTextField dbNameField;

  private final JButton confirmButton;

  public ConnectToDbWindow(RootState rootState) {
    super("Connect to DB", 300, 150, rootState);
    this.rootState = rootState;
    controller = new ConnectToDbController(this, getMessageDialog());

    inputFieldsPanel = new JPanel();

    hostLabel = new JLabel("Host");
    hostField = new JTextField(rootState.getEnvValue(EnvKey.DB_HOST));
    portLabel = new JLabel("Port");
    portField = new JTextField(rootState.getEnvValue(EnvKey.DB_PORT));
    usernameLabel = new JLabel("Username");
    usernameField = new JTextField(rootState.getEnvValue(EnvKey.DB_USERNAME));
    passwordLabel = new JLabel("Password");
    passwordField = new JPasswordField(rootState.getEnvValue(EnvKey.DB_PASSWORD));
    dbNameLabel = new JLabel("DB name");
    dbNameField = new JPasswordField(rootState.getEnvValue(EnvKey.DB_NAME));

    confirmButton = new JButton("Connect to DB");
    confirmButton.addActionListener(e -> controller.onClickConnectToDb());
  }

  @Override
  void appendElements(JFrame frame, JPanel rootPanel) {
    rootPanel.setLayout(new BorderLayout());
    inputFieldsPanel.setLayout(new GridLayout(4, 2));

    inputFieldsPanel.add(hostLabel);
    inputFieldsPanel.add(hostField);
    inputFieldsPanel.add(portLabel);
    inputFieldsPanel.add(portField);
    inputFieldsPanel.add(usernameLabel);
    inputFieldsPanel.add(usernameField);
    inputFieldsPanel.add(passwordLabel);
    inputFieldsPanel.add(passwordField);

    rootPanel.add(inputFieldsPanel, BorderLayout.CENTER);
    rootPanel.add(confirmButton, BorderLayout.SOUTH);
  }
}
