import java.util.ArrayList;
import java.util.List;

public class Graph {
    private char[][] maze;
    private Node startNode;
    private List<Node> path; // Lưu đường đi thoát
    private int canExit=-1;

    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";  // Màu xanh lá cho đường đi (0)
    public static final String ANSI_RESET = "\u001B[0m";

    public Graph(char[][] maze) {
        this.maze = maze;
        path = new ArrayList<>();
        InitStartNode();
    }

    // Xác định vị trí bắt đầu trong mê cung (kí hiệu X)
    private void InitStartNode() {
        for (int i = 0; i < maze.length; i++) {
            for (int j = 0; j < maze[0].length; j++) {
                if (maze[i][j] == 'X') {
                    startNode = new Node(i, j);
                    break;
                }
            }
        }
    }

    // Hàm kiểm tra xem điểm hiện tại có nằm ở biên và là điểm thoát hợp lệ không
    private boolean isExit(Node node) {
        int x = node.getX(), y = node.getY();
        return (x == 0 || x == maze.length - 1 || y == 0 || y == maze[0].length - 1) && maze[x][y] == '0';
    }

    // Hàm BFS để tìm đường thoát và truy vết lại đường đi
    public boolean findEscapePath() {
        if(canExit!=-1){
            printMaze();
            if(canExit==0){
                System.out.println("Không tìm thấy đường thoát.");
                return false;
            }
            return true;
        }
        Queue queue = new Queue();
        queue.enqueue(startNode);

        boolean[][] visited = new boolean[maze.length][maze[0].length];

        visited[startNode.getX()][startNode.getY()] = true;

        Node[][] last = new Node[maze.length][maze[0].length]; // Lưu lại bước trước đó để truy vết

        while (!queue.isEmpty()) {
            Node current = queue.dequeue();

            // Kiểm tra nếu đã đến điểm thoát
            if (isExit(current)) {
                tracePath(current, last); // Truy vết đường đi
                printMaze(); // In mê cung với đường thoát
                canExit=1;
                return true;
            }

            // Duyệt các ô lân cận
            for (Node neighbor : getNeighbors(current)) {
                int nx = neighbor.getX();
                int ny = neighbor.getY();
                if (!visited[nx][ny] && maze[nx][ny] == '0') {
                    queue.enqueue(neighbor);
                    visited[nx][ny] = true;
                    last[nx][ny] = current; // Đánh dấu ô trước đó để truy vết
                }
            }
        }
        printMaze();
        System.out.println("Không tìm thấy đường thoát.");
        canExit=0;
        return false;
    }

    // Hàm truy vết đường đi từ điểm thoát đến điểm bắt đầu và đánh dấu bằng ký tự 'E'
    private void tracePath(Node exitNode, Node[][] last) {
        Node current = exitNode;
        while (current != startNode) {
            maze[current.getX()][current.getY()] = 'E';
            current = last[current.getX()][current.getY()];
        }
    }

    // Hàm lấy các ô lân cận có thể đi đến
    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<>();
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        for (int[] d : directions) {
            int newX = node.getX() + d[0];
            int newY = node.getY() + d[1];
            if (isInBounds(newX, newY)) {
                neighbors.add(new Node(newX, newY));
            }
        }
        return neighbors;
    }

    // Kiểm tra xem ô có nằm trong phạm vi ma trận hay không
    private boolean isInBounds(int x, int y) {
        return x >= 0 && x < maze.length && y >= 0 && y < maze[0].length;
    }

    // Hàm in mê cung để xem kết quả sau khi tìm đường
    public void printMaze() {
        for (char[] row : maze) {
            for (char cell : row) {
                if (cell == 'E' || cell == 'X') {
                    System.out.print(ANSI_RED + cell + ANSI_RESET + " ");
                } else if (cell == '0') {
                    System.out.print(ANSI_GREEN + cell + ANSI_RESET + " ");
                } else {
                    System.out.print(cell + " ");
                }
            }
            System.out.println();
        }
    }
}
