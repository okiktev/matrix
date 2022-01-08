package com.delfin.matrix;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Test {

    public static void main(String[] args) {
        new Test();
    }

    public Test() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }

                JFrame frame = new JFrame("Testing");
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.add(new TestPane());
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }

    public class TestPane extends JPanel {

        private DefaultPaddle player1;
        private DefaultPaddle player2;
        private Ball b;

        public TestPane() {
            setBackground(Color.BLACK);

            player1 = new DefaultPaddle(1);
            player2 = new DefaultPaddle(2);
            b = new Ball();

            addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "Player1.up.pressed", new UpAction(player1, true));
            addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "Player1.up.released", new UpAction(player1, false));
            addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "Player1.down.pressed", new DownAction(player1, true));
            addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "Player1.down.released", new DownAction(player1, false));

            addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "Player2.up.pressed", new UpAction(player2, true));
            addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "Player2.up.released", new UpAction(player2, false));
            addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "Player2.down.pressed", new DownAction(player2, true));
            addKeyBinding(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "Player2.down.released", new DownAction(player2, false));

            Timer timer = new Timer(25, new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    player1.move();
                    player2.move();
                    b.move();
//                  colitionchecker(1);
                    repaint();
                }
            });
            timer.start();
        }

        protected void addKeyBinding(KeyStroke ks, String name, Action action) {
            InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
            ActionMap am = getActionMap();

            im.put(ks, name);
            am.put(name, action);
        }

        public void colitionchecker(int num) {
            if (num == 1) {
                if (b.getX() < 50 && b.getX() > 20 && b.getY() > player2.getY()
                                && b.getY() >= player2.getY() - 80) {
                    b.xv = -b.xv;
                } else {
                    if (b.getX() < 700 && b.getX() > 660 && b.getY() >= player1.getY() && b.getY() <= player1.getY() + 80) {
                        b.xv = -b.xv;
                    }
                }
            }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(700, 500);
        }

        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(Color.BLACK);
//          if (!(b.getX() < -10 || b.getX() > 690)) {
            player1.draw(g);
            b.draw(g);
            player2.draw(g);
//          } else if (b.getX() < -10) {
//              g.setColor(Color.WHITE);
//              g.drawString("Right Player Won!", 350, 250);
//          } else {
//              g.setColor(Color.WHITE);
//              g.drawString("Left Player Won!", 350, 250);
//          }
            g2d.dispose();
        }

    }

    public class UpAction extends AbstractAction {

        private DefaultPaddle paddle;
        private boolean pressed;

        public UpAction(DefaultPaddle paddle, boolean pressed) {
            this.paddle = paddle;
            this.pressed = pressed;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println("Up " + pressed);
            paddle.setUp(pressed);
        }

    }

    public class DownAction extends AbstractAction {

        private DefaultPaddle paddle;
        private boolean pressed;

        public DownAction(DefaultPaddle paddle, boolean pressed) {
            this.paddle = paddle;
            this.pressed = pressed;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            paddle.setDown(pressed);
        }

    }

    public interface Paddle {

        public void draw(Graphics g);

        public int getY();

        public void move();
    }

    public class DefaultPaddle implements Paddle {

        final double GRAVITY = 0.94;
        double y = 210, yv;
        boolean up, down;
        int player, x;

        public DefaultPaddle(int player) {
            up = false;
            down = false;
            if (player == 1) {
                x = 20;
            } else {
                x = 660;
            }
        }

        @Override
        public void draw(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(x, (int) y, 20, 80);
        }

        public void move() {
            if (up) {
                yv -= 1;
            } else if (down) {
                yv += 1;
            } else if (!down && !up) {
                yv *= GRAVITY;
            }
            if (yv >= 15) {
                yv = 5;
            } else if (yv <= -5) {
                yv = -5;
            }
            y += yv;
            if (y <= 0) {
                y = 0;
            } else if (y >= 420) {
                y = 420;
            }
        }

        public void setUp(boolean up) {
            this.up = up;
        }

        public void setDown(boolean down) {
            this.down = down;
        }

        public int getY() {
            return (int) y;
        }
    }

    public class Ball {

        double xv, yv, x, y;

        public Ball() {
            x = 350;
            y = 250;
            xv = 2;
            yv = 1;
        }

        public int getY() {
            return (int) y;
        }

        public int getX() {
            return (int) x;
        }

        public void move() {
            x += xv;
            y += yv;
            if (y < 10) {
                yv = -yv;
            }
            if (y > 490) {
                yv = -yv;
            }
        }

        public void draw(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillOval((int) x - 10, (int) y - 10, 20, 20);
        }
    }

}
