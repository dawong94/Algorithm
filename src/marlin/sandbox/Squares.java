package marlin.sandbox;

import marlin.graphicsLib.I;
import marlin.graphicsLib.UC;
import marlin.graphicsLib.G;
import marlin.graphicsLib.G.VS;
import marlin.graphicsLib.Window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class Squares extends Window implements ActionListener{
  public static Timer timer;
  public Squares(){
    super("Squares", UC.mainWindowWidth,UC.mainWindowHeight);
    timer = new Timer(30,this);
    timer.setInitialDelay(2000);
    timer.start();
  }
  public static Square.List LIST = new Square.List();
  public static I.Area curArea;
  public static Square daSquare;
  public static Square BACKGROUND = new Square(0,0){
    public void pressed(int x, int y){daSquare = new Square(x,y); LIST.add(daSquare);}
    public void dragged(int x, int y){daSquare.resize(x,y); }
  };
  static{BACKGROUND.c = Color.WHITE; BACKGROUND.size.set(3000,3000); LIST.add(BACKGROUND);}
  private static G.V mouseDelta = new G.V(0,0); // we will overwrite this coordinate in mousePressed.

  protected void paintComponent(Graphics g){
    LIST.draw(g);
  }

  public static G.V pressedLoc= new G.V(0,0);
  public void mousePressed(MouseEvent me){
    int x = me.getX(), y = me.getY();
    curArea = LIST.hit(x,y); // the Background on the list should force this to be non-null
    curArea.pressed(x,y);
    repaint();
  }

  public void mouseDragged(MouseEvent me){
    int x = me.getX(), y = me.getY();
    curArea.dragged(x,y);
    repaint();
  }

  public void actionPerformed(ActionEvent ae){repaint();}

  public static class Square extends G.VS implements I.Area{
    public Color c = G.rndColor();
    //public G.V dv = new G.V(G.rnd(20)-10, G.rnd(20)-10);
    public G.V dv = new G.V(0,0);
    public Square(int x, int y){super(x,y,100,100);}
    public void draw(Graphics g){fill(g,c); moveAndBounce();}
    public void resize(int x, int y){if(x>loc.x && y>loc.y){size.set(x - loc.x, y - loc.y);}}
    public void move(int x, int y){loc.set(x, y);}
    public void moveAndBounce(){
      loc.add(dv);
      if(lox() < 0 && dv.x <0){dv.x = - dv.x;}
      if(hix() > 1000 && dv.x >0){dv.x = - dv.x;}
      if(loy() < 0 && dv.y <0){dv.y = - dv.y;}
      if(hiy() > 800 && dv.y >0){dv.y = - dv.y;}
    }
    public void pressed(int x, int y){ mouseDelta.set(loc.x - x, loc.y - y);}
    public void dragged(int x, int y){loc.set(x + mouseDelta.x, y + mouseDelta.y); }
    public void released(int x, int y){}

    public static class List extends ArrayList<Square>{
      public void draw(Graphics g){for(Square s : this){s.draw(g);}}
      public Square hit(int x, int y){
        Square res = null;
        for(Square s: this){if(s.hit(x,y)){res = s;}}
        return res;
      }
    }
  }
}

