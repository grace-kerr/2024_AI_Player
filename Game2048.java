import java.util.Random;
import java.util.Scanner;

public class Game2048 {
  private int[][] board;
  private int score;
  private boolean gameOver;
  private Random random;

  public Game2048() {
    board = new int[4][4];
    score = 0;
    gameOver = false;
    random = new Random();
    addNewTile();
    addNewTile();
  }

  private void addNewTile() {
    int value = (random.nextInt(2) + 1) * 2; // Generate 2 or 4 randomly
    int row, col;

    do {
      row = random.nextInt(4);
      col = random.nextInt(4);
    } while (board[row][col] != 0);

    board[row][col] = value;
  }

  private void printBoard() {
    System.out.println("Score: " + score);
    for (int[] row : board) {
      for (int cell : row) {
        System.out.print(cell + "\t");
      }
      System.out.println();
    }
    System.out.println();
  }

  private void moveTilesLeft() {
    for (int[] row : board) {
      row = mergeTiles(row);
    }
  }

  private void moveTilesRight() {
    for (int i = 0; i < 4; i++) {
      int[] row = new int[4];
      for (int j = 0; j < 4; j++) {
        row[j] = board[i][3 - j];
      }
      row = mergeTiles(row);
      for (int j = 0; j < 4; j++) {
        board[i][3 - j] = row[j];
      }
    }
  }

  private void moveTilesUp() {
    for (int j = 0; j < 4; j++) {
      int[] col = new int[4];
      for (int i = 0; i < 4; i++) {
        col[i] = board[i][j];
      }
      col = mergeTiles(col);
      for (int i = 0; i < 4; i++) {
        board[i][j] = col[i];
      }
    }
  }

  private void moveTilesDown() {
    for (int j = 0; j < 4; j++) {
      int[] col = new int[4];
      for (int i = 0; i < 4; i++) {
        col[i] = board[3 - i][j];
      }
      col = mergeTiles(col);
      for (int i = 0; i < 4; i++) {
        board[3 - i][j] = col[i];
      }
    }
  }

  private int[] mergeTiles(int[] line) {
    int[] result = new int[4];
    int index = 0;

    for (int i = 0; i < 4; i++) {
      if (line[i] != 0) {
        if (result[index] == 0) {
          result[index] = line[i];
        } else if (result[index] == line[i]) {
          result[index] *= 2;
          score += result[index];
          index++;
        } else {
          index++;
          result[index] = line[i];
        }
      }
    }

    return result;
  }

  private boolean isGameOver() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (board[i][j] == 0
            || (i > 0 && board[i][j] == board[i - 1][j])
            || (j > 0 && board[i][j] == board[i][j - 1])) {
          return false;
        }
      }
    }
    return true;
  }

  public void play() {
    Scanner scanner = new Scanner(System.in);
    while (!gameOver) {
      printBoard();

      System.out.print("Enter move (W/A/S/D): ");
      String move = AIPlayer.turn();
      move = move.toUpperCase();
      System.out.println(move);
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {

      }

      switch (move) {
        case "W":
          moveTilesUp();
          break;
        case "A":
          moveTilesLeft();
          break;
        case "S":
          moveTilesDown();
          break;
        case "D":
          moveTilesRight();
          break;
        default:
          System.out.println("Invalid move. Please enter W/A/S/D.");
          continue;
      }

      addNewTile();
      gameOver = isGameOver();
    }

    printBoard();
    System.out.println("Game Over! Your score: " + score);
    scanner.close();
  }
}
