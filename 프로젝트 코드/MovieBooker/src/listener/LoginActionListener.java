package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import controller.LoginController;

public class LoginActionListener implements ActionListener{
	
	private LoginController loginController;
	
	public LoginActionListener(LoginController loginController) {
		this.loginController = loginController;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		String command = e.getActionCommand();
		
		if("Admin Login".equals(command)) {
			String username = loginController.getMainView().getLoginPanel().getAdminUsername();
			String password = loginController.getMainView().getLoginPanel().getAdminPassword();
			loginController.authenticateAdmin(username, password);
		} else if("Member Login".equals(command)) {
			String username = loginController.getMainView().getLoginPanel().getMemberUsername();
			String password = loginController.getMainView().getLoginPanel().getMemberPassword();
			loginController.authenticateUser(username, password);
		}
	}
}
