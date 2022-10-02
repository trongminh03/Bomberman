package uet.oop.bomberman.entities.character.enemy.PathFinding;

public class Node implements Comparable<Node> {
    private int gCost;
    private int hCost;
    private int fCost;
    private int gridX;
    private int gridY;
    private Node parent;
    private boolean isBlocked = false;

    public Node(int gridX, int gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public int getgCost() {
        return gCost;
    }

    public void setgCost(int gCost) {
        this.gCost = gCost;
    }

    public int gethCost() {
        return hCost;
    }

    public void sethCost(int hCost) {
        this.hCost = hCost;
    }

    public int getGridX() {
        return gridX;
    }

    public void setGridX(int gridX) {
        this.gridX = gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public void setGridY(int gridY) {
        this.gridY = gridY;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public void calculateFinalCost() {
        int fCost = this.gCost + this.hCost;
        setFinalCost(fCost);
    }

    public int getFinalCost() {
        return this.fCost;
    }

    public void setFinalCost(int fCost) {
        this.fCost = fCost;
    }

    public void setNodeValue(Node currentNode, int cost) {
        int gCost = currentNode.getgCost() + cost;
        setgCost(gCost);
        setParent(currentNode);
        calculateFinalCost();
    }

    public boolean checkBetterPath(Node currentNode, int cost) {
        int gCost = currentNode.getgCost() + cost;
        if (gCost < this.getgCost()) {
            setNodeValue(currentNode, cost);
            return true;
        }
        return false;
    }

    public void calculateHeuristicCost(Node goalNode) {
        this.hCost =  Math.abs(this.gridX - goalNode.gridX) + Math.abs(this.gridY - goalNode.gridY);
    }

    public void setBlocked(boolean blocked) {
        this.isBlocked = blocked;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Node) {
            Node other = (Node) obj;
            return (other.gridX == gridX && other.gridY == gridY);
        }
        return false;
    }

    @Override
    public int compareTo(Node o) {
        return (this.getFinalCost() - o.getFinalCost());
    }

    public Node subtract(Node other) {
        return new Node(this.getGridX() - other.getGridX(),
                this.getGridY() - other.getGridY());
    }
}
