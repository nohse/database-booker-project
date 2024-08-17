package view.panel.member;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class CustomImagePanel extends JPanel {
    private Image image;

    public CustomImagePanel(String imagePath) {
        File imgFile = new File(imagePath);
        if (imgFile.exists()) {
            ImageIcon icon = new ImageIcon(imagePath);
            this.image = icon.getImage();
            if (image == null) {
                System.out.println("ImageIcon failed to load image.");
            }
        } else {
            System.out.println("File does not exist: " + imagePath);
            this.image = null;
        }
        setPreferredSize(new Dimension(100, 150));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        } else {
            g.drawString("포스터 없음", getWidth() / 2 - 30, getHeight() / 2);
        }
    }

    // Forcing a repaint to see if it resolves any issues
    public void refresh() {
        revalidate();
        repaint();
    }
}
