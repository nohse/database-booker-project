package model;

import java.time.LocalDate;
import java.time.LocalTime;

public class BookingInfoDTO {
	private MovieInfoVO movieInfo;
    private LocalDate selectedDate;
    private LocalTime selectedTime;
    private int selectedPeople;
    private int selectedTheaterId;
    private int seatId;
    private int screeningId;

    public BookingInfoDTO(MovieInfoVO movieInfo, LocalDate selectedDate, 
    		LocalTime selectedTime, int selectedPeople, int selectedTheaterId, int screeningId) {
        this.movieInfo = movieInfo;
        this.selectedDate = selectedDate;
        this.selectedTime = selectedTime;
        this.selectedTheaterId = selectedTheaterId;
        this.screeningId = screeningId;
    }

    public MovieInfoVO getMovieInfo() {
        return movieInfo;
    }
    
    public void setMovieInfo(MovieInfoVO movieInfo) {
    	this.movieInfo = movieInfo;
    }

    public LocalDate getSelectedDate() {
        return selectedDate;
    }
    
    public void setSelectedDate(LocalDate selectedDate) {
    	this.selectedDate = selectedDate;
    }

    public LocalTime getSelectedTime() {
        return selectedTime;
    }
    
    public void setSelectedTime(LocalTime selectedTime) {
    	this.selectedTime = selectedTime;
    }

    public int getSelectedPeople() {
        return selectedPeople;
    }
    
    public void setSelectedPeople(int selectedPeople) {
    	this.selectedPeople = selectedPeople;
    }
    
    public int getSelectedTheaterId() {
    	return selectedTheaterId;
    }
    
    public void setSelectedTheaterId(int selectedTheaterId) {
    	this.selectedTheaterId = selectedTheaterId;
    }
    
    public int getSeatId() {
    	return seatId;
    }
    
    public void setSeatId(int seatId) {
    	this.seatId = seatId;
    }
    
    public int getScreeningId() { // 추가된 부분
        return screeningId;
    }

    public void setScreeningId(int screeningId) { // 추가된 부분
        this.screeningId = screeningId;
    }
}
