package listener;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import model.DBConnector;
import model.DBResetDAO;
import view.MainView;
public class ManageButtonListener implements ActionListener {

    private JTextField textField;
    private MainView mainView;
    private DBResetDAO db;
    public ManageButtonListener(DBResetDAO db, MainView mainView) {
        this.textField = new JTextField("ex) DROP TABLE IF EXISTS Tickets;");
        this.db=db;
        this.mainView=mainView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 새로운 프레임 생성
        JFrame manageFrame = new JFrame("Manage Window");

        // 프레임 크기 설정
        manageFrame.setSize(400, 150);

        // 닫기 버튼 동작 설정
        manageFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // 텍스트 필드를 담을 패널 생성
        JPanel textFieldPanel = new JPanel(new BorderLayout());

        // 라벨 추가 (예시로 입력)
        JLabel label = new JLabel("쿼리문 입력 :");
        textFieldPanel.add(label, BorderLayout.WEST);

        // 텍스트 필드 추가
        textField.setPreferredSize(new Dimension(200, 25));
        textFieldPanel.add(textField, BorderLayout.CENTER);

        // 프레임에 텍스트 필드 패널 추가
        manageFrame.add(textFieldPanel, BorderLayout.CENTER);

        // 버튼을 담을 패널 생성
        JPanel buttonPanel = new JPanel(new BorderLayout());

        // 저장 버튼 추가
        JButton saveButton = new JButton("저장");
        saveButton.setPreferredSize(new Dimension(100, 40));
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 저장 버튼 클릭 시 동작
                String text = textField.getText();
                try {
                	db.updateDatabase(text);
                    System.out.println("데이터베이스가 다음과 같은 쿼리문으로 업데이트되었습니다 : " + text);
				} catch (SQLException e1) {
					JOptionPane aa=new JOptionPane();
					aa.showMessageDialog(null, "쿼리문 입력 오류", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
				}
            }
        });
        buttonPanel.add(saveButton, BorderLayout.WEST);

        // 취소 버튼 추가
        JButton cancelButton = new JButton("취소");
        cancelButton.setPreferredSize(new Dimension(100, 40));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 취소 버튼 클릭 시 동작
                textField.setText(""); // 텍스트 필드를 비움
                System.out.println("Text field cleared");
            }
        });
        buttonPanel.add(cancelButton, BorderLayout.EAST);

        // 버튼 패널을 프레임의 남쪽에 추가
        manageFrame.add(buttonPanel, BorderLayout.SOUTH);

        // 프레임을 보이도록 설정
        manageFrame.setVisible(true);
    }
}
