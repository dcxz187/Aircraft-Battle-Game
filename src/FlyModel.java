
import java.awt.image.BufferedImage;

/**
 * 会飞的模型类(玩家飞机、导弹、敌机、空投物资的父类)
 */
public abstract class FlyModel {
    // 定义一个BufferedImage类型的变量image，用于存储模型的图片
    protected BufferedImage image;
    protected int x; // 图片左上角的x坐标
    protected int y; // 图片左上角的y坐标
    protected int width; // 图片的宽度
    protected int height; // 图片的高度

    // 使用get()和set()方法封装模型类中的属性

    // 获取图片左上角的x坐标
    public int getX() {
        return x;
    }

    // 设置图片左上角的x坐标
    public void setX(int x) {
        this.x = x;
    }

    // 获取图片左上角的y坐标
    public int getY() {
        return y;
    }

    // 设置图片左上角的y坐标
    public void setY(int y) {
        this.y = y;
    }

    // 获取图片的宽度
    public int getWidth() {
        return width;
    }

    // 设置图片的宽度
    public void setWidth(int width) {
        this.width = width;
    }

    // 获取图片的高度
    public int getHeight() {
        return height;
    }

    // 设置图片的高度
    public void setHeight(int height) {
        this.height = height;
    }

    // 获取模型的图片
    public BufferedImage getImage() {
        return image;
    }

    // 设置模型的图片
    public void setImage(BufferedImage image) {
        this.image = image;
    }

    /**
     * 会飞的模型的移动方法，抽象方法
     */
    public abstract void move();

    /**
     * 会飞的模型是否移动到游戏面板外
     */
    public abstract boolean outOfPanel();

    /**
     * 检查当前会飞的模型是否被导弹击中
     * @param Ammo 导弹对象
     */
    public boolean shootBy(Ammo ammo) {
        int x = ammo.x; //导弹图片左上角的x坐标
        int y = ammo.y; //导弹图片左上角的y坐标
        // 判断导弹是否在模型的矩形区域内
        return this.x < x && x < this.x + width && this.y < y && y < this.y + height;
    }
}