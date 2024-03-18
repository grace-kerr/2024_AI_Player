import java.util.Arrays;
import java.util.Random;

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
  }

  private void addNewTile() {
    int value = (random.nextDouble() < 0.9) ? 2 : 4;
    int row, col;

    do {
      row = random.nextInt(4);
      col = random.nextInt(4);
    } while (board[row][col] != 0);

    board[row][col] = value;
  }

  public void play() {
    while (!gameOver) {
      printBoard();
      String move = getBestMove();
      executeMove(move);
      addNewTile();
      gameOver = isGameOver();
      System.out.println();
    }
    printBoard();
    System.out.println("Game Over! Your score: " + score);
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

  private boolean isGameOver() {
    // Check if the board is full
    if (isBoardFull()) {
      // Check if valid moves are available in any direction
      return !canMoveLeft() && !canMoveRight() && !canMoveUp() && !canMoveDown();
    }
    return false;
  }

  private boolean isBoardFull() {
    for (int[] row : board) {
      for (int cell : row) {
        if (cell == 0) {
          return false;
        }
      }
    }
    return true;
  }

  private boolean canMoveLeft() {
    for (int i = 0; i < 4; i++) {
      for (int j = 1; j < 4; j++) {
        if (board[i][j] == 0 || board[i][j] == board[i][j - 1]) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean canMoveRight() {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 3; j++) {
        if (board[i][j] == 0 || board[i][j] == board[i][j + 1]) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean canMoveUp() {
    for (int j = 0; j < 4; j++) {
      for (int i = 1; i < 4; i++) {
        if (board[i][j] == 0 || board[i][j] == board[i - 1][j]) {
          return true;
        }
      }
    }
    return false;
  }

  private boolean canMoveDown() {
    for (int j = 0; j < 4; j++) {
      for (int i = 0; i < 3; i++) {
        if (board[i][j] == 0 || board[i][j] == board[i + 1][j]) {
          return true;
        }
      }
    }
    return false;
  }

  private void executeMove(String move) {
    switch (move) {
      case "A":
        moveLeft();
        break;
      case "D":
        moveRight();
        break;
      case "W":
        moveUp();
        break;
      case "S":
        moveDown();
        break;
      default:
        System.out.println("Invalid move: " + move);
        break;
    }
  }

  private void moveLeft() {
    for (int i = 0; i < 4; i++) {
      int[] row = board[i];
      row = mergeTiles(row);
      board[i] = row;
    }
  }

  private void moveRight() {
    for (int i = 0; i < 4; i++) {
      int[] row = Arrays.copyOf(board[i], 4);
      row = mergeTiles(row);
      // Reverse the row after merging to maintain the original direction
      for (int j = 0; j < 4; j++) {
        board[i][j] = row[3 - j];
      }
    }
  }

  private void moveUp() {
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

  private void moveDown() {
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

  private double evaluateHeuristic(Game2048 game) {
    double heuristicValue = 0;

    // Heuristic: Monotonicity
    heuristicValue += monotonicity(game);

    // Heuristic: Smoothness
    heuristicValue += smoothness(game);

    // Heuristic: Number of empty cells
    heuristicValue += emptyCellsHeuristic(game.getBoard());

    // Heuristic: Prioritize corners
    heuristicValue += cornerBias(game.getBoard());

    return heuristicValue;
  }

  private double cornerBias(int[][] board) {
    // Corners and their adjacent tiles have higher weights
    double cornerWeight = 0.0;

    // Check each corner
    cornerWeight += board[0][0] + board[0][1] + board[1][0];
    cornerWeight += board[0][3] + board[0][2] + board[1][3];
    cornerWeight += board[3][0] + board[3][1] + board[2][0];
    cornerWeight += board[3][3] + board[3][2] + board[2][3];

    return cornerWeight;
  }

  private String getBestMove() {
    String[] possibleMoves = {"A", "D", "W", "S"};
    double bestScore = Double.NEGATIVE_INFINITY;
    String bestMove = "";

    boolean canMoveUpDown = canMoveUp() || canMoveDown();
    boolean canMoveLeftRight = canMoveLeft() || canMoveRight();

    // If both up/down and left/right movements are possible or only one direction is possible,
    // consider the corresponding moves.
    if ((canMoveUpDown && canMoveLeftRight) || (!canMoveUpDown && !canMoveLeftRight)) {
      for (String move : possibleMoves) {
        // get the nexw game board, if the move is executed
        Game2048 copyGame = copy();
        copyGame.executeMove(move);

        // Heuristic evaluation
        double moveScore = copyGame.getScore() - score; // Difference in score after the move
        double heuristicScore = evaluateHeuristic(copyGame); // Heuristic evaluation for the move
        double combinedScore =
            moveScore + heuristicScore; // Combine score difference and heuristic score
        double moveExpectiMax = expectiMax(copyGame, 4, false);
        combinedScore += moveExpectiMax; // Add the expectiMax score to the combined score
        if (combinedScore > bestScore) {
          bestScore = combinedScore;
          bestMove = move;
        }
      }
    } else if (canMoveUpDown) {
      // If only up/down movement is possible, consider up and down movements
      bestMove = canMoveUp() ? "W" : "S";
    } else if (canMoveLeftRight) {
      // If only left/right movement is possible, consider left and right movements
      bestMove = canMoveLeft() ? "A" : "D";
    }

    return bestMove;
  }

  private double expectiMax(Game2048 game, int depth, boolean isChanceNode) {
    // base case for the recursion
    if (depth == 0 || game.isGameOver()) {
      return game.getScore() + evaluateHeuristic(game);
    }

    if (isChanceNode) {
      double sum = 0;
      int emptyCells = countEmptyCells(game.getBoard());
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if (game.getBoard()[i][j] == 0) {
            Game2048 copyGame2 = game.copy();
            copyGame2.getBoard()[i][j] = 2;
            double value = 0.9 * expectiMax(copyGame2, depth - 1, false);
            value += 0.1 * expectiMax(copyGame2, depth - 1, true);
            sum += value / emptyCells;
          }
        }
      }
      return sum;
    } else {
      String[] possibleMoves = {"A", "D", "W", "S"};
      double bestScore = Double.NEGATIVE_INFINITY;
      for (String move : possibleMoves) {
        Game2048 copyGame = game.copy();
        copyGame.executeMove(move);
        double moveScore = expectiMax(copyGame, depth - 1, true);
        if (moveScore > bestScore) {
          bestScore = moveScore;
        }
      }
      return bestScore;
    }
  }

  private double emptyCellsHeuristic(int[][] board) {
    double[] scoreGrid = {
      Math.pow(4, 15), Math.pow(4, 14), Math.pow(4, 13), Math.pow(4, 12),
      Math.pow(4, 8), Math.pow(4, 9), Math.pow(4, 10), Math.pow(4, 11),
      Math.pow(4, 7), Math.pow(4, 6), Math.pow(4, 5), Math.pow(4, 4),
      Math.pow(4, 0), Math.pow(4, 1), Math.pow(4, 2), Math.pow(4, 3)
    };

    double emptyCellsScore = 0;
    int cellIndex = 0;

    // Traverse the board in snake-like order and calculate the score
    for (int i = 0; i < 4; i++) {
      if (i % 2 == 0) {
        for (int j = 0; j < 4; j++) {
          if (board[i][j] == 0) {
            emptyCellsScore += scoreGrid[cellIndex];
          }
          cellIndex++;
        }
      } else {
        for (int j = 3; j >= 0; j--) {
          if (board[i][j] == 0) {
            emptyCellsScore += scoreGrid[cellIndex];
          }
          cellIndex++;
        }
      }
    }

    return emptyCellsScore;
  }

  private int countEmptyCells(int[][] board) {
    int count = 0;
    for (int[] row : board) {
      for (int cell : row) {
        if (cell == 0) {
          count++;
        }
      }
    }
    return count;
  }

  private Game2048 copy() {
    Game2048 copyGame = new Game2048();
    copyGame.setBoard(copyBoard(board));
    copyGame.setScore(score);
    copyGame.setGameOver(gameOver);
    return copyGame;
  }

  private int[][] copyBoard(int[][] original) {
    int[][] copy = new int[4][4];
    for (int i = 0; i < 4; i++) {
      System.arraycopy(original[i], 0, copy[i], 0, 4);
    }
    return copy;
  }

  private double monotonicity(Game2048 game) {
    // Weights for monotonicity in each direction
    double[] weights = {1.0, 0.8, 0.6, 0.4};
    double monoValue = 0;

    // Check monotonicity in rows
    for (int i = 0; i < 4; i++) {
      int current = 0;
      int next = current + 1;
      while (next < 4) {
        while (next < 4 && game.getBoard()[i][next] == 0) {
          next++;
        }
        if (next >= 4) {
          next--;
        }
        int currentValue =
            (game.getBoard()[i][current] != 0)
                ? (int) (Math.log(game.getBoard()[i][current]) / Math.log(2))
                : 0;
        int nextValue =
            (game.getBoard()[i][next] != 0)
                ? (int) (Math.log(game.getBoard()[i][next]) / Math.log(2))
                : 0;

        if (currentValue > nextValue) {
          monoValue += (nextValue - currentValue) * weights[i];
        } else if (nextValue > currentValue) {
          monoValue += (currentValue - nextValue) * weights[i];
        }
        current = next;
        next++;
      }
    }

    // Check monotonicity in columns
    for (int j = 0; j < 4; j++) {
      int current = 0;
      int next = current + 1;
      while (next < 4) {
        while (next < 4 && game.getBoard()[next][j] == 0) {
          next++;
        }
        if (next >= 4) {
          next--;
        }
        int currentValue =
            (game.getBoard()[j][current] != 0)
                ? (int) (Math.log(game.getBoard()[j][current]) / Math.log(2))
                : 0;
        int nextValue =
            (game.getBoard()[j][next] != 0)
                ? (int) (Math.log(game.getBoard()[j][next]) / Math.log(2))
                : 0;
        if (currentValue > nextValue) {
          monoValue += (nextValue - currentValue) * weights[j];
        } else if (nextValue > currentValue) {
          monoValue += (currentValue - nextValue) * weights[j];
        }
        current = next;
        next++;
      }
    }

    return monoValue;
  }

  private double smoothness(Game2048 game) {
    double smoothValue = 0;

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        if (game.getBoard()[i][j] != 0) {
          int value = (int) (Math.log(game.getBoard()[i][j]) / Math.log(2));
          // Check right
          if (j < 3 && game.getBoard()[i][j + 1] != 0) {
            int rightValue = (int) (Math.log(game.getBoard()[i][j + 1]) / Math.log(2));
            smoothValue -= Math.abs(value - rightValue);
          }
          // Check down
          if (i < 3 && game.getBoard()[i + 1][j] != 0) {
            int downValue = (int) (Math.log(game.getBoard()[i + 1][j]) / Math.log(2));
            smoothValue -= Math.abs(value - downValue);
          }
        }
      }
    }

    return smoothValue;
  }

  public int[][] getBoard() {
    return board;
  }

  public void setBoard(int[][] board) {
    this.board = board;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void setGameOver(boolean gameOver) {
    this.gameOver = gameOver;
  }

  public static void main(String[] args) {
    Game2048 game = new Game2048();
    game.play();
  }
}
