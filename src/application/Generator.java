package application;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Generator {


  String photo;
  String Bar;
  String QR;
  String folder,directoryName;
  ArrayList<String> text;   
  public ImageView cardView;
  String borderPath;
  public Generator(String photo,String Bar,String QR,ArrayList<String> text,String folder,String directoryName,ImageView cardView)
  {
	  this.photo=photo;
	  this.Bar=Bar;
	  this.QR=QR;
	  this.text=text;
	  this.folder=folder;
	  this.directoryName=directoryName;
	  this.cardView=cardView;
	  
  }
  
  
	public  boolean generateCard() 
    {
		
		ImageInputStream fg 	= null;		
		ImageInputStream bar 	= null;
		ImageInputStream qr		= null;
		
		
		// User Photo
		try {
			byte[] rawData2 = getRawBytesFromFile(directoryName + photo); // some code to read raw bytes from image file		
			fg = ImageIO.createImageInputStream(new ByteArrayInputStream(rawData2));
			
		} catch (FileNotFoundException e1) {
			
			System.out.println("Photo Not Found : " + directoryName + photo);
			//e1.printStackTrace();
			return false;
		} catch (IOException e1) {
		 
			//e1.printStackTrace();
			System.out.println("Photo Not Found : " + directoryName + photo);
			return false;
		}
  
		// Barcode
		try {
			
			byte[] rawData3 = getRawBytesFromFile(directoryName + Bar); // some code to read raw bytes from image file
			bar = ImageIO.createImageInputStream(new ByteArrayInputStream(rawData3));
		} 
		catch (FileNotFoundException e1) {				
			
			System.out.println("Bar Not FOund: " + directoryName + Bar);
			return false;
		} catch (IOException e1) {			 
			
			System.out.println("Bar Not FOund: " + directoryName + Bar);
			return false;
		}
	
		// QR Code
		try {
			
			byte[] rawData4 = getRawBytesFromFile(directoryName + QR); // some code to read raw bytes from image file
			qr = ImageIO.createImageInputStream(new ByteArrayInputStream(rawData4));
			
		} catch (FileNotFoundException e1) {
			 
			System.out.println("QR Not FOund: " + directoryName + QR);
			return false;
			
		} catch (IOException e1) {
			 
			System.out.println("QR Not FOund: " + directoryName + QR);
			return false;
			
		}
  
		ImageInputStream border = null;
		
		try {
			borderPath = getClass().getResource("/card_templates/border.png").toString().replace("file:/","");
			

			byte[] rawData5 = getRawBytesFromFile(borderPath); // some code to read raw bytes from image file
			border = ImageIO.createImageInputStream(new ByteArrayInputStream(rawData5));
			
		} catch (FileNotFoundException e1) {
			 
			System.out.println("Border Not FOund: " + borderPath);
			return false;
		} catch (IOException e1) {			
			e1.printStackTrace();
			return false;
		}
  
		
        try {
        	
        	 String cardBackgroundPath = getClass().getResource("/card_templates/card2.jpg").toString().replace("file:/","");
			 byte[] rawData = getRawBytesFromFile(cardBackgroundPath); 
			 ImageInputStream bg 	 = ImageIO.createImageInputStream(new ByteArrayInputStream(rawData));  		
			 BufferedImage imgBG 	 = ImageIO.read(bg);    // Image Background			 
			 BufferedImage imgFG 	 = ImageIO.read(fg);    // Image Foreground
			 
			 BufferedImage barCode   = ImageIO.read(bar);   // Image Bar Code
			 BufferedImage qrCode 	 = ImageIO.read(qr);    // Image QR Code
      
			 BufferedImage borderImg = ImageIO.read(border); // Image Border Image
			 			 
			 imgFG					 = scale(imgFG,218,258);
		     
			 // imgFG =crop(imgFG, 200, 235);
		     // qrCode=scale(qrCode,120,120);
		         
		     BufferedImage cropped 	 = new BufferedImage(218, 258, BufferedImage.TYPE_INT_ARGB);
		     
		     cropped.getGraphics().drawImage(imgFG, 
		                 0, 0, 225, 275, //draw onto the entire 225X275 destination image
		                 30, 5, 210, 235, //draw the section of the image between (30, 5) and (210, 235)
		                 null);
		     
		     imgFG = cropped;
			
			// qrCode=scale(qrCode,120,120);		 			 
			// barCode=scale(barCode,290,80);
			// For simplicity we will presume the images are of identical size
      
			
			 BufferedImage combinedImage = new BufferedImage( 
			        imgBG.getWidth(), 
			        imgBG.getHeight(), 
			        BufferedImage.TYPE_INT_ARGB );
			
			Graphics2D g = combinedImage.createGraphics();
      
			g.drawImage(imgBG,0,0,null);			
			g.drawImage(borderImg,28,160,null);   
			g.drawImage(imgFG,32,164,null);   
			g.drawImage(qrCode,668,293,null);       
			g.drawImage(barCode,300,385,null);						
			g.setColor(Color.BLACK);
   		
			String text1 =  text.get(0);			
			String text2 = 	text.get(1) ;
			String text3 = 	text.get(2); 
			String text4 = 	text.get(3) ; 
			String text5 = 	text.get(4); 
			String text6 = 	text.get(5) ;
	
			Map<?, ?> desktopHints =  (Map<?, ?>) Toolkit.getDefaultToolkit().getDesktopProperty("awt.font.desktophints");

			Graphics2D g2d = (Graphics2D) g;
			
			if (desktopHints != null) { g2d.setRenderingHints(desktopHints); }
				
			g.setFont(new Font("Arial", Font.BOLD, 40));

			g.drawString(text1, 300,192);
			 
			g.setFont(new Font("Arial", Font.BOLD, 27));
			   
			g.drawString(text2, 300,230);
			
			g.drawString(text3, 300,265);
			
			g.drawString(text4, 300,300);
			
			g.drawString(text5, 300,335);
			
			g.drawString(text6, 300,370);
			   
      
			try 
			{
				
			    if (ImageIO.write(combinedImage, "png", new File( directoryName + folder + "\\card_" + folder + ".png")))
			    {
			    	Image image = SwingFXUtils.toFXImage(combinedImage, null);			    				    				
			    	cardView.setImage(image);
			       
			    }
			    
			} 
			catch (IOException e) 
			{
				System.out.println(directoryName + folder + "\\card_" + folder + ".png");			
			    return false;
			}
						
			combinedImage=scale(combinedImage, 300, 200);
		 
			g.dispose();
					
			Runnable r = new Runnable() {
			    @Override
			    public void run() {
			        /*JPanel gui = new JPanel(new GridLayout(1,1));			  
			        gui.add(new JLabel(new ImageIcon(temp)));
			        JOptionPane.showMessageDialog(null, gui);*/
			    }
			};
			
			// Swing GUIs should be created and updated on the EDT
			// http://docs.oracle.com/javase/tutorial/uiswing/concurrency/initial.html
			SwingUtilities.invokeLater(r);
			
			return true;
			
		} catch (FileNotFoundException e) {
			
			System.out.println( directoryName + folder + "\\card_" + folder + ".png");
			//e.printStackTrace();
			System.out.println("**** File Not Found Error !");
			//e.printStackTrace();
			return false;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			System.out.println("IO Error !");
			return false;
		}
    }
    
    
    
    byte[] getRawBytesFromFile(String path) throws FileNotFoundException, IOException {

        byte[] image;
        File file 	= new File(path);
        image 		= new byte[(int)file.length()];

        FileInputStream fileInputStream = new FileInputStream(file);
        fileInputStream.read(image);
       
        return image;
    }
    
    public   BufferedImage scale(BufferedImage src, int w, int h)
    {
        BufferedImage img =   new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int x, y;
        int ww = src.getWidth();
        int hh = src.getHeight();
        int[] ys = new int[h];
        for (y = 0; y < h; y++)
            ys[y] = y * hh / h;
        for (x = 0; x < w; x++) {
            int newX = x * ww / w;
            for (y = 0; y < h; y++) {
                int col = src.getRGB(newX, ys[y]);
                img.setRGB(x, y, col);
            }
        }
        return img;
    }
    
    public   void drawStringMultiLine(Graphics2D g, String text, int lineWidth, int x, int y) {
        FontMetrics m = g.getFontMetrics();
        if(m.stringWidth(text) < lineWidth) {
            g.drawString(text, x, y);
        } else {
            String[] words = text.split(" ");
            String currentLine = words[0];
            for(int i = 1; i < words.length; i++) {
                if(m.stringWidth(currentLine+words[i]) < lineWidth) {
                    currentLine += " "+words[i];
                } else {
                    g.drawString(currentLine, x, y);
                    y += m.getHeight();
                    currentLine = words[i];
                }
            }
            if(currentLine.trim().length() > 0) {
                g.drawString(currentLine, x, y);
            }
        }
    }
    
    

}