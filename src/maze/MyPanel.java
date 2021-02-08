package maze;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MyPanel extends JPanel {
    private Maze maze;
    private boolean[][] board;
    private int frame_size_x, frame_size_y;
    private int scale;
    private boolean save_frames = false;
    private int delay = 10;
    private int solution_delay = 1;

    public MyPanel(Maze maze, int scale) {
        super();
        this.maze = maze;
        this.scale = scale/2;
        setBackground(Color.BLACK);
    }


    public void paint(Graphics g) {
        super.paint(g);

        //g.fillRect(position[0], position[1], scale, scale);
        /*
        board = maze.get_board();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j]) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(i*scale, j*scale, scale, scale);
            }
        }
        */

    }

    public void paint_whole(Graphics g) {
        board = maze.get_board();

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j]) {
                    g.setColor(Color.WHITE);
                } else {
                    g.setColor(Color.BLACK);
                }
                g.fillRect(i*scale, j*scale, scale, scale);
            }
        }
    }


    public void paint_generations(Graphics g) throws InterruptedException, IOException {
        board = maze.get_board();

        g.setColor(Color.RED);

        int[][] positions = maze.generate_frame();

        for (int[] position: positions) {
            g.fillRect(scale * (position[0] * 2 + 1), scale * (position[1] * 2 + 1), scale, scale);
        }
        Thread.sleep(10);
        //repaint();

        int frame = 0;


        while(!maze.is_generated()) {

            int[][] previous = copy(positions);
            positions = maze.generate_frame();

            for (int i = 0; i < positions.length; i++) {
                g.setColor(Color.RED);
                //position = maze.generate_frame();
                g.fillRect(scale * (positions[i][0] * 2 + 1), scale * (positions[i][1] * 2 + 1), scale, scale);
            }
            for (int i = 0; i < previous.length; i++) {
                g.setColor(Color.WHITE);
                g.fillRect(scale * (previous[i][0] * 2 + 1), scale * (previous[i][1] * 2 + 1), scale, scale);
                //g.fillRect(scale * (previous[i][0] * 2 + 1 + positions[i][0] * 2 + 1) / 2, scale * (previous[i][1] * 2 + 1 + positions[i][1] * 2 + 1) / 2, scale, scale);
            }

            ArrayList<int[]> walls = maze.get_walls();
            for (int[] wall: walls) {
                g.fillRect(scale*wall[0], scale*wall[1], scale, scale);
            }
            Thread.sleep(delay);
            //repaint();

            if (save_frames) {
                frame++;
                String pathname = "frames/";

                for (int i = 0; i < 6 - Math.log10(frame); i++) {
                    pathname += "0";
                }
                pathname += frame;
                pathname += ".bmp";

                File f = new File(pathname);
                ImageIO.write(maze.get_image(), "bmp", f);
            }
        }

        ArrayList<Node> solution = maze.get_solution();

        g.setColor(Color.GREEN);
        g.fillRect(scale*(solution.get(0).get_x() * 2 + 1), scale*(solution.get(0).get_y() * 2 + 1), scale, scale);

        for (int i = 1; i < maze.get_solution().size(); i++) {
            g.fillRect(scale*(solution.get(i - 1).get_x() * 2 + 1), scale*(solution.get(i - 1).get_y() * 2 + 1), scale, scale);
            g.fillRect(scale*(solution.get(i - 1).get_x() * 2 + 1 + solution.get(i).get_x() * 2 + 1)/2, scale*(solution.get(i - 1).get_y() * 2 + 1 + solution.get(i).get_y() * 2 + 1)/2, scale, scale);
            Thread.sleep(solution_delay);

            if (save_frames) {
                frame++;
                String pathname = "frames/";

                for (int j = 0; j < 6 - Math.log10(frame); j++) {
                    pathname += "0";
                }
                pathname += frame;
                pathname += ".bmp";

                File f = new File(pathname);
                BufferedImage im = maze.get_image();

                for (int j = 1; j <= i; j++) {
                    for (int k = 0; k < maze.res; k++) {
                        for (int l = 0; l < maze.res; l++) {
                            im.setRGB(k, maze.res + l, Color.GREEN.getRGB());
                            im.setRGB(maze.res * (solution.get(j - 1).get_x() * 2 + 1) + k, maze.res * (solution.get(j - 1).get_y() * 2 + 1) + l, Color.GREEN.getRGB());
                            im.setRGB(maze.res * (solution.get(j - 1).get_x() * 2 + 1 + solution.get(j).get_x() * 2 + 1) / 2 + k, maze.res * (solution.get(j - 1).get_y() * 2 + 1 + solution.get(j).get_y() * 2 + 1) / 2 + l, Color.GREEN.getRGB());
                        }
                    }
                }

                ImageIO.write(im, "bmp", f);
            }
        }
        g.fillRect(scale*(solution.get(solution.size()-1).get_x() * 2 + 1), scale*(solution.get(solution.size() - 1).get_y() * 2 + 1), scale, scale);

        if (save_frames) {
            for (int u = 0; u < 60; u++) {
                frame++;
                String pathname = "frames/";

                for (int j = 0; j < 6 - Math.log10(frame); j++) {
                    pathname += "0";
                }
                pathname += frame;
                pathname += ".bmp";

                File f = new File(pathname);
                BufferedImage im = maze.get_image();

                for (int j = 1; j < maze.get_solution().size(); j++) {
                    for (int k = 0; k < maze.res; k++) {
                        for (int l = 0; l < maze.res; l++) {
                            im.setRGB(k, maze.res + l, Color.GREEN.getRGB());
                            im.setRGB(maze.res * (solution.get(j - 1).get_x() * 2 + 1) + k, maze.res * (solution.get(j - 1).get_y() * 2 + 1) + l, Color.GREEN.getRGB());
                            im.setRGB(maze.res * (solution.get(j - 1).get_x() * 2 + 1 + solution.get(j).get_x() * 2 + 1) / 2 + k, maze.res * (solution.get(j - 1).get_y() * 2 + 1 + solution.get(j).get_y() * 2 + 1) / 2 + l, Color.GREEN.getRGB());
                        }
                    }
                }

                for (int k = 0; k < maze.res; k++) {
                    for (int l = 0; l < maze.res; l++) {
                        im.setRGB(maze.res * (maze.get_board().length - 1) + k, maze.res * (maze.get_board()[0].length - 2) + l, Color.GREEN.getRGB());
                        im.setRGB(maze.res * (maze.get_board().length - 2) + k, maze.res * (maze.get_board()[0].length - 2) + l, Color.GREEN.getRGB());
                    }
                }

                ImageIO.write(im, "bmp", f);
            }
            System.out.println("all is done now");
        }

    }

    private int[] copy(int[] other) {
        int[] out = new int[other.length];

        for (int i = 0; i < other.length; i++) {
            out[i] = other[i];
        }

        return out;
    }

    private int[][] copy(int[][] other) {
        int[][] out = new int[other.length][other[0].length];

        for (int i = 0; i < other.length; i++) {
            for (int j = 0; j < other[0].length; j++) {
                out[i][j] = other[i][j];
            }
        }
        return out;
    }

}
