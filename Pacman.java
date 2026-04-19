import java.util.*;

// MAIN CLASS
public class Pacman {
    public static void main(String[] args) {
        GameBoard board = new GameBoard(10, 10);
        Player pacman = new Player(1, 1);
        Ghost ghost = new Ghost(8, 8);
        Food food = new Food();

        board.initialize(food);

        Scanner sc = new Scanner(System.in);

        while (true) {
            board.display(pacman, ghost, food);

            System.out.print("Move (>, <, ^, v): ");
            char move = sc.next().charAt(0);

            pacman.move(move, board);

            // Eat food
            if (food.eatFood(pacman.getX(), pacman.getY())) {
                pacman.increaseScore();
            }

            // Move ghost
            ghost.move(board);

            // Lose condition
            if (pacman.getX() == ghost.getX() && pacman.getY() == ghost.getY()) {
                System.out.println("Game Over! Ghost caught you!");
                break;
            }

            // Win condition
            if (food.isAllEaten()) {
                System.out.println("You Win! Score: " + pacman.getScore());
                break;
            }
        }

        sc.close();
    }
}


// GAME BOARD CLASS
class GameBoard {
    private int rows, cols;
    private char[][] board;

    public GameBoard(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new char[rows][cols];
    }

    public void initialize(Food food) {
        String[] layout = {
            "##########",
            "#........#",
            "#.####...#",
            "#.#..#.#.#",
            "#.#..#.#.#",
            "#.####.#.#",
            "#........#",
            "#.######.#",
            "#........#",
            "##########"
        };

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                char ch = layout[i].charAt(j);
                board[i][j] = ch;

                if (ch == '.') {
                    food.addFood(i, j);
                }
            }
        }
    }

    public void display(Player pacman, Ghost ghost, Food food) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (i == pacman.getX() && j == pacman.getY()) {
                    System.out.print('P');
                } 
                else if (i == ghost.getX() && j == ghost.getY()) {
                    System.out.print('G');
                } 
                else if (board[i][j] == '.' && !food.hasFood(i, j)) {
                    System.out.print(' ');
                } 
                else {
                    System.out.print(board[i][j]);
                }
            }
            System.out.println();
        }
        System.out.println("Score: " + pacman.getScore());
    }

    public boolean isWall(int x, int y) {
        return board[x][y] == '#';
    }
}


// PLAYER (PACMAN) CLASS
class Player {
    private int x, y;
    private int score;

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        this.score = 0;
    }

    public void move(char direction, GameBoard board) {
        int newX = x, newY = y;

        if (direction == '>') newY++;
        else if (direction == '<') newY--;
        else if (direction == '^') newX--;
        else if (direction == 'v') newX++;

        if (!board.isWall(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public void increaseScore() {
        score++;
    }

    public int getScore() {
        return score;
    }

    public int getX() { return x; }
    public int getY() { return y; }
}


// GHOST CLASS
class Ghost {
    private int x, y;
    private Random rand = new Random();

    public Ghost(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void move(GameBoard board) {
        int[] dx = {0, 0, -1, 1};
        int[] dy = {1, -1, 0, 0};

        int dir = rand.nextInt(4);

        int newX = x + dx[dir];
        int newY = y + dy[dir];

        if (!board.isWall(newX, newY)) {
            x = newX;
            y = newY;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}


// FOOD CLASS
class Food {
    private Set<String> foodPositions = new HashSet<>();

    public void addFood(int x, int y) {
        foodPositions.add(x + "," + y);
    }

    public boolean eatFood(int x, int y) {
        return foodPositions.remove(x + "," + y);
    }

    public boolean hasFood(int x, int y) {
        return foodPositions.contains(x + "," + y);
    }

    public boolean isAllEaten() {
        return foodPositions.isEmpty();
    }
}
