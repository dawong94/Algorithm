package marlin.reactions;

import java.awt.Color;
import java.awt.Graphics;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import marlin.graphicsLib.G;
import marlin.graphicsLib.I;
import marlin.graphicsLib.UC;

public class Shape implements Serializable {

  public String name;
  public Prototype.List prototypes = new Prototype.List();

  public Shape(String name) {
    this.name = name;
  }

  public static HashMap<String, Shape> DB = loadDB();

  public static Shape DOT = DB.get("DOT");
  public static Collection<Shape> LIST = DB.values();

  public static HashMap<String, Shape> loadDB() {
    HashMap<String, Shape> res = new HashMap<>();
    res.put("DOT", new Shape("DOT"));
    try {
      ObjectInputStream ois = new ObjectInputStream(new FileInputStream(UC.ShapeDBFileName));
      res = (HashMap<String, Shape>) ois.readObject();
      System.out.println("Loading Shape DB.");

    } catch (Exception e) {
      System.out.println("Filed loading Shape DB.");

      System.out.println(e);
    }
    return res;
  }

  public static void saveDB() {
    try {
      ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(UC.ShapeDBFileName));
      oos.writeObject(DB);
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  public static Shape recognize(Ink ink) { //can return null
    if (ink.vs.size.x < UC.dotThreshold && ink.vs.size.y < UC.dotThreshold) {
      return DOT;
    }
    Shape bestMatched = null;
    int bestSoFar = UC.noMatchDist;
    for (Shape s : LIST) {
      int d = s.prototypes.bestDist(ink.norm);
      if (d < bestSoFar) {
        bestMatched = s;
        bestSoFar = d;
      }
    }
    return bestMatched;
  }

  public static class Prototype extends Ink.Norm implements Serializable {

    public int nBlends;

    public void blend(Ink.Norm norm) {
      for (int i = 0; i < N; i++) {
        points[i].blend(norm.points[i], nBlends);

      }
    }

    public static class List extends ArrayList<Prototype> implements I.Show, Serializable {

      public static Prototype bestMatch; // this is set as a side effect when running bestDist

      public int bestDist(Ink.Norm norm) {
        bestMatch = null;
        int bestSoFar = UC.noMatchDist; // assume no match
        for (Prototype p : this) {
          int d = p.dist(norm);
          if (d < bestSoFar) {
            bestMatch = p;
            bestSoFar = d;
          }
        }
        return bestSoFar;
      }
    }

    private static int m = 10, w = 60;
    private static G.VS showbox = new G.VS(m, m, w, w);

    public void show(Graphics g) { // draw a list of boxes across top of screen
      g.setColor(Color.ORANGE);
      for (int i = 0; i < size(); i++) {
        Prototype p = get(i);
        int x = m + i * (m + w);
        showbox.loc.set(x, m); // march the showbox across the top of the screen
        p.drawAt(g, showbox);
        g.drawString("" + p.nBlend, x, 20);
      }
    }

  }


}
