package com.tank;

import java.awt.*;


public class Wall {
    int x, y, w, h;
    TankClient tankClient;

    public Wall(int x, int y, int w, int h, TankClient tankClient) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.tankClient = tankClient;
    }

    public void draw (Graphics graphics) {
        graphics.fillRect(x,y,w,h);
    }

    public Rectangle getRect() {
        return new Rectangle(x,y,w,h);
    }
}
