package application;

import java.awt.image.BufferedImage;
import java.io.OutputStream;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;

public class CodeGenerator {
	
	
	public static BufferedImage generateBarcode(String barcodeText,int width,int height) throws Exception {
		Code128Writer barcodeWriter = new Code128Writer();
	    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.EAN_13, width, height);

	    return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	
	public static void writeBarcode(String barcodeText,int width,int height,OutputStream os) throws Exception {
	  
		
		Code128Writer barcodeWriter = new Code128Writer();
	    BitMatrix bitMatrix = barcodeWriter.encode(barcodeText, BarcodeFormat.CODE_128, width, height);

	    MatrixToImageWriter.writeToStream(bitMatrix,"jpg",os);
	}
	
	public static BufferedImage generateQRCodeImage(String barcodeText,int width,int height) throws Exception {
	    QRCodeWriter barcodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = 
	      barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, width, height);

	   
	    return MatrixToImageWriter.toBufferedImage(bitMatrix);
	}
	
	public static void writeQRCodeImage(String barcodeText,int width,int height,OutputStream os) throws Exception {
	    QRCodeWriter barcodeWriter = new QRCodeWriter();
	    BitMatrix bitMatrix = 
	      barcodeWriter.encode(barcodeText, BarcodeFormat.QR_CODE, width, height);

	   
	     MatrixToImageWriter.writeToStream(bitMatrix,"png",os);
	}

}
