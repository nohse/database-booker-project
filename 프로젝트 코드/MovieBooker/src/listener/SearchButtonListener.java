package listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import controller.MemberController;
import model.MovieDAO;
import model.MovieInfoVO;
import view.panel.member.MemberPanel;
/*import view.panel.member.MovieResultPanel;
import view.panel.member.MovieSearchPanel;*/

public class SearchButtonListener implements ActionListener {

	// =========TODO MemberController에서 List띄우는 함수 받아와서 실행=============//

	/*
	 * private MemberController memberController; private MovieSearchPanel
	 * movieSearchPanel; private MovieResultPanel movieResultPanel; private
	 * MemberPanel memberPanel; private MovieDAO movieDAO;
	 */

	public SearchButtonListener(MemberController memberController) {
		/*
		 * this.memberController = memberController; this.movieDAO =
		 * memberController.getMovieDAO(); this.memberPanel =
		 * memberController.getMainView().getLoginPanel().getMemberPanel();
		 * this.movieSearchPanel = memberPanel.getMovieSearchPanel();
		 * this.movieResultPanel = memberPanel.getMovieResultsPanel();
		 */
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		/*
		 * String movieName = movieSearchPanel.getMovieName(); String directorName =
		 * movieSearchPanel.getDirectorName(); String actorName =
		 * movieSearchPanel.getActorName(); String genreName =
		 * movieSearchPanel.getGenreNameField();
		 * 
		 * List<MovieInfoVO> movieInfos = movieDAO.searchMovies(movieName, directorName,
		 * actorName, genreName);
		 * 
		 * if (!movieInfos.isEmpty()) {
		 * 
		 * movieResultPanel.setMovieResults(movieInfos);
		 * memberPanel.getCardLayout().show(memberPanel, "Movie Result");
		 * 
		 * } else { System.out.println("검색 결과가 없어용"); }
		 */
	}
}
