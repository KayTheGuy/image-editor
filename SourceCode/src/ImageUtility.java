import java.awt.Color;
import java.awt.image.BufferedImage;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class ImageUtility {
	
	//Returns the grayscale of the original image
	public static BufferedImage makeGrayScale(BufferedImage image) {
		int width = image.getWidth();
        int height = image.getHeight();
        
        for(int y=0; y<height; y++){
           for(int x=0; x<width; x++){
              Color c = new Color(image.getRGB(x, y));
              int red = (int)(c.getRed() * 0.299);
              int green = (int)(c.getGreen() * 0.587);
              int blue = (int)(c.getBlue() *0.114);
              Color newColor = new Color(red+green+blue,
              red+green+blue,red+green+blue);
              image.setRGB(x,y,newColor.getRGB());
           }
        }
        return image;
	}
	

	// makes a histogram of the grayscale of the image
	// Returns an image of the histogram
	// Warning: for color images the histogram will only show the blue
	public static BufferedImage makeGrayscaleHistogram(BufferedImage image) {
		XYSeries xySeries = new XYSeries("Grayscale Histogram");
		
		int[] frequencies = new int[256];
		for(int i=0; i<256; i++){
			frequencies[i] = 0;
		}
		int width = image.getWidth();
        int height = image.getHeight();
        Color c;
        for(int y=0; y<height; y++){
        	for(int x=0; x<width; x++){
        		c = new Color(image.getRGB(x, y));
        		frequencies[c.getBlue()]++;
        	}
        } 
        for(int i=0; i<256; i++){
			xySeries.add(i, frequencies[i]);
		}
		XYSeriesCollection data = new XYSeriesCollection();
		data.addSeries(xySeries);	
		JFreeChart histogram = ChartFactory.createXYLineChart("Grayscale Histogram", "Gray Value", "Frequency [# of pixels]", data);

		return histogram.createBufferedImage(700,400);
	}
	
	// Returns the gamma corrected version of the image
	public static BufferedImage applyGammaCorrection(BufferedImage image)  {
		int width = image.getWidth();
        int height = image.getHeight();
        double gamme_inversed = 1/2.0;
        for(int y=0; y<height; y++){
           for(int x=0; x<width; x++){
              Color c = new Color(image.getRGB(x, y));
              int red = (int) (255 * (Math.pow((double) c.getRed() / (double) 255, gamme_inversed)));
              int green = (int) (255 * (Math.pow((double) c.getGreen() / (double) 255, gamme_inversed)));
              int blue = (int) (255 * (Math.pow((double) c.getBlue() / (double) 255, gamme_inversed)));
              
              Color newColor = new Color(red,green,blue);
              image.setRGB(x,y,newColor.getRGB());
           }
        }
		return image;
	}
}
