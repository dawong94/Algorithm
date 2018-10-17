package marlin.sandbox;

import marlin.graphicsLib.Window;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Paint extends Window{
  public Paint(){super("Paint", 1000, 800);}

  public static Path daPath = new Path();
  public static Pic daPic = new Pic();

  @Override
  protected void paintComponent(Graphics g){
    g.setColor(Color.WHITE); g.fillRect(0,0,3000,3000); // clear background
    g.setColor(Color.BLACK);
    daPic.draw(g);
  }

  public void mousePressed(MouseEvent me){
    daPath = new Path();
    daPath.add(me.getPoint());
    daPic.add(daPath);
    repaint();
  }

  @Override
  public void mouseDragged(MouseEvent me){
    daPath.add(me.getPoint());
    repaint();
  }

  public static class Path extends ArrayList<Point>{
    public void draw(Graphics g){
      for(int i = 1; i<this.size(); i++){
        Point p = get(i-1), n = get(i); // get two adjacent points
        g.drawLine(p.x,p.y,n.x,n.y);
      }
    }
  }

  public static class Pic extends ArrayList<Path>{
    public void draw(Graphics g){ for(Path p : this){p.draw(g);} }
  }
}
