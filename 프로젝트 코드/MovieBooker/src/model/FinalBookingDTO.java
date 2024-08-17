package model;

import java.util.List;

public class FinalBookingDTO {
    private int screeningId;
    private int theaterId;
    private List<Integer> seatIds;
    private double totalAmount;
    private int customerId;
    private String movieName;  
    private String screeningDate;

    public FinalBookingDTO(int screeningId, int theaterId, List<Integer> seatIds, double totalAmount, int customerId) {
        this.screeningId = screeningId;
        this.theaterId = theaterId;
        this.seatIds = seatIds;
        this.totalAmount = totalAmount;
        this.customerId = customerId;
    }

    public int getScreeningId() {
        return screeningId;
    }

    public int getTheaterId() {
        return theaterId;
    }

    public List<Integer> getSeatIds() {
        return seatIds;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public int getCustomerId() {
        return customerId;
    }
    
    public String getMovieName() {
        return movieName;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public String getScreeningDate() {
        return screeningDate;
    }

    public void setScreeningDate(String screeningDate) {
        this.screeningDate = screeningDate;
    }
}
