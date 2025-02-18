/**
 * 导弹类（继承会飞的模型类）
 */
public class Ammo extends FlyModel {
    private int speed = 3; // 导弹的移动速度

    /** 初始化数据 */
    public Ammo(int x,int y){
        this.x = x;
        this.y = y;
        this.image = GamePanel.ammoImage;
    }

    /**
     * 导弹图片的移动方法
     */
    public void move(){
        y -= speed;
    }

    /**
     * 导弹图片是否移动到游戏面板外
     */
    public boolean outOfPanel() {
        return y < -height;
    }
}