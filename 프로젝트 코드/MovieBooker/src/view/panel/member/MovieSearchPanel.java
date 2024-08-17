package view.panel.member;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.MovieDAO;
import model.MovieInfoVO;
import view.MainView;

public class MovieSearchPanel extends JPanel {
    
    private JTextField searchField;
    private JButton searchButton;
    private JPanel moviePanel;
    private JButton prevButton;
    private JButton nextButton;
    private MainView mainView;
    private MemberPanel memberPanel;
    
    private List<MovieInfoVO> movieList;
    private int currentPage;
    private final int MOVIES_PER_PAGE = 4;
    private MovieDAO movieDAO;
    
    public MovieSearchPanel(MainView mainView, MemberPanel memberPanel) {
    	this.mainView = mainView;
    	this.memberPanel = memberPanel;
    	
        movieDAO = new MovieDAO();
        initializePanel();
        fetchAllMoviesFromDB();
    }
    
    private void initializePanel() {
        setLayout(new BorderLayout());
        
        // 검색 패널 생성
        JPanel searchPanel = new JPanel(new GridLayout(1, 2));
        searchButton = new JButton("영화 검색");
        searchPanel.add(searchButton);
        
        // 영화 패널 생성
        moviePanel = new JPanel(new GridLayout(1, 4));
        JScrollPane scrollPane = new JScrollPane(moviePanel);
        
        // 내비게이션 버튼 생성
        prevButton = new JButton("이전");
        nextButton = new JButton("다음");
        
        JPanel navPanel = new JPanel(new BorderLayout());
        navPanel.add(prevButton, BorderLayout.WEST);
        navPanel.add(nextButton, BorderLayout.EAST);
        
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(navPanel, BorderLayout.SOUTH);
        
        // 이벤트 리스너 추가
        searchButton.addActionListener(e -> openSearchFrame());
        prevButton.addActionListener(e -> showPreviousPage());
        nextButton.addActionListener(e -> showNextPage());
    }
    
    private void fetchAllMoviesFromDB() {
        movieList = movieDAO.searchMovies(null, null, null, null);
        updateMovieDisplay();
    }
    
    private void updateMovieDisplay() {
        moviePanel.removeAll();
        int start = currentPage * MOVIES_PER_PAGE;
        int end = Math.min(start + MOVIES_PER_PAGE, movieList.size());
        
        for (int i = start; i < end; i++) {
            MovieInfoVO movie = movieList.get(i);
            JPanel movieInfoPanel = createMovieInfoPanel(movie);
            movieInfoPanel.setBorder(new EmptyBorder(5,5,5,5));
            moviePanel.add(movieInfoPanel);
        }
        
     // 빈 패널로 채우기 (결과가 적은 경우)
        for (int i = end; i < start + MOVIES_PER_PAGE; i++) {
            JPanel emptyPanel = new JPanel();
            emptyPanel.setPreferredSize(new Dimension(100, 150));
            emptyPanel.setBorder(new EmptyBorder(5,5,5,5));
            moviePanel.add(emptyPanel);
        }
        
        moviePanel.revalidate();
        moviePanel.repaint();
    }
    
    private JPanel createMovieInfoPanel(MovieInfoVO movie) {
        JPanel panel = new JPanel(new BorderLayout());
        
        // 영화 포스터 이미지 패널
        String posterPath = getPosterImagePath(movie.getGenreName());
        CustomImagePanel imagePanel = new CustomImagePanel(posterPath);
        imagePanel.setPreferredSize(new Dimension(100, 150));

        // 패널 클릭 이벤트 추가
        imagePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                showBookingConfirmationDialog(movie);
            }
        });
     
        
        // 영화 정보 패널
        JPanel infoPanel = new JPanel(new GridLayout(4, 1));
        infoPanel.add(new JLabel("영화 제목: " + movie.getMovieName()));
        infoPanel.add(new JLabel("감독: " + movie.getDirectorName()));
        infoPanel.add(new JLabel("배우: " + movie.getActorName()));
        infoPanel.add(new JLabel("장르: " + movie.getGenreName()));
        
        panel.add(imagePanel, BorderLayout.CENTER);
        panel.add(infoPanel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private void showPreviousPage() {
        if (currentPage > 0) {
            currentPage--;
            updateMovieDisplay();
        }
    }
    
    private void showNextPage() {
        if ((currentPage + 1) * MOVIES_PER_PAGE < movieList.size()) {
            currentPage++;
            updateMovieDisplay();
        }
    }
    
    private void openSearchFrame() {
        JFrame searchFrame = new JFrame("영화 검색");
        searchFrame.setSize(300, 200);
        searchFrame.setLocationRelativeTo(null);
        
        JPanel searchPanel = new JPanel(new GridLayout(5, 2));
        JTextField movieNameField = new JTextField();
        JTextField directorNameField = new JTextField();
        JTextField actorNameField = new JTextField();
        JTextField genreNameField = new JTextField();
        
        searchPanel.add(new JLabel("영화 제목:"));
        searchPanel.add(movieNameField);
        searchPanel.add(new JLabel("감독 이름:"));
        searchPanel.add(directorNameField);
        searchPanel.add(new JLabel("배우 이름:"));
        searchPanel.add(actorNameField);
        searchPanel.add(new JLabel("장르:"));
        searchPanel.add(genreNameField);
        
        JButton searchButton = new JButton("검색");
        searchPanel.add(searchButton);
        
        searchFrame.add(searchPanel);
        searchFrame.setVisible(true);
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                performSearch(movieNameField.getText(), directorNameField.getText(), actorNameField.getText(), genreNameField.getText());
                searchFrame.dispose();
            }
        });
    }
    
    private void performSearch(String movieName, String directorName, String actorName, String genreName) {
        movieList = movieDAO.searchMovies(movieName, directorName, actorName, genreName);
        currentPage = 0;
        updateMovieDisplay();
    }
    
    private String getPosterImagePath(String genre) {
    	String imageName = "";
    	String path = "src\\assets/";
    	switch(genre) {
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
    
    private void showBookingConfirmationDialog(MovieInfoVO movie) {
        JFrame dialogFrame = new JFrame("영화 예매");
        dialogFrame.setSize(400, 300);
        dialogFrame.setLocationRelativeTo(null);
        
        JPanel dialogPanel = new JPanel(new BorderLayout());
        
        // 영화 포스터 이미지 패널
        String posterPath = getPosterImagePath(movie.getGenreName());
        System.out.println(posterPath);
        CustomImagePanel imagePanel = new CustomImagePanel(posterPath);
        imagePanel.setPreferredSize(new Dimension(100, 150));
        
        // 영화 정보 패널
        JPanel movieInfoPanel = new JPanel(new GridLayout(4, 1));
        movieInfoPanel.add(new JLabel("영화 제목: " + movie.getMovieName()));
        movieInfoPanel.add(new JLabel("감독: " + movie.getDirectorName()));
        movieInfoPanel.add(new JLabel("배우: " + movie.getActorName()));
        movieInfoPanel.add(new JLabel("장르: " + movie.getGenreName()));
        
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
            	// MainView의 인스턴스를 얻어와서 모든 컴포넌트를 제거하고 새로운 패널을 추가
                MainView mainView = (MainView) getTopLevelAncestor();
                mainView.removeAllComponents();
                mainView.setSize(800, 500);
                
                // 새로운 패널을 추가 (예시로 새로운 MovieBookingPanel 추가)
                MovieBookingPanel bookingPanel = new MovieBookingPanel(movie, memberPanel);
                mainView.addPanel(bookingPanel);
                
                dialogFrame.dispose();
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
