import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.HistogramDataset;

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
		JFreeChart histogram = ChartFactory.createHistogram("Histograms", "Gray Value", "Frequency [# of pixels]", histogramdataset, PlotOrientation.VERTICAL, true, true, false);
		return histogram.createBufferedImage(700,400);
//        XYPlot xyplot = (XYPlot) jfreechart.getPlot();
//        xyplot.setForegroundAlpha(0.85F);
//        XYBarRenderer xybarrenderer = (XYBarRenderer) xyplot.getRenderer();
//        xybarrenderer.setDrawBarOutline(false);
//        JPanel jpanel = new ChartPanel(jfreechart);
//        jpanel.setPreferredSize(new Dimension(1000, 600));
//        setContentPane(jpanel);
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
}
