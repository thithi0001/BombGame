package AI.MapGenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class Fitness {
    public static double evaluateFitness(int[][] map) {
        double blockProbality = evaluateBlockProbability(map);
        double connectivityScore = evaluateConnectivity(map);
        double wallBalanceScore = evaluateWallBalance(map);
        double safeStartScore = evaluateSafeStart(map);
        double monsterDistribution = evaluateMonsterDistribution(map);
    
        // Trọng số cho các tiêu chí
        double w1 = 0.1, w2 = 0.25, w3 = 0.15, w6 = 0.2;
    
        // Tổng hợp điểm số
        return 2 *safeStartScore * (w1* blockProbality + w2 * connectivityScore + w3 * wallBalanceScore + w6 * monsterDistribution);
    }

    public static double evaluateBlockProbability(int[][] map) {
        int mapHeight = map.length;
        int mapWidth = map[0].length;
    
        // Biến tổng hợp
        double groupBalanceScore = 0; // Điểm cho sự phân bố nhóm tường phá hủy
        
        int groupCount = 0;   // Số nhóm ô phá hủy
        int totalGroupCells = 0;   // Tổng số ô thuộc nhóm tường phá hủy
        
        // Duyệt qua tất cả các ô trên bản đồ để tìm nhóm tường phá hủy
        boolean[][] visited = new boolean[mapHeight][mapWidth];
        
        // Dùng DFS hoặc BFS để tìm các nhóm liên tiếp của ô phá hủy
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (map[i][j] == 2 && !visited[i][j]) {
                    // Tìm nhóm ô phá hủy bắt đầu từ (i, j)
                    List<int[]> group = new ArrayList<>();
                    dfs(map, i, j, visited, group);
                    
                    // Cập nhật số lượng nhóm và tổng số ô thuộc nhóm
                    groupCount++;
                    totalGroupCells += group.size();
                }
            }
        }
        
        // Tính điểm sự phân bố nhóm
        if (groupCount > 0) {
            groupBalanceScore = (double) totalGroupCells / (mapHeight * mapWidth);
        }
    
        // Trọng số cho sự phân bố nhóm
        double groupBalanceWeight = 1.0; // Bạn có thể điều chỉnh trọng số này
        
        // Trả về điểm fitness (sử dụng thêm các yếu tố khác nếu cần)
        return groupBalanceWeight * groupBalanceScore;
    }
    
    // Hàm tìm nhóm ô phá hủy sử dụng DFS
    private static void dfs(int[][] map, int i, int j, boolean[][] visited, List<int[]> group) {
        int mapHeight = map.length;
        int mapWidth = map[0].length;
    
        if (i < 0 || i >= mapHeight || j < 0 || j >= mapWidth || map[i][j] != 2 || visited[i][j]) {
            return;
        }
        visited[i][j] = true;
        group.add(new int[]{i, j});  
        // Thực hiện DFS ở các hướng 4 chiều
        dfs(map, i - 1, j, visited, group); // Up
        dfs(map, i + 1, j, visited, group); // Down
        dfs(map, i, j - 1, visited, group); // Left
        dfs(map, i, j + 1, visited, group); // Right
    }
    
    

    public static double evaluateConnectivity(int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
    
        boolean[][] visited = new boolean[rows][cols];
        int totalCells = 0; // Tổng số ô có thể kết nối
        int reachableCells = 0; // Số ô có thể tiếp cận
        int disconnectedRegions = 0; // Số vùng không kết nối
    
        // Đếm tổng số ô hợp lệ
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == 0 || map[i][j] == 2 || map[i][j] == 3) {
                    totalCells++;
                }
            }
        }
    
        // Duyệt toàn bộ bản đồ để tìm các vùng kết nối
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!visited[i][j] && (map[i][j] == 0 || map[i][j] == 2 || map[i][j] == 3)) {
                    // Tìm thấy một vùng kết nối mới, sử dụng BFS để đếm số ô trong vùng
                    int regionSize = bfsCountRegion(map, visited, i, j);
                    reachableCells += regionSize;
                    disconnectedRegions++;
                }
            }
        }
    
        // Tính tỷ lệ ô kết nối được trên tổng số ô
        double connectivityScore = (double) reachableCells / totalCells;
    
        // Phạt nếu có nhiều vùng không kết nối
        double penalty = disconnectedRegions > 1 ? 1.0 / (1.0 + disconnectedRegions - 1) : 1.0;
    
        // Điểm cuối cùng là điểm kết nối nhân với hệ số phạt
        return connectivityScore * penalty;
    }
    
    // BFS để đếm số ô trong một vùng kết nối
    private static int bfsCountRegion(int[][] map, boolean[][] visited, int startX, int startY) {
        int rows = map.length;
        int cols = map[0].length;
        Queue<int[]> queue = new LinkedList<>();
        int regionSize = 0;
    
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}}; // Các hướng di chuyển
    
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;
    
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            regionSize++;
    
            for (int[] dir : directions) {
                int x = current[0] + dir[0];
                int y = current[1] + dir[1];
    
                // Kiểm tra tính hợp lệ của ô
                if (x >= 0 && x < rows && y >= 0 && y < cols &&
                    !visited[x][y] &&
                    (map[x][y] == 0 || map[x][y] == 2 || map[x][y] == 3)) {
                    visited[x][y] = true;
                    queue.add(new int[]{x, y});
                }
            }
        }
        return regionSize;
    }
    

    private static double evaluateWallBalance(int[][] map) {
        int destroyableCount = 0;
        int emptyCount = 0;
        double idealRatio = 0.45;
        // Duyệt qua bản đồ để đếm số lượng
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] == 2) destroyableCount++;
                if (map[i][j] == 0) emptyCount++;
            }
        }
    
        // Tính tỷ lệ thực tế
        double totalWeight = destroyableCount + emptyCount;
        double actualRatio = destroyableCount / totalWeight;
    
        // Tính điểm dựa trên độ lệch so với tỷ lệ lý tưởng
        double balanceScore = 1 - Math.abs(idealRatio - actualRatio);
        return Math.max(0, balanceScore); // Đảm bảo điểm không âm
    }
    

    private static double evaluateSafeStart(int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        
        // Khởi tạo BFS từ ô (1,1) nếu nó là ô trống
        if (map[1][1] != 0) {
            return 0; // Nếu ô (1,1) không phải là ô trống, trả về điểm 0
        }
    
        boolean[][] visited = new boolean[rows][cols];
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{1, 1});  // Bắt đầu từ ô (1,1)
        visited[1][1] = true;
    
        int safeCount = 0;
        int monsterPenalty = 0;
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
    
        // BFS để tìm tất cả các ô trống liên kết với ô (1,1)
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int x = current[0];
            int y = current[1];
    
            // Kiểm tra ô hiện tại
            if (map[x][y] == 0) { // Ô trống
                safeCount++;
            } else if (map[x][y] == 3) { // Ô có quái
                safeCount++;
                monsterPenalty++;
            }
    
            // Thêm các ô lân cận vào queue nếu chúng là ô trống và chưa được thăm
            for (int[] dir : directions) {
                int nx = x + dir[0];
                int ny = y + dir[1];
    
                // Kiểm tra tính hợp lệ của ô (nx, ny)
                if (nx >= 0 && nx < rows && ny >= 0 && ny < cols && !visited[nx][ny] && map[nx][ny] == 0) {
                    visited[nx][ny] = true;
                    queue.add(new int[]{nx, ny});
                }
            }
        }
    
        
        
    
        // Tính điểm dựa trên số lượng ô trống trong khu vực bắt đầu
        double idealSafeCount = 4; // Mục tiêu lý tưởng là 4 ô trống
        double score = 1.0 / (1.0 + Math.abs(safeCount - idealSafeCount));

        // Nếu có quái trong khu vực bắt đầu, điểm sẽ được chia cho số (quái + 1)
        if (monsterPenalty > 0) {
            return score/ (monsterPenalty + 1); 
        }
        return  score;// Tính điểm dựa trên sự chênh lệch
    }

    public static double evaluateMonsterDistribution(int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
    
        // Kích thước khu vực (tuỳ chỉnh)
        int regionSize = 3;
    
        // Số khu vực theo hàng và cột
        int regionsX = (int) Math.ceil((double) cols / regionSize);
        int regionsY = (int) Math.ceil((double) rows / regionSize);
    
        // Ma trận lưu số quái trong từng khu vực
        int[][] regionMonsterCount = new int[regionsY][regionsX];
        int monsterCount = 0;
        // Đếm số lượng quái trong từng khu vực
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == 3) { // Nếu ô là quái
                    int regionX = j / regionSize;
                    int regionY = i / regionSize;
                    regionMonsterCount[regionY][regionX]++;
                    monsterCount++;
                }
            }
        }
    
        // Số lượng lý tưởng quái mỗi khu vực
        double idealMonsterPerRegion = (double)  monsterCount/ (regionsX * regionsY);
        // Tính điểm phân bố
        double totalScore = 0.0;
        int regionCount = regionsX * regionsY;
        for (int y = 0; y < regionsY; y++) {
            for (int x = 0; x < regionsX; x++) {
                int monstersInRegion = regionMonsterCount[y][x];
    
                // Tính điểm cho khu vực
                // Điểm giảm dần khi số quái lệch nhiều khỏi số lý tưởng
                double score = 1.0 / (1.0 + Math.abs(monstersInRegion - idealMonsterPerRegion));
                totalScore += score;
            }
        }
        // Trả về điểm trung bình
        return totalScore / regionCount;
    }
}
