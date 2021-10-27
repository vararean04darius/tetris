package Tetris;

public class GameThread extends Thread
{
    private GameArea ga;
    private GameForm gf;
    private int score;
    private int level = 1;
    private int scoreperlevel = 3;

    private int pause = 1000;
    private int speedupPerLevel = 50;

    public GameThread(GameArea ga, GameForm gf)
    {
        this.ga = ga;
        this.gf = gf;
    }
    @Override
    public void run()
    {
        while(true)
        {
            ga.spawnBlock();

            while(ga.moveBlockDown())
            {
                try {
                    Thread.sleep(pause);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if(ga.isBlockOutOfBounds())
            {
                System.out.println("Game over");
                break;
            }

            ga.moveBlocktoBackground();
            score +=ga.clearLines();
            gf.updateScore(score);

            int lvl = score / scoreperlevel + 1;
            if(lvl>level)
            {
                level = lvl;
                gf.updateLevel(level);
                pause-=speedupPerLevel;
            }
        }
    }

}
