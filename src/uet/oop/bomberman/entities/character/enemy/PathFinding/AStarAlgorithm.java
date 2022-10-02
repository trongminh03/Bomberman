package uet.oop.bomberman.entities.character.enemy.PathFinding;

import uet.oop.bomberman.constants.Direction;
import uet.oop.bomberman.constants.GlobalConstants;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.StaticEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.static_objects.Brick;
import uet.oop.bomberman.gui.GameViewManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class AStarAlgorithm extends RandomMove {
    private static final int COST = 1;
    private Bomber bomber;
    private GameViewManager game;
    private Node startNode;
    private Node goalNode;

//    private Node currentNode;

    private static final int SCOPE = 20;
    private Node[][] map;
    private PriorityQueue<Node> open;
    private List<Node> closed;

    public AStarAlgorithm(Character enemy, Bomber bomber, GameViewManager game) {
        super(enemy);
        this.game = game;
        this.bomber = bomber;
        setStartNode(new Node(enemy.getGridX(), enemy.getGridY()));
        setGoalNode(new Node(bomber.getGridX(), bomber.getGridY()));
//        this.currentNode = this.startNode;
        map = new Node[game.getRows()][game.getColumns()];
        open = new PriorityQueue<Node>();
        closed = new ArrayList<Node>();
        setNodes();
        setObstacles();
    }

    public EnemyDirection chasePlayer() {
        if (Math.abs(bomber.getGridX() - enemy.getGridX()) <= SCOPE
                && Math.abs(bomber.getGridY() - enemy.getGridY()) <= SCOPE
                && Math.abs(bomber.getGridX() - enemy.getGridX()) >= 0
                && Math.abs(enemy.getGridY() - bomber.getGridY()) >= 0 && !bomber.checkHitEnemy()) { // check if bomber in scope
            if (enemy.getX() % 32 == 0 && enemy.getY() % 32 == 0) {
                List<Node> path = findPath();
                if (path == null) {
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
//        setObstacles();
        List<Node> path = new ArrayList<Node>();
        setStartNode(new Node(enemy.getGridX(), enemy.getGridY()));
//        currentNode = startNode;
        setGoalNode(new Node(bomber.getGridX(), bomber.getGridY()));
        open.add(startNode);
//        print();
//        closed.add(startNode);
//        addNeighborsToOpenList();
//        while (this.currentNode != this.goalNode) {
//            if (this.open.isEmpty()) { // nothing to examine
//                return null;
//            }
//            System.out.println(currentNode.getGridX() + " " + currentNode.getGridY());
//            this.currentNode = open.poll();
//            closed.add(this.currentNode);
//            addNeighborsToOpenList();
//        }
//        path.add(this.currentNode);
//        while (!this.currentNode.equals(startNode)) {
//            this.currentNode = this.currentNode.getParent();
//            path.add(0, this.currentNode);
//        }
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
        for (Entity entity : game.getStillObjects()) {
            if (entity instanceof StaticEntity) {
                int row = entity.getGridY();
                int column = entity.getGridX();
                setBlock(row, column);
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
        System.out.println(dir);
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
