package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.text.NumberFormatter;

import graphviz.GraphViz;
import structures.*;

import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFormattedTextField;

import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class GUI {

	private JFrame frmGUI;

	private DynamicSet<Integer> set;
	private static final String BASE_FILENAME = System.getProperty("user.dir") + System.getProperty("file.separator") + "temp" + System.getProperty("file.separator") + "set" + ".gif";

	//Buttons to switch DS
	private JButton btnSimpleBST;
	private JButton btnAVLTree;
	private JButton btnRedBlackTree;
	private JButton btnSplayTree;
	private JButton btnScapegoatTree;
	private JButton btnTreap;
	private JButton btnSkipList;

	//Operation components
	private JLabel lblFind;
	private JSpinner spnFind;
	private JButton btnFind;

	private JLabel lblInsert;
	private JSpinner spnInsert;
	private JButton btnInsert;

	private JLabel lblRemove;
	private JSpinner spnRemove;
	private JButton btnRemove;

	//For the pane with the data structure image
	private JPanel pnlDS;

	private BufferedImage img;
	private ImageIcon originalIcon;
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
					new GUI();					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		this.zoom = 0.7;
		this.firstPos = true;

		this.set = new SimpleBST<Integer>();

		initialize();

		this.frmGUI.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 * @param label 
	 */
	private void initialize() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		frmGUI = new JFrame();
		frmGUI.setBounds((int)screenSize.getWidth()/16, (int)screenSize.getHeight()/16, (int)screenSize.getWidth()*7/8, (int)screenSize.getHeight()*7/8);
		frmGUI.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmGUI.getContentPane().setLayout(null);
		if(null == null) { //TODO
			frmGUI.setTitle("Help");
			ImageIcon icon = new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif"));
			frmGUI.setIconImage(icon.getImage());
		}
		else {
			frmGUI.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icon.png"));
		}

		initPnlDS();

		//Initialize the buttons
		initBtns();

		//Initialize the components for the operations (find, insert ...)
		initOps();
	}

	void initPnlDS() {
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

		updateImage();

		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setVerticalAlignment(SwingConstants.CENTER);
		label.setBounds((int)Math.round(frmGUI.getWidth()*1/32.0), (int)Math.round(frmGUI.getHeight()*3/32.0), (int)Math.round(frmGUI.getWidth()*23/32.0), (int)Math.round(frmGUI.getHeight()*25/32.0));
		/*
		//label.setBounds(0, 0, frmGUI.getWidth(), frmGUI.getHeight());
		scroll = new JScrollPane(label);
		//scroll.setBounds(1, 3, (int)Math.round(frmGUI.getWidth()*20/32.0), (int)Math.round(frmGUI.getHeight()*25/32.0));
		//scroll.setBounds((int)Math.round(frmGUI.getWidth()*1/32.0), (int)Math.round(frmGUI.getHeight()*3/32.0), (int)Math.round(frmGUI.getWidth()*20/32.0), (int)Math.round(frmGUI.getHeight()*25/32.0));

		pnlDS = new JPanel();
		//pnlDS.setLayout(null);
		pnlDS.add(scroll);
		pnlDS.setBounds((int)Math.round(frmGUI.getWidth()*1/32.0), (int)Math.round(frmGUI.getHeight()*3/32.0), (int)Math.round(frmGUI.getWidth()*23/32.0), (int)Math.round(frmGUI.getHeight()*25/32.0));
		 */
		//label.setBounds(0, 0, pnlDS.getWidth(), pnlDS.getHeight());


		scroll = new JScrollPane(label);
		scroll.setBounds((int)Math.round(frmGUI.getWidth()*1/32.0), (int)Math.round(frmGUI.getHeight()*3/32.0), (int)Math.round(frmGUI.getWidth()*23/32.0), (int)Math.round(frmGUI.getHeight()*25/32.0));
		frmGUI.getContentPane().add(scroll);
	}

	void initBtns() {
		btnSimpleBST = new JButton("Simple BST");
		btnSimpleBST.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new SimpleBST<Integer>();
				updateImage();
			}
		});
		//btnSimpleBST.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnSimpleBST.setBounds((int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnSimpleBST);

		btnAVLTree = new JButton("AVL Tree");
		btnAVLTree.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new AVLTree<Integer>();
				updateImage();
			}
		});
		//btnAVLTree.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnAVLTree.setBounds((int)Math.round(frmGUI.getWidth()*6/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnAVLTree);

		btnRedBlackTree = new JButton("RB Tree");
		btnRedBlackTree.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new RedBlackTree<Integer>();
				updateImage();
			}
		});
		//btnRedBlackTree.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnRedBlackTree.setBounds((int)Math.round(frmGUI.getWidth()*10/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnRedBlackTree);

		btnSplayTree = new JButton("Splay Tree");
		btnSplayTree.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new SplayTree<Integer>();
				updateImage();
			}
		});
		//btnSplayTree.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnSplayTree.setBounds((int)Math.round(frmGUI.getWidth()*14/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnSplayTree);

		btnScapegoatTree = new JButton("Scapegoat Tree");
		btnScapegoatTree.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new ScapegoatTree<Integer>(0.5); //TODO: GUI to choose param
				updateImage();
			}
		});
		//btnScapegoatTree.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnScapegoatTree.setBounds((int)Math.round(frmGUI.getWidth()*18/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnScapegoatTree);

		btnTreap = new JButton("Treap");
		btnTreap.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

			}
		});
		//btnTreap.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnTreap.setBounds((int)Math.round(frmGUI.getWidth()*22/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnTreap);

		btnSkipList = new JButton("Skip List");
		btnSkipList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {

			}
		});
		//btnSkipList.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnSkipList.setBounds((int)Math.round(frmGUI.getWidth()*26/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnSkipList);
	}

	void initOps() {
		SpinnerModel elemModel = new SpinnerNumberModel(0, 0, 10000000, 1);

		//Find

		lblFind = new JLabel("Find");
		lblFind.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*3/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblFind);

		spnFind = new JSpinner(elemModel);
		//Prevent invalid input
		JFormattedTextField txt = ((JSpinner.NumberEditor) spnFind.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

		spnFind.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*5/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(spnFind);

		btnFind = new JButton("");
		btnFind.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Integer value = (Integer)spnFind.getValue();
				set.find(value);
				updateImage();
			}
		});
		btnFind.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnFind.setBounds((int)Math.round(frmGUI.getWidth()*29/32.0), (int)Math.round(frmGUI.getHeight()*5/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(btnFind);

		lblFind = new JLabel("Find");
		lblFind.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*3/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblFind);

		//Insert

		lblInsert = new JLabel("Insert");
		lblInsert.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*7/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblInsert);

		spnInsert = new JSpinner(elemModel);
		//Prevent invalid input
		txt = ((JSpinner.NumberEditor) spnInsert.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

		spnInsert.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*9/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(spnInsert);

		btnInsert = new JButton("");
		btnInsert.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Integer value = (Integer)spnInsert.getValue();
				set.insert(value);
				updateImage();
			}
		});
		btnInsert.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnInsert.setBounds((int)Math.round(frmGUI.getWidth()*29/32.0), (int)Math.round(frmGUI.getHeight()*9/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(btnInsert);

		//Remove

		lblRemove = new JLabel("Remove");
		lblRemove.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*11/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblRemove);

		spnRemove = new JSpinner(elemModel);
		//Prevent invalid input
		txt = ((JSpinner.NumberEditor) spnRemove.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

		spnRemove.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*13/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(spnRemove);

		btnRemove = new JButton("");
		btnRemove.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Integer value = (Integer)spnRemove.getValue();
				set.remove(value);
				updateImage();
			}
		});
		btnRemove.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnRemove.setBounds((int)Math.round(frmGUI.getWidth()*29/32.0), (int)Math.round(frmGUI.getHeight()*13/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(btnRemove);
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
		updateSetLabel();
	}

	//Updates the graphviz image used by the GUI
	private void updateImage() {
		GraphViz gv = new GraphViz();
		System.out.println(set.toDotString());
		gv.add(set.toDotString());
		gv.increaseDpi();   // 106 dpi

		File out = new File(BASE_FILENAME);
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), "gif", "dot"), out );

		this.zoom = 0.7;
		this.firstPos = true;

		try {
			img = ImageIO.read(new File(BASE_FILENAME));
			originalIcon = new ImageIcon(img);
			System.out.println("called here");
			updateSetLabel();

		} catch (IOException e) {
			e.printStackTrace();
		}

		frmGUI.invalidate();
		frmGUI.repaint();
	}

	private void updateSetLabel() {
		ImageIcon icon = new ImageIcon(originalIcon.getImage().getScaledInstance((int)(originalIcon.getIconWidth() * zoom),
				(int)(originalIcon.getIconHeight() * zoom), Image.SCALE_SMOOTH));
		label.setIcon(icon);
	}
}
