package controller;

import listener.LoginActionListener;
import view.MainView;

public class LoginController {

	private MainView mainView;
	private LoginActionListener loginActionListener;
	
	public LoginController(MainView mainView) {
		this.mainView = mainView;
		this.loginActionListener = new LoginActionListener(this);
		this.mainView.getLoginPanel().setLoginActionListener(loginActionListener);
	}
	
	public void authenticateAdmin(String username, String password) {
        if ("root".equals(username) && "1234".equals(password)) {
            getMainView().getLoginPanel().showAdminPanel();
        } else {
        	getMainView().getLoginPanel().showErrorMessage("관리자 로그인 실패: 잘못된 아이디 또는 비밀번호");
        }
    }

    public void authenticateUser(String username, String password) {
        if ("user1".equals(username) && "user1".equals(password)) {
        	getMainView().getLoginPanel().showMemberPanel();
        } else {
        	getMainView().getLoginPanel().showErrorMessage("회원 로그인 실패: 잘못된 아이디 또는 비밀번호");
        }
    }
	
	public MainView getMainView() {
		return mainView;
	}
}
