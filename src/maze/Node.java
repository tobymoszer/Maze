package maze;

public class Node {
    private boolean visited;
    private int x, y;

    public Node(int x, int y) {
        visited = false;
        this.x = x;
        this.y = y;
    }

    public Node(int x, int y, boolean visited) {
        this.x = x;
        this.y = y;
        this.visited = visited;
    }

    public void visit() {
        visited = true;
    }

    public boolean visited() {
        return visited;
    }

    public int get_x() {
        return x;
    }

    public int get_y() {
        return y;
    }

    public Node copy() {
        return new Node(x, y, visited);
    }
}
