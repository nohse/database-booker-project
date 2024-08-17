package view.panel;

import java.awt.CardLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import view.MainView;
import view.panel.member.MemberPanel;

public class LoginPanel extends JPanel{
	
	private CardLayout cardLayout;
	
	private JPanel selectionPanel;
	private JPanel adminLoginPanel;
	private JPanel memberLoginPanel;

	private AdminPanel adminPanel;
	private MemberPanel memberPanel;
	
	private JTextField adminUsernameField;
	private JPasswordField adminPasswordField;
    private JTextField memberUsernameField;
    private JPasswordField memberPasswordField;
    
    private JButton adminLoginButton;
    private JButton memberLoginButton;
    
    private MainView mainView;
    
	public LoginPanel(MainView mainView) {
		
		this.mainView = mainView;
    	memberPanel = new MemberPanel(mainView);
		adminPanel = new AdminPanel();
		initializePanel();
		
	}
	
	//패널 초기화
	private void initializePanel() {
		
		cardLayout = new CardLayout();
		setLayout(cardLayout);
		selectionPanel = createSelectionPanel();
		adminLoginPanel = createLoginPanel("관리자 아이디:", "관리자 비밀번호:", true);
		memberLoginPanel = createLoginPanel("회원 아이디:", "회원 비밀번호:", false);
		
		addPanel();
		showInitialPanel();
				
	}
	
	//내부 패널 추가
	private void addPanel() {
		
		add(selectionPanel, "Selection Panel");
		add(adminLoginPanel, "Admin Login");
		add(memberLoginPanel, "Member Login");
		
	}
	
	//cardLayout에 띄우기
	private void showInitialPanel() {
		
		cardLayout.show(this,  "Selection Panel");
		
	}
	
	//SelectionPanel 생성 함수
	private JPanel createSelectionPanel() {
		
		JPanel panel = new JPanel();
		JButton adminButton = new JButton("관리자 로그인");
		JButton memberButton = new JButton("회원 로그인");
		
		adminButton.addActionListener(e -> cardLayout.show(this,  "Admin Login"));
		memberButton.addActionListener(e -> cardLayout.show(this,  "Member Login"));
		
		panel.add(adminButton);
		panel.add(memberButton);
		
		return panel;
		
	}
	
	//LoginPanel 생성 함수
	private JPanel createLoginPanel(String usernameLabel, String passwordLabel, boolean isAdmin) {
		
		JPanel panel = new JPanel(new GridLayout(4, 2));
        
		panel.add(new JLabel(usernameLabel));    
        JTextField usernameField = new JTextField();
        if (isAdmin) {
            adminUsernameField = usernameField;
        } else {
            memberUsernameField = usernameField;
        }
        panel.add(usernameField);
        
        panel.add(new JLabel(passwordLabel));        
        JPasswordField passwordField = new JPasswordField();
        if (isAdmin) {
            adminPasswordField = passwordField;
        } else {
            memberPasswordField = passwordField;
        }
        panel.add(passwordField);
        
        JButton loginButton = new JButton("로그인");
        if (isAdmin) {
            adminLoginButton = loginButton;
            loginButton.setActionCommand("Admin Login");
        } else {
            memberLoginButton = loginButton;
            loginButton.setActionCommand("Member Login");
        }
        panel.add(loginButton);
        panel.add(loginButton);
        
        JButton backButton = new JButton("뒤로 가기");
        backButton.addActionListener(e -> cardLayout.show(this, "Selection Panel"));
        panel.add(backButton);
        
        loginButton.setActionCommand(isAdmin ? "Admin Login" : "Member Login");
        panel.add(loginButton);
        
        return panel;
		
	}
	
    //관리자 패널 생성
    public void showAdminPanel() {
        
        add(adminPanel, "Admin Panel");
        cardLayout.show(this, "Admin Panel");
    }
    
    //회원 패널 생성
    public void showMemberPanel() {

    	add(memberPanel, "Member Panel");
    	cardLayout.show(this,  "Member Panel");
    }
    
    //로그인 오류 메시지 출력
    public void showErrorMessage(String message) {
        System.out.println(message);
    }
    
    // 관리자 로그인 필드 가져오기
    public String getAdminUsername() {
        return adminUsernameField.getText();
    }

    public String getAdminPassword() {
        return new String(adminPasswordField.getPassword());
    }

    public String getMemberUsername() {
        return memberUsernameField.getText();
    }

    public String getMemberPassword() {
        return new String(memberPasswordField.getPassword());
    }
    
	public AdminPanel getAdminPanel() {
			return adminPanel;
	}
	
	public MemberPanel getMemberPanel() {
			return memberPanel;
	}
    
    // 로그인 버튼에 대한 ActionListener 설정
    public void setLoginActionListener(ActionListener actionListener) {
        adminLoginButton.addActionListener(actionListener);
        memberLoginButton.addActionListener(actionListener);
    }
}
