package model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MovieDAO {
	
	private DBConnector dbConnector;
	
	public MovieDAO() {
		dbConnector = DBConnector.getInstance();

		dbConnector.connectDB("root", "1234"); //후에 user1, user1로 변경해야함
	}
	
	public List<MovieInfoVO> searchMovies(String movieName, String directorName, String actorName, String genreName){
		List<MovieInfoVO> movies = new ArrayList<>();
		StringBuilder query = new StringBuilder("SELECT * FROM movies WHERE 1=1");
		
		if(movieName != null && !movieName.isEmpty()) {
			query.append(" AND title LIKE '%").append(movieName).append("%'");
		}
		
		if(directorName != null && !directorName.isEmpty()) {
			query.append(" AND director LIKE '%").append(directorName).append("%'");
		}
		
		if(actorName != null && !actorName.isEmpty()) {
			query.append(" AND actors LIKE '%").append(actorName).append("%'");
		}
		
		if(genreName != null && !genreName.isEmpty()) {
			query.append(" AND genre LIKE '%").append(genreName).append("%'");
		}
		
		try {
			ResultSet rs = dbConnector.select(query.toString());
			
			while(rs.next()) {
				String movieNameResult = rs.getString("title");
				String directorNameResult = rs.getString("director");
				String actorNameResult = rs.getString("actors");
				String genreNameResult = rs.getString("genre");
				
				MovieInfoVO movieInfo = new MovieInfoVO(movieNameResult, directorNameResult, actorNameResult, genreNameResult);
				movies.add(movieInfo);
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		
		return movies;
	}
	
	 public List<ScreeningInfoDTO> fetchScreeningDates(String movieName) {
	        List<ScreeningInfoDTO> screeningInfoList = new ArrayList<>();
	        String query = "SELECT s.screening_id, s.movie_id, s.theater_id, s.start_date, s.day_of_week, s.session, s.start_time "
	                     + "FROM Screenings s "
	                     + "JOIN Movies m ON s.movie_id = m.movie_id "
	                     + "WHERE m.title LIKE '%" + movieName + "%'";

	        try {
	            ResultSet rs = dbConnector.select(query);

	            while (rs.next()) {
	                int screeningId = rs.getInt("screening_id");
	                int movieId = rs.getInt("movie_id");
	                int theaterId = rs.getInt("theater_id");
	                LocalDate startDate = rs.getDate("start_date").toLocalDate();
	                String dayOfWeek = rs.getString("day_of_week");
	                int session = rs.getInt("session");
	                LocalTime startTime = rs.getTime("start_time").toLocalTime();

	                ScreeningInfoDTO screeningInfo = new ScreeningInfoDTO(screeningId, movieId, theaterId, startDate, dayOfWeek, session, startTime);
	                screeningInfoList.add(screeningInfo);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return screeningInfoList;
	   }
	 
	 public TheaterInfoDTO getTheaterInfo(int theaterId) {
	        String query = "SELECT width, length, standard_price FROM Theaters WHERE theater_id = ?";
	        try (Connection conn = dbConnector.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, theaterId);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    int width = rs.getInt("width");
	                    int length = rs.getInt("length");
	                    double standardPrice = rs.getDouble("standard_price");
	                    return new TheaterInfoDTO(theaterId, width, length, standardPrice);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return null;
	    }


	 public List<Integer> getReservedSeats(int theaterId, LocalDate date, LocalTime time) {
	        List<Integer> reservedSeats = new ArrayList<>();
	        String query = "SELECT seat_id FROM Tickets WHERE theater_id = ? AND screening_id IN (SELECT screening_id FROM Screenings WHERE start_date = ? AND start_time = ?)";
	        try (Connection conn = dbConnector.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, theaterId);
	            stmt.setDate(2, java.sql.Date.valueOf(date));
	            stmt.setTime(3, java.sql.Time.valueOf(time));
	            try (ResultSet rs = stmt.executeQuery()) {
	                while (rs.next()) {
	                    reservedSeats.add(rs.getInt("seat_id"));
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return reservedSeats;
	    }
	 
	 public double getTheaterPrice(int theaterId) {
	        String query = "SELECT standard_price FROM Theaters WHERE theater_id = ?";
	        try (Connection conn = dbConnector.getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setInt(1, theaterId);
	            try (ResultSet rs = stmt.executeQuery()) {
	                if (rs.next()) {
	                    return rs.getDouble("standard_price");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return 0.0;
	    }
	 
	 public void saveFinalBooking(FinalBookingDTO finalBooking) {
	        String bookingQuery = "INSERT INTO Bookings (payment_method, payment_status, amount, customer_id, payment_date) VALUES (?, ?, ?, ?, ?)";
	        String ticketQuery = "INSERT INTO Tickets (screening_id, theater_id, seat_id, booking_id, is_issued, standard_price, sale_price) VALUES (?, ?, ?, ?, ?, ?, ?)";
	        
	        try (Connection conn = dbConnector.getConnection();
	             PreparedStatement bookingStmt = conn.prepareStatement(bookingQuery, PreparedStatement.RETURN_GENERATED_KEYS);
	             PreparedStatement ticketStmt = conn.prepareStatement(ticketQuery)) {

	            // Save booking
	            bookingStmt.setString(1, "Credit Card"); // 가정된 값
	            bookingStmt.setString(2, "Paid"); // 가정된 값
	            bookingStmt.setDouble(3, finalBooking.getTotalAmount());
	            bookingStmt.setInt(4, finalBooking.getCustomerId()); // 가정된 customer_id 값
	            bookingStmt.setDate(5, java.sql.Date.valueOf(LocalDate.now()));
	            bookingStmt.executeUpdate();

	            // Get generated booking_id
	            try (ResultSet generatedKeys = bookingStmt.getGeneratedKeys()) {
	                if (generatedKeys.next()) {
	                    int bookingId = generatedKeys.getInt(1);

	                    // Save tickets
	                    for (int seatId : finalBooking.getSeatIds()) {
	                        ticketStmt.setInt(1, finalBooking.getScreeningId());
	                        ticketStmt.setInt(2, finalBooking.getTheaterId());
	                        ticketStmt.setInt(3, seatId);
	                        ticketStmt.setInt(4, bookingId);
	                        ticketStmt.setBoolean(5, true);
	                        ticketStmt.setDouble(6, finalBooking.getTotalAmount() / finalBooking.getSeatIds().size()); // 좌석당 가격
	                        ticketStmt.setDouble(7, finalBooking.getTotalAmount() / finalBooking.getSeatIds().size()); // 할인이 없는 경우
	                        ticketStmt.executeUpdate();
	                    }
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }
	 
	 

	    public List<FinalBookingDTO> getAllBookings() {
	        List<FinalBookingDTO> bookings = new ArrayList<>();
	        String query = "SELECT b.booking_id, b.amount, b.customer_id, t.screening_id, t.theater_id, GROUP_CONCAT(t.seat_id) AS seats, " +
	                       "s.start_date, m.title " +
	                       "FROM Bookings b " +
	                       "JOIN Tickets t ON b.booking_id = t.booking_id " +
	                       "JOIN Screenings s ON t.screening_id = s.screening_id " +
	                       "JOIN Movies m ON s.movie_id = m.movie_id " +
	                       "GROUP BY b.booking_id, t.screening_id, t.theater_id, s.start_date, m.title";

	        try (Connection connection = dbConnector.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query);
	             ResultSet resultSet = statement.executeQuery()) {

	            while (resultSet.next()) {
	                int bookingId = resultSet.getInt("booking_id");
	                int screeningId = resultSet.getInt("screening_id");
	                int theaterId = resultSet.getInt("theater_id");
	                String seats = resultSet.getString("seats");
	                double totalPrice = resultSet.getDouble("amount");
	                int customerId = resultSet.getInt("customer_id");
	                String movieName = resultSet.getString("title");
	                String screeningDate = resultSet.getString("start_date");

	                List<Integer> seatList = parseSeats(seats);
	                FinalBookingDTO booking = new FinalBookingDTO(screeningId, theaterId, seatList, totalPrice, customerId);
	                booking.setMovieName(movieName);
	                booking.setScreeningDate(screeningDate);

	                bookings.add(booking);
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return bookings;
	    }

	    private List<Integer> parseSeats(String seats) {
	        List<Integer> seatList = new ArrayList<>();
	        String[] seatArray = seats.split(",");
	        for (String seat : seatArray) {
	            seatList.add(Integer.parseInt(seat.trim()));
	        }
	        return seatList;
	    }
	    
	    public List<ScreeningInfoDTO> getScreeningsByMovieName(String movieName) {
	        List<ScreeningInfoDTO> screenings = new ArrayList<>();
	        String query = "SELECT s.screening_id, s.movie_id, s.theater_id, s.start_date, s.day_of_week, s.session, s.start_time " +
	                       "FROM Screenings s " +
	                       "JOIN Movies m ON s.movie_id = m.movie_id " +
	                       "WHERE m.title = ?";

	        try (Connection connection = dbConnector.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setString(1, movieName);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    int screeningId = resultSet.getInt("screening_id");
	                    int movieId = resultSet.getInt("movie_id");
	                    int theaterId = resultSet.getInt("theater_id");
	                    LocalDate startDate = resultSet.getDate("start_date").toLocalDate();
	                    String dayOfWeek = resultSet.getString("day_of_week");
	                    int session = resultSet.getInt("session");
	                    LocalTime startTime = resultSet.getTime("start_time").toLocalTime();

	                    ScreeningInfoDTO screening = new ScreeningInfoDTO(screeningId, movieId, theaterId, startDate, dayOfWeek, session, startTime);
	                    screenings.add(screening);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return screenings;
	    }

	    public List<Integer> getSeatsByScreeningId(int screeningId) {
	        List<Integer> seatIds = new ArrayList<>();
	        String query = "SELECT seat_id FROM Tickets WHERE screening_id = ?";

	        try (Connection connection = dbConnector.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setInt(1, screeningId);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                while (resultSet.next()) {
	                    seatIds.add(resultSet.getInt("seat_id"));
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return seatIds;
	    }

	    public double getPriceByScreeningId(int screeningId) {
	        String query = "SELECT standard_price FROM Tickets WHERE screening_id = ?";
	        double price = 0.0;
	        try (Connection connection = dbConnector.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setInt(1, screeningId);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    price = resultSet.getDouble("standard_price");
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	        return price;
	    }
	    
	    public void deleteBookingByDetails(String movieName, String screeningDate, int theaterId, int customerId) {
	        String selectQuery = "SELECT b.booking_id FROM Bookings b " +
	                             "JOIN Tickets t ON b.booking_id = t.booking_id " +
	                             "JOIN Screenings s ON t.screening_id = s.screening_id " +
	                             "JOIN Movies m ON s.movie_id = m.movie_id " +
	                             "WHERE m.title = ? AND s.start_date = ? AND s.theater_id = ? AND b.customer_id = ?";

	        String deleteQuery = "DELETE FROM Tickets WHERE booking_id = ?";

	        try (Connection connection = dbConnector.getConnection();
	             PreparedStatement selectStatement = connection.prepareStatement(selectQuery);
	             PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
	            
	            selectStatement.setString(1, movieName);
	            selectStatement.setString(2, screeningDate);
	            selectStatement.setInt(3, theaterId);
	            selectStatement.setInt(4, customerId);
	            
	            try (ResultSet resultSet = selectStatement.executeQuery()) {
	                while (resultSet.next()) {
	                    int bookingId = resultSet.getInt("booking_id");

	                    deleteStatement.setInt(1, bookingId);
	                    deleteStatement.executeUpdate();
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public MovieInfoVO getMovieInfoByTitle(String movieName) {
	        String query = "SELECT title, director, actors, genre FROM Movies WHERE title = ?";
	        MovieInfoVO movieInfo = null;

	        try (Connection connection = dbConnector.getConnection();
	             PreparedStatement statement = connection.prepareStatement(query)) {

	            statement.setString(1, movieName);
	            try (ResultSet resultSet = statement.executeQuery()) {
	                if (resultSet.next()) {
	                    String directorName = resultSet.getString("director");
	                    String actorName = resultSet.getString("actors");
	                    String genreName = resultSet.getString("genre");

	                    movieInfo = new MovieInfoVO(movieName, directorName, actorName, genreName);
	                }
	            }
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return movieInfo;
	    }
}
