package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import model.DBResetDAO;
import view.MainView;

public class ResetButtonListener implements ActionListener{
	
	private DBResetDAO dbResetDAO;
	private MainView mainView;
	
	public ResetButtonListener(DBResetDAO dbResetDAO, MainView mainView) {
		this.dbResetDAO = dbResetDAO;
		this.mainView = mainView;
	}
	
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
    		System.out.println("초기화 진행중...");
            dbResetDAO.resetDatabase();
            mainView.getLoginPanel().getAdminPanel().showSuccessMessage("데이터베이스가 성공적으로 초기화되었습니다.");
        } catch (SQLException ex) {
            ex.printStackTrace();
            mainView.getLoginPanel().getAdminPanel().showErrorMessage("데이터베이스 초기화 실패: " + ex.getMessage());
        }
    }
}
