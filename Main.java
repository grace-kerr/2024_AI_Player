public class Main {


  public static void main(String[] args) {
      Game2048 game = new Game2048();
      TreeNode2048<Game2048> gameTree = new TreeNode2048<>(game);
      if (gameTree.isRoot()){
          game.setRootGame(gameTree);

      }
      game.expandNode(gameTree);
      game.play();

  }
}
