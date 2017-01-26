import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


/**
 * @author Kayhan Dehghani Mohammadi
 * Spring 2017
 * CMPT 365 Assignment 1
 * Image Editor
 */
public class UI extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	//UI Variables
	private JFrame mainFrame;
	private JPanel editButtonsPanel, txtPanel;
	private JFileChooser fc;
	private static String imageFilePath;
	private JButton _color, _gray, _grayHist, _gammaHist, _inv, _invHist, _dith;
	JMenuBar menuBar;
    JMenu menu, fileMenu;
	JMenuItem selectItem, exitItem;
	JLabel label;
	
	//Image Variables
	private BufferedImage originalImg, grayImg, invertImg;
	public static void main(String[] args) {
		new UI();
	}
	
	public UI() {
		//Frame
		mainFrame = new JFrame("CMPT365 Image Editing");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setResizable(false);
		mainFrame.setSize(600, 400);
		mainFrame.setLocationRelativeTo(null);
		
		//Panels
		editButtonsPanel = new JPanel(new GridLayout(4,3)); 
		txtPanel = new JPanel(new GridLayout(4,3)); 
		mainFrame.add(editButtonsPanel,BorderLayout.SOUTH);
		mainFrame.add(txtPanel,BorderLayout.NORTH);
		
		//Menu
		menuBar = new JMenuBar();
        menu = new JMenu("Window");
        fileMenu = new JMenu("File");
        menuBar.add(fileMenu);
        menuBar.add(menu);
        selectItem = new JMenuItem("Open File...");
        selectItem.addActionListener(this);
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(this);
        fileMenu.add(selectItem);
        menu.add(exitItem);
        mainFrame.setJMenuBar(menuBar);
		
		//Buttons
		_color = new JButton("Color");
		_color.addActionListener(this);
		_gray = new JButton("Grayscale");
		_gray.addActionListener(this);
		_grayHist = new JButton("Grayscale Histogram");
		_grayHist.addActionListener(this);
		_gammaHist = new JButton("Gamma-corrected Histogram");
		_gammaHist.addActionListener(this);
		_inv = new JButton("Inverse");
		_inv.addActionListener(this);
		_invHist = new JButton("Inverse Histogram");
		_invHist.addActionListener(this);
		_dith = new JButton("Ordered-dithering");
		_dith.addActionListener(this);
		
		editButtonsPanel.add(_color);
		editButtonsPanel.add(_gray);
		editButtonsPanel.add(_grayHist);
		editButtonsPanel.add(_gammaHist);
		editButtonsPanel.add(_inv);
		editButtonsPanel.add(_invHist);
		editButtonsPanel.add(_dith);
		
		//TextArea
		label = new JLabel("<html><br>Welcome!</html>");
		txtPanel.add(label);

		

		editButtonsPanel.setVisible(true);
		txtPanel.setVisible(true);
		mainFrame.setVisible(true);
	}
	
	private void selectFile() {
		JDialog dialog = new JDialog(mainFrame, "Select Image", ModalityType.APPLICATION_MODAL);
		fc = new JFileChooser();
		fc.setCurrentDirectory(new File(System.getProperty("user.home")));
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
		fc.setVisible(true);
		int result = fc.showOpenDialog(dialog);
		
		if (result == JFileChooser.APPROVE_OPTION) { 
        	File file = fc.getSelectedFile();
        	imageFilePath = file.getAbsolutePath();
        	//TODO: complete logic
        	
        	//Load images
        	try {
    			originalImg = ImageIO.read(new File(imageFilePath));
    			grayImg = ImageUtility.makeGrayScale(originalImg);
    			invertImg = ImageUtility.invertImage(grayImg);
    			
    			//show image attributes
    			String filename = Paths.get(imageFilePath).getFileName().toString();
    			String dimensions = String.valueOf(originalImg.getWidth()) + " x " + String.valueOf(originalImg.getHeight());
    			label.setText("<html><br>File Name: " + filename + "<br>" + "Dimensions (Width x Height): " + dimensions + "</html>");
    			
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
        }
		
	}
	
	private void viewImage(BufferedImage image, String header) {
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);
        label.setAutoscrolls(true);
        JOptionPane.showMessageDialog(null, label,header, JOptionPane.PLAIN_MESSAGE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==exitItem) {
			System.exit(0);
		}
		else if (e.getSource()==selectItem) {
			selectFile();
		}
		else if (e.getSource()==_color) {
			viewImage(originalImg, "Original Image");
		}
		else if (e.getSource()==_gray) {
	        viewImage(grayImg, "Grayscale Image");
		}
		else if (e.getSource()==_grayHist) {
			BufferedImage histogramImage = ImageUtility.makeGrayscaleHistogram(grayImg);
	        viewImage(histogramImage, "Grayscale Histogram");
		}
		else if (e.getSource()==_gammaHist) {
			BufferedImage gammaCorrected = ImageUtility.applyGammaCorrection(grayImg);
	        BufferedImage gammaHistogramImage = ImageUtility.makeGrayscaleHistogram(gammaCorrected);
	        viewImage(gammaHistogramImage, "Gamma Corrected Histogram");
		}
		else if (e.getSource()==_inv) {
	        viewImage(invertImg, "Inverted Image");
		}
		else if (e.getSource()==_invHist) {
			BufferedImage invertHistogramImage = ImageUtility.makeGrayscaleHistogram(invertImg);
	        viewImage(invertHistogramImage, "Inverted Histogram");
		}
		else if (e.getSource()==_dith) {
			BufferedImage ditheredImg = ImageUtility.grayscalOrderedDither(grayImg);
	        viewImage(ditheredImg, "Ordered-Dithered Image");
		}
	}
}
