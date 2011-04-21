/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modsys_coursework;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.Vector;



/**
 *
 * @author dshmelyo
 */
public class GraphCanvas extends Canvas {

    Vector TimeOfComin;
    Vector TimeOfResolv;
    Vector TimeOfRecord;
    int People = 300;

    

    @Override
    public void paint(Graphics g) {
        double width = (double) getSize().width/People;
        for (int i=0; i< People; i++) {
                int x1 = (int) (i * width + 1);
                int x2 = (int) (x1 + width - 1);

                int y = (Integer) TimeOfComin.elementAt(i);
                g.setColor(Color.RED);
                g.fillRect(x1, getSize().height - y, (int)width,y) ;

                int old_y = y;
                y = y + (Integer) TimeOfResolv.elementAt(i);
                g.setColor(Color.BLUE);
                g.fillRect(x1, getSize().height-y, (int)width, y-old_y);

                old_y = y;
                y = y + (Integer) TimeOfRecord.elementAt(i);
                g.setColor(Color.WHITE);
                g.fillRect(x1, getSize().height-y, (int)width, y-old_y);
        }
    }
}
