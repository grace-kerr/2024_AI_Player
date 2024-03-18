import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Game2048 {
  public int[][] getBoard() {
    return board;
  }

  public void setBoard(int[][] board) {
    this.board = board;
  }

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
    int value;
    double randomValue = Math.random(); // Generate a random value between 0 and 1

    // 90% chance of generating 2, 10% chance of generating 4
    if (randomValue < 0.9) {
      value = 2;
    } else {
      value = 4;
    }

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

  /** Might use it */
  private int[][] moveBoardTilesToLeft(int[][] board) {
    int[][] oldBoard = copyBoard(board);
    int[][] boardAfterLeftMove = copyBoard(board);
    for (int i = 0; i < 4; i++) {
      int[] row = new int[4];
      for (int j = 0; j < 4; j++) {
        row[j] = boardAfterLeftMove[i][j];
      }
      row = mergeTiles(row);
      for (int j = 0; j < 4; j++) {
        boardAfterLeftMove[i][j] = row[j];
      }
    }
    if (!Arrays.deepEquals(boardAfterLeftMove, oldBoard)) {
      return boardAfterLeftMove;
    } else {
      System.out.println("Left move was not possible");
      return oldBoard;
    }
  }

  private int[][] moveBoardTilesToRight(int[][] board) {
    int[][] oldBoard = copyBoard(board);
    int[][] boardAfterRightMove = copyBoard(board);
    for (int i = 0; i < 4; i++) {
      int[] row = new int[4];
      for (int j = 0; j < 4; j++) {
        row[j] = boardAfterRightMove[i][3 - j];
      }
      row = mergeTiles(row);
      for (int j = 0; j < 4; j++) {
        boardAfterRightMove[i][3 - j] = row[j];
      }
    }
    if (!Arrays.deepEquals(boardAfterRightMove, oldBoard)) {
      return boardAfterRightMove;
    } else {
      System.out.println("Right move was not possible");
      return oldBoard;
    }
  }

  private int[][] moveBoardTilesUp(int[][] board) {
    int[][] oldBoard = copyBoard(board);
    int[][] boardAfterUpMove = copyBoard(board);
    for (int j = 0; j < 4; j++) {
      int[] col = new int[4];
      for (int i = 0; i < 4; i++) {
        col[i] = boardAfterUpMove[i][j];
      }
      col = mergeTiles(col);
      for (int i = 0; i < 4; i++) {
        boardAfterUpMove[i][j] = col[i];
      }
    }
    if (!Arrays.deepEquals(boardAfterUpMove, oldBoard)) {
      return boardAfterUpMove;
    } else {
      System.out.println("Up move was not possible");
      return oldBoard;
    }
  }

  private int[][] moveBoardTilesDown(int[][] board) {
    int[][] oldBoard = copyBoard(board);
    int[][] boardAfterDownMove = copyBoard(board);
    for (int j = 0; j < 4; j++) {
      int[] col = new int[4];
      for (int i = 0; i < 4; i++) {
        col[i] = boardAfterDownMove[3 - i][j];
      }
      col = mergeTiles(col);
      for (int i = 0; i < 4; i++) {
        boardAfterDownMove[3 - i][j] = col[i];
      }
    }
    if (!Arrays.deepEquals(boardAfterDownMove, oldBoard)) {
      return boardAfterDownMove;
    } else {
      System.out.println("Up move was not possible");
      return oldBoard;
    }
  }

  private boolean moveTilesLeft() {
    int[][] oldBoard = copyBoard(board);
    for (int i = 0; i < 4; i++) {
      int[] row = new int[4];
      for (int j = 0; j < 4; j++) {
        row[j] = board[i][j];
      }
      row = mergeTiles(row);
      for (int j = 0; j < 4; j++) {
        board[i][j] = row[j];
      }
    }
    return !Arrays.deepEquals(board, oldBoard);
  }

  private boolean moveTilesRight() {
    int[][] oldBoard = copyBoard(board);
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
    return !Arrays.deepEquals(board, oldBoard);
  }

  private boolean moveTilesUp() {
    int[][] oldBoard = copyBoard(board);
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
    return !Arrays.deepEquals(board, oldBoard);
  }

  private boolean moveTilesDown() {
    int[][] oldBoard = copyBoard(board);
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
    return !Arrays.deepEquals(board, oldBoard);
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

  private int[][] copyBoard(int[][] board) {
    int[][] copy = new int[4][4];
    for (int i = 0; i < 4; i++) {
      System.arraycopy(board[i], 0, copy[i], 0, 4);
    }
    return copy;
  }

  public void play() {
    Scanner scanner = new Scanner(System.in);
    while (!gameOver) {
      printBoard();
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }

      expandNode(rootGame);

      boolean moved = false;
      while (!moved) {
        String move = AIPlayer.turn(); // Get move from AIPlayer
        switch (move) {
          case "W":
            moved = moveTilesUp();
            break;
          case "A":
            moved = moveTilesLeft();
            break;
          case "S":
            moved = moveTilesDown();
            break;
          case "D":
            moved = moveTilesRight();
            break;
        }

        // If none of the moves resulted in changes, prompt for a new move
        if (!moved) {
          System.out.println("Move didn't result in any changes. Trying a different move...");
        }
      }

      addNewTile();
      gameOver = isGameOver();
    }

    printBoard();
    System.out.println("Game Over! Your score: " + score);
    scanner.close();
  }

  public TreeNode2048<Game2048> getRootGame() {
    return rootGame;
  }

  public void setRootGame(TreeNode2048<Game2048> rootGame) {
    this.rootGame = rootGame;
  }

  private TreeNode2048<Game2048> rootGame;

  public void expandNode(TreeNode2048<Game2048> game) {
    if (!game.isChanceNode()) {
      Game2048 mainGame2048 = game.getData();

      Game2048 tempGame = mainGame2048;
      int[][] tempBoard = mainGame2048.getBoard();

      tempBoard = moveBoardTilesToLeft(tempBoard);
      tempGame.setBoard(tempBoard);
      game.addChild(tempGame);
      System.out.println("Left move");
      for (int i = 0; i < tempBoard.length; i++) {
        System.out.println(Arrays.toString(tempBoard[i]));
      }

      tempGame = game.getData();
      tempBoard = tempGame.getBoard();

      tempBoard = moveBoardTilesToRight(tempBoard);
      tempGame.setBoard(tempBoard);
      game.addChild(tempGame);
      System.out.println("Right move");
      for (int i = 0; i < tempBoard.length; i++) {
        System.out.println(Arrays.toString(tempBoard[i]));
      }

      tempGame = game.getData();
      tempBoard = tempGame.getBoard();

      tempBoard = moveBoardTilesDown(tempBoard);
      tempGame.setBoard(tempBoard);
      game.addChild(tempGame);
      System.out.println("Down move");
      for (int i = 0; i < tempBoard.length; i++) {
        System.out.println(Arrays.toString(tempBoard[i]));
      }

      tempGame = game.getData();
      tempBoard = tempGame.getBoard();

      tempBoard = moveBoardTilesUp(tempBoard);
      tempGame.setBoard(tempBoard);
      game.addChild(tempGame);
      System.out.println("Up move");
      for (int i = 0; i < tempBoard.length; i++) {
        System.out.println(Arrays.toString(tempBoard[i]));
      }
    }

    if (game.isRoot()) {
      rootGame.addChild(game);
    } else {

    }
  }

  public void evaluateChanceNodes(TreeNode2048<Game2048> game) {
    if (!game.isChanceNode()) {}
  }

  // TODO
  public void evaluateUtilityNodes() {}
}
