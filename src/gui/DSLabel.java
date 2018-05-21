package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

public class DSLabel extends JLabel {

	private JScrollPane scroll; 
	
	private String name;
	private BufferedImage img;
	private ImageIcon originalIcon;
	private String path;
	private double zoom;
	
	private int oldMouseX;
	private int oldMouseY;
	
	private boolean firstPos;
	
	public DSLabel(JScrollPane scroll, String name) {
		super();
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
		    	if (e.getClickCount() == 2) {
		    		if(SwingUtilities.isLeftMouseButton(e)) {
		    			zoom(2);
		    		}
		    		else {
		    			zoom(0.5);
		    		}
		    	}
			}
		});
		
		addMouseMotionListener(new MouseMotionListener() {
			
			@Override
			public void mouseDragged(MouseEvent arg0) {
				if(firstPos) {
					firstPos = false;
				}
				else {
					int deltaX = arg0.getX() - oldMouseX;
					int deltaY = arg0.getY() - oldMouseY;
					
					double dx = -deltaX/2.0;
					double dy = -deltaY/2.0;
								
					move(dx, dy);
				}
				
				oldMouseX = arg0.getX();
				oldMouseY = arg0.getY();
			}

			@Override
			public void mouseMoved(MouseEvent arg0) {
				if(firstPos) {
					firstPos = false;
				}
				
				oldMouseX = arg0.getX();
				oldMouseY = arg0.getY();
			}
		});
		
		addMouseWheelListener(new MouseWheelListener() {


			public void mouseWheelMoved(MouseWheelEvent arg0) {
				double rotations = arg0.getPreciseWheelRotation();

				if(rotations != 0){
					if(rotations < 0){
						zoom(-1.2*rotations);
					}
					else if(rotations > 0){
						zoom(rotations/1.2);
					}
				}
			}
		});
		
		if(name == null) {
			path = System.getProperty("user.dir") + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "tutorial.png";
		}
		else {
			path = System.getProperty("user.dir") + System.getProperty("file.separator") + "temp" + System.getProperty("file.separator") + name + ".gif";
		}

		try {
			img = ImageIO.read(new File(path));
			originalIcon = new ImageIcon(img);
			ImageIcon icon = new ImageIcon(originalIcon.getImage().getScaledInstance((int)(originalIcon.getIconWidth() * zoom),
					(int)(originalIcon.getIconHeight() * zoom), Image.SCALE_SMOOTH));
			setIcon(icon);

		} catch (IOException e) {
			e.printStackTrace();
		}

		/*label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);

		label.setBounds(0, 0, this.getWidth(), this.getHeight());
		*/
	}
	
	private void move(double deltaX, double deltaY) {
		int scrollX = scroll.getHorizontalScrollBar().getValue();
		int scrollY = scroll.getVerticalScrollBar().getValue();
		
		scrollX = Math.max(Math.min((int)Math.round(scrollX + deltaX), scroll.getHorizontalScrollBar().getMaximum()), 
				scroll.getHorizontalScrollBar().getMinimum());
		scrollY = Math.max(Math.min((int)Math.round(scrollY + deltaY), scroll.getVerticalScrollBar().getMaximum()),
				scroll.getVerticalScrollBar().getMinimum());

		scroll.getHorizontalScrollBar().setValue(scrollX);
        scroll.getVerticalScrollBar().setValue(scrollY);
	}
	
	private void zoom(double factor) {
		zoom = Math.max(Math.min(4.0, zoom*factor),0.25);
		ImageIcon icon = new ImageIcon(originalIcon.getImage().getScaledInstance((int)(originalIcon.getIconWidth() * zoom),
				(int)(originalIcon.getIconHeight() * zoom), Image.SCALE_SMOOTH));
		setIcon(icon);
	}
}
