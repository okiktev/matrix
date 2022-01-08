package com.delfin.matrix;

import java.awt.*;

public class Main2 extends Frame {

    public static void main(String argv[]) {
        new Main2().show();
      }

    Main2() {
        add("Center", new InvokeDialog(this));
        setSize(500, 500);
        setMinimumSize(new Dimension(500, 500));
        pack();
      }

}



class InvokeDialog extends Button {
    Frame frame;

    InvokeDialog(Frame fr) {
      super("Show dialog");
      frame = fr;
    }

    public boolean action(Event evt, Object what) {
      Dialog d = new Dialog(frame, false);
      d.add("Center", new Label("Hello"));
      d.pack();
      d.show();
      return true;
    }
}
