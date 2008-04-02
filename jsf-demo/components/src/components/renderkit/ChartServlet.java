/*
 * $Id: ChartServlet.java,v 1.2 2004/03/08 17:55:36 rkitain Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or
 * without modification, are permitted provided that the following
 * conditions are met:
 *
 * - Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 *
 * - Redistribution in binary form must reproduce the above
 *   copyright notice, this list of conditions and the following
 *   disclaimer in the documentation and/or other materials
 *   provided with the distribution.
 *
 * Neither the name of Sun Microsystems, Inc. or the names of
 * contributors may be used to endorse or promote products derived
 * from this software without specific prior written permission.
 *
 * This software is provided "AS IS," without a warranty of any
 * kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
 * WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
 * EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
 * DAMAGES OR LIABILITIES SUFFERED BY LICENSEE AS A RESULT OF OR
 * RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THIS SOFTWARE OR
 * ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
 * FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
 * SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
 * CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
 * THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF SUN HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 *
 * You acknowledge that this software is not designed, licensed or
 * intended for use in the design, construction, operation or
 * maintenance of any nuclear facility.
 */

package components.renderkit;

import components.model.ChartItem;

import com.sun.image.codec.jpeg.*;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public final class ChartServlet extends HttpServlet {

    /**
     * <p>The <code>ServletConfig</code> instance for this servlet.</p>
     */
    private ServletConfig servletConfig = null;


    
    /**
     * <p>Release all resources acquired at startup time.</p>
     */
    public void destroy() {
        servletConfig = null;
    }

    /**
     * <p>Return the <code>ServletConfig</code> instance for this servlet.</p>
     */
    public ServletConfig getServletConfig() {

        return (this.servletConfig);

    }

    /**
     * <p>Return information about this Servlet.</p>
     */
    public String getServletInfo() {

        return (this.getClass().getName());

    }

    /**
     * <p>Perform initialization.</p>
     *
     * @exception ServletException if, for any reason, 
     * bn error occurred during the processing of
     * this <code>init()</code> method.
     */
    public void init(ServletConfig servletConfig) throws ServletException {
	
        // Save our ServletConfig instance
        this.servletConfig = servletConfig;
    }

    /**
     * <p>Process an incoming request, and create the corresponding
     * response.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception ServletException if a servlet error occurs during processing
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {


	// Here's where we'd get the ChartBean from the session and determine
	// whether we're generating a pie chart or bar chart...
	//
	String type = request.getParameter("type");
	if ((type == null) || 
	    (!type.equals("bar")) && (!type.equals("pie"))) {
	    type = "bar";
	}

        generatePieChart(request, response);

	if (type.equals("bar")) {
	    generateBarChart(request, response);
	} else {
            generatePieChart(request, response);
	}
    }

    /**
     * <p>Process an incoming request, and create the corresponding
     * response.</p>
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception ServletException if a servlet error occurs during processing
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws IOException, ServletException {
	doGet(request, response);
    }

    /**
     * <p> Generate a bar chart from data.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception ServletException if a servlet error occurs during processing
     */
    private void generateBarChart(HttpServletRequest request,
		                  HttpServletResponse response)
        throws IOException, ServletException {

        final int VERTICAL = 0;
	final int HORIZONTAL = 1;

	response.setContentType("image/jpeg");
	
	// get chart parameters
	String title = request.getParameter("title");
	if (title == null) {
	    title = "Chart";
	}
	
	int orientation = VERTICAL;
	String orientationStr = request.getParameter("orientation");
	if ((orientationStr == null) || 
	    (!orientationStr.equals("horizontal")) && (!orientationStr.equals("vertical"))) {
	    orientation = VERTICAL;
	} else if (orientationStr.equals("vertical")) {
	    orientation = VERTICAL;
	} else {
	    orientation = HORIZONTAL;
	}

	// default image size
	int width = 400;
	int height = 300;
	String widthStr = request.getParameter("width");
	String heightStr = request.getParameter("height");
	if (widthStr != null) {
	    width = Integer.parseInt(widthStr);
	}
	if (heightStr != null) {
	    height = Integer.parseInt(heightStr);
	}
	
	// get an array of chart items containing our data..
        HttpSession session = request.getSession(true);
	ChartItem[] chartItems = (ChartItem[])session.getAttribute("chart");
	if (chartItems == null) {
	    throw new ServletException("No data items specified...");
	}

	// maximum data value
	int maxDataValue = 0;
	// maximum label width
	int maxLabelWidth = 0;
	// space between bars
	int barSpacing = 10;
	// width of each bar
	int barWidth = 0;
	// x,y coordinates
	int cx, cy;
	// number of chart items
	int columns = chartItems.length;
	int scale = 10;
        // an individual chart data item
	ChartItem chartItem = null;
	String label = null;
	int value = 0;

        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bi.createGraphics();
        Font titleFont = new java.awt.Font("Courier", Font.BOLD, 12);
        FontMetrics titleFontMetrics = g2d.getFontMetrics(titleFont);
	
	// loop through and figure out the the widest item label, as well as
	// the maximum value.
        for (int i=0; i < columns; i++) {
	    chartItem = chartItems[i];
	    label = chartItem.getLabel();
	    value = chartItem.getValue();
	    if (value > maxDataValue) {
	        maxDataValue = value;
	    }
	    maxLabelWidth = Math.max(titleFontMetrics.stringWidth(label), maxLabelWidth);
	}

	// calculate chart dimensions
	int[] xcoords = new int[columns];
	int[] ycoords = new int[columns];
	int totalWidth = 0;
	int totalHeight = 0;
	for (int i=0; i < columns; i++) {
	    switch (orientation) {
	      case VERTICAL:
	      default: 
                barWidth = maxLabelWidth;
		cx = (Math.max((barWidth + barSpacing),maxLabelWidth) * i) +
		    barSpacing;
		totalWidth += cx;
		break;
	      case HORIZONTAL:
		barWidth = titleFont.getSize();
		cy = ((barWidth + barSpacing) * i) + barSpacing;
		totalHeight = cy + (4 * titleFont.getSize()); 
		break;
	    }

	}
	if (orientation == VERTICAL) {
            totalHeight = maxDataValue + (4 * titleFont.getSize());
	} else {
	    totalWidth = maxDataValue + (4 * titleFont.getSize() +
		(Integer.toString(maxDataValue).length() * titleFont.getSize()));
	}

        bi = new BufferedImage(totalWidth, totalHeight, BufferedImage.TYPE_INT_RGB);
        g2d = bi.createGraphics();
        titleFontMetrics = g2d.getFontMetrics(titleFont);

	// graph dimensions
	Dimension graphDim = new Dimension(totalWidth,totalHeight);
        Rectangle graphRect = new Rectangle(graphDim);

	// border dimensions
	Dimension borderDim = new Dimension(totalWidth-2,totalHeight-2);
        Rectangle borderRect = new Rectangle(borderDim);
	
	// background color
	g2d.setColor(Color.white);
	g2d.fill(graphRect);

	// draw border
	g2d.setColor(Color.black);
	borderRect.setLocation(1,1);
        g2d.draw(borderRect);

	// draw the title centered at the bottom of the bar graph
	int i = titleFontMetrics.stringWidth(title);
	g2d.setFont(titleFont);
	g2d.setColor(Color.black);
	g2d.drawString(title, Math.max((totalWidth - i)/2, 0),
	    totalHeight - titleFontMetrics.getDescent());

	// loop through to draw the chart items.
        for (i=0; i < columns; i++) {
	    chartItem = chartItems[i];
	    label = chartItem.getLabel();
	    value = chartItem.getValue();
	    String colorStr = chartItem.getColor();

	    Object color = null;
	    if (colorStr != null) {
		if (colorStr.equals("red")) {
		    color = Color.red;
		} else if (colorStr.equals("green")) {
		    color = Color.green;
		} else if (colorStr.equals("blue")) {
		    color = Color.blue;
		} else if (colorStr.equals("pink")) {
		    color = Color.pink;
		} else if (colorStr.equals("orange")) {
		    color = Color.orange;
		} else if (colorStr.equals("magenta")) {
		    color = Color.magenta;
		} else if (colorStr.equals("cyan")) {
		    color = Color.cyan;
		} else if (colorStr.equals("white")) {
		    color = Color.white;
		} else if (colorStr.equals("yellow")) {
		    color = Color.yellow;
		} else if (colorStr.equals("gray")) {
		    color = Color.gray;
		} else if (colorStr.equals("darkGray")) {
		    color = Color.darkGray;
		} else {
		    color = Color.gray;
		}
	    } else {
		color = Color.gray;
	    }   

	    switch (orientation) {
	      case VERTICAL:
	      default: 
                barWidth = maxLabelWidth;
		// set the next X coordinate to account for the label
		// being wider than the bar width.
		cx = (Math.max((barWidth + barSpacing),maxLabelWidth) * i) +
		    barSpacing;

		// center the bar chart
		cx += Math.max((totalWidth - (columns * (barWidth + 
		    (2 * barSpacing))))/2,0);
		   
		// set the next Y coordinate to account for the height
		// of the bar as well as the title and labels painted
		// at the bottom of the chart.
		cy = totalHeight - (value) - 1 - (2 * titleFont.getSize());
	
		// draw the label
		g2d.setColor(Color.black);
		g2d.drawString((String)label, cx,
		    totalHeight - titleFont.getSize() - titleFontMetrics.getDescent());	

		// draw the shadow bar
		if (color == Color.black) {
		    g2d.setColor(Color.gray);
		}
		g2d.fillRect(cx + 5, cy - 3, barWidth,  (value));
		// draw the bar with the specified color
		g2d.setColor((Color)(color));
                g2d.fillRect(cx, cy, barWidth, (value));
                g2d.drawString("" + value, cx, cy - titleFontMetrics.getDescent());
		break;
	      case HORIZONTAL:
		barWidth = titleFont.getSize();
		// set the Y coordinate
		cy = ((barWidth + barSpacing) * i) + barSpacing;

		// set the X coordinate to be the width of the widest label
		cx = maxLabelWidth + 1;

		cx += Math.max((totalWidth - (maxLabelWidth + 1 +
	            titleFontMetrics.stringWidth("" + maxDataValue) +
                    (maxDataValue))) / 2, 0);
		// draw the labels and the shadow
		g2d.setColor(Color.black);
		g2d.drawString((String)label, cx - maxLabelWidth - 1,
			     cy + titleFontMetrics.getAscent());
		if (color == Color.black) {
		    g2d.setColor(Color.gray);
		}
		g2d.fillRect(cx + 3, cy + 5, (value), barWidth);

		// draw the bar in the current color
		g2d.setColor((Color)(color));
                g2d.fillRect(cx, cy, (value), barWidth);
                g2d.drawString("" + value, cx + (value ) + 3,
                    cy + titleFontMetrics.getAscent());
		break;
	    }
	}
        OutputStream output = response.getOutputStream();
        JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(output);
        encoder.encode(bi);
        output.close();
    }

    /**
     * <p> Generate a pie chart from data.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs during processing
     * @exception ServletException if a servlet error occurs during processing
     */
    private void generatePieChart(HttpServletRequest request,
		                  HttpServletResponse response)
        throws IOException, ServletException {

    }
}
