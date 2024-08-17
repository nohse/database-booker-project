package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScreeningInfoDTO {
	 private int screeningId;
	    private int movieId;
	    private int theaterId;
	    private LocalDate startDate;
	    private String dayOfWeek;
	    private int session;
	    private LocalTime startTime;

	    public ScreeningInfoDTO(int screeningId, int movieId, int theaterId, LocalDate startDate, String dayOfWeek, int session, LocalTime startTime) {
	        this.screeningId = screeningId;
	        this.movieId = movieId;
	        this.theaterId = theaterId;
	        this.startDate = startDate;
	        this.dayOfWeek = dayOfWeek;
	        this.session = session;
	        this.startTime = startTime;
	    }

	    // Getters and setters

	    public int getScreeningId() {
	        return screeningId;
	    }

	    public void setScreeningId(int screeningId) {
	        this.screeningId = screeningId;
	    }

	    public int getMovieId() {
	        return movieId;
	    }

	    public void setMovieId(int movieId) {
	        this.movieId = movieId;
	    }

	    public int getTheaterId() {
	        return theaterId;
	    }

	    public void setTheaterId(int theaterId) {
	        this.theaterId = theaterId;
	    }

	    public LocalDate getStartDate() {
	        return startDate;
	    }

	    public void setStartDate(LocalDate startDate) {
	        this.startDate = startDate;
	    }

	    public String getDayOfWeek() {
	        return dayOfWeek;
	    }

	    public void setDayOfWeek(String dayOfWeek) {
	        this.dayOfWeek = dayOfWeek;
	    }

	    public int getSession() {
	        return session;
	    }

	    public void setSession(int session) {
	        this.session = session;
	    }

	    public LocalTime getStartTime() {
	        return startTime;
	    }

	    public void setStartTime(LocalTime startTime) {
	        this.startTime = startTime;
	    }
}
