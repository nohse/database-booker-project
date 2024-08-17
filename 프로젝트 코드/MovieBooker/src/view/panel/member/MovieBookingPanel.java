package view.panel.member;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.EmptyBorder;

import model.BookingInfoDTO;
import model.MovieDAO;
import model.MovieInfoVO;
import model.ScreeningInfoDTO;
import view.MainView;

public class MovieBookingPanel extends JPanel {

    private JComboBox<String> monthComboBox;
    private JPanel calendarPanel;
    private List<ScreeningInfoDTO> screeningInfoList;
    private MovieDAO movieDAO;
    private JPanel timeSelectionPanel;
    private JPanel peopleSelectionPanel;
    private JPanel theaterSelectionPanel;
    private JPanel rightPanel;
    private JLabel selectedInfoLabel;
    private ButtonGroup timeButtonGroup;
    private ButtonGroup peopleButtonGroup;
    private ButtonGroup theaterButtonGroup;
    private MovieInfoVO movie;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private MemberPanel memberPanel;
    private int selectedPeople;
    private int selectedTheaterId;
    private int selectedScreeningId;

    public MovieBookingPanel(MovieInfoVO movie, MemberPanel memberPanel) {
        this.movie = movie;
        this.memberPanel = memberPanel;
        movieDAO = new MovieDAO();
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        screeningInfoList = movieDAO.fetchScreeningDates(movie.getMovieName());
        mainPanel.add(createDateSelectionPanel(), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        mainPanel.add(createRightPanel(), gbc);

        add(mainPanel, BorderLayout.CENTER);

        add(createSummaryPanel(), BorderLayout.EAST);
    }

    private JPanel createDateSelectionPanel() {
        JPanel dateSelectionPanel = new JPanel(new BorderLayout());

        JLabel dateSelectionLabel = new JLabel("날짜 선택");
        dateSelectionPanel.add(dateSelectionLabel, BorderLayout.NORTH);

        JPanel monthPanel = new JPanel(new BorderLayout());
        JLabel yearLabel = new JLabel("2024년");
        monthComboBox = new JComboBox<>(new String[] { "1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월" });
        monthComboBox.setSelectedIndex(4);
        monthComboBox.addActionListener(e -> updateCalendar());

        monthPanel.add(yearLabel, BorderLayout.WEST);
        monthPanel.add(monthComboBox, BorderLayout.EAST);

        // 중간 패널에 monthPanel과 calendarPanel을 추가
        JPanel middlePanel = new JPanel(new BorderLayout());
        middlePanel.add(monthPanel, BorderLayout.NORTH);

        calendarPanel = new JPanel(new GridLayout(0, 7));
        middlePanel.add(calendarPanel, BorderLayout.CENTER);

        dateSelectionPanel.add(middlePanel, BorderLayout.CENTER);

        JLabel instructionLabel = new JLabel("원하는 날짜를 선택해주세요", JLabel.CENTER);
        dateSelectionPanel.setBorder(new EmptyBorder(10, 0, 0, 0));
        dateSelectionPanel.add(instructionLabel, BorderLayout.SOUTH);

        updateCalendar();

        return dateSelectionPanel;
    }

    private JPanel createRightPanel() {
        rightPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel timeSelectionLabel = new JLabel("상영 시간 선택");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);
        rightPanel.add(timeSelectionLabel, gbc);

        timeSelectionPanel = new JPanel(new GridLayout(0, 1));
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(timeSelectionPanel, gbc);

        JLabel peopleSelectionLabel = new JLabel("인원 선택");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(peopleSelectionLabel, gbc);

        peopleSelectionPanel = new JPanel(new GridLayout(0, 1));
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(peopleSelectionPanel, gbc);

        JLabel theaterSelectionLabel = new JLabel("상영관 선택");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.WEST;
        rightPanel.add(theaterSelectionLabel, gbc);

        theaterSelectionPanel = new JPanel(new GridLayout(0, 1));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        rightPanel.add(theaterSelectionPanel, gbc);

        timeButtonGroup = new ButtonGroup();
        peopleButtonGroup = new ButtonGroup();
        theaterButtonGroup = new ButtonGroup();

        // 인원 선택 라디오 버튼 추가
        for (int i = 1; i <= 6; i++) {
            final int peopleCount = i;
            JRadioButton peopleButton = new JRadioButton(i + "명");
            peopleButton.addActionListener(e -> {
                selectedPeople = peopleCount;
                updateSelectedInfo();
            });
            peopleButtonGroup.add(peopleButton);
            peopleSelectionPanel.add(peopleButton);
        }

        return rightPanel;
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        YearMonth yearMonth = YearMonth.of(2024, monthComboBox.getSelectedIndex() + 1);
        LocalDate firstDayOfMonth = yearMonth.atDay(1);
        int dayOfWeekValue = firstDayOfMonth.getDayOfWeek().getValue();

        // 요일 라벨 추가
        for (DayOfWeek day : DayOfWeek.values()) {
            JLabel dayLabel = new JLabel(day.getDisplayName(TextStyle.SHORT, Locale.KOREAN), JLabel.CENTER);
            calendarPanel.add(dayLabel);
        }

        // 첫 주 빈 칸 채우기
        for (int i = 1; i < dayOfWeekValue; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // 날짜 버튼 추가
        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            LocalDate date = yearMonth.atDay(day);
            JButton dayButton = new JButton(String.valueOf(day));
            dayButton.setPreferredSize(new Dimension(50, 50));
            dayButton.setFont(new Font("Arial", Font.PLAIN, 10)); // 글씨 크기 설정

            // 버튼 활성화/비활성화 설정
            if (isScreeningDate(date)) {
                dayButton.setEnabled(true);
                dayButton.addActionListener(e -> {
                    selectedDate = date;
                    updateTimeSelection(date);
                    updateTheaterSelection(date);
                    updateSelectedInfo();
                });
            } else {
                dayButton.setEnabled(false);
            }

            calendarPanel.add(dayButton);
        }

        // 빈 칸 채우기
        int remainingCells = 7 - (calendarPanel.getComponentCount() % 7);
        if (remainingCells < 7) {
            for (int i = 0; i < remainingCells; i++) {
                calendarPanel.add(new JLabel(""));
            }
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private boolean isScreeningDate(LocalDate date) {
        for (ScreeningInfoDTO screeningInfo : screeningInfoList) {
            if (screeningInfo.getStartDate().equals(date)) {
                return true;
            }
        }
        return false;
    }

    private void updateTimeSelection(LocalDate date) {
        timeSelectionPanel.removeAll();
        timeButtonGroup = new ButtonGroup();

        List<LocalTime> availableTimes = new ArrayList<>();
        for (ScreeningInfoDTO screeningInfo : screeningInfoList) {
            if (screeningInfo.getStartDate().equals(date)) {
                availableTimes.add(screeningInfo.getStartTime());
            }
        }

        for (LocalTime time : availableTimes) {
            JRadioButton timeButton = new JRadioButton(time.toString());
            timeButton.addActionListener(e -> {
                selectedTime = time;
                selectedScreeningId = getScreeningIdForSelectedTime(date, time);
                updateSelectedInfo();
            });
            timeButtonGroup.add(timeButton);
            timeSelectionPanel.add(timeButton);
        }

        timeSelectionPanel.revalidate();
        timeSelectionPanel.repaint();
    }
    
    private int getScreeningIdForSelectedTime(LocalDate date, LocalTime time) { // 추가된 부분
        for (ScreeningInfoDTO screeningInfo : screeningInfoList) {
            if (screeningInfo.getStartDate().equals(date) && screeningInfo.getStartTime().equals(time)) {
                return screeningInfo.getScreeningId();
            }
        }
        return -1; // 없을 경우의 기본값
    }

    private void updateTheaterSelection(LocalDate date) {
        theaterSelectionPanel.removeAll();
        theaterButtonGroup = new ButtonGroup();

        List<Integer> availableTheaters = screeningInfoList.stream()
            .filter(screeningInfo -> screeningInfo.getStartDate().equals(date))
            .map(ScreeningInfoDTO::getTheaterId)
            .distinct()
            .collect(Collectors.toList());

        for (Integer theaterId : availableTheaters) {
            JRadioButton theaterButton = new JRadioButton("상영관 " + theaterId);
            theaterButton.addActionListener(e -> {
                selectedTheaterId = theaterId;
                updateSelectedInfo();
            });
            theaterButtonGroup.add(theaterButton);
            theaterSelectionPanel.add(theaterButton);
        }

        theaterSelectionPanel.revalidate();
        theaterSelectionPanel.repaint();
    }

    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new BorderLayout());
        summaryPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 영화 포스터 이미지 패널
        String posterPath = getPosterImagePath(movie.getGenreName());
        CustomImagePanel imagePanel = new CustomImagePanel(posterPath);
        imagePanel.setPreferredSize(new Dimension(150, 200));
        summaryPanel.add(imagePanel, BorderLayout.NORTH);

        // 선택한 정보 표시 라벨
        selectedInfoLabel = new JLabel();
        updateSelectedInfo();
        summaryPanel.add(selectedInfoLabel, BorderLayout.CENTER);

        // 예매하기 버튼
        JButton bookButton = new JButton("예매하기");
        bookButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedDate == null || selectedTime == null || selectedPeople == 0 || selectedTheaterId == 0) {
                    JOptionPane.showMessageDialog(MovieBookingPanel.this, "모든 정보를 입력해주세요.", "오류", JOptionPane.ERROR_MESSAGE);
                } else {
                    BookingInfoDTO bookingInfo = new BookingInfoDTO(movie, selectedDate, selectedTime, selectedPeople, selectedTheaterId, selectedScreeningId);
                    bookingInfo.setMovieInfo(movie);
                    bookingInfo.setSelectedDate(selectedDate);
                    bookingInfo.setSelectedPeople(selectedPeople);
                    bookingInfo.setSelectedTheaterId(selectedTheaterId);
                    bookingInfo.setSelectedTime(selectedTime);
                    bookingInfo.setScreeningId(selectedScreeningId);

                    MainView mainView = (MainView) getTopLevelAncestor();
                    mainView.removeAllComponents();

                    BookingSeatPanel bookingSeatPanel = new BookingSeatPanel(bookingInfo, memberPanel);
                    mainView.addPanel(bookingSeatPanel);
                }
            }
        });
        summaryPanel.add(bookButton, BorderLayout.SOUTH);

        return summaryPanel;
    }

    private void updateSelectedInfo() {
        String dateStr = selectedDate != null ? selectedDate.toString() : "날짜 선택";
        String timeStr = selectedTime != null ? selectedTime.toString() : "시간 선택";
        String peopleStr = selectedPeople > 0 ? selectedPeople + "명" : "인원 선택";
        String theaterStr = selectedTheaterId > 0 ? "상영관 번호: " + selectedTheaterId : "상영관 선택";

        selectedInfoLabel.setText("<html>선택한 날짜: " + dateStr + "<br>선택한 시간: " + timeStr + "<br>선택한 인원: " + peopleStr + "<br>" + theaterStr + "</html>");
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
        // 장르에 따른 이미지 경로 반환 로직 구현
        return path + imageName + ".jpg";
    }
}
