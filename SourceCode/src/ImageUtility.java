import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

/**
 * @author Kayhan Dehghani Mohammadi
 * Spring 2017
 * CMPT 365 Assignment 1
 * Image Editor
 */

public class ImageUtility {
	
	//Returns the grayscale of the original image
	public static BufferedImage makeGrayScale(BufferedImage image) {
		int width = image.getWidth();
        	int height = image.getHeight();
        	BufferedImage resultImage = new BufferedImage(width, height, image.getType());
        
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
			      Color c = new Color(image.getRGB(x, y));
			      int red = (int)(c.getRed() * 0.299);
			      int green = (int)(c.getGreen() * 0.587);
			      int blue = (int)(c.getBlue() *0.114);
			      Color newColor = new Color(red+green+blue,
			      red+green+blue,red+green+blue);
			      resultImage.setRGB(x,y,newColor.getRGB());
			}
		}
		return resultImage;
	}
	

	// makes a histogram of the grayscale of the image
	// Returns an image of the histogram
	// Warning: for color images the histogram will only show the blue
	public static BufferedImage makeGrayscaleHistogram(BufferedImage image) {
		int width = image.getWidth();
        	int height = image.getHeight();
        
        	ArrayList<Integer> histFreq = new ArrayList<>();
        	double[] histFreqdouble = new double[width+height];
        		
        	Color c;
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
				c = new Color(image.getRGB(x, y));
				histFreq.add(c.getBlue());
			}
		} 
        
		for(int i=0; i<width+height; i++) {
			histFreqdouble[i] = (double) histFreq.get(i);
		}

		HistogramDataset histogramdataset = new HistogramDataset();
		histogramdataset.addSeries("Grayscale Histogram", histFreqdouble, 256);
		JFreeChart histogram = ChartFactory.createHistogram("Histogram", "gray value", "frequency [# of pixels]", histogramdataset, PlotOrientation.VERTICAL, true, true, false);
		return histogram.createBufferedImage(700,400);
	}
	
	// Returns the gamma corrected version of the image
	public static BufferedImage applyGammaCorrection(BufferedImage image)  {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage resultImage = new BufferedImage(width, height, image.getType());
		double gamma_inversed = 1/2.2;
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
			      Color c = new Color(image.getRGB(x, y));
			      int red = (int) (255 * (Math.pow((double) c.getRed() / (double) 255, gamma_inversed)));
			      int green = (int) (255 * (Math.pow((double) c.getGreen() / (double) 255, gamma_inversed)));
			      int blue = (int) (255 * (Math.pow((double) c.getBlue() / (double) 255, gamma_inversed)));

			      Color newColor = new Color(red,green,blue);
			      resultImage.setRGB(x,y,newColor.getRGB());
			}
		}
		return resultImage;
	}
	
	// invert the image
	public static BufferedImage invertImage(BufferedImage image) {
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage resultImage = new BufferedImage(width, height, image.getType());
		for(int y=0; y<height; y++){
			for(int x=0; x<width; x++){
			      Color c = new Color(image.getRGB(x, y));
			      int red = (int) (255-c.getRed());
			      int green = (int) (255-c.getGreen());
			      int blue = (int) (255-c.getBlue());

			      Color newColor = new Color(red,green,blue);
			      resultImage.setRGB(x,y,newColor.getRGB());
		   	}
		}
		return resultImage;
	}
	
	// apply ordered dithering to the image
	// Warning: input should be grayscale otherwise only the blue will be affected
	public static BufferedImage grayscalOrderedDither(BufferedImage image) {
		Random random = new Random();
		int n = 5;

		int[][] ditherMatrix = new int[n][n];
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				ditherMatrix[i][j] = random.nextInt(255);
			}
		}
		int width = image.getWidth();
		int height = image.getHeight();
		BufferedImage resultImage = new BufferedImage(width, height, image.getType());
        
		int i,j,grayValue;
		Color c;
		for(int x=0; x<width; x++){
			for(int y=0; y<height; y++){
				i = x % ditherMatrix.length;
				j = y % ditherMatrix.length;
				c = new Color(image.getRGB(x, y));
				grayValue = (int) (c.getBlue());

				if(grayValue > ditherMatrix[i][j]) {
					resultImage.setRGB(x,y,Color.WHITE.getRGB());
				}
				else{
					resultImage.setRGB(x,y,Color.BLACK.getRGB());
				}
		   }
		}
		return resultImage;
	}
}
