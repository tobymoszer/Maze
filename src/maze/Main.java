package maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.File;
import java.io.IOException;

public class Main {
    private static int size_x = 100;
    private static int size_y = 50;
    private static int scale = 32;
    private static boolean animate = true;

    public static void main(String[] args) throws InterruptedException, IOException {

	    JFrame frame = new JFrame("maze");

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setResizable(false);

	    frame.setSize(scale*size_x + scale/2, scale*size_y + 57 + scale/2);



        Maze maze = new Maze(size_x, size_y);
        if (!animate) {
            maze.generate();
        }

	    MyPanel panel = new MyPanel(maze, scale);

	    frame.add(panel);
        frame.setVisible(true);

        Thread.sleep(300);
        panel.repaint();

        if (animate) {
            panel.paint_generations(panel.getGraphics());
        } else {
            panel.paint_whole(panel.getGraphics());
        }
        frame.setTitle("maze done");

        File f = new File("maze.bmp");
        ImageIO.write(maze.get_image(), "bmp", f);

        f = new File("solved.bmp");
        ImageIO.write(maze.get_solved_image(), "bmp", f);
    }
}
