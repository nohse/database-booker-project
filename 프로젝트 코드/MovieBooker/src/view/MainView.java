package view;

import javax.swing.JFrame;
import javax.swing.JPanel;

import view.panel.LoginPanel;

public class MainView extends JFrame{
	
	private LoginPanel loginPanel;
	
	public MainView() {
		
		InitializeUI();
		
	}
	
	private void InitializeUI() {
		setTitle("MovieBooker");
		setSize(600,400);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		
		createComponents();
		
		setVisible(true);
	}
	
	private void createComponents() {
		loginPanel = new LoginPanel(this);
		add(loginPanel);
	}
	
	public LoginPanel getLoginPanel() {
		return loginPanel;
	}
	
	 // 모든 컴포넌트를 제거하는 메서드
    public void removeAllComponents() {
        getContentPane().removeAll();
        revalidate();
        repaint();
    }
    
    // 새로운 패널을 추가하는 메서드
    public void addPanel(JPanel panel) {
        add(panel);
        revalidate();
        repaint();
    }
}
