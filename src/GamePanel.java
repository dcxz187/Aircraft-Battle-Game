
import java.awt.Font;   // Font 类用于定义和操作字体，包括字体的名称、样式和大小
import java.awt.Color;  // Color 类用于定义颜色对象
import java.awt.Graphics;   // Graphics 类提供了基本的绘图功能
import java.awt.event.MouseAdapter; // MouseAdapter 是一个抽象类，提供了鼠标事件的默认实现
import java.awt.event.MouseEvent;   // MouseEvent 类用于封装与鼠标事件相关的数据，例如点击、释放、移动等操作
import java.util.Arrays;    // Arrays类提供了一系列用于操作数组的方法，包括排序、搜索、填充等
import java.util.Random;    // Random类用于生成伪随机数
import java.util.Timer; // Timer类用于安排在后台线程中执行任务
import java.util.TimerTask; // TimerTask 是一个抽象类，用于创建定时任务
import java.awt.image.BufferedImage;    // BufferedImage 类用于表示内存中的图像

import javax.imageio.ImageIO;   // ImageIO 类提供了用于读取和写入图像的标准方法
import javax.swing.JFrame;  // JFrame 类用于创建窗口框架
import javax.swing.JPanel;  // JPanel 是 Swing 库中的一个组件，用于在图形用户界面（GUI）中创建容器，可以用来放置其他组件（如按钮、标签等）

/**
 * 游戏面板
 */
public class GamePanel extends JPanel {
    // 常量：表示窗体的宽度和高度
    public static final int WIDTH = 360;
    public static final int HEIGHT = 600;
    private int state; // 游戏的状态
    // 常量：表示游戏的状态
    private static final int START = 0;
    private static final int RUNNING = 1;
    private static final int OVER = 2;

    private int scores = 0; // 游戏开始时，得分为0
    private Timer timer; // 声明定时器
    private int intervel = 1000 / 100; // 初始化时间间隔（毫秒）

    public static BufferedImage startImage; // 游戏开始时的窗体背景图片
    public static BufferedImage backgroundImage; // 窗体背景图片
    public static BufferedImage enemyImage; // 敌机图片
    public static BufferedImage airdropImage; // 空投物资图片
    public static BufferedImage ammoImage; // 导弹图片
    public static BufferedImage player1Image; // 玩家飞机图片（喷气量小）
    public static BufferedImage player2Image; // 玩家飞机图片（喷气量大）
    public static BufferedImage gameoverImage; // 游戏结束图片

    private FlyModel[] flyModels = {}; // 声明会飞的模型（例如敌机、空投物资等）数组
    private Ammo[] ammos = {}; // 声明导弹数组
    private Player player = new Player(); // 新建玩家飞机类对象

    static { // 初始化图片资源
        try {
            startImage = ImageIO.read(GamePanel.class.getResource("start.png"));
            backgroundImage = ImageIO.read(GamePanel.class.getResource("background.png"));
            enemyImage = ImageIO.read(GamePanel.class.getResource("enemy.png"));
            airdropImage = ImageIO.read(GamePanel.class.getResource("airdrop.png"));
            ammoImage = ImageIO.read(GamePanel.class.getResource("ammo.png"));
            player1Image = ImageIO.read(GamePanel.class.getResource("player1.png"));
            player2Image = ImageIO.read(GamePanel.class.getResource("player2.png"));
            gameoverImage = ImageIO.read(GamePanel.class.getResource("gameover.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 画背景图片、玩家飞机、导弹、敌机、记分牌和游戏状态
     */
    public void paint(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, null); // 画背景图片
        paintPlayer(g); // 画玩家飞机
        paintAmmo(g); // 画导弹
        paintFlyModel(g); // 画会飞的模型
        paintScores(g); // 画分数
        paintGameState(g); // 画游戏状态
    }

    /**
     * 画玩家飞机
     */
    public void paintPlayer(Graphics g) {
        g.drawImage(player.getImage(), player.getX(), player.getY(), null);
    }

    /**
     * 画导弹
     */
    public void paintAmmo(Graphics g) {
        for (int i = 0; i < ammos.length; i++) {
            Ammo a = ammos[i];
            g.drawImage(a.getImage(), a.getX() - a.getWidth() / 2, a.getY(), null);
        }
    }

    /**
     * 画会飞的模型
     */
    public void paintFlyModel(Graphics g) {
        for (int i = 0; i < flyModels.length; i++) {
            FlyModel f = flyModels[i];
            g.drawImage(f.getImage(), f.getX(), f.getY(), null);
        }
    }

    /**
     * 画分数
     */
    public void paintScores(Graphics g) {
        int x = 10; // 显示分数时的x坐标
        int y = 25; // 显示分数时的y坐标
        Font font = new Font(Font.SANS_SERIF, Font.BOLD, 14); // 字体
        g.setColor(Color.YELLOW); // 字体颜色
        g.setFont(font); // 设置字体
        g.drawString("SCORE:" + scores, x, y); // 画分数
        y += 20; // y坐标增20
        g.drawString("LIFE:" + player.getLifeNumbers(), x, y); // 画玩家飞机的生命数
    }

    /**
     * 画游戏状态
     */
    public void paintGameState(Graphics g) {
        switch (state) {
            case START: // 游戏开始
                g.drawImage(startImage, 0, 0, null);
                break;
            case OVER: // 游戏结束
                g.drawImage(gameoverImage, 0, 0, null);
                break;
        }
    }

    /**
     * 游戏面板对象加载会飞的模型 
     */
    public void load() {
        // 鼠标监听事件
        MouseAdapter mouseAdapter = new MouseAdapter() {
            public void mouseMoved(MouseEvent e) { // 移动鼠标
                if (state == RUNNING) { // 运行状态下，使得玩家飞机随鼠标位置移动
                    int x = e.getX();
                    int y = e.getY();
                    player.updateXY(x, y);
                }
            }

            public void mouseClicked(MouseEvent e) { // 单击鼠标
                switch (state) {
                    case START:
                        state = RUNNING; // 启动状态下运行
                        break;
                    case OVER: // 游戏结束，清理游戏面板
                        flyModels = new FlyModel[0]; // 清空会飞的模型
                        ammos = new Ammo[0]; // 清空导弹
                        player = new Player(); // 重新创建玩家飞机
                        scores = 0; // 清空分数
                        state = START; // 重置游戏状态为“游戏开始”
                        break;
                }
            }
        };
        this.addMouseListener(mouseAdapter); // 单击鼠标时执行的操作
        this.addMouseMotionListener(mouseAdapter); // 移动鼠标时执行的操作

        timer = new Timer(); // 新建定时器对象
        timer.schedule(new TimerTask() { // 游戏开始时的动画设计过程
            public void run() {
                if (state == RUNNING) { // 游戏正在运行
                    flyModelsEnter(); // 空投物资或者敌机入场
                    step(); // 敌机、空投物资、导弹和玩家飞机开始移动
                    fire(); // 玩家飞机发射导弹
                    hitFlyModel(); // 导弹击打敌机或者空投物资
                    delete(); // 删除移动到游戏面板外的敌机、空投物资和导弹
                    overOrNot(); // 判断游戏是否结束
                }
                repaint(); // 重绘，调用paint()方法
            }

        }, intervel, intervel);
    }

    int flyModelsIndex = 0; // 初始化会飞的模型的入场时间

    /**
     * 空投物资或者敌机入场
     */
    public void flyModelsEnter() {
        flyModelsIndex++;
        if (flyModelsIndex % 40 == 0) { // 每隔400毫秒（10*40）生成一个会飞的模型
            FlyModel obj = nextOne(); // 随机生成一个空投物资或者敌机
            flyModels = (FlyModel[]) Arrays.copyOf(flyModels, flyModels.length + 1);
            flyModels[flyModels.length - 1] = obj;
        }
    }


    /**
     * 随机生成一个空投物资或者敌机
     * @return 一个空投物资或者敌机
     */
    public static FlyModel nextOne() {
        Random random = new Random();
        int type = random.nextInt(20); // [0,20)
        if (type == 0) {
            return new Airdrop(); // 空投物资
        } else {
            return new Enemy(); // 敌机
        }
    }

    /**
     * 敌机、空投物资、导弹和玩家飞机开始移动
     */
    public void step() {
        for (int i = 0; i < flyModels.length; i++) { // 敌机、空投物资开始移动
            FlyModel f = flyModels[i];
            f.move();;
        }

        for (int i = 0; i < ammos.length; i++) { // 导弹开始移动
            Ammo b = ammos[i];
            b.move();
        }
        player.move(); // 玩家飞机开始移动
    }

    int fireIndex = 0; // 初始化玩家飞机发射导弹的时间

    /**
     * 玩家飞机发射导弹
     */
    public void fire() {
        fireIndex++;
        if (fireIndex % 30 == 0) { // 每300毫秒发一枚导弹
            Ammo[] as = player.fireAmmo(); // 玩家飞机发射导弹
            ammos = (Ammo[]) Arrays.copyOf(ammos, ammos.length + as.length);
            System.arraycopy(as, 0, ammos, ammos.length - as.length, as.length);
        }
    }

    /**
     * 导弹击打敌机或者空投物资
     */
    public void hitFlyModel() {
        for (int i = 0; i < ammos.length; i++) { // 遍历所有导弹
            Ammo aos = ammos[i];
            bingoOrNot(aos); // 导弹是否击中敌机或者空投物资
        }
    }

    /**
     * 导弹是否击中敌机或者空投物资
     */
    public void bingoOrNot(Ammo ammo) {
        int index = -1; // 击中的敌机或者空投物资的索引
        for (int i = 0; i < flyModels.length; i++) {
            FlyModel obj = flyModels[i];
            if (obj.shootBy(ammo)) { // 判断是否击中
                index = i; // 记录被击中的敌机或者空投物资的索引
                break;
            }
        }
        if (index != -1) { // 敌机或者空投物资被击中
            FlyModel one = flyModels[index]; // 记录被击中的敌机或者空投物资
            // 被击中的飞行物与最后一个飞行物交换
            FlyModel temp = flyModels[index];
            flyModels[index] = flyModels[flyModels.length - 1];
            flyModels[flyModels.length - 1] = temp;

            flyModels = (FlyModel[]) Arrays.copyOf(flyModels, flyModels.length - 1); // 删除最后一个飞行物(即被击中的)

            // 检查one的类型
            if (one instanceof Hit) { // 如果是敌机，则加分
                Hit e = (Hit) one; // 强制类型转换
                scores += e.getScores(); // 加分
            } else { // 如果是空投物资
                player.fireDoubleAmmos(); // 设置双倍火力
            }
        }
    }

    /**
     * 删除移动到游戏面板外的敌机、空投物资和导弹
     */
    public void delete() {
        int index = 0; // 索引
        FlyModel[] flyingLives = new FlyModel[flyModels.length]; // 用于存储未被击中的敌机和空投物资
        for (int i = 0; i < flyModels.length; i++) {
            FlyModel f = flyModels[i];
            if (!f.outOfPanel()) {
                flyingLives[index++] = f;  // 把没有移动到游戏面板外的敌机、空投物资储存在flyingLives中
            }
        }
        flyModels = (FlyModel[]) Arrays.copyOf(flyingLives, index); // 将不越界的飞行物都留着

        index = 0; // 索引重置为0
        Ammo[] ammoLives = new Ammo[ammos.length]; // 用于存储游戏面板内的导弹
        for (int i = 0; i < ammos.length; i++) {
            Ammo ao = ammos[i];
            if (!ao.outOfPanel()) {
                ammoLives[index++] = ao; // 把没有移动到游戏面板外的导弹储存在ammoLives中
            }
        }
        ammos = (Ammo[]) Arrays.copyOf(ammoLives, index); // 将不越界的子弹留着
    }

    /**
     * 判断游戏是否结束
     */
    public void overOrNot() {
        if (isOver()) { // 游戏结束
            state = OVER; // 改变状态
        }
    }

    /**
     * 游戏结束 
     */
    public boolean isOver() {
        for (int i = 0; i < flyModels.length; i++) {
            int index = -1; // 初始化与玩家飞机发生碰撞的敌机索引
            FlyModel obj = flyModels[i];
            if (player.hit(obj)) { // 如果玩家飞机与敌机碰撞
                player.loseLifeNumbers(); // 玩家飞机减命
                index = i; // 记录与玩家飞机发生碰撞的敌机索引
            }
            if (index != -1) { // 发生碰撞后
                // 交换已发生碰撞的敌机
                FlyModel t = flyModels[index];
                flyModels[index] = flyModels[flyModels.length - 1];
                flyModels[flyModels.length - 1] = t;

                flyModels = (FlyModel[]) Arrays.copyOf(flyModels, flyModels.length - 1); // 删除碰上的飞行物
            }
        }

        return player.getLifeNumbers() <= 0;
    }

    // 程序的入口
    public static void main(String[] args) {
        JFrame frame = new JFrame("飞机大战"); // 新建标题为“飞机大战”的窗体对象
        GamePanel gamePanel = new GamePanel(); // 新建游戏面板
        frame.add(gamePanel); // 把游戏面板添加到窗体中
        frame.setSize(WIDTH, HEIGHT); // 设置窗体的宽和高
        frame.setAlwaysOnTop(true); // 设置窗体在其他窗口上方
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 设置窗体的关闭方式
        frame.setLocationRelativeTo(null); // 设置窗体显示在屏幕中央
        frame.setVisible(true); // 设置窗体可见
        gamePanel.load(); // 游戏面板对象加载会飞的模型
    }
}