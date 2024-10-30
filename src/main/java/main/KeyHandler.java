package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyHandler implements KeyListener {

    public KeyHandler() {

    }

    public boolean movePressed;
    public boolean upPressed, downPressed, leftPressed, rightPressed;
    public boolean enterPressed;
    public boolean activateBomb;

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                upPressed = true;
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                downPressed = true;
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = true;
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = true;
                break;

            case KeyEvent.VK_ENTER:
                enterPressed = true;
                break;

            case KeyEvent.VK_R:
            case KeyEvent.VK_SPACE:
                activateBomb = true;
        }

        movePressed = upPressed || downPressed || leftPressed || rightPressed;
    }

    @Override
    public void keyReleased(KeyEvent e) {

        int code = e.getKeyCode();

        switch (code) {
            case KeyEvent.VK_W:
            case KeyEvent.VK_UP:
                upPressed = false;
                break;

            case KeyEvent.VK_S:
            case KeyEvent.VK_DOWN:
                downPressed = false;
                break;

            case KeyEvent.VK_A:
            case KeyEvent.VK_LEFT:
                leftPressed = false;
                break;

            case KeyEvent.VK_D:
            case KeyEvent.VK_RIGHT:
                rightPressed = false;
                break;

            case KeyEvent.VK_ENTER:
                enterPressed = false;
                break;

            case KeyEvent.VK_R:
            case KeyEvent.VK_SPACE:
                activateBomb = false;
        }

        movePressed = upPressed || downPressed || leftPressed || rightPressed;
    }

}