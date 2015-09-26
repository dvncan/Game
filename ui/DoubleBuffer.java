package ankhmorpork.ui;

import java.awt.*;

import javax.swing.JFrame;

/**
 * @author codeproject.com
 * http://www.codeproject.com/Articles/2136/Double-buffer-in-standard-Java-AWT
 */
@SuppressWarnings("serial")
public abstract class DoubleBuffer extends JFrame
{
	//	class variables
	private int bufferWidth;
	private int bufferHeight;
	private Image bufferImage;
	private Graphics bufferGraphics;
	
	public DoubleBuffer()
	{
		super();
	}
	
	public void update(Graphics g)
	{
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		//	checks the buffer-size with the current panel-size
		//	or initializes the image with the first paint
		if(	bufferWidth!=getSize().width ||
			bufferHeight!=getSize().height ||
			bufferImage==null ||
			bufferGraphics==null)
		{
				resetBuffer();
		}
	
		if(bufferGraphics != null)
		{
			//	this clears the off-screen image, not the on-screen one
			bufferGraphics.clearRect(0,0,bufferWidth,bufferHeight);
			
			//	calls the paint-buffer method with the off-screen graphics as a parameter
			paintBuffer(bufferGraphics);
	
			//	we finally paint the off-screen image onto the on-screen image
			g.drawImage(bufferImage,0,0,this);
		}
	}
	
	private void resetBuffer()
	{
		// always keep track of the image size
		bufferWidth=getSize().width;
		bufferHeight=getSize().height;
	
		//	clean up the previous image
		if(bufferGraphics!=null)
		{
			bufferGraphics.dispose();
			bufferGraphics=null;
		}
		
		if(bufferImage!=null)
		{
			bufferImage.flush();
			bufferImage=null;
		}
		
		System.gc();

		//	create the new image with the size of the panel
		bufferImage = createImage(bufferWidth, bufferHeight);
		bufferGraphics = bufferImage.getGraphics();
	}

	//	in classes extended from this one, add something to paint here!
	//	always remember, g is the off-screen graphics
	public abstract void paintBuffer(Graphics g);
}