package uet.oop.bomberman.entities.enemy.PathFinding;

import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.entities.Bomb;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.enemy.Enemy;
import uet.oop.bomberman.entities.Brick;
import uet.oop.bomberman.entities.static_objects.Wall;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.gui.GameViewManager;

import java.util.*;

public class AStarAlgorithm extends RandomMove {
    private static final int COST = 1;
    private Bomber bomber;
    private GameViewManager game;
    private Node startNode;
    private Node goalNode;
    private Node[][] map;
    private PriorityQueue<Node> open;
//    private List<Node> closed;
    private Set<Node> closed;

    public AStarAlgorithm(Enemy enemy, Bomber bomber, GameViewManager game) {
        super(enemy);
        this.game = game;
        this.bomber = bomber;
        setStartNode(new Node(enemy.getGridX(), enemy.getGridY()));
        setGoalNode(new Node(bomber.getGridX(), bomber.getGridY()));
//        this.currentNode = this.startNode;
        map = new Node[game.getRows()][game.getColumns()];
        open = new PriorityQueue<Node>();
//        closed = new ArrayList<Node>();
        closed = new HashSet<>();
        setNodes();
        setObstacles();
    }

    public EnemyDirection chasePlayer() {
        // check if bomber in enemy scope
        if (Math.abs(bomber.getGridX() - enemy.getGridX()) <= enemy.getFindingScope()
            && Math.abs(bomber.getGridY() - enemy.getGridY()) <= enemy.getFindingScope()
            && Math.abs(bomber.getGridX() - enemy.getGridX()) >= 0
            && Math.abs(enemy.getGridY() - bomber.getGridY()) >= 0 && !bomber.checkFatalHit()) {
            // Check exact grid
            if (enemy.getX() % Sprite.SCALED_SIZE == 0 && enemy.getY() % Sprite.SCALED_SIZE == 0) {
                List<Node> path = findPath();
                if (path.size() == 0) {
                    return EnemyDirection.DETECT_FAILED;
                }
                Node step = path.get(1).subtract(path.get(0));
                if (step.getGridX() > 0) {
                    return EnemyDirection.RIGHT;
                } else if (step.getGridX() < 0) {
                    return EnemyDirection.LEFT;
                } else if (step.getGridY() > 0) {
                    return EnemyDirection.DOWN;
                } else if (step.getGridY() < 0) {
                    return EnemyDirection.UP;
                }
            }
        }
        return EnemyDirection.DETECT_FAILED;
    }

    public List<Node> findPath() {
        open.clear();
        closed.clear();
        setNodes();
        setObstacles();
        List<Node> path = new ArrayList<Node>();
        setStartNode(new Node(enemy.getGridX(), enemy.getGridY()));
        setGoalNode(new Node(bomber.getGridX(), bomber.getGridY()));
        open.add(startNode);
        while (!open.isEmpty()) {
            Node currentNode = open.poll();
            closed.add(currentNode);
            if (currentNode.equals(goalNode)) {
                path.add(currentNode);
                while (!currentNode.equals(startNode)) {
                    currentNode = currentNode.getParent();
                    path.add(0, currentNode);
                }
                return path;
            } else {
                addNeighborsToOpenList(currentNode);
            }
        }
        return new ArrayList<>();
    }

    public void setNodes() {
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                Node node = new Node(j, i);
                node.calculateHeuristicCost(getGoalNode());
                map[i][j] = node;
            }
        }
    }

    public void setObstacles() {
        if (enemy.canBrickPass()) {
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall || entity instanceof Bomb) {
                    int row = entity.getGridY();
                    int column = entity.getGridX();
                    if (row >= 0 && column >= 0) {
                        setBlock(row, column);
                    }
                }
            }
        } else {
            for (Entity entity : game.getStillObjects()) {
                if (entity instanceof Wall || entity instanceof Brick || entity instanceof Bomb) {
                    int row = entity.getGridY();
                    int column = entity.getGridX();
                    if (row >= 0 && column >= 0) {
                        setBlock(row, column);
                    }
                }
            }
        }
    }

    private void addNeighborsToOpenList(Node currentNode) {
        Node adjacentNode;
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                if ((x == 0 || y == 0) && x != y) {
                    if (currentNode.getGridX() + x < game.getColumns() && currentNode.getGridY() + y < game.getRows()
                            && currentNode.getGridX() + x >= 0 && currentNode.getGridY() + y >= 0) {
                        adjacentNode = map[currentNode.getGridY() + y][currentNode.getGridX() + x];
                        if (!adjacentNode.isBlocked() && !closed.contains(adjacentNode)) {
                            if (!open.contains(adjacentNode)) {
                                adjacentNode.setNodeValue(currentNode, COST);
                                open.add(adjacentNode);
                            } else {
                                boolean changed = adjacentNode.checkBetterPath(currentNode, COST);
                                if (changed) {
                                    open.remove(adjacentNode);
                                    open.add(adjacentNode);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public void setBlock(int row, int column) {
        map[row][column].setBlocked(true);
    }

    public Node getGoalNode() {
        return new Node(enemy.getGridX(), enemy.getGridY());
    }

    public void setGoalNode(Node goalNode) {
        this.goalNode = goalNode;
    }

    public void setStartNode(Node startNode) {
        this.startNode = startNode;
    }

    public void updateEnemyDirection() {
        EnemyDirection dir = chasePlayer();
        if (dir == EnemyDirection.RIGHT) {
            enemy.setDirection(Direction.RIGHT);
        } else if (dir == EnemyDirection.LEFT) {
            enemy.setDirection(Direction.LEFT);
        } else if (dir == EnemyDirection.UP) {
            enemy.setDirection(Direction.UP);
        } else if (dir == EnemyDirection.DOWN) {
            enemy.setDirection(Direction.DOWN);
        } else {
            setRandomDirection();
        }
    }

    public void print() {
        for (int i = 0; i < game.getRows(); i++) {
            for (int j = 0; j < game.getColumns(); j++) {
                if (map[i][j].isBlocked()) {
                    System.out.println(i + " " + j);
                }
            }
        }
    }
}
