package AI.pathFinding;

import MenuSetUp.DimensionSize;
import entity.Entity;
import main.GamePanel;
import tile.Tile;

import java.awt.Point;
import java.util.*;

public class DStartLite {

    private final double INF = Double.POSITIVE_INFINITY;
    private final int[][] DIRECTIONS = {
            {0, -1}, // up
            {0, 1}, // down
            {-1, 0}, // left
            {1, 0} // right
    };
    private final int WIDTH, HEIGHT;

    private final Entity self;
    private final Entity target;

    private Point start;
    private Point goal;
    private double km; // key modifier

    private Map<Point, Double> g; // chi phi tu tot nhat tu start den 1 diem
    private Map<Point, Double> rhs; // chi uoc luong
    private Map<Pair, Double> cost; // chi phi giua cac diem
    private PriorityQueue<Point> U;
    private Comparator<Point> comparator;

    private boolean[][] grid;

    public DStartLite(GamePanel gp, Entity self, Entity target) {
        this.WIDTH = DimensionSize.maxScreenCol;
        this.HEIGHT = DimensionSize.maxScreenRow;
        this.self = self;
        this.target = target;
        this.start = self.getPosition();
        this.goal = target.getPosition();
        this.km = 0;

        initGrid(gp.tileManager.tile, gp.map.mapTileNum);
        initCost();
        initQueue();
    }

    void initQueue() {
        comparator = (s1, s2) -> {
            double[] k1 = calculateKey(s1);
            double[] k2 = calculateKey(s2);

            if (k1[0] < k2[0]) return -1;
            if (k1[0] > k2[0]) return 1;
            if (k1[1] < k2[1]) return -1;
            if (k1[1] > k2[1]) return 1;
            return 0;
        };

        U = new PriorityQueue<>(comparator);
        U.add(goal);
    }

    void initGrid(Tile[] tile, int[][] mapTileNum) {
        grid = new boolean[WIDTH][HEIGHT];

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                grid[i][j] = tile[mapTileNum[i][j]].collision;
            }
        }

        if (grid[start.x][start.y])
            throw new IllegalArgumentException("Can not start from an obstacle.");

        if (grid[goal.x][goal.y])
            throw new IllegalArgumentException("Goal can not be an obstacle.");
    }

    void initCost() {
        g = new HashMap<>();
        rhs = new HashMap<>();
        cost = new HashMap<>();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Point s = new Point(i, j);
                g.put(s, INF);
                rhs.put(s, INF);

                for (int[] dir: DIRECTIONS) {
                    Point neighbor = new Point(i + dir[0], j + dir[1]);
                    if (isValid(neighbor)) {
                        double edgeCost = 1.0;
                        if (grid[i][j] || grid[neighbor.x][neighbor.y]) {
                            edgeCost = INF;
                        }

                        Pair key = new Pair(s, neighbor);
                        cost.put(key, edgeCost);
                    }
                }
            }
        }
        rhs.put(goal, 0.0);
    }

    public void resetCost() {
        rhs.clear();
        g.clear();
        U.clear();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                Point s = new Point(i, j);
                g.put(s, INF);
                rhs.put(s, INF);

                for (int[] dir: DIRECTIONS) {
                    Point neighbor = new Point(i + dir[0], j + dir[1]);
                    if (isValid(neighbor)) {
                        double edgeCost = 1.0;
                        if (grid[i][j] || grid[neighbor.x][neighbor.y]) {
                            edgeCost = INF;
                        }

                        Pair key = new Pair(s, neighbor);
                        cost.put(key, edgeCost);
                    }
                }
            }
        }
    }

    double[] calculateKey(Point s) {
        double k1 = Math.min(g.get(s), rhs.get(s)) + h(s, start) + km;
        double k2 = Math.min(g.get(s), rhs.get(s));
        return new double[]{k1, k2};
    }

    boolean isValid(Point state) {
        return 0 <= state.x && state.x < WIDTH &&
                0 <= state.y && state.y < HEIGHT;
    }

    boolean isObstacles(Point s) {
        return isValid(s) && grid[s.x][s.y];
    }

    double h(Point s1, Point s2) {
        return Math.abs(s1.x - s2.x) + Math.abs(s1.y - s2.y);
    }

    void updateState(Point u) {
        if (!u.equals(goal)) {
            double minRhs = INF;
            for (int[] dir: DIRECTIONS) {
                Point neighbor = new Point(u.x + dir[0], u.y + dir[1]);
                if (isValid(neighbor)) {
                    double c = getCost(u, neighbor);
                    minRhs = Math.min(minRhs, g.get(neighbor) + c);
                }
            }
            rhs.put(u, minRhs);
        }

        U.remove(u);

        if (g.get(u) != rhs.get(u)) {
            U.add(u);
        }
    }

    double getCost(Point s1, Point s2) {
        Pair key = new Pair(s1, s2);
        return cost.getOrDefault(key, INF);
    }

    void setCost(Point s1, Point s2, double c) {
        cost.put(new Pair(s1, s2), c);
        cost.put(new Pair(s2, s1), c);
        updateState(s1);
        updateState(s2);
    }

    public Point getSelfPosition() {
        return self.getPosition();
    }

    public Entity getTarget() {
        return target;
    }

    public Point getTargetPosition() {
        return target.getPosition();
    }

    public Point getGoal() {
        return goal;
    }

    public void setNewGoal(Point newGoal) {
        goal = newGoal;
        resetCost();
        rhs.put(goal, 0.0);
        U.add(goal);

        computeShortestPath();
    }

    public void addObstacle(Point obstacle) {
        if (isObstacles(obstacle)) return;
        grid[obstacle.x][obstacle.y] = true;

        for (int[] dir : DIRECTIONS) {
            Point neighbor = new Point(obstacle.x + dir[0], obstacle.y + dir[1]);
            if (isValid(neighbor)) {
                setCost(obstacle, neighbor, INF);
                setCost(neighbor, obstacle, INF);
            }
        }
    }

    public void removeObstacle(Point obstacle) {
        if (!isObstacles(obstacle)) return;
        grid[obstacle.x][obstacle.y] = false;

        for (int[] dir : DIRECTIONS) {
            Point neighbor = new Point(obstacle.x + dir[0], obstacle.y + dir[1]);
            if (!isObstacles(neighbor)) {
                setCost(obstacle, neighbor, 1.0);
                setCost(neighbor, obstacle, 1.0);
            }
        }
    }

    public void updateObstacles(ArrayList<Point> removed, ArrayList<Point> added) {
        removed.forEach(this::removeObstacle);
        added.forEach(this::addObstacle);
        if (added.isEmpty() && removed.isEmpty()) return;
        computeShortestPath();
    }

    public void computeShortestPath() {
        while (!U.isEmpty() &&
                (comparator.compare(U.peek(), start) < 0 ||
                        !rhs.get(start).equals(g.get(start)))) {

            Point u = U.poll();
            assert u != null;
            if (g.get(u).equals(rhs.get(u))) continue; // node hop le

            if (g.get(u) > rhs.get(u)) {
                g.put(u, rhs.get(u));
            } else {
                g.put(u, INF);
                updateState(u);
            }

            for (int[] dir: DIRECTIONS) {
                Point neighbor = new Point(u.x + dir[0], u.y + dir[1]);
                if (isValid(neighbor))
                    updateState(neighbor);
            }
        }
    }

    public List<Point> findPath() {
        List<Point> path = new ArrayList<>();
        Point current = start;
        path.add(current);

        computeShortestPath();

        if (rhs.get(start) == INF) {
            System.out.println("Can not find path from " + start + " to " + goal);
            return path;
        }

        while (!current.equals(goal)) {
            double minCost = INF;
            Point nextState = null;
            for (int[] dir: DIRECTIONS) {
                Point neighbor = new Point(current.x + dir[0], current.y + dir[1]);
                if (isValid(neighbor) && !isObstacles(neighbor)) {
                    double c = getCost(current, neighbor) + g.get(neighbor);
                    if (c < minCost) {
                        minCost = c;
                        nextState = neighbor;
                    }
                }
            }

            if (nextState == null || minCost == INF) {
                System.out.println("Can not find next path from " + current);
                break;
            }

            current = nextState;
            path.add(current);
        }
        return path;
    }

    public void moveAndRescan(Point newStart) {
        if (isObstacles(newStart)) {
            System.out.println("Can not move to an obstacle " + newStart);
            return;
        }

        km += h(start, newStart);
        start = newStart;

        computeShortestPath();
    }

    public Point fleeingPoint() {
        int[][] distFromTarget = new int[WIDTH][HEIGHT];
        for (int[] row: distFromTarget) Arrays.fill(row, -1);

        if (!target.getPosition().equals(goal)) {
            goal = target.getPosition();
        }

        Queue<Point> queue = new LinkedList<>();
        queue.add(goal);
        distFromTarget[goal.x][goal.y] = 0;

        while (!queue.isEmpty()) {
            Point p = queue.poll();
            for (int[] dir: DIRECTIONS) {
                Point n = new Point(p.x + dir[0], p.y + dir[1]);
                if (isValid(n) && !isObstacles(n) && distFromTarget[n.x][n.y] == -1) {
                    distFromTarget[n.x][n.y] = distFromTarget[p.x][p.y] + 1;
                    queue.add(n);
                }
            }
        }

        int maxDist = -1;
        Point f = start;

        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                if (!grid[x][y] && distFromTarget[x][y] > maxDist) {
                    maxDist = distFromTarget[x][y];
                    f = new Point(x, y);
                }
            }
        }

        return f;
    }

    public void flee() {

    }

    public void detectEnvironmentChanges(Map<Point, Map<Point, Double>> changedEdges) {
        for (Map.Entry<Point, Map<Point, Double>> entry: changedEdges.entrySet()) {
            Point from = entry.getKey();

            if (isObstacles(from)) continue;

            for (Map.Entry<Point, Double> edge: entry.getValue().entrySet()) {
                Point to = edge.getKey();

                if (isObstacles(to)) continue;

                double newCost = edge.getValue();
                setCost(from, to, newCost);
            }
        }

        computeShortestPath();
    }

    public void printMap(List<Point> path) {
        Set<Point> pathSet = new HashSet<>(path);
        System.out.println("\nMAP:");
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Point current = new Point(x, y);
                if (current.equals(start)) {
                    System.out.print("S ");
                } else if (current.equals(goal)) {
                    System.out.print("G ");
                } else if (grid[x][y]) {
                    System.out.print("X ");
                } else if (pathSet.contains(current)) {
                    System.out.print("* ");
                } else {
                    System.out.print(". ");
                }
            }
            System.out.println();
        }
    }
}
