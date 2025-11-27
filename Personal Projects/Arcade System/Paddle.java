import java.awt.Color;
import java.awt.Rectangle;

public class Paddle extends Rectangle {

    Color color;
    int yVelocity;

    Paddle(int x, int y, int width, int height, Color color) {
        super(x, y, width, height);
        this.color = color;
    }

    public void yDirection(int Direction) {
        yVelocity = Direction;
    }

    public void movePaddle() {
        y += yVelocity;
    }

}