package model;

public class MovieInfoVO {
	
	private String movieName;
	private String directorName;
	private String actorName;
	private String genreName;
	
	public MovieInfoVO(String movieName, String directorName, String actorName, String genreName) {
		this.movieName = movieName;
		this.directorName = directorName;
		this.actorName = actorName;
		this.genreName = genreName;
	}
	
	public String getMovieName() {
		return movieName;
	}
	
	public String getDirectorName() {
		return directorName;
	}
	
	public String getActorName() {
		return actorName;
	}
	
	public String getGenreName() {
		return genreName;
	}
}	
