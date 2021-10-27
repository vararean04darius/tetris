package Tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class GameForm extends JFrame {
    private JPanel gameAreaPlaceholder;
    private JLabel levelDisplay;
    private JLabel scoreDisplay;
    protected GameArea ga;
    public GameForm()
    {
        this.setBackground(Color.black);
        ga = new GameArea(gameAreaPlaceholder, 10);
        this.add(ga);
        ga.setBounds(165,190,200,200);
        gameAreaPlaceholder.setSize(400,200);
        gameAreaPlaceholder.setLocation(500,200);
        this.add(levelDisplay);
        this.add(scoreDisplay);
        ga.setSize(165,190);
        levelDisplay.setSize(50,50);
        scoreDisplay.setSize(50,50);
        initControls();

        startGame(ga);
        scoreDisplay.setVisible(true);
        levelDisplay.setVisible(true);
        gameAreaPlaceholder.setVisible(true);
        this.pack();
    }


    private void initControls()
    {
        InputMap im = this.getRootPane().getInputMap();
        ActionMap am = this.getRootPane().getActionMap();

        im.put(KeyStroke.getKeyStroke("RIGHT"), "right");
        im.put(KeyStroke.getKeyStroke("LEFT"), "left");
        im.put(KeyStroke.getKeyStroke("UP"), "up");
        im.put(KeyStroke.getKeyStroke("DOWN"), "down");

        am.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.moveBlockRight();
            }
        });
        am.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.moveBlockLeft();
            }
        });
        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.rotateBlock();
            }
        });
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ga.dropBlock();
            }
        });

    }
    public void startGame(GameArea ga)
    {
        new GameThread(ga, this).start();
    }
    public void updateScore(int score)
    {
        scoreDisplay.setText("Score: " + score);
    }
    public void updateLevel(int level)
    {
        levelDisplay.setText("Level: " + level);
    }

    public static void main(String[] args) {
        new GameForm().setVisible(true);
    }
}
