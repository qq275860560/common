package com.github.qq275860560.common.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

/**
 * @author jiangyuanlin@163.com
 * Zxing图形码工具
 */
public class ZxingUtil {
	//Zxing图形码生成工具
 
	public static File encode(String contents, BarcodeFormat barcodeFormat, Integer margin,
			ErrorCorrectionLevel errorLevel, String format, int width, int height, File saveImgFile) throws Exception {
		BufferedImage bufImg;
		Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
		// 指定纠错等级
		hints.put(EncodeHintType.ERROR_CORRECTION, errorLevel);
		hints.put(EncodeHintType.MARGIN, margin);
		hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
		// contents = new String(contents.getBytes("UTF-8"), "ISO-8859-1");
		BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, barcodeFormat, width, height, hints);
		MatrixToImageConfig config = new MatrixToImageConfig(0xFF000001, 0xFFFFFFFF);
		bufImg = MatrixToImageWriter.toBufferedImage(bitMatrix, config);
		ImageIO.write(bufImg, format, saveImgFile);
		return saveImgFile;
	}

	 
	@SuppressWarnings("finally")
	public static String decode(File srcImgFile) throws Exception {
		BufferedImage image = ImageIO.read(srcImgFile);
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
		return new MultiFormatReader().decode(bitmap, hints).getText();

	}

}
