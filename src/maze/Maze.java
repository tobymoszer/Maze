package maze;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

public class Maze {
    private int size_x, size_y;
    private Node[][] nodes;
    private boolean[][] board;
    private ArrayList<ArrayList<Node>> stacks;
    private boolean started;
    private int visited_count = 0;
    private double straightness = .5;
    private double split = .01;
    private ArrayList<Node> solution;
    private ArrayList<int[]> walls;
    public int res = 2;
    private int[][] neighbor_moves = new int[][] {
            {0, 1},
            {0, -1},
            {1, 0},
            {-1, 0}
    };



    public Maze(int size_x, int size_y) {
        this.size_x = size_x;
        this.size_y = size_y;
        setup();
    }

    private void setup() {
        nodes = new Node[size_x][size_y];

        shuffle_moves();

        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[0].length; j++) {
                nodes[i][j] = new Node(i, j);
            }
        }
        board = new boolean[size_x*2+1][size_y*2+1];
        /*
        for (int i = 1; i <= size_x; i++) {
            for (int j = 1; j <= size_y; j++) {
                board[2*i-1][2*j-1] = true;
            }
        }
        */

        stacks = new ArrayList<>();
        stacks.add(new ArrayList<Node>(size_x*size_y));
    }

    public void generate() {
        int x, y;
        walls = new ArrayList<>();
        x = 0;
        y = 0;

        nodes[x][y].visit();
        visited_count++;

        stacks.get(0).add(nodes[x][y]);

        int percent = 0;

        while (visited_count < size_x*size_y) {


            if ((int)((double)visited_count/(size_x*size_y)*100) > percent) {
                percent = (int)((double)visited_count/(size_x*size_y)*100);
                System.out.println(percent + "%");
            }





            for (int i = 0; i < stacks.size(); i++) {
                if (Math.random() < split) {

                    ArrayList<Node> stack = copy(stacks.get((int) (Math.random() * stacks.size())));

                    int neighbor_count = 0;
                    for (int[] move : neighbor_moves) {
                        if (stack.get(stack.size()-1).get_x() + move[0] < 0 || stack.get(stack.size()-1).get_x() + move[0] >= nodes.length || stack.get(stack.size()-1).get_y() + move[1] < 0 || stack.get(stack.size()-1).get_y() + move[1] >= nodes[0].length) {

                        } else {
                            if (!nodes[stack.get(stack.size() - 1).get_x() + move[0]][stack.get(stack.size() - 1).get_y() + move[1]].visited()) {
                                neighbor_count++;
                            }
                        }
                    }
                    if (neighbor_count > 1) {
                        stacks.add(stack);
                    }


                }
            }

            for (ArrayList<Node> stack: stacks) {

                x = stack.get(stack.size() - 1).get_x();
                y = stack.get(stack.size() - 1).get_y();

                if (Math.random() < straightness) {
                    shuffle_moves();
                }
                for (int i = 0; i < neighbor_moves.length; i++) {
                    if (x + neighbor_moves[i][0] < 0 || x + neighbor_moves[i][0] >= nodes.length || y + neighbor_moves[i][1] < 0 || y + neighbor_moves[i][1] >= nodes[0].length) {

                    } else {
                        if (!nodes[x + neighbor_moves[i][0]][y + neighbor_moves[i][1]].visited()) {
                            stack.add(nodes[x + neighbor_moves[i][0]][y + neighbor_moves[i][1]]);

                            board[2 * x + 1 + neighbor_moves[i][0]][2 * y + 1 + neighbor_moves[i][1]] = true;

                            x += neighbor_moves[i][0];
                            y += neighbor_moves[i][1];
                            nodes[x][y].visit();
                            visited_count++;
                            break;
                        }
                    }
                    if (i == neighbor_moves.length - 1) {
                        stack.remove(stack.size() - 1);
                        if (stack.size() != 0) {
                            x = stack.get(stack.size() - 1).get_x();
                            y = stack.get(stack.size() - 1).get_y();
                        }
                    }
                }
            }


            ArrayList<ArrayList<Node>> copy = stack_copy(stacks);

            for (ArrayList<Node> stack: copy) {
                if (stack.size() <= 0) {
                    stacks.remove(stack);
                }
            }




            for (int i = 0; i < stacks.size(); i++) {

                if (stacks.get(i).size() == 0) {
                    System.out.println("shouldn't");
                    System.out.println(stacks.size());
                    //stacks.remove(i);
                    System.out.println(stacks.size());
                }

                if (stacks.get(i).get(stacks.get(i).size() - 1).get_x() == size_x - 1 && stacks.get(i).get(stacks.get(i).size() - 1).get_y() == size_y - 1) {
                    solution = arrayListCopy(stacks.get(i));
                }
            }

        }

    }

    public int[][] generate_frame() {
        int x, y;
        walls = new ArrayList<>();
        if (!started) {
            x = 0;
            y = 0;

            nodes[x][y].visit();
            visited_count++;


            stacks.get(0).add(nodes[x][y]);
            started = true;
        } else {
            for (int i = 0; i < stacks.size(); i++) {
                if (Math.random() < split) {

                    ArrayList<Node> stack = copy(stacks.get((int) (Math.random() * stacks.size())));

                    int neighbor_count = 0;
                    for (int[] move : neighbor_moves) {
                        if (stack.get(stack.size()-1).get_x() + move[0] < 0 || stack.get(stack.size()-1).get_x() + move[0] >= nodes.length || stack.get(stack.size()-1).get_y() + move[1] < 0 || stack.get(stack.size()-1).get_y() + move[1] >= nodes[0].length) {

                        } else {
                            if (!nodes[stack.get(stack.size() - 1).get_x() + move[0]][stack.get(stack.size() - 1).get_y() + move[1]].visited()) {
                                neighbor_count++;
                            }
                        }
                    }
                    if (neighbor_count > 1) {
                        stacks.add(stack);
                    }


                }
            }

            walls.clear();

            for (ArrayList<Node> stack: stacks) {

                x = stack.get(stack.size() - 1).get_x();
                y = stack.get(stack.size() - 1).get_y();

                if (Math.random() > straightness) {
                    shuffle_moves();
                } else {
                    if (stack.size() > 1) {
                        int[] move = new int[]{stack.get(stack.size() - 1).get_x() - stack.get(stack.size() - 2).get_x(), stack.get(stack.size() - 1).get_y() - stack.get(stack.size() - 2).get_y()};
                        swap(move);
                    }
                }
                for (int i = 0; i < neighbor_moves.length; i++) {
                    if (x + neighbor_moves[i][0] < 0 || x + neighbor_moves[i][0] >= nodes.length || y + neighbor_moves[i][1] < 0 || y + neighbor_moves[i][1] >= nodes[0].length) {

                    } else {
                        if (!nodes[x + neighbor_moves[i][0]][y + neighbor_moves[i][1]].visited()) {
                            stack.add(nodes[x + neighbor_moves[i][0]][y + neighbor_moves[i][1]]);

                            board[2 * x + 1 + neighbor_moves[i][0]][2 * y + 1 + neighbor_moves[i][1]] = true;
                            board[2 * x + 1][2 * y + 1] = true;
                            walls.add(new int[] {2 * x + 1 + neighbor_moves[i][0], 2 * y + 1 + neighbor_moves[i][1]});

                            x += neighbor_moves[i][0];
                            y += neighbor_moves[i][1];
                            board[2 * x + 1][2 * y + 1] = true;
                            nodes[x][y].visit();
                            visited_count++;
                            break;
                        }
                    }
                    if (i == neighbor_moves.length - 1) {
                        stack.remove(stack.size() - 1);
                        if (stack.size() != 0) {
                            x = stack.get(stack.size() - 1).get_x();
                            y = stack.get(stack.size() - 1).get_y();
                        }
                    }
                }
            }

            ArrayList<ArrayList<Node>> copy = stack_copy(stacks);

            for (ArrayList<Node> stack: copy) {
                if (stack.size() <= 0) {
                    stacks.remove(stack);
                }
            }




            for (int i = 0; i < stacks.size(); i++) {

                if (stacks.get(i).size() == 0) {
                    System.out.println("shouldn't");
                    System.out.println(stacks.size());
                    //stacks.remove(i);
                    System.out.println(stacks.size());
                }

                if (stacks.get(i).get(stacks.get(i).size() - 1).get_x() == size_x - 1 && stacks.get(i).get(stacks.get(i).size() - 1).get_y() == size_y - 1) {
                    solution = arrayListCopy(stacks.get(i));
                }
            }

        }

        int[][] positions = new int[stacks.size()][2];

        for (int i = 0; i < stacks.size(); i++) {
            positions[i][0] = stacks.get(i).get(stacks.get(i).size()-1).get_x();
            positions[i][1] = stacks.get(i).get(stacks.get(i).size()-1).get_y();
        }

        return positions;
    }

    private void swap(int[] move) {
        for (int i = 0; i < neighbor_moves.length; i++) {
            if (neighbor_moves[i][0] == move[0] && neighbor_moves[i][1] == move[1]) {
                int[] temp_move = new int[] {neighbor_moves[0][0], neighbor_moves[0][1]};
                neighbor_moves[0][0] = move[0];
                neighbor_moves[0][1] = move[1];
                neighbor_moves[i][0] = temp_move[0];
                neighbor_moves[i][1] = temp_move[1];
                break;
            }
        }
    }

    public ArrayList<ArrayList<Node>> stack_copy(ArrayList<ArrayList<Node>> other) {
        ArrayList<ArrayList<Node>> out = new ArrayList<>();

        for (ArrayList<Node> l: other) {
            out.add(copy(l));
        }

        return out;
    }

    public ArrayList<Node> copy(ArrayList<Node> other) {
        ArrayList<Node> out = new ArrayList<>();
        for (Node node: other) {
            out.add(node.copy());
        }
        return out;
    }

    public ArrayList<int[]> get_walls() {
        return walls;
    }

    private ArrayList<Node> arrayListCopy(ArrayList<Node> E) {
        ArrayList<Node> copy = new ArrayList<>(E.size());
        for (int i = 0; i < E.size(); i++) {
            copy.add(E.get(i).copy());
        }
        return copy;
    }

    public ArrayList<Node> get_solution() {
        return solution;
    }

    public boolean[][] get_board() {
        return board;
    }

    public int get_size_x() {
        return size_x;
    }

    public int get_size_y() {
        return size_y;
    }

    public void shuffle_moves() {
        List<int[]> shuffled = new ArrayList<>();

        for (int[] move: neighbor_moves) {
            shuffled.add(move);
        }

        Collections.shuffle(shuffled);

        for (int i = 0; i < 4; i++) {
            neighbor_moves[i] = shuffled.get(i);
        }
    }

    public boolean is_generated() {
        /*
        if (visited_count == size_x * size_y) {
            return true;
        }
        return false;
        */
        if (stacks.size() == 0) {
            return true;
        }
        return false;
    }


    public BufferedImage get_image() {
        BufferedImage im = new BufferedImage(board.length*res, board[0].length*res, BufferedImage.TYPE_INT_RGB);

        board[0][1] = true;
        board[board.length-1][board[0].length-2] = true;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j]) {
                    for (int k = 0; k < res; k++) {
                        for (int l = 0; l < res; l++) {
                            im.setRGB(i * res + k, j*res + l, Color.WHITE.getRGB());
                        }
                    }
                } else {
                    //im.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }


        for (int[] position: walls) {
            for (int k = 0; k < res; k++) {
                for (int l = 0; l < res; l++) {
                    im.setRGB(res*position[0]+k, res*position[1]+l, Color.WHITE.getRGB());
                }
            }
        }


        for (ArrayList<Node> stack: stacks) {
            for (int k = 0; k < res; k++) {
                for (int l = 0; l < res; l++) {
                    im.setRGB(res*(stack.get(stack.size()-1).get_x()*2+1)+k, res*(stack.get(stack.size()-1).get_y()*2+1)+l, Color.RED.getRGB());
                }
            }
        }
        return im;
    }

    public BufferedImage get_solved_image() {
        BufferedImage im = new BufferedImage(board.length, board[0].length, BufferedImage.TYPE_INT_RGB);

        int color = 0;

        board[0][1] = true;
        board[board.length-1][board[0].length-2] = true;

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j]) {
                    im.setRGB(i, j, Color.WHITE.getRGB());
                } else {
                    im.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }

        ArrayList<Node> solution = get_solution();

        im.setRGB(solution.get(0).get_x()*2+1, solution.get(0).get_y()*2+1, Color.GREEN.getRGB());

        for (int i = 1; i < solution.size(); i++) {
            im.setRGB(solution.get(i).get_x()*2+1, solution.get(i).get_y()*2+1, Color.GREEN.getRGB());
            im.setRGB((solution.get(i).get_x() * 2 + 1 + solution.get(i-1).get_x() * 2 + 1)/2, (solution.get(i).get_y() * 2 + 1 + solution.get(i-1).get_y() * 2 + 1)/2, Color.GREEN.getRGB());
        }

        im.setRGB(0, 1, Color.GREEN.getRGB());
        im.setRGB(board.length-1, board[0].length-2, Color.GREEN.getRGB());

        return im;
    }
}
