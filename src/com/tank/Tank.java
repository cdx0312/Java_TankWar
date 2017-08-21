package com.tank;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;


public class Tank {

    public static final int XSPEED = 5;
    public static final int YSPEED = 5;

    private Direction ptDir = Direction.D;
    private boolean good;
    private boolean live = true;
    private int life = 100;
    private BloodBar bloodBar = new BloodBar();

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    private static Random random = new Random();
    private int step = random.nextInt(12) + 3;

    public boolean isGood() {
        return good;
    }

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    TankClient tankClient;

    private static Toolkit toolkit = Toolkit.getDefaultToolkit();
    private static Image[] tankImages = null;
    private static Map<String, Image> imgs = new HashMap<>();
    static {
        tankImages = new Image[] {
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/tankL.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/tankLU.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/tankU.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/tankRU.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/tankR.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/tankRD.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/tankD.gif")),
                toolkit.getImage(Explode.class.getClassLoader().getResource("images/tankLD.gif")),
        };
        imgs.put("L", tankImages[0]);
        imgs.put("LU", tankImages[1]);
        imgs.put("U", tankImages[2]);
        imgs.put("RU", tankImages[3]);
        imgs.put("R", tankImages[4]);
        imgs.put("RD", tankImages[5]);
        imgs.put("D", tankImages[6]);
        imgs.put("LD", tankImages[7]);
    }

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;


    public Tank(int x, int y,boolean good, Direction dir, TankClient tankClient) {
        this(x, y, good);
        this.direction = dir;
        this.tankClient = tankClient;
    }

    private int x, y;
    private int oldX, oldY;

    private boolean bL = false, bU = false, bR = false, bD = false;

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = false;
                break;
            case KeyEvent.VK_UP:
                bU = false;
                break;
            case KeyEvent.VK_RIGHT:
                bR = false;
                break;
            case KeyEvent.VK_DOWN:
                bD = false;
                break;
            case KeyEvent.VK_CONTROL:
                tankClient.missiles.add(fire());
                break;
            case KeyEvent.VK_A :
                superFire();
                break;
        }
        locateDirection();
    }

    private Direction direction = Direction.STOP;

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.good = good;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void draw(Graphics graphics) {
        if (!live) {
            if (!good) {
                tankClient.tanks.remove(this);
            }
            return;
        }

        if (good)
            bloodBar.draw(graphics);
        switch (ptDir) {
            case L:
                graphics.drawImage(imgs.get("L"),x,y,null);
                break;
            case LU:
                graphics.drawImage(imgs.get("LU"),x,y,null);
                break;
            case U:
                graphics.drawImage(imgs.get("U"),x,y,null);
                break;
            case RU:
                graphics.drawImage(imgs.get("RU"),x,y,null);
                break;
            case R:
                graphics.drawImage(imgs.get("R"),x,y,null);
                break;
            case RD:
                graphics.drawImage(imgs.get("RD"),x,y,null);
                break;
            case D:
                graphics.drawImage(imgs.get("D"),x,y,null);
                break;
            case LD:
                graphics.drawImage(imgs.get("LD"),x,y,null);
        }
        move();
    }

    void move() {
        //记录上一步坦克所在的位置
        this.oldX = x;
        this.oldY = y;

        switch (direction) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;
        }
        if (this.direction != Direction.STOP) {
            ptDir = this.direction;
        }
        if (x < 0)
            x = 0;
        if (y < 30)
            y = 30;
        if (x + Tank.WIDTH > TankClient.GAMEWIDTH)
            x = TankClient.GAMEWIDTH - Tank.WIDTH;
        if (y + Tank.HEIGHT > TankClient.GAMEHEIGHT)
            y = TankClient.GAMEHEIGHT - Tank.HEIGHT;

        if (!good) {
            if (step == 0) {
                step = random.nextInt(12) + 3;
                Direction[] dirs = Direction.values();
                int randomNumber = random.nextInt(dirs.length);
                direction = dirs[randomNumber];

            }
            step--;
            if (random.nextInt(40) > 37)
                this.fire();
        }
    }

    public void keyPressed(KeyEvent event) {
        int key = event.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT:
                bL = true;
                break;
            case KeyEvent.VK_UP:
                bU = true;
                break;
            case KeyEvent.VK_RIGHT:
                bR = true;
                break;
            case KeyEvent.VK_DOWN:
                bD = true;
                break;
            case KeyEvent.VK_F2:
                if (!this.live) {
                    this.live = true;
                    this.life = 100;
                }
                break;
        }
        locateDirection();
    }

    void locateDirection() {
        if (bL && !bU && !bR && !bD )
            direction = Direction.L;
         else if (bL && bU && !bR && !bD )
            direction = Direction.LU;
         else if (!bL && bU && !bR && !bD )
            direction = Direction.U;
        else if (!bL && bU && bR && !bD )
            direction = Direction.RU;
        else if (!bL && !bU && bR && !bD )
            direction = Direction.R;
        else if (!bL && !bU && bR && bD )
            direction = Direction.RD;
        else if (!bL && !bU && !bR && bD )
            direction = Direction.D;
        else if (bL && !bU && !bR && bD )
            direction = Direction.LD;
        else if (!bL && !bU && !bR && !bD )
            direction = Direction.STOP;
    }

    public Missile fire() {
        return fire(ptDir);
    }

    public Rectangle getRect() {
        return new Rectangle(x, y, tankImages[0].getWidth(null), tankImages[0].getHeight(null));
    }

    public boolean collideWithWall (Wall wall) {
        if (this.live && this.getRect().intersects(wall.getRect())) {
            this.stay();
            return true;
        }
        return false;
    }

    private void stay () {
        x = oldX;
        y = oldY;
    }

    public boolean collidesWithTanks (java.util.List<Tank> tanks) {
        for (int i = 0; i < tanks.size(); i++) {
            Tank tank = tanks.get(i);
            if (this != tank) {
                if (this.live && tank.isLive() && this.getRect().intersects(tank.getRect())) {
                    this.stay();
                    tank.stay();
                    return true;
                }
            }
        }
        return false;
    }

    public Missile fire (Direction dir) {
        if (!live)
            return null;
        int x = this.x + Tank.WIDTH / 2 - Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT / 2 - Missile.HEIGHT / 2;
        Missile missile = new Missile(x, y,good,dir,this.tankClient);
        tankClient.missiles.add(missile);
        return missile;
    }

    private void superFire() {
        Direction[] dirs = Direction.values();
        for (int i = 0; i < 8; i++) {
            tankClient.missiles.add(fire(dirs[i]));
        }
    }

    private class BloodBar {
        public void draw (Graphics graphics) {
            Color c = graphics.getColor();
            graphics.setColor(Color.RED);
            graphics.drawRect(x,y-10,WIDTH, 10);
            int w = WIDTH * life / 100;
            graphics.fillRect(x,y-10,w,10);
            graphics.setColor(c);
        }
    }

    public boolean eat(Blood blood) {
        if (this.live && blood.isLive() && this.getRect().intersects(blood.getRect())) {
            this.setLife(100);
            blood.setLive(false);
            return true;
        }
        return false;
    }
}
