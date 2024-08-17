package main;

import controller.AdminController;
import controller.LoginController;
import controller.MemberController;
import view.MainView;

public class MovieBooker {
	
	private MainView mainView;
	private LoginController loginController;
	private AdminController adminController;
	private MemberController memberController;
	
	public MovieBooker() {
		initializeComponents();
	}
	
	private void initializeComponents() {
		this.mainView = new MainView();
		this.loginController = new LoginController(mainView);
		this.adminController = new AdminController(mainView);
		this.memberController = new MemberController(mainView);
	}
}
