import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class MainWindow extends JPanel{
	private JFrame mainFrame;
	private JPanel chooseFilePanel, showColorPanel;
	private JFileChooser fc;
	private static String imageFilePath;

	public void startSteps() {
		mainFrame = new JFrame("CMPT365 Image Editing");
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setSize(600, 600);
		mainFrame.setLocationRelativeTo(null);
	    mainFrame.setLayout(new FlowLayout());
		chooseFilePanel = new JPanel(new FlowLayout());
		showColorPanel = new JPanel(new FlowLayout());
		mainFrame.add(chooseFilePanel);	
		mainFrame.add(showColorPanel);
		
		showSelectFileWindow();
		
		chooseFilePanel.setVisible(true);
		mainFrame.setVisible(true);
	}
	
	private void showSelectFileWindow(){
		fc = new JFileChooser();
		fc.setSize(100, 100);
		fc.setAcceptAllFileFilterUsed(false);
		fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
//		fc.addChoosableFileFilter(new FileFilter() {
//			public String getDescription() {
//		        return "IMAGE (*.JPEG)";
//		    } 
//		    public boolean accept(File f) {
//		        if (f.isDirectory()) {
//		            return true;
//		        } else {
//		            return f.getName().toLowerCase().endsWith(".JPEG");
//		        }
//		    }
//		});
		
		fc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent actionEvent) {
				JFileChooser theFileChooser = (JFileChooser) actionEvent.getSource();
		        String command = actionEvent.getActionCommand();
		        //write changes before opening file to prevent file mismatch
		        if (command.equals(JFileChooser.APPROVE_SELECTION)) { 
		        	File file = theFileChooser.getSelectedFile();
		        	imageFilePath = file.getAbsolutePath();
		        	showColorImage();
		        	
		        }
			}
		});
		chooseFilePanel.add(fc);
		fc.setVisible(true);
	}
	
	private void showColorImage() {
		chooseFilePanel.setVisible(false);
		try {
			// STEP 1
	         BufferedImage originalImg = ImageIO.read(new File(imageFilePath));
	         viewImage(originalImg, "Original Image");
	         
	         // STEP 2
	         BufferedImage grayImg = ImageUtility.makeGrayScale(originalImg);
	         viewImage(grayImg, "Grayscale Image");
	         
	         // STEP 3
	         BufferedImage histogramImage = ImageUtility.makeGrayscaleHistogram(grayImg);
	         viewImage(histogramImage, "Grayscale Histogram");
	         
	         // STEP 4
	         BufferedImage gammaCorrected = ImageUtility.applyGammaCorrection(grayImg);
	         BufferedImage gammaHistogramImage = ImageUtility.makeGrayscaleHistogram(gammaCorrected);
	         viewImage(gammaHistogramImage, "Gamma Corrected Histogram");
	         
	         // STEP 5
	         BufferedImage invertImg = ImageUtility.invertImage(grayImg);
	         viewImage(invertImg, "Inverted Image");
	         
	         BufferedImage invertHistogramImage = ImageUtility.makeGrayscaleHistogram(invertImg);
	         viewImage(invertHistogramImage, "Inverted Histogram");
	         
	         // STEP 6
	         BufferedImage ditheredImg = ImageUtility.grayscalOrderedDither(grayImg);
	         viewImage(ditheredImg, "Ordered-Dithered Image");	         
	      } catch (IOException e) {
	         e.printStackTrace();
	    }
	}
	
	private void viewImage(BufferedImage image, String header) {
		ImageIcon icon = new ImageIcon(image);
		JLabel label = new JLabel(icon);
        label.setAutoscrolls(true);
        JOptionPane.showMessageDialog(null, label,header, JOptionPane.PLAIN_MESSAGE);
	}
}
