package com.tank;

import java.awt.*;


public class Blood {
    int x,y,w,h;

    private boolean live = true;

    TankClient tankClient;
    int strp = 0;

    private int[][] pos= {
        {350,300},{360, 300},{375, 275},{400, 200},{360, 270},{365, 290},{340, 280}
    };

    public Blood() {
        x = pos[0][0];
        y = pos[0][1];
        w = h = 15;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public TankClient getTankClient() {
        return tankClient;
    }

    public void setTankClient(TankClient tankClient) {
        this.tankClient = tankClient;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public void draw(Graphics graphics) {
        if (!live) return;
        Color c = graphics.getColor();
        graphics.setColor(Color.MAGENTA);
        graphics.fillRect(x,y,w,h);
        graphics.setColor(c);

        move();
    }

    private void move() {
        strp++;
        if (strp == pos.length) {
            strp = 0;
        }
        x = pos[strp][0];
        y = pos[strp][1];
    }

    public Rectangle getRect() {
        return new Rectangle(x,y,w,h);
    }
}
