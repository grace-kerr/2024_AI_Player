import java.util.Arrays;
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
    // Initialize the root game state and the decision tree
    constructDecisionTree(1);

    Scanner scanner = new Scanner(System.in);
    while (!gameOver) {
      printBoard();

      boolean moved = false;
      while (!moved) {
        // Get the best move from the decision tree
        String move = getBestMove(rootGame);
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

  public void constructDecisionTree(int depthLimit) {
    // 1. Initialize the root node with the current game state
    rootGame = new TreeNode2048<>(new Game2048());

    // 2. Expand the root node to build the decision tree
    expandNode(rootGame, depthLimit);
  }

  public void expandNode(TreeNode2048<Game2048> game, int depthLimit) {
    if (!game.isChanceNode()) {
      Game2048 game2048 = game.getData();
      int currentDepth = calculateDepth(game);

      System.out.println("Expanding node at depth: " + currentDepth);

      // Check if the depth limit has been reached
      if (depthLimit == 0) {
        System.out.println("Depth limit reached.");
        return;
      }

      // Expand the node by simulating moves in all directions
      boolean moved = false;

      if (moveTilesLeft()) {
        Game2048 left = new Game2048();
        left.setBoard(copyBoard(game2048.getBoard()));
        System.out.println("Set board.");
        TreeNode2048<Game2048> leftChild = new TreeNode2048<>(left);
        leftChild.setChanceNode(true); // Set the child as a chance node
        System.out.println("set chance node.");
        game.addChild(leftChild);
        System.out.println("Added child to game");
        System.out.println("Expanded left.");
        expandNode(leftChild, depthLimit - 1);
        moved = true;
      } else {
        System.out.println("Cannot move left.");
      }

      // Right move
      if (moveTilesRight()) {
        // Create a new game state for the right move
        Game2048 right = new Game2048();
        right.setBoard(copyBoard(game2048.getBoard()));
        TreeNode2048<Game2048> rightChild = new TreeNode2048<>(right);
        rightChild.setChanceNode(true); // Set the child as a chance node
        game.addChild(rightChild);
        expandNode(rightChild, depthLimit - 1); // Decrement depth limit
        moved = true;
      }

      // Up move
      if (moveTilesUp()) {
        // Create a new game state for the up move
        Game2048 up = new Game2048();
        up.setBoard(copyBoard(game2048.getBoard()));
        TreeNode2048<Game2048> upChild = new TreeNode2048<>(up);
        upChild.setChanceNode(true); // Set the child as a chance node
        game.addChild(upChild);
        expandNode(upChild, depthLimit - 1); // Decrement depth limit
        moved = true;
      }

      // Down move
      if (moveTilesDown()) {
        // Create a new game state for the down move
        Game2048 down = new Game2048();
        down.setBoard(copyBoard(game2048.getBoard()));
        TreeNode2048<Game2048> downChild = new TreeNode2048<>(down);
        downChild.setChanceNode(true); // Set the child as a chance node
        game.addChild(downChild);
        expandNode(downChild, depthLimit - 1); // Decrement depth limit
        moved = true;
      }

      // If no moves were made, the game might be over
      if (!moved) {
        // Check if the game is over and handle it appropriately
        if (isGameOver()) {
          return;
        } else {
          System.out.println("No valid moves available.");
          return;
        }
      }

      // Evaluate chance nodes and update utility values
      evaluateChanceNodes(game);

      System.out.println("Node expansion completed at depth: " + currentDepth);
    }
  }

  // public void expandNode(TreeNode2048<Game2048> game, int depthLimit) {
  //   if (!game.isChanceNode()) {
  //     Game2048 game2048 = game.getData();
  //     int currentDepth = calculateDepth(game);

  //     System.out.println("Expanding node at depth: " + currentDepth);

  //     // Check if the depth limit has been reached
  //     if (depthLimit == 0) {
  //       System.out.println("Depth limit reached. Returning.");
  //       return;
  //     }

  //     // Expand the node by simulating the move to the left
  //     if (moveTilesLeft()) {
  //       Game2048 left = new Game2048();
  //       left.setBoard(copyBoard(game2048.getBoard()));
  //       System.out.println("Set board.");
  //       TreeNode2048<Game2048> leftChild = new TreeNode2048<>(left);
  //       leftChild.setChanceNode(true); // Set the child as a chance node
  //       System.out.println("set chance node.");
  //       game.addChild(leftChild);
  //       System.out.println("Added child to game");
  //       System.out.println("Expanded left.");
  //       expandNode(leftChild, depthLimit - 1);
  //     } else {
  //       System.out.println("Cannot move left.");
  //     }

  //     // Simulate moves to the right
  //     if (moveTilesRight()) {
  //       Game2048 right = new Game2048();
  //       right.setBoard(copyBoard(game2048.getBoard()));
  //       TreeNode2048<Game2048> rightChild = new TreeNode2048<>(right);
  //       rightChild.setChanceNode(true); // Set the child as a chance node
  //       game.addChild(rightChild);
  //       // Decrement depth limit for right move
  //       expandNode(rightChild, depthLimit - 1);
  //     }

  //     // Simulate moves upward
  //     if (moveTilesUp()) {
  //       Game2048 up = new Game2048();
  //       up.setBoard(copyBoard(game2048.getBoard()));
  //       TreeNode2048<Game2048> upChild = new TreeNode2048<>(up);
  //       upChild.setChanceNode(true); // Set the child as a chance node
  //       game.addChild(upChild);
  //       // Decrement depth limit for upward move
  //       expandNode(upChild, depthLimit - 1);
  //     }

  //     // Simulate moves downward
  //     if (moveTilesDown()) {
  //       Game2048 down = new Game2048();
  //       down.setBoard(copyBoard(game2048.getBoard()));
  //       TreeNode2048<Game2048> downChild = new TreeNode2048<>(down);
  //       downChild.setChanceNode(true); // Set the child as a chance node
  //       game.addChild(downChild);
  //       // Decrement depth limit for downward move
  //       expandNode(downChild, depthLimit - 1);
  //     }

  //     evaluateChanceNodes(game);

  //     System.out.println("Node expansion completed at depth: " + currentDepth);
  //   }
  // }

  // private boolean isRepeatedState(TreeNode2048<Game2048> node) {
  //   // Traverse the parent nodes to check for repetition
  //   TreeNode2048<Game2048> parent = node.getParent();
  //   while (parent != null) {
  //     if (Arrays.deepEquals(parent.getData().getBoard(), node.getData().getBoard())) {
  //       return true; // Repeated state found
  //     }
  //     parent = parent.getParent();
  //   }
  //   return false; // No repeated state found
  // }

  private int calculateDepth(TreeNode2048<Game2048> node) {
    int depth = 0;
    while (node.getParent() != null) {
      depth++;
      node = node.getParent();
    }
    return depth;
  }

  // Method to get the best move from the decision tree
  private String getBestMove(TreeNode2048<Game2048> rootGame) {
    // Initialize variables to keep track of the maximum utility and the corresponding move
    float maxUtility = Float.MIN_VALUE;
    String bestMove = "";

    // Traverse the children of the root node to find the child with the highest utility
    for (TreeNode2048<Game2048> child : rootGame.getChildren()) {
      float utility = findMaxUtility(child);
      if (utility > maxUtility) {
        maxUtility = utility;
        bestMove = getMoveFromChild(rootGame, child);
      }
    }

    return bestMove;
  }

  // Recursive function to find the maximum utility value in the decision tree
  private float findMaxUtility(TreeNode2048<Game2048> node) {
    if (node.isChanceNode()) {
      // If it's a chance node, return the maximum utility value among its children
      float maxChildUtility = Float.MIN_VALUE;
      for (TreeNode2048<Game2048> child : node.getChildren()) {
        maxChildUtility = Math.max(maxChildUtility, findMaxUtility(child));
      }
      return maxChildUtility;
    } else {
      // If it's a decision node, return its utility value
      return node.getNodeUtil();
    }
  }

  // Helper function to determine the move required to reach a child node from its parent
  private String getMoveFromChild(TreeNode2048<Game2048> parent, TreeNode2048<Game2048> child) {
    // Traverse the children of the parent node to find the index of the child
    for (int i = 0; i < parent.getChildren().size(); i++) {
      if (parent.getChildren().get(i) == child) {
        // Map the index to the corresponding move direction
        switch (i) {
          case 0:
            return "W"; // Up
          case 1:
            return "A"; // Left
          case 2:
            return "S"; // Down
          case 3:
            return "D"; // Right
        }
      }
    }
    return ""; // Default case (should not occur)
  }

  public int[][] getBoard() {
    return board;
  }

  private static TreeNode2048<Game2048> rootGame;

  public void setBoard(int[][] newBoard) {
    if (newBoard.length != 4 || newBoard[0].length != 4) {
      throw new IllegalArgumentException("Board dimensions must be 4x4");
    }

    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        this.board[i][j] = newBoard[i][j];
      }
    }
  }

  public void setCell(int row, int col, int value) {
    if (row < 0 || row >= 4 || col < 0 || col >= 4) {
      throw new IllegalArgumentException("Invalid row or column index");
    }
    this.board[row][col] = value;
  }

  public void evaluateChanceNodes(TreeNode2048<Game2048> game) {
    if (game.isChanceNode()) {
      Game2048 game2048 = game.getData();
      int[][] tempBoard = game2048.getBoard();

      // Simulate random tile placement (2 or 4) on empty cells
      // Adding resulting game states as children
      for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 4; j++) {
          if (tempBoard[i][j] == 0) {
            double rand = Math.random();

            Game2048 newGame = new Game2048();
            newGame.setBoard(tempBoard);
            // 90% chance for 2, 10% chance for 4
            newGame.setCell(i, j, (rand < 0.9) ? 2 : 4);
            TreeNode2048<Game2048> child = new TreeNode2048<>(newGame);
            // Set the child as a decision node
            child.setChanceNode(false);
            game.addChild(child);
          }
        }
      }
    }
  }

  public void evaluateUtilityNodes(TreeNode2048<Game2048> game) {
    if (!game.isChanceNode()) {
      Game2048 game2048 = game.getData();

      // Evaluate utility for the current game state
      int utility = evaluateUtility(game2048);

      // Set the utility value for the node
      game.setNodeUtil(utility);

      // Recursively evaluate utility for children nodes
      for (TreeNode2048<Game2048> child : game.getChildren()) {
        evaluateUtilityNodes(child);
      }
    }
  }

  private int evaluateUtility(Game2048 game) {
    int emptySpaces = countEmptySpaces(game.getBoard());
    int edgeValue = calculateEdgeValue(game.getBoard());

    int utility = emptySpaces * 2 + edgeValue * 3;

    return utility;
  }

  private int countEmptySpaces(int[][] board) {
    int emptySpaces = 0;
    for (int[] row : board) {
      for (int cell : row) {
        if (cell == 0) {
          emptySpaces++;
        }
      }
    }
    return emptySpaces;
  }

  private int calculateEdgeValue(int[][] board) {
    int maxEdgeValue = 0;

    // Check top and bottom edges
    for (int i = 0; i < 4; i++) {
      maxEdgeValue = Math.max(maxEdgeValue, Math.max(board[0][i], board[3][i]));
    }

    // Check left and right edges
    for (int i = 0; i < 4; i++) {
      maxEdgeValue = Math.max(maxEdgeValue, Math.max(board[i][0], board[i][3]));
    }

    return maxEdgeValue;
  }
}
