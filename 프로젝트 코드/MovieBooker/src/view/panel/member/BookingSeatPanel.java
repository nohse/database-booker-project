package view.panel.member;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import model.BookingInfoDTO;
import model.FinalBookingDTO;
import model.MovieDAO;
import model.TheaterInfoDTO;
import view.MainView;

public class BookingSeatPanel extends JPanel {

    private BookingInfoDTO bookingInfo;
    private MovieDAO movieDAO;
    private TheaterInfoDTO theaterInfo;
    private List<Integer> reservedSeats;
    private List<Integer> selectedSeats;
    private JPanel selectedSeatsPanel;
    private JLabel selectedSeatsLabel;
    private JPanel seatPanel;
    private JLabel totalAmountLabel;
    private double standardPrice;
    private MemberPanel memberPanel;

    public BookingSeatPanel(BookingInfoDTO bookingInfo, MemberPanel memberPanel) {
        this.bookingInfo = bookingInfo;
        this.movieDAO = new MovieDAO();
        this.theaterInfo = movieDAO.getTheaterInfo(bookingInfo.getSelectedTheaterId());
        this.reservedSeats = movieDAO.getReservedSeats(bookingInfo.getSelectedTheaterId(), bookingInfo.getSelectedDate(), bookingInfo.getSelectedTime());
        this.selectedSeats = new ArrayList<>();
        this.standardPrice = theaterInfo.getStandardPrice();
        this.memberPanel = memberPanel;

        setLayout(new BorderLayout());
        add(createMainPanel(), BorderLayout.CENTER);
        add(createSelectedSeatsPanel(), BorderLayout.EAST);
        updateSelectedSeatsPanel();
        updateTotalAmount();
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Screen Label
        JLabel screenLabel = createScreenLabel();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(10, 0, 10, 0);
        mainPanel.add(screenLabel, gbc);

        // Seat Panel
        seatPanel = createSeatPanel();
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        mainPanel.add(seatPanel, gbc);

        return mainPanel;
    }

    private JPanel createSeatPanel() {
        JPanel seatPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // 좌석 번호 (1, 2, 3, ...)
        gbc.gridy = 0;
        for (int col = 0; col < theaterInfo.getWidth(); col++) {
            gbc.gridx = col + 1;
            gbc.insets = new Insets(0, 0, 0, 5);
            JLabel colLabel = new JLabel(String.valueOf(col + 1));
            colLabel.setFont(new Font("Arial", Font.BOLD, 12));
            seatPanel.add(colLabel, gbc);
        }

        // 행 레이블 (A, B, C, ...)
        for (int row = 0; row < theaterInfo.getLength(); row++) {
            gbc.gridx = 0;
            gbc.gridy = row + 1;
            gbc.insets = new Insets(0, 0, 5, 5);
            JLabel rowLabel = new JLabel(String.valueOf((char) ('A' + row)));
            rowLabel.setFont(new Font("Arial", Font.BOLD, 12));
            seatPanel.add(rowLabel, gbc);
        }

        // 좌석 체크박스
        for (int row = 0; row < theaterInfo.getLength(); row++) {
            for (int col = 0; col < theaterInfo.getWidth(); col++) {
                int seatId = row * theaterInfo.getWidth() + col + 1;
                JCheckBox seatCheckBox = new JCheckBox();
                seatCheckBox.setPreferredSize(new Dimension(30, 30));
                seatCheckBox.setFont(new Font("Arial", Font.PLAIN, 10));
                if (reservedSeats.contains(seatId)) {
                    seatCheckBox.setEnabled(false);
                } else {
                    seatCheckBox.addActionListener(e -> selectSeat(seatId, seatCheckBox));
                }
                gbc.gridx = col + 1;
                gbc.gridy = row + 1;
                gbc.insets = new Insets(0, 0, 5, 5);
                seatPanel.add(seatCheckBox, gbc);
            }
        }

        return seatPanel;
    }

    private JLabel createScreenLabel() {
        JLabel screenLabel = new JLabel("Screen", JLabel.CENTER);
        screenLabel.setFont(new Font("Arial", Font.BOLD, 20));
        screenLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        return screenLabel;
    }

    private void selectSeat(int seatId, JCheckBox seatCheckBox) {
        if (seatCheckBox.isSelected()) {
            if (selectedSeats.size() < bookingInfo.getSelectedPeople()) {
                selectedSeats.add(seatId);
                updateSelectedSeatsPanel();
                updateTotalAmount();
            } else {
                seatCheckBox.setSelected(false);
                JOptionPane.showMessageDialog(this, "좌석을 모두 선택했습니다.", "정보", JOptionPane.INFORMATION_MESSAGE);
            }
        } else {
            selectedSeats.remove((Integer) seatId);
            updateSelectedSeatsPanel();
            updateTotalAmount();
        }
    }

    private JPanel createSelectedSeatsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));
        panel.setPreferredSize(new Dimension(200, 0));

        selectedSeatsLabel = new JLabel("선택된 좌석:");
        selectedSeatsLabel.setVerticalAlignment(JLabel.TOP);
        panel.add(selectedSeatsLabel, BorderLayout.NORTH);

        selectedSeatsPanel = new JPanel(new GridBagLayout());
        panel.add(selectedSeatsPanel, BorderLayout.CENTER);

        totalAmountLabel = new JLabel("총 금액: 0원");
        panel.add(totalAmountLabel, BorderLayout.SOUTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 0, 10));
        JButton resetButton = new JButton("좌석 초기화");
        resetButton.addActionListener(e -> resetSeats());
        buttonPanel.add(resetButton);

        JButton finalizeButton = new JButton("최종 예약");
        finalizeButton.addActionListener(e -> finalizeBooking());
        buttonPanel.add(finalizeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void updateSelectedSeatsPanel() {
        selectedSeatsPanel.removeAll();
        int selectedPeople = bookingInfo.getSelectedPeople();
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(5, 0, 5, 0);

        if (selectedSeats.isEmpty()) {
            for (int i = 0; i < selectedPeople; i++) {
                JLabel defaultLabel = new JLabel("좌석 선택");
                defaultLabel.setForeground(Color.GRAY);
                selectedSeatsPanel.add(defaultLabel, gbc);
                gbc.gridy++;
            }
        } else {
            for (int seatId : selectedSeats) {
                char rowLabel = (char) ('A' + (seatId - 1) / theaterInfo.getWidth());
                int colLabel = (seatId - 1) % theaterInfo.getWidth() + 1;
                selectedSeatsPanel.add(new JLabel(rowLabel + "-" + colLabel), gbc);
                gbc.gridy++;
            }
            for (int i = selectedSeats.size(); i < selectedPeople; i++) {
                JLabel defaultLabel = new JLabel("좌석 선택");
                defaultLabel.setForeground(Color.GRAY);
                selectedSeatsPanel.add(defaultLabel, gbc);
                gbc.gridy++;
            }
        }
        selectedSeatsPanel.revalidate();
        selectedSeatsPanel.repaint();
    }

    private void updateTotalAmount() {
        int selectedPeople = bookingInfo.getSelectedPeople();
        double totalAmount = selectedSeats.size() * standardPrice;
        totalAmountLabel.setText("총 금액: " + totalAmount + "원");
    }

    private void resetSeats() {
        selectedSeats.clear();
        for (Component component : seatPanel.getComponents()) {
            if (component instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) component;
                checkBox.setSelected(false);
            }
        }
        updateSelectedSeatsPanel();
        updateTotalAmount();
    }

    private void finalizeBooking() {
        if (selectedSeats.size() != bookingInfo.getSelectedPeople()) {
            JOptionPane.showMessageDialog(this, "모든 좌석을 선택해 주세요.", "오류", JOptionPane.ERROR_MESSAGE);
        } else {
            JFrame dialogFrame = new JFrame("최종 예약");
            dialogFrame.setSize(400, 300);
            dialogFrame.setLocationRelativeTo(null);

            JPanel dialogPanel = new JPanel(new BorderLayout());

            // 영화 포스터 이미지 패널
            String posterPath = getPosterImagePath(bookingInfo.getMovieInfo().getGenreName());
            CustomImagePanel imagePanel = new CustomImagePanel(posterPath);
            imagePanel.setPreferredSize(new Dimension(100, 150));

            // 영화 정보 패널
            JPanel movieInfoPanel = new JPanel(new GridLayout(5, 1));
            movieInfoPanel.add(new JLabel("영화 제목: " + bookingInfo.getMovieInfo().getMovieName()));
            movieInfoPanel.add(new JLabel("상영 날짜: " + bookingInfo.getSelectedDate()));
            movieInfoPanel.add(new JLabel("상영관 번호: " + bookingInfo.getSelectedTheaterId()));
            
            // 좌석 정보
            StringBuilder seatsInfo = new StringBuilder();
            for (int seatId : selectedSeats) {
                char rowLabel = (char) ('A' + (seatId - 1) / theaterInfo.getWidth());
                int colLabel = (seatId - 1) % theaterInfo.getWidth() + 1;
                seatsInfo.append(rowLabel).append("-").append(colLabel).append(" ");
            }
            movieInfoPanel.add(new JLabel("좌석: " + seatsInfo.toString().trim()));
            
            double totalPrice = bookingInfo.getSelectedPeople() * standardPrice* 1000;
            movieInfoPanel.add(new JLabel("금액: " + totalPrice));

            // 영화 포스터와 정보를 함께 담을 패널
            JPanel movieDetailsPanel = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.insets = new Insets(15, 0, 0, 10); // 오른쪽에 10픽셀 간격 추가
            movieDetailsPanel.add(imagePanel, gbc);

            gbc.gridx = 1;
            gbc.gridy = 0;
            gbc.insets = new Insets(15, 0, 0, 0); // 기본 간격
            movieDetailsPanel.add(movieInfoPanel, gbc);

            JLabel confirmLabel = new JLabel("해당 영화를 예매하시겠습니까?");
            confirmLabel.setHorizontalAlignment(JLabel.CENTER);

            JPanel buttonPanel = new JPanel(new GridLayout(1, 2));
            JButton yesButton = new JButton("예");
            JButton noButton = new JButton("아니오");

            yesButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    FinalBookingDTO finalBooking = new FinalBookingDTO(
                        bookingInfo.getScreeningId(),
                        bookingInfo.getSelectedTheaterId(),
                        selectedSeats,
                        totalPrice/1000,
                        1 // 가정된 customer_id 값
                    );
                    movieDAO.saveFinalBooking(finalBooking);
                    JOptionPane.showMessageDialog(dialogFrame, "예매가 완료되었습니다.", "완료", JOptionPane.INFORMATION_MESSAGE);
                    
                    dialogFrame.dispose();
                   

                    MainView mainView = (MainView) getTopLevelAncestor();
                    mainView.removeAllComponents();
                    MemberPanel newMemberPanel = new MemberPanel(mainView);
                    mainView.addPanel(newMemberPanel);
                    newMemberPanel.showMemberMenu();

                }
            });

            noButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dialogFrame.dispose();
                }
            });

            buttonPanel.add(yesButton);
            buttonPanel.add(noButton);

            dialogPanel.add(movieDetailsPanel, BorderLayout.NORTH);
            dialogPanel.add(confirmLabel, BorderLayout.CENTER);
            dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

            dialogFrame.add(dialogPanel);
            dialogFrame.setVisible(true);
        }
    }
    
    private String getFormattedSeats() {
        StringBuilder formattedSeats = new StringBuilder();
        for (int i = 0; i < selectedSeats.size(); i++) {
            int seatId = selectedSeats.get(i);
            char rowLabel = (char) ('A' + (seatId - 1) / theaterInfo.getWidth());
            int colLabel = (seatId - 1) % theaterInfo.getWidth() + 1;
            formattedSeats.append(rowLabel).append("-").append(colLabel);
            if (i < selectedSeats.size() - 1) {
                formattedSeats.append(", ");
            }
        }
        return formattedSeats.toString();
    }

    private String getPosterImagePath(String genre) {
        String imageName = "";
        String path = "src/assets/";
        
        switch (genre) {
            case "Action":
                imageName = "action_cover";
                break;
            case "Romance":
                imageName = "romance_cover";
                break;
            case "Sci-Fi":
                imageName = "science_cover";
                break;
            case "Horror":
                imageName = "horror_cover";
                break;
            case "Comedy":
                imageName = "comedy_cover";
                break;
            case "Mystery":
                imageName = "mystery_cover";
                break;
            case "Drama":
                imageName = "drama_cover";
                break;
            case "Animation":
                imageName = "animation_cover";
                break;
            case "Fantasy":
                imageName = "fantasy_cover";
                break;
            case "Thriller":
                imageName = "thriller_cover";
                break;
            case "Documentary":
                imageName = "document_cover";
                break;
        }
        return path + imageName + ".jpg";
    }
}
