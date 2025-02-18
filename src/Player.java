
import java.awt.image.BufferedImage;

/**
 * 玩家飞机类（继承会飞的模型类）
 */
public class Player extends FlyModel {

    private BufferedImage[] playerImages; // 用于保存玩家飞机的图片 playerImages[]
    private int imageIndex; // 初始化切换玩家飞机图片时的索引 imageIndex
    private int doubleAmmos; // 玩家飞机同时发射两枚导弹 doubleAmmos
    private int lifeNumbers; // 玩家飞机剩余的生命数 lifeNumbers

    /**
     * 在构造方法中，初始化玩家飞机类中数据
     */
    public Player() {
        lifeNumbers = 1; // 游戏开始时玩家飞机有1条命
        doubleAmmos = 1; // 设置游戏开始时，玩家飞机只能在同一时间内发射一枚导弹
        playerImages = new BufferedImage[] { GamePanel.player1Image, GamePanel.player2Image }; // 初始化玩家飞机的图片
        image = GamePanel.player1Image; // 设置游戏开始时的玩家飞机的图片
        width = image.getWidth(); // 初始化玩家飞机图片的宽度
        height = image.getHeight(); // 初始化玩家飞机图片的高度
        x = 145; // 设置游戏开始时，玩家飞机图片左上角的x坐标
        y = 450; // 设置游戏开始时，玩家飞机图片左上角的y坐标
    }

    /**
     * 玩家飞机同时发射两枚导弹
     */
    public void fireDoubleAmmos() {
        doubleAmmos = 2;
    }

    /**
     * 减少生命数
     */
    public void loseLifeNumbers() {
        lifeNumbers--;
    }

    /**
     * 获得生命数
     */
    public int getLifeNumbers() {
        return lifeNumbers;
    }

    /**
     * 更新玩家飞机移动后的中心点坐标
     * @param mouseX 鼠标所处位置的x坐标
     * @param mouseY 鼠标所处位置的y坐标
     */
    public void updateXY(int mouseX, int mouseY) {
        this.x = mouseX - width/2;
        this.y = mouseY - height/2;
    }

    /**
     * 玩家飞机的图片不能移动到游戏面板外
     */
    public boolean outOfPanel() {
        return false;
    }

    /**
     * 玩家飞机发射导弹
     * @return 发射的导弹对象
     */
    public Ammo[] fireAmmo() {
        int xStep = width / 4; // 把玩家飞机图片的宽度平均分为4份
        int yStep = 20; // 游戏开始时，第一枚导弹与玩家飞机的距离
        if (doubleAmmos == 1) { // 发射一枚导弹
            Ammo[] ammos = new Ammo[1]; // 一枚导弹
            // x + 2 * xStep（导弹相对玩家飞机的x坐标），y-yStep（导弹相对玩家飞机的y坐标）
            ammos[0] = new Ammo(x + 2 * xStep, y - yStep);
            return ammos;
        } else { // 发射两枚导弹
            Ammo[] ammos = new Ammo[2]; // 两枚导弹
            ammos[0] = new Ammo(x + xStep, y - yStep);
            ammos[1] = new Ammo(x + 3 * xStep, y - yStep);
            return ammos;
        }
    }

    /**
     * 玩家飞机图片的移动方法
     */
    public void move() {
        if (playerImages.length > 0) {
            image = playerImages[imageIndex++ / 10 % playerImages.length]; // 每移动一步，玩家飞机的图片就在player1Image和player2Image之间切换一次
        }
    }

    /**
     * 判断玩家飞机是否发生碰撞
     */
    public boolean hit(FlyModel model) {
        int x1 = model.x - this.width / 2; // 距离玩家飞机最小的x坐标
        int x2 = model.x + this.width / 2 + model.width; // 距离玩家飞机最大的x坐标
        int y1 = model.y - this.height / 2; // 距离玩家飞机最小的y坐标
        int y2 = model.y + this.height / 2 + model.height; // 距离玩家飞机最大的y坐标

        int playerx = this.x + this.width / 2; // 表示玩家飞机中心点的x坐标
        int playery = this.y + this.height / 2; // 表示玩家飞机中心点的y坐标

        return playerx > x1 && playerx < x2 && playery > y1 && playery < y2; // 区间范围内发生碰撞
    }
}