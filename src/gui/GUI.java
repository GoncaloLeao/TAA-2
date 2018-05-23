package gui;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextPane;
import javax.swing.JToggleButton;
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
import java.util.ArrayList;
import java.util.Random;
import java.awt.Color;
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
	private static final String BASE_FILENAME = System.getProperty("user.dir") + System.getProperty("file.separator") + "temp" + System.getProperty("file.separator") + "set" + ".gif"; //path to the dot image of the set being displayed
	private static final int MAX_VALUE = 10000000; //max integer allowed on the sets
	
	//Help button
	private JButton btnHelp;
	
	//Buttons to switch DS
	private ArrayList<JToggleButton> btnsList;
	private JToggleButton btnSimpleBST;
	private JToggleButton btnAVLTree;
	private JToggleButton btnRedBlackTree;
	private JToggleButton btnSplayTree;
	private JToggleButton btnScapegoatTree;
	private JToggleButton btnTreap;
	private JToggleButton btnSkipList;
	
	private JLabel lblAlpha;
	private JSpinner spnAlpha;

	//Operation components
	private static final String GO_FILENAME = System.getProperty("user.dir") + System.getProperty("file.separator") + "resources" + System.getProperty("file.separator") + "go.png";
	
	private JLabel lblFind;
	private JSpinner spnFind;
	private JButton btnFind;

	private JLabel lblInsert;
	private JSpinner spnInsert;
	private JButton btnInsert;

	private JLabel lblRemove;
	private JSpinner spnRemove;
	private JButton btnRemove;
	
	private JLabel lblGetMin;
	private JButton btnGetMin;
	private JLabel lblGetMax;
	private JButton btnGetMax;
	
	private JLabel lblInsertRand;
	private JSpinner spnInsertRand;
	private JButton btnInsertRand;
	
	//Console
	JLabel lblConsole;
	JTextPane txtPaneConsole;
	JScrollPane pnlConsole;

	//For the pane with the data structure image
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
		
		frmGUI.setTitle("Dynamic Sets");
		frmGUI.setIconImage(Toolkit.getDefaultToolkit().getImage("resources/icon.png"));

		initPnlDS();

		//Initialize the buttons
		initBtns();

		//Initialize the components for the operations (find, insert ...)
		initOps();
		
		initConsole();
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
	
		scroll = new JScrollPane(label);
		scroll.setBounds((int)Math.round(frmGUI.getWidth()*1/32.0), (int)Math.round(frmGUI.getHeight()*3/32.0), (int)Math.round(frmGUI.getWidth()*23/32.0), (int)Math.round(frmGUI.getHeight()*27/32.0));
		scroll.setBackground(Color.WHITE);
		frmGUI.getContentPane().add(scroll);
	}

	void initBtns() {
		btnsList = new ArrayList<JToggleButton>();
		
		//Help button
		btnHelp = new JButton("Help");
		btnHelp.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				new HelpFrame();
			}
		});
		btnHelp.setIcon(new ImageIcon(GUI.class.getResource("/com/sun/java/swing/plaf/motif/icons/Inform.gif")));
		btnHelp.setBounds((int)Math.round(frmGUI.getWidth()*28/32.0), (int)Math.round(frmGUI.getHeight()*2.5/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(btnHelp);
		
		//Data structure buttons
		
		btnSimpleBST = new JToggleButton("Simple BST");
		btnSimpleBST.setSelected(true);
		btnSimpleBST.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new SimpleBST<Integer>();
				updateImage();
				
				txtPaneConsole.setText("Initialized a Simple BST.");
				
				final int INDEX = 0;
				for(int i = 0; i < btnsList.size(); i++) {
					if(i != INDEX) btnsList.get(i).setSelected(false);
				}
			}
		});
		btnSimpleBST.setBounds((int)Math.round(frmGUI.getWidth()*0.5/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnSimpleBST);
		btnsList.add(btnSimpleBST);

		btnAVLTree = new JToggleButton("AVL Tree");
		btnAVLTree.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new AVLTree<Integer>();
				updateImage();
				
				txtPaneConsole.setText("Initialized an AVL Tree.");
				
				final int INDEX = 1;
				for(int i = 0; i < btnsList.size(); i++) {
					if(i != INDEX) btnsList.get(i).setSelected(false);
				}
			}
		});
		btnAVLTree.setBounds((int)Math.round(frmGUI.getWidth()*4.5/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnAVLTree);
		btnsList.add(btnAVLTree);

		btnRedBlackTree = new JToggleButton("RB Tree");
		btnRedBlackTree.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new RedBlackTree<Integer>();
				updateImage();
				
				txtPaneConsole.setText("Initialized a Red-Black Tree.");
				
				final int INDEX = 2;
				for(int i = 0; i < btnsList.size(); i++) {
					if(i != INDEX) btnsList.get(i).setSelected(false);
				}
			}
		});
		btnRedBlackTree.setBounds((int)Math.round(frmGUI.getWidth()*8.5/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnRedBlackTree);
		btnsList.add(btnRedBlackTree);

		btnSplayTree = new JToggleButton("Splay Tree");
		btnSplayTree.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new SplayTree<Integer>();
				updateImage();
				
				txtPaneConsole.setText("Initialized a Splay Tree.");
				
				final int INDEX = 3;
				for(int i = 0; i < btnsList.size(); i++) {
					if(i != INDEX) btnsList.get(i).setSelected(false);
				}
			}
		});
		btnSplayTree.setBounds((int)Math.round(frmGUI.getWidth()*12.5/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnSplayTree);
		btnsList.add(btnSplayTree);

		btnScapegoatTree = new JToggleButton("Scapegoat Tree");
		btnScapegoatTree.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				double alpha = (double)spnAlpha.getValue();
				final double P10EN = 100000d; //used to determine the number of decimal places for the rounding
				alpha = (double)Math.round(alpha * P10EN) / P10EN;
				set = new ScapegoatTree<Integer>(alpha);
				updateImage();
				
				txtPaneConsole.setText("Initialized a Scapegoat Tree with alpha = " + alpha + ".");
				
				final int INDEX = 4;
				for(int i = 0; i < btnsList.size(); i++) {
					if(i != INDEX) btnsList.get(i).setSelected(false);
				}
			}
		});
		btnScapegoatTree.setBounds((int)Math.round(frmGUI.getWidth()*16.5/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*4/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnScapegoatTree);
		btnsList.add(btnScapegoatTree);
		
		lblAlpha = new JLabel("Alpha");
		lblAlpha.setBounds((int)Math.round(frmGUI.getWidth()*20.5/32.0), (int)Math.round(frmGUI.getHeight()*-0.5/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblAlpha);
		
		SpinnerModel modelAlpha = new SpinnerNumberModel(0.5, 0.5, 0.99, 0.05);
		spnAlpha = new JSpinner(modelAlpha);
		//Prevent invalid input
		JFormattedTextField txt = ((JSpinner.NumberEditor) spnAlpha.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

		spnAlpha.setBounds((int)Math.round(frmGUI.getWidth()*20.5/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(spnAlpha);

		btnTreap = new JToggleButton("Treap");
		btnTreap.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new Treap<Integer>();
				updateImage();
				
				txtPaneConsole.setText("Initialized a Treap.");
				
				final int INDEX = 5;
				for(int i = 0; i < btnsList.size(); i++) {
					if(i != INDEX) btnsList.get(i).setSelected(false);
				}
			}
		});
		btnTreap.setBounds((int)Math.round(frmGUI.getWidth()*23.5/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnTreap);
		btnsList.add(btnTreap);

		btnSkipList = new JToggleButton("Skip List");
		btnSkipList.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				set = new SkipList<Integer>();
				updateImage();
				
				txtPaneConsole.setText("Initialized a Skip List.");
				
				final int INDEX = 6;
				for(int i = 0; i < btnsList.size(); i++) {
					if(i != INDEX) btnsList.get(i).setSelected(false);
				}
			}
		});
		btnSkipList.setBounds((int)Math.round(frmGUI.getWidth()*27.5/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*1/32.0));
		frmGUI.getContentPane().add(btnSkipList);
		btnsList.add(btnSkipList);
	}

	void initOps() {

		//Find
		lblFind = new JLabel("Find");
		lblFind.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*3/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblFind);

		SpinnerModel modelFind = new SpinnerNumberModel(0, 0, MAX_VALUE, 1);
		spnFind = new JSpinner(modelFind);
		//Prevent invalid input
		JFormattedTextField txt = ((JSpinner.NumberEditor) spnFind.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

		spnFind.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*5/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(spnFind);

		btnFind = new JButton("");
		btnFind.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Integer value = (Integer)spnFind.getValue();
				long start = System.nanoTime();
				Integer result = set.find(value);
				long time = System.nanoTime() - start;
				updateImage();
				
				String timeStr = "Time elapsed: " + time + " ns.";
				if(result != null) txtPaneConsole.setText("Found element: " + result + ".\n" + timeStr);
				else txtPaneConsole.setText("Didn't find element: " + value + ".\n" + timeStr);
			}
		});
		try {
			img = ImageIO.read(new File(GO_FILENAME));
		} catch (IOException e) {
			e.printStackTrace();
		}
		btnFind.setBounds((int)Math.round(frmGUI.getWidth()*29/32.0), (int)Math.round(frmGUI.getHeight()*5/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		btnFind.setIcon(new ImageIcon(img.getScaledInstance(btnFind.getWidth()/2,btnFind.getHeight()/2, Image.SCALE_SMOOTH)));
		frmGUI.getContentPane().add(btnFind);

		//Insert

		lblInsert = new JLabel("Insert");
		lblInsert.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*7/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblInsert);

		SpinnerModel modelInsert = new SpinnerNumberModel(0, 0, MAX_VALUE, 1);
		spnInsert = new JSpinner(modelInsert);
		//Prevent invalid input
		txt = ((JSpinner.NumberEditor) spnInsert.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

		spnInsert.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*9/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(spnInsert);

		btnInsert = new JButton("");
		btnInsert.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Integer value = (Integer)spnInsert.getValue();
				long start = System.nanoTime();
				set.insert(value);
				long time = System.nanoTime() - start;
				updateImage();
				
				String timeStr = "Time elapsed: " + time + " ns.";
				txtPaneConsole.setText("Inserted: " + value + ".\n" + timeStr);
			}
		});
		btnInsert.setBounds((int)Math.round(frmGUI.getWidth()*29/32.0), (int)Math.round(frmGUI.getHeight()*9/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		btnInsert.setIcon(new ImageIcon(img.getScaledInstance(btnInsert.getWidth()/2,btnInsert.getHeight()/2, Image.SCALE_SMOOTH)));
		frmGUI.getContentPane().add(btnInsert);

		//Remove

		lblRemove = new JLabel("Remove");
		lblRemove.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*11/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblRemove);

		SpinnerModel modelRemove = new SpinnerNumberModel(0, 0, MAX_VALUE, 1);
		spnRemove = new JSpinner(modelRemove);
		//Prevent invalid input
		txt = ((JSpinner.NumberEditor) spnRemove.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

		spnRemove.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*13/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(spnRemove);

		btnRemove = new JButton("");
		btnRemove.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Integer value = (Integer)spnRemove.getValue();
				long start = System.nanoTime();
				set.remove(value);
				long time = System.nanoTime() - start;
				updateImage();
				
				String timeStr = "Time elapsed: " + time + " ns.";
				txtPaneConsole.setText("Removed: " + value + ".\n" + timeStr);
			}
		});
		btnRemove.setBounds((int)Math.round(frmGUI.getWidth()*29/32.0), (int)Math.round(frmGUI.getHeight()*13/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		btnRemove.setIcon(new ImageIcon(img.getScaledInstance(btnRemove.getWidth()/2,btnRemove.getHeight()/2, Image.SCALE_SMOOTH)));
		frmGUI.getContentPane().add(btnRemove);
		
		//Get min/max
		
		lblGetMin = new JLabel("Get min");
		lblGetMin.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*15/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblGetMin);
		
		btnGetMin = new JButton("");
		btnGetMin.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				long start = System.nanoTime();
				Integer result = set.getMin();
				long time = System.nanoTime() - start;
				updateImage();
				
				String timeStr = "Time elapsed: " + time + " ns.";
				if(result != null) txtPaneConsole.setText("Found min element: " + result + ".\n" + timeStr);
				else txtPaneConsole.setText("Set is empty.\n" + timeStr);
			}
		});
		btnGetMin.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*17/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		btnGetMin.setIcon(new ImageIcon(img.getScaledInstance(btnGetMin.getWidth()/2,btnGetMin.getHeight()/2, Image.SCALE_SMOOTH)));
		frmGUI.getContentPane().add(btnGetMin);
		
		lblGetMax = new JLabel("Get max");
		lblGetMax.setBounds((int)Math.round(frmGUI.getWidth()*28/32.0), (int)Math.round(frmGUI.getHeight()*15/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblGetMax);
		
		btnGetMax = new JButton("");
		btnGetMax.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				long start = System.nanoTime();
				Integer result = set.getMax();
				long time = System.nanoTime() - start;
				updateImage();
				
				String timeStr = "Time elapsed: " + time + " ns.";
				if(result != null) txtPaneConsole.setText("Found max element: " + result + ".\n" + timeStr);
				else txtPaneConsole.setText("Set is empty.\n" + timeStr);
			}
		});
		btnGetMax.setBounds((int)Math.round(frmGUI.getWidth()*28/32.0), (int)Math.round(frmGUI.getHeight()*17/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		btnGetMax.setIcon(new ImageIcon(img.getScaledInstance(btnGetMax.getWidth()/2,btnGetMax.getHeight()/2, Image.SCALE_SMOOTH)));
		frmGUI.getContentPane().add(btnGetMax);
		
		//Insert random elements
		
		lblInsertRand = new JLabel("Insert N random elements");
		lblInsertRand.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*19/32.0), (int)Math.round(frmGUI.getWidth()*5/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblInsertRand);

		SpinnerModel modelInsertRand = new SpinnerNumberModel(10, 0, 1000, 1);
		spnInsertRand = new JSpinner(modelInsertRand);
		//Prevent invalid input
		txt = ((JSpinner.NumberEditor) spnInsertRand.getEditor()).getTextField();
		((NumberFormatter) txt.getFormatter()).setAllowsInvalid(false);

		spnInsertRand.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*21/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(spnInsertRand);

		btnInsertRand = new JButton("");
		btnInsertRand.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				Integer numInserts = (Integer)spnInsertRand.getValue();
				Random rand = new Random(System.currentTimeMillis());
				long start = System.nanoTime();
				for(int i = 0; i < numInserts; i++) {
					set.insert(rand.nextInt(MAX_VALUE + 1));
				}
				long time = System.nanoTime() - start;
				updateImage();
				
				String timeStr = "Time elapsed: " + time + " ns.";
				txtPaneConsole.setText("Inserted " + numInserts + " random elements.\n" + timeStr);
			}
		});
		btnInsertRand.setBounds((int)Math.round(frmGUI.getWidth()*29/32.0), (int)Math.round(frmGUI.getHeight()*21/32.0), (int)Math.round(frmGUI.getWidth()*2/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		btnInsertRand.setIcon(new ImageIcon(img.getScaledInstance(btnInsertRand.getWidth()/2,btnInsertRand.getHeight()/2, Image.SCALE_SMOOTH)));
		frmGUI.getContentPane().add(btnInsertRand);
	}

	void initConsole() {
		lblConsole = new JLabel("Console");
		//lblConsole.setForeground(new Color(0, 0, 128));
		//lblConsole.setFont(new Font("Consolas", Font.PLAIN, 15));
		lblConsole.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*23/32.0), (int)Math.round(frmGUI.getWidth()*3/32.0), (int)Math.round(frmGUI.getHeight()*2/32.0));
		frmGUI.getContentPane().add(lblConsole);
		
		txtPaneConsole = new JTextPane();
		//txtPaneConsole.setFont(new Font("Consolas", Font.PLAIN, 12));
		//txtPaneConsole.setBackground(new Color(173,216,230));
		//DefaultCaret caret = (DefaultCaret) txtPaneConsole.getCaret();
		//caret.setUpdatePolicy(DefaultCaret.NEVER_UPDATE);
		txtPaneConsole.requestFocus();
		txtPaneConsole.setEditable(false);
		
		pnlConsole = new JScrollPane(txtPaneConsole);
		pnlConsole.setBounds((int)Math.round(frmGUI.getWidth()*25/32.0), (int)Math.round(frmGUI.getHeight()*25/32.0), (int)Math.round(frmGUI.getWidth()*6/32.0), (int)Math.round(frmGUI.getHeight()*5/32.0));
		frmGUI.getContentPane().add(pnlConsole);
		
		txtPaneConsole.setText("Initialized a Simple BST.");
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
		gv.add(set.toDotString());
		gv.increaseDpi();   // 106 dpi

		File out = new File(BASE_FILENAME);
		gv.writeGraphToFile( gv.getGraph(gv.getDotSource(), "gif", "dot"), out );

		this.zoom = 0.7;
		this.firstPos = true;

		try {
			img = ImageIO.read(new File(BASE_FILENAME));
			originalIcon = new ImageIcon(img);
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
