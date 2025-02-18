import java.util.Random;
/**
 * 敌机，继承会飞的模型类
 */
public class Enemy extends FlyModel implements Hit {
    private int speed = 3; // 敌机图片的移动速度

    /**
     * 初始化数据
     */
    public Enemy(){
        this.image = GamePanel.enemyImage; // 敌机图片
        width = image.getWidth(); // 敌机图片的宽度
        height = image.getHeight(); // 敌机图片的高度
        y = -height; // 游戏开始时，敌机图片左上角的y坐标
        Random rand = new Random(); // 创建随机数对象
        x = rand.nextInt(GamePanel.WIDTH - width); // 游戏开始时，敌机图片左上角的x坐标（随机）
    }

    /**
     * 获得分数
     */
    public int getScores() {
        return 5; // 击落一架敌机得5分
    }

    /**
     * 敌机图片移动
     */
    public void move() {
        y += speed;
    }

    /**
     * 敌机图片是否移动到游戏面板外
     */
    public  boolean outOfPanel() {
        return y > GamePanel.HEIGHT;
    }
}
