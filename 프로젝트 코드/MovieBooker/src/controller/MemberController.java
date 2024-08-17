package controller;

import listener.SearchButtonListener;
import model.MovieDAO;
import view.MainView;
import view.panel.member.MemberPanel;

public class MemberController {
	
	//============TODO 검색 성공했을 때 해당 검색에 대한 UI 띄우는 함수 생성===========//

	private MainView mainView;
	private MemberPanel memberPanel;
	private MovieDAO movieDAO;
	private SearchButtonListener searchButtonListener;
	
	public MemberController(MainView mainView) {
		
		  this.mainView = mainView; this.memberPanel =
		  mainView.getLoginPanel().getMemberPanel(); this.movieDAO = new MovieDAO();
		  this.searchButtonListener = new SearchButtonListener(this);
		  //this.memberPanel.getMovieSearchPanel().addSearchButtonListener(
		// searchButtonListener);
		 
	}
	
	public MovieDAO getMovieDAO() {
		return movieDAO;
	}
	
	public MainView getMainView() {
		return mainView;
	}
}
