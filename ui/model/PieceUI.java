package ankhmorpork.ui.model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.util.HashMap;

import ankhmorpork.model.Color;

public class PieceUI extends ObjectUI
{
	private HashMap<Color, StructColorPictureInfo> colorPictureInfoMap = new HashMap<Color, StructColorPictureInfo>();

	/**
	 * Set the picture that will represent that UI piece and the list of points delimiters
	 * @param a_color the color of the piece
	 * @param a_picturePath the picture path
	 * @param a_topleftPicture the top-left point
	 * @param a_bottomrightPicture the bottom-right point
	 */
	public void setPicture(final Color a_color, final String a_picturePath, final Point a_topleftPicture, final Point a_bottomrightPicture)
	{
		StructColorPictureInfo info = new StructColorPictureInfo() 
		{{
			color = a_color;
			picturePath = a_picturePath;
			pointTopLeft = a_topleftPicture;
			pointBottomRight = a_bottomrightPicture;
		}};
		
		colorPictureInfoMap.put(info.color, info);
	}
	
	/**
	 * find the picture of the piece based on it color
	 * @param color the color of the piece to return
	 * @return the picture
	 */
	public Graphics getPicture(Color color)
	{
		StructColorPictureInfo info = colorPictureInfoMap.get(color);
		Image image = Toolkit.getDefaultToolkit().getImage(info.picturePath);
		BufferedImage bi = new BufferedImage(info.pointBottomRight.x - info.pointTopLeft.x, info.pointTopLeft.y - info.pointBottomRight.y, BufferedImage.TYPE_INT_ARGB);
		Graphics big = bi.getGraphics();
		big.drawImage(image, info.pointTopLeft.x, info.pointTopLeft.y, null);


		return big;
	}

	public void drawPicture(Color color, Graphics2D g2, Point position) 
	{
		StructColorPictureInfo info = colorPictureInfoMap.get(color);
		Image image = Toolkit.getDefaultToolkit().getImage(info.picturePath);
		g2.drawImage(image, position.x, position.y, position.x + (int)((info.pointBottomRight.x-info.pointTopLeft.x) * K_RATIO), position.y + (int)((info.pointBottomRight.y - info.pointTopLeft.y) * K_RATIO), info.pointTopLeft.x, info.pointTopLeft.y, info.pointBottomRight.x, info.pointBottomRight.y, null);
	}
	
	/**
	 * Class used as a structure for picture info linked to color
	 */
	protected class StructColorPictureInfo
	{
		public Color color;
		public String picturePath;
		public Point pointTopLeft;
		public Point pointBottomRight;
	}
}
