package model;

public class TheaterInfoDTO {
	
	private int theaterId;
    private int totalSeats;
    private int isActive;
    private int width;
    private int length;
    private double standardPrice;

    public TheaterInfoDTO(int theaterId, int width, int length, double standardPrice) {
        this.theaterId = theaterId;
        this.width = width;
        this.length = length;
        this.standardPrice = standardPrice;
    }

    public int getTheaterId() {
        return theaterId;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public int getIsActive() {
        return isActive;
    }

    public int getWidth() {
        return width;
    }

    public int getLength() {
        return length;
    }
    
    public double getStandardPrice() {
    	return standardPrice;
    }

}
