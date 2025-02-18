
import java.util.Random;

/**
 * 空投物资（继承会飞的模型类）
 */
public class Airdrop extends FlyModel {
    private int xSpeed = 1; // 空投物资图片x坐标的移动速度
    private int ySpeed = 2; // 空投物资图片y坐标的移动速度

    /**
     * 初始化数据
     */
    public Airdrop(){
        this.image = GamePanel.airdropImage; // 空投物资的图片
        width = image.getWidth(); // 空投物资图片的宽度
        height = image.getHeight(); // 空投物资图片的高度
        y = -height; // 游戏开始时，空投物资图片左上角的y坐标
        Random rand = new Random(); // 创建随机数对象
        x = rand.nextInt(GamePanel.WIDTH - width); // 初始时，空投物资图片左上角的x坐标（随机）
    }

    /**
     * 空投物资的图片是否移动到游戏面板外
     */
    public boolean outOfPanel() {
        return y > GamePanel.HEIGHT;
    }

    /**
     * 空投物资图片的移动方法
     */
    public void move() {
        x += xSpeed;
        y += ySpeed;
        if(x > GamePanel.WIDTH-width){
            xSpeed = -1;
        }
        if(x < 0){
            xSpeed = 1;
        }
    }
}