package com.tank;

import java.awt.*;


public class Explode {
    private int x, y;
    private boolean live = true;
    private TankClient tankClient;

    private static Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static Image[] images = {
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
            toolkit.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
    };
    int step = 0;
    private static boolean init = false;

    public Explode(int x, int y, TankClient tankClient) {
        this.x = x;
        this.y = y;
        this.tankClient = tankClient;
    }

    public void draw(Graphics graphics) {
        if (!init) {
            for (int i = 0; i < images.length; i++) {
                graphics.drawImage(images[i], -100, -100, null);
            }
            init = true;
        }

        if (!live){
            tankClient.explodes.remove(this);
            return;
        }

        if (step == images.length){
            live = false;
            step = 0;
            return;
        }

        graphics.drawImage(images[step], x, y, null);
        step++;
    }
}
