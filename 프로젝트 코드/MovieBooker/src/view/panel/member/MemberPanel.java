package view.panel.member;

import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import view.MainView;

public class MemberPanel extends JPanel {

    private CardLayout cardLayout;
    private MovieSearchPanel movieSearchPanel;
    private TicketingCheckPanel ticketingCheckPanel;
    private MainView mainView;

    public MemberPanel(MainView mainView) {
        this.mainView = mainView;
        initializePanel();
    }

    private void initializePanel() {
        cardLayout = new CardLayout();
        ticketingCheckPanel = new TicketingCheckPanel(mainView, this);
        movieSearchPanel = new MovieSearchPanel(mainView, this);

        setLayout(cardLayout);

        addPanels();
        addButtonEventListener();
    }

    private void addPanels() {
        add(createMemberMenu(), "Member Menu");
        add(movieSearchPanel, "Movie Search");
        add(ticketingCheckPanel, "Ticketing Check");
    }

    private void addButtonEventListener() {
        ticketingCheckPanel.addBackButtonListener(e -> cardLayout.show(this, "Member Menu"));
    }

    private JPanel createMemberMenu() {
        JPanel panel = new JPanel();

        JButton movieSearchButton = new JButton("영화 검색");
        JButton ticketingCheckButton = new JButton("예매 확인");

        movieSearchButton.addActionListener(e -> {
            mainView.setSize(800, 400);
            cardLayout.show(this, "Movie Search");
        });
        ticketingCheckButton.addActionListener(e -> {
        	mainView.setSize(800, 400);
        	cardLayout.show(this, "Ticketing Check");});

        panel.add(movieSearchButton);
        panel.add(ticketingCheckButton);

        return panel;
    }

    public TicketingCheckPanel getTicketingCheckPanel() {
        return ticketingCheckPanel;
    }

    public CardLayout getCardLayout() {
        return cardLayout;
    }

    public void showMemberMenu() {
        cardLayout.show(this, "Member Menu");
    }
}
