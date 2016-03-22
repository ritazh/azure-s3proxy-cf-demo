package com.example;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;

/**
 * A {@link PhotoManipulator} to add Doge images.
 *
 * @author Josh Long
 * @author Phillip Webb
 */
public class DogePhotoManipulator {

	private static final int IMAGE_WIDTH = 300;
	
	public byte[] overLay(byte[] photo, String very, String so, String what) throws IOException {
		BufferedImage sourceImage = ImageIO.read( new ByteArrayInputStream(photo));
		BufferedImage destinationImage = manipulate(sourceImage, new TextOverlay(very, so, what));
	
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ImageOutputStream ios = ImageIO.createImageOutputStream(outputStream);
		ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
		ImageWriteParam param = writer.getDefaultWriteParam();
		param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
		param.setCompressionQuality(0.85f);
		writer.setOutput(ios);
		writer.write(null, new IIOImage(destinationImage, null, null), param);
		ImageIO.write(destinationImage, "jpeg", outputStream);
		return outputStream.toByteArray();		
	}

	private BufferedImage manipulate(BufferedImage sourceImage, TextOverlay overlay) {
		double aspectRatio = sourceImage.getHeight() / (double) sourceImage.getWidth();
		int height = (int) Math.floor(IMAGE_WIDTH * aspectRatio);
		BufferedImage destinationImage = new BufferedImage(IMAGE_WIDTH, height,
				BufferedImage.TYPE_INT_RGB);
		render(sourceImage, destinationImage, overlay);
		return destinationImage;
	}

	private void render(BufferedImage sourceImage, BufferedImage destinationImage, TextOverlay overLay) {
		Graphics2D destinationGraphics = destinationImage.createGraphics();
		try {
			setGraphicsHints(destinationGraphics);
			renderBackground(sourceImage, destinationImage, destinationGraphics);
			overLay.render(destinationImage, destinationGraphics);
		}
		finally {
			destinationGraphics.dispose();
		}
	}

	private void renderBackground(BufferedImage sourceImage,
			BufferedImage destinationImage, Graphics2D destinationGraphics) {
		destinationGraphics.drawImage(sourceImage, 0, 0, IMAGE_WIDTH,
				destinationImage.getHeight(), null);
	}

	private void setGraphicsHints(Graphics2D graphics) {
		graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		graphics.setRenderingHint(RenderingHints.KEY_RENDERING,
				RenderingHints.VALUE_RENDER_QUALITY);
		graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
				RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
	}

	

	/**
	 * Text overlay
	 */
	private static class TextOverlay {

		private final String very;

		private final String so;

		private final String such;

		public TextOverlay(String very, String so, String such) {
			this.very = very;
			this.so = so;
			this.such = such;
		}

		public void render(BufferedImage image, Graphics2D g) {
			double r = image.getHeight() / 448.0;
		}

		private void renderText(Graphics2D g, String text, int fontSize, Paint paint,
				double x, double y) {
			Font font = new Font("Comic Sans MS", Font.BOLD, fontSize);
			GlyphVector vector = font.createGlyphVector(g.getFontRenderContext(),
					text.toCharArray());
			Shape shape = vector.getOutline((int) x, (int) y);
			g.setStroke(new BasicStroke(0.5f));
			g.setPaint(paint);
			g.fill(shape);
			g.setColor(Color.BLACK);
			g.draw(shape);
		}
	}
}