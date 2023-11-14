import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

class Point {
    int x, y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}

class Snake {
    LinkedList<Point> body;
    char direction;

    public Snake() {
        body = new LinkedList<>();
        body.add(new Point(0, 0)); // Initial position of the snake
        direction = 'R'; // Initial direction (Right)
    }

    public void move() {
        Point head = body.getFirst();
        Point newHead;

        switch (direction) {
            case 'w':
                newHead = new Point(head.x - 1, head.y);
                break;
            case 's':
                newHead = new Point(head.x + 1, head.y);
                break;
            case 'a':
                newHead = new Point(head.x, head.y - 1);
                break;
            case 'd':
                newHead = new Point(head.x, head.y + 1);
                break;
            default:
                throw new IllegalArgumentException("Invalid direction");
        }

        body.addFirst(newHead);

        // Remove the tail
        body.removeLast();
    }

    public void grow() {
        // Snake grows by adding a new segment
        Point tail = body.getLast();
        body.addLast(new Point(tail.x, tail.y));
    }

    public boolean checkCollision() {
        Point head = body.getFirst();

        // Check if the head collides with the body
        for (int i = 1; i < body.size(); i++) {
            if (head.x == body.get(i).x && head.y == body.get(i).y) {
                return true;
            }
            if(head.x<0 || head.x>=10 || head.y<0 || head.y>=10) {
                return true;
            }
        }

        return false;
    }
}

class Food {
    Point position;

    public Food() {
        position = new Point(0, 0); // Initial position of the food
    }

    public void generateFood(LinkedList<Point> snakeBody) {
        Random rand = new Random();

        do {
            position.x = rand.nextInt(10);
            position.y = rand.nextInt(10);
        } while (isFoodOnSnake(snakeBody));
    }

    private boolean isFoodOnSnake(LinkedList<Point> snakeBody) {
        for (Point snakeSegment : snakeBody) {
            if (position.x == snakeSegment.x && position.y == snakeSegment.y) {
                return true;
            }
        }
        return false;
    }
}

class Game {
    Snake snake;
    Food food;
    char[][] board;
    int score;

    public Game() {
        snake = new Snake();
        food = new Food();
        food.generateFood(snake.body);
        board = new char[10][10];
        initializeBoard();
        score = 0;
    }

    private void initializeBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                board[i][j] = '.';
            }
        }

        // Place snake on the board
        for (Point point : snake.body) {
            board[point.x][point.y] = 'O';
        }

        // Place food on the board
        board[food.position.x][food.position.y] = '*';
    }

    public void printBoard() {
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public void play() {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            printBoard();

            char input = scanner.next().charAt(0);

            if (input == 'q') {
                System.out.println("Game over!");
                break;
            }

            snake.direction = input;
            snake.move();

            if (snake.checkCollision()) {
                System.out.println("Collision! Game over!");
                break;
            }

            Point head = snake.body.getFirst();

            // Check if the snake eats the food
            if (head.x == food.position.x && head.y == food.position.y) {
                snake.grow();
                food.generateFood(snake.body);
                score++; // Increment the score
            }

            // Update the board
            initializeBoard();
        }

        System.out.println("Your final score: " + score);
        scanner.close();
    }
}

public class SnakeAndFoodGame {
    public static void main(String[] args) {
        Game game = new Game();
        System.out.println("Game Started");
        System.out.println("Use the controls for playing the game");
        System.out.println("       w(up)\na(left)      d(right)\n       s(down)");
        game.play();
    }
}
