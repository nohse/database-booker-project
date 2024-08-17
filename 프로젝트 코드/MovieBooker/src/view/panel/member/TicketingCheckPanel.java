package view.panel.member;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import model.MovieDAO;
import model.FinalBookingDTO;
import model.MovieInfoVO;
import model.ScreeningInfoDTO;
import view.MainView;

public class TicketingCheckPanel extends JPanel {

    private JButton backButton;
    private JButton deleteButton;
    private JButton changeMovieButton;
    private JButton changeScreeningButton;
    private MovieDAO movieDAO;
    private JTable bookingsTable;
    private DefaultTableModel tableModel;
    
    private MainView mainView;
    private MemberPanel memberPanel;

    public TicketingCheckPanel(MainView mainView, MemberPanel memberPanel) {
        movieDAO = new MovieDAO();
        this.mainView = mainView;
        this.memberPanel = memberPanel;
        initializePanel();
        loadBookingData();
    }

    private void initializePanel() {
        setLayout(new BorderLayout());

        JLabel headerLabel = new JLabel("Ticketing Check Page", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24)); // Font size increased to 24
        headerLabel.setPreferredSize(new Dimension(400, 50)); // Set preferred size to ensure it fits
        headerLabel.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0)); // Padding added

        add(headerLabel, BorderLayout.NORTH);

        backButton = new JButton("뒤로가기");
        deleteButton = new JButton("예매 취소");
        changeMovieButton = new JButton("다른 영화 예매 변경");
        changeScreeningButton = new JButton("다른 상영일정 예매 변경");
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(backButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(changeMovieButton);
        buttonPanel.add(changeScreeningButton);
        add(buttonPanel, BorderLayout.SOUTH);

        String[] columnNames = {"영화명", "상영 날짜", "상영관 번호", "좌석 번호", "판매 가격", "예약 ID"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookingsTable = new JTable(tableModel);
        bookingsTable.removeColumn(bookingsTable.getColumnModel().getColumn(5));

        bookingsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = bookingsTable.getSelectedRow();
                if (selectedRow != -1) {
                    String movieName = (String) tableModel.getValueAt(selectedRow, 0);
                    showMovieDetailsDialog(movieName);
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(bookingsTable);
        add(scrollPane, BorderLayout.CENTER);

        deleteButton.addActionListener(e -> deleteSelectedBooking());
        changeMovieButton.addActionListener(e -> deleteSelectedBooking2());
        changeScreeningButton.addActionListener(e -> deleteSelectedBooking3());
    }

    private void loadBookingData() {
        List<FinalBookingDTO> bookings = movieDAO.getAllBookings();

        for (FinalBookingDTO booking : bookings) {
            String movieName = booking.getMovieName();
            String screeningDate = booking.getScreeningDate();
            int theaterId = booking.getTheaterId();
            String seats = getFormattedSeats(booking.getSeatIds());
            double totalPrice = booking.getTotalAmount();
            int bookingId = booking.getCustomerId();

            Object[] rowData = {movieName, screeningDate, theaterId, seats, totalPrice, bookingId};
            tableModel.addRow(rowData);
        }
    }

    private String getFormattedSeats(List<Integer> seatIds) {
        StringBuilder formattedSeats = new StringBuilder();
        for (int i = 0; i < seatIds.size(); i++) {
            if (i > 0) formattedSeats.append(", ");
            formattedSeats.append(seatIds.get(i));
        }
        return formattedSeats.toString();
    }

    public void addBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    private void showMovieDetailsDialog(String movieName) {
        JDialog dialog = new JDialog((Frame) null, "상영 일정 및 티켓 정보", true);
        dialog.setSize(600, 400);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Detail Information : " + movieName, JLabel.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(label, BorderLayout.NORTH);

        String[] columnNames = {"상영 날짜", "상영 시간", "상영관 번호", "좌석 번호", "판매 가격"};
        DefaultTableModel detailsTableModel = new DefaultTableModel(columnNames, 0);
        JTable detailsTable = new JTable(detailsTableModel);

        List<ScreeningInfoDTO> screenings = movieDAO.getScreeningsByMovieName(movieName);
        for (ScreeningInfoDTO screening : screenings) {
            String screeningDate = screening.getStartDate().toString();
            String screeningTime = screening.getStartTime().toString();
            int theaterId = screening.getTheaterId();
            List<Integer> seatIds = movieDAO.getSeatsByScreeningId(screening.getScreeningId());
            double price = movieDAO.getPriceByScreeningId(screening.getScreeningId());

            String seats = getFormattedSeats(seatIds);
            Object[] rowData = {screeningDate, screeningTime, theaterId, seats, price};
            detailsTableModel.addRow(rowData);
        }

        JScrollPane scrollPane = new JScrollPane(detailsTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void deleteSelectedBooking() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow != -1) {
            String movieName = (String) tableModel.getValueAt(selectedRow, 0);
            String screeningDate = tableModel.getValueAt(selectedRow, 1).toString();

            Object theaterIdObj = tableModel.getValueAt(selectedRow, 2);
            int theaterId;

            if (theaterIdObj instanceof Integer) {
                theaterId = (Integer) theaterIdObj;
            } else {
                theaterId = Integer.parseInt(theaterIdObj.toString());
            }

            // 여기서 customerId는 하드코딩된 값 1로 지정
            int customerId = 1;

            // Delete from the database
            movieDAO.deleteBookingByDetails(movieName, screeningDate, theaterId, customerId);

            // Remove from the table
            tableModel.removeRow(selectedRow);

            JOptionPane.showMessageDialog(this, "예매가 취소되었습니다.", "취소 완료", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "취소할 예매를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBooking2() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow != -1) {
            String movieName = (String) tableModel.getValueAt(selectedRow, 0);
            String screeningDate = tableModel.getValueAt(selectedRow, 1).toString();

            Object theaterIdObj = tableModel.getValueAt(selectedRow, 2);
            int theaterId;

            if (theaterIdObj instanceof Integer) {
                theaterId = (Integer) theaterIdObj;
            } else {
                theaterId = Integer.parseInt(theaterIdObj.toString());
            }

            // 여기서 customerId는 하드코딩된 값 1로 지정
            int customerId = 1;

            // Delete from the database
            movieDAO.deleteBookingByDetails(movieName, screeningDate, theaterId, customerId);

            // Remove from the table
            tableModel.removeRow(selectedRow);

            // MovieSearchPanel로 전환
            CardLayout cardLayout = (CardLayout) memberPanel.getLayout();
            mainView.setSize(800, 400);
            cardLayout.show(memberPanel, "Movie Search");
        }
        else {
            JOptionPane.showMessageDialog(this, "변경할 영화를 선택하세요.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedBooking3() {
        int selectedRow = bookingsTable.getSelectedRow();
        if (selectedRow != -1) {
            String movieName = (String) tableModel.getValueAt(selectedRow, 0);
            String screeningDate = tableModel.getValueAt(selectedRow, 1).toString();

            Object theaterIdObj = tableModel.getValueAt(selectedRow, 2);
            int theaterId;

            if (theaterIdObj instanceof Integer) {
                theaterId = (Integer) theaterIdObj;
            } else {
                theaterId = Integer.parseInt(theaterIdObj.toString());
            }

            // 여기서 customerId는 하드코딩된 값 1로 지정
            int customerId = 1;

            // Delete from the database
            movieDAO.deleteBookingByDetails(movieName, screeningDate, theaterId, customerId);

            // Remove from the table
            tableModel.removeRow(selectedRow);

            // MovieBookingPanel로 전환
            MovieInfoVO movieInfo = movieDAO.getMovieInfoByTitle(movieName);
            if (movieInfo != null) {
                MovieBookingPanel bookingPanel = new MovieBookingPanel(movieInfo, memberPanel);
                mainView.removeAllComponents();
                mainView.addPanel(bookingPanel);
                mainView.setSize(800, 500);
            } else {
                JOptionPane.showMessageDialog(this, "상영 일정을 변경할 영화를 선택해주세요", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
