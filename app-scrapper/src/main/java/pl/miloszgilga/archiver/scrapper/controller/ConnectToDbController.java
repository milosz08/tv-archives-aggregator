package pl.miloszgilga.archiver.scrapper.controller;

import lombok.RequiredArgsConstructor;
import pl.miloszgilga.archiver.scrapper.db.DataSource;
import pl.miloszgilga.archiver.scrapper.db.JdbiDataHandler;
import pl.miloszgilga.archiver.scrapper.gui.MessageDialog;
import pl.miloszgilga.archiver.scrapper.gui.window.ConnectToDbWindow;
import pl.miloszgilga.archiver.scrapper.gui.window.RootWindow;
import pl.miloszgilga.archiver.scrapper.state.RootState;

import java.net.ConnectException;
import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class ConnectToDbController {
  private final ConnectToDbWindow connectToDbWindow;
  private final MessageDialog messageDialog;

  public void onClickConnectToDb() {
    final String host = connectToDbWindow.getHostField().getText();
    final String port = connectToDbWindow.getPortField().getText();
    final String username = connectToDbWindow.getUsernameField().getText();
    final String password = new String(connectToDbWindow.getPasswordField().getPassword());
    final String dbName = connectToDbWindow.getDbNameField().getText();
    try {
      final int parsedPort = Integer.parseInt(port);
      final InetSocketAddress address = new InetSocketAddress(host, parsedPort);
      final RootState rootState = connectToDbWindow.getRootState();
      final DataSource dataSource = new DataSource(address, username, password, dbName);
      if (!dataSource.isSuccessfullyConnected()) {
        throw new ConnectException();
      }
      rootState.setDataHandler(new JdbiDataHandler(dataSource));
      final RootWindow rootWindow = new RootWindow(rootState);
      rootWindow.createWindow();
      connectToDbWindow.closeWindow();
    } catch (NumberFormatException ex) {
      messageDialog.showError("Incorrect port");
    } catch (ConnectException ex) {
      messageDialog.showError("Unable to connect to DB");
    }
  }
}
