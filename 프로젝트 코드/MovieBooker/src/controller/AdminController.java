package controller;

import listener.ResetButtonListener;
import listener.ManageButtonListener;
import listener.ViewButtonListener;
import model.DBResetDAO;
import view.MainView;

public class AdminController {
	
	private MainView mainView;
	private DBResetDAO dbResetDAO;
	private ResetButtonListener resetButtonListener;
	private ManageButtonListener manageButtonListener;
	private ViewButtonListener viewButtonListener;
	
	public AdminController (MainView mainView) {
		this.mainView = mainView;
		this.dbResetDAO = new DBResetDAO();
		this.resetButtonListener = new ResetButtonListener(dbResetDAO, mainView);
		this.manageButtonListener = new ManageButtonListener(dbResetDAO, mainView);
		this.viewButtonListener = new ViewButtonListener(dbResetDAO, mainView);
		this.mainView.getLoginPanel().getAdminPanel().addResetButtonListener(resetButtonListener);
		this.mainView.getLoginPanel().getAdminPanel().addManageButtonListener(manageButtonListener);
		this.mainView.getLoginPanel().getAdminPanel().addViewButtonListener(viewButtonListener);
	}
}
