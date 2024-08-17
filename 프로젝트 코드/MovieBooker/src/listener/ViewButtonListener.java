package listener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;


import model.DBConnector;
import model.DBResetDAO;
import view.MainView;
public class ViewButtonListener implements ActionListener {

    private JTextField textField;
    private MainView mainView;
    private DBResetDAO db;
    private ResultSet res;
    Statement stmt = null;
    public ViewButtonListener(DBResetDAO db, MainView mainView) {
        this.textField = new JTextField("query");
        this.db=db;
        this.mainView=mainView;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // 새로운 프레임 생성
        JFrame viewFrame = new JFrame("View Window");
        JPanel buttonPanel = new JPanel(new GridLayout(5, 5, 10, 10));

        // 프레임 크기 설정
        viewFrame.setSize(600, 400);

        // 닫기 버튼 동작 설정
        viewFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // 프레임을 보이도록 설정
        int flag=1;
        List<String> tableNames = new ArrayList<>();
        try {
            res = db.selectDatabase("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'db2'");
            if(!res.next()) {
            	flag=0;
            }
            do{
                String tableName = res.getString("TABLE_NAME");
                tableNames.add(tableName);
            }while (res.next());
        } catch (SQLException e1) {
        }  
        for (String tableName : tableNames) {
            JButton tableButton = new JButton(tableName);
            tableButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showTableData(tableName);
                }
            });
            buttonPanel.add(tableButton);
        }
        
        // 리스트에 저장된 테이블 이름 출력
        if(flag==1) {
            viewFrame.add(buttonPanel);
            viewFrame.setVisible(true);
        }
        else {
        	JOptionPane aa=new JOptionPane();
			aa.showMessageDialog(null, "테이블이 존재하지 않습니다.", "ERROR_MESSAGE", JOptionPane.ERROR_MESSAGE);
        }
            db.disconnect();
        }
    private void showTableData(String tableName) {
        JFrame tableFrame = new JFrame(tableName + " Data");
        tableFrame.setSize(800, 600);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultTableModel tableModel = new DefaultTableModel();
        JTable table = new JTable(tableModel);

        try {
            res = db.selectDatabase("SELECT * FROM " + tableName);
            java.sql.ResultSetMetaData metaData = res.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Add column names to the table model
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(metaData.getColumnName(i));
            }

            // Add rows to the table model
            while (res.next()) {
                Object[] row = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    row[i - 1] = res.getObject(i);
                }
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(table);
        tableFrame.add(scrollPane, BorderLayout.CENTER);
        tableFrame.setVisible(true);
    }

    }
//}
