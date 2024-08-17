package view.panel;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import listener.ManageButtonListener;
import listener.ViewButtonListener;
import model.DBConnector;

public class AdminPanel extends JPanel{
	
	private JButton resetButton;
	private JButton manageButton;
	private JButton viewButton;
	public AdminPanel() {
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());

        // 상단에 로그인 성공 메시지 표시
        add(new JLabel("로그인 성공! 관리자 패널입니다."), BorderLayout.NORTH);
        
        // 중앙에 버튼 패널 추가
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 10));
        createResetButton();
        createManageButton();
        createViewButton();
        buttonPanel.add(resetButton);
        buttonPanel.add(manageButton);
        buttonPanel.add(viewButton);
        add(buttonPanel, BorderLayout.CENTER);
    }

    private void createResetButton() {
        resetButton = new JButton("데이터베이스 초기화");
        resetButton.setPreferredSize(new Dimension(100, 40));
        addMouseEffect(resetButton);
    }

    private void createManageButton() {
        manageButton = new JButton("입력/삭제/변경");
        manageButton.setPreferredSize(new Dimension(100, 40));
        addMouseEffect(manageButton);
    }

    private void createViewButton() {
        viewButton = new JButton("테이블 조회");
        viewButton.setPreferredSize(new Dimension(100, 40));
        addMouseEffect(viewButton);
    }
    private void addMouseEffect(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setSize(button.getWidth() - 2, button.getHeight() - 2);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setSize(button.getWidth() + 2, button.getHeight() + 2);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setSize(button.getWidth() - 2, button.getHeight() - 2);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setSize(button.getWidth() + 2, button.getHeight() + 2);
            }
        });
    }
    public void addResetButtonListener(ActionListener actionListener) {
        resetButton.addActionListener(actionListener);
    }
    
    public void addManageButtonListener(ActionListener actionListener) {
        manageButton.addActionListener(actionListener);
    }
    
    public void addViewButtonListener(ActionListener actionListener) {
        viewButton.addActionListener(actionListener);
    }
    
    public void showSuccessMessage(String message) {
        System.out.println(message);
    }
    
    public void showErrorMessage(String message) {
        System.out.println(message);
    }
}
