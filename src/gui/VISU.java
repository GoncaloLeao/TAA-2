package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
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

public class VISU {

	private JFrame frameVISU;
	private String name;
	private BufferedImage img;
	private ImageIcon originalIcon;
	private String path;
	private double zoom;
	
	private JLabel label;
	private JScrollPane scroll;
	
	private int oldMouseX;
	private int oldMouseY;
	
	private boolean firstPos;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new VISU();					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public VISU() {
		this.name = null;
		this.zoom = 0.7;
		this.firstPos = true;

		initialize();

		this.frameVISU.setVisible(true);
	}
	
	public VISU(String name) {
		this.name = name;
		this.zoom = 1;
		this.firstPos = true;

		initialize();

		this.frameVISU.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 * @param label 
	 */
	private void initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		frameVISU = new JFrame();
		frameVISU.setBounds((int)screenSize.getWidth()/16, (int)screenSize.getHeight()/16, (int)screenSize.getWidth()*7/8, (int)screenSize.getHeight()*7/8);
		frameVISU.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frameVISU.getContentPane().setLayout(null);
		name = "A";
		if(name == null) {
			frameVISU.setTitle("Help");
			ImageIcon icon = new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif"));
			frameVISU.setIconImage(icon.getImage());
		}
		else {
			frameVISU.setTitle("Viewer - " + name);
			frameVISU.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icon.png"));
		}

		label = new JLabel();
		label.addMouseListener(new MouseAdapter() {
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
		label.addMouseMotionListener(new MouseMotionListener() {
			
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
		label.addMouseWheelListener(new MouseWheelListener() {


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
		/* INPUT */
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
			label.setIcon(icon);

		} catch (IOException e) {
			e.printStackTrace();
		}

		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);

		label.setBounds(0, 0, frameVISU.getWidth(), frameVISU.getHeight());
		scroll = new JScrollPane(label);

		frameVISU.setContentPane(scroll);

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
		label.setIcon(icon);
	}
}
