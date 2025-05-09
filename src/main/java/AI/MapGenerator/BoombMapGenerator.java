package AI.MapGenerator;

import res.LoadResource;
import main.Main;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

public class BoombMapGenerator {
    private static final int wall = 1;
    private static final int destroyable = 2;
    private static final int empty = 0;
    private static final int POPULATION_SIZE = 100;
    private static final int GENERATIONS = 100;
    private static final double MUTATION_RATE = 0.05;
    private static final int itemScore = 200;
    public static int[][] randomMap(int mapWidth, int mapHeight){
        int map[][] = new int[mapHeight][mapWidth];
        Random rand = new Random();

         // Tạo tường cố định và biên
         for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                if (i == 0 || i == mapHeight - 1 || j == 0 || j ==  mapWidth - 1 ) {
                    map[i][j] = wall;
                } else {
                    map[i][j] = (rand.nextDouble() < 0.3) ? wall:((rand.nextDouble() < 0.7) ? destroyable:empty);
                }
            }
        }

        //bỏ trống 4 gócgóc
        map[1][1] = empty;
        map[2][1] = empty;
        map[1][2] = empty;

        map[1][mapWidth - 2] = empty;
        map[2][mapWidth - 2] = empty;
        map[1][mapWidth - 3] = empty;

        map[mapHeight-2][1] = empty;
        map[mapHeight-3][1] = empty;
        map[mapHeight-2][2] = empty;

        map[mapHeight-2][mapWidth - 2] = empty;
        map[mapHeight-3][mapWidth - 2] = empty;
        map[mapHeight-2][mapWidth - 3] = empty;
        return map;
    }

    public static void placeMonsters(int[][] map, int monsterCount) {
        Random rand = new Random();
        int placedMonsters = 0;
    
        while (placedMonsters < monsterCount) {
            int x = rand.nextInt(map.length);
            int y = rand.nextInt(map[0].length);
            if (map[x][y] == 0 || map[x][y] == 22) {
                map[x][y] = 3; 
                placedMonsters++;
            }
        }
    }
    

    private static double evaluateFitness(int[][] map) {
        double blockProbality = evaluateBlockProbability(map);
        double connectivityScore = evaluateConnectivity(map);
        double wallBalanceScore = evaluateWallBalance(map);
        double safeStartScore = evaluateSafeStart(map);
        double monsterMobility = evaluateMonsterMobility(map);
        double monsterDistribution = evaluateMonsterDistribution(map);
    
        // Trọng số cho các tiêu chí
        double w1 = 0.1, w2 = 0.25, w3 = 0.15, w5 = 0.3, w6 = 0.2;
    
        // Tổng hợp điểm số
        return 2 *safeStartScore * (w1* blockProbality + w2 * connectivityScore + w3 * wallBalanceScore + w5 * monsterMobility + w6 * monsterDistribution);
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
        double idealRatio = 0.35;
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
    
    public static double evaluateMonsterMobility(int[][] map) {
        int rows = map.length;
        int cols = map[0].length;
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  // Các hướng di chuyển (trái, phải, lên, xuống)
        
        boolean[][] visited = new boolean[rows][cols];  // Mảng lưu trạng thái đã thăm
        double totalFitnessScore = 0.0;
        int monterZones = 0;
        // Duyệt qua toàn bộ bản đồ để tìm các ô quái
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (map[i][j] == 3 && !visited[i][j]) {  // Nếu gặp quái và chưa thăm
                    List<int[]> zone = new ArrayList<>();
                    Queue<int[]> queue = new LinkedList<>();
                    queue.add(new int[]{i, j});
                    visited[i][j] = true;
                    zone.add(new int[]{i, j});
                    int monsterCountInZone = 0;
                    // BFS mở rộng đến các ô trống xung quanh
                    while (!queue.isEmpty()) {
                        int[] current = queue.poll();
                        for (int[] dir : directions) {
                            int x = current[0] + dir[0];
                            int y = current[1] + dir[1];
                            if (x >= 0 && x < rows && y >= 0 && y < cols && !visited[x][y]) {
                                if (map[x][y] == 0 ) {  // Nếu là ô trống hoặc có quái
                                    visited[x][y] = true;
                                    queue.add(new int[]{x, y});
                                    zone.add(new int[]{x, y});  // Thêm ô vào vùng
                                }
                                if(map[x][y] == 3){
                                    visited[x][y] = true;
                                    queue.add(new int[]{x, y});
                                    zone.add(new int[]{x, y});
                                    monsterCountInZone ++;
                                }
                            }
                        }
                    }
                    int zoneSize = zone.size();  // Kích thước của vùng
                    int idealMonsterCount = zoneSize / 9;
                    if (zoneSize % 9 != 0) {
                        idealMonsterCount++;  // Nếu không chia hết, thêm 1 quái vào
                    }
                    
                    // Phạt nếu số quái quá ít hoặc quá nhiều
                    double fitnessForZone = 1.0 / (1.0 + Math.abs(monsterCountInZone - idealMonsterCount));
                    totalFitnessScore += fitnessForZone;  // Cộng điểm cho vùng này
                    // monsterZones.add(zone);  // Thêm vùng mà quái có thể di chuyển vào danh sách
                    monterZones++;
                }
            }
        }
        return totalFitnessScore / monterZones;
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
    
    

    private static List<int[][]> sortPopulation(List<int[][]> population) {
        population.sort((map1, map2) -> Double.compare(evaluateFitness(map2), evaluateFitness(map1)));
        return population;
    }

    public static int[][] selectParent(List<int[][]> population, List<Double> fitnessScores) {
        double totalFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).sum();
        double r = Math.random() * totalFitness;
        double cumulativeFitness = 0.0;
    
        for (int i = 0; i < population.size(); i++) {
            cumulativeFitness += fitnessScores.get(i);
            if (r <= cumulativeFitness) {
                return population.get(i);
            }
        }
    
        // Trường hợp lỗi hiếm gặp (r vượt tổng fitness do lỗi làm tròn)
        return population.get(population.size() - 1);
    }

    public static int[][] crossoverMaps(int[][] parent1, int[][] parent2, int monsterCount) {
        int rows = parent1.length;
        int cols = parent1[0].length;
        int[][] offspring = new int[rows][cols];
    
        // Giao phối: Chọn nửa trên từ parent1, nửa dưới từ parent2
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if(parent1[i][j] == 3 || parent2[i][j] == 3){
                    offspring[i][j] = 3;
                    continue;
                }
                if (i < rows / 2) {
                    offspring[i][j] = parent1[i][j]; // Nửa trên từ parent1
                } else {
                    offspring[i][j] = parent2[i][j]; // Nửa dưới từ parent2
                }
            }
        }
    
        // Đảm bảo số lượng quái vật trong offspring
        adjustMonsters(offspring, monsterCount);
    
        return offspring;
    }
    

   public static void adjustMonsters(int[][] map, int monsterCount) {
    List<int[]> monsterPositions = new ArrayList<>();
    // Tạo danh sách lưu số lượng quái trong từng khu vực (9 khu vực)
    int gridRows = map.length / 3;
    int gridCols = map[0].length / 3;
    int[] regionMonsterCount = new int[9];
    Map<Integer, List<int[]>> regionMonsterPositions = new HashMap<>();

    // Khởi tạo danh sách quái trong từng khu vực
    for (int i = 0; i < 9; i++) {
        regionMonsterPositions.put(i, new ArrayList<>());
    }

    // Duyệt qua bản đồ
    for (int i = 0; i < map.length; i++) {
        for (int j = 0; j < map[i].length; j++) {
            int regionIndex = (i / gridRows) * 3 + (j / gridCols);

            // Xác định quái
            if (map[i][j] == 3) {
                monsterPositions.add(new int[]{i, j});
                regionMonsterCount[regionIndex]++;
                regionMonsterPositions.get(regionIndex).add(new int[]{i, j});
            }

        }
    }

    Random rand = new Random();

    // Loại bỏ quái nếu số lượng lớn hơn yêu cầu
    while (monsterPositions.size() > monsterCount) {
        // Tìm khu vực ưu tiên để loại bỏ
        int regionToRemove = 0; // Mặc định khu vực 0
        int maxMonsters = regionMonsterCount[0];

        // Tìm khu vực đông quái nhất
        for (int i = 0; i < 9; i++) {
            if (regionMonsterCount[i] > maxMonsters) {
                maxMonsters = regionMonsterCount[i];
                regionToRemove = i;
            }
        }

        // Loại bỏ quái từ khu vực này
        if (regionMonsterCount[regionToRemove] > 0) {
            List<int[]> monstersInRegion = regionMonsterPositions.get(regionToRemove);
            int[] position = monstersInRegion.remove(rand.nextInt(monstersInRegion.size()));
            map[position[0]][position[1]] = 0; // Loại bỏ quái
            monsterPositions.removeIf(pos -> pos[0] == position[0] && pos[1] == position[1]);
            regionMonsterCount[regionToRemove]--;
        }
    }
}

    

    private static void mutate(int[][] map) {
        Random rand = new Random();
        int rows = map.length;
        int cols = map[0].length;
        // Duyệt qua các ô trên bản đồ, bỏ qua tường cố định
        for (int i = 1; i < rows - 1; i++) {
            for (int j = 1; j < cols - 1; j++) {
                if (rand.nextDouble() < MUTATION_RATE) {
                    // Chỉ thay đổi các ô có thể phá hủy hoặc ô trống
                    if (map[i][j] == 0) {
                        map[i][j] = 2; // Đổi ô trống thành tường phá hủy
                    } else if (map[i][j] == 2) {
                        map[i][j] = 0; // Đổi tường phá hủy thành ô trống
                    }
                }
            }
        }
    }
    
    public static void generateMap(int monsterCount, int mapWidth, int mapHeight) {
        List<int[][]> population = new ArrayList<>();
        // Random rand = new Random();
    
        // Khởi tạo quần thể ban đầu
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[][] newMap = randomMap(mapWidth, mapHeight);
            placeMonsters(newMap, monsterCount);
            if(evaluateFitness(newMap) < 0.4){
                i--;
                continue;
            }
            population.add(newMap);
        }
    
        double previousAverageFitness = 0.0;
    
        // Lặp qua các thế hệ
        for (int generation = 0; generation < GENERATIONS; generation++) {
            // Tính điểm fitnessSystem.out.println("generation "+ generation);
            List<Double> fitnessScores = new ArrayList<>();
            for (int[][] map : population) {
                double fitness = evaluateFitness(map);
                fitnessScores.add(fitness);
                if(fitness <0)System.out.println(fitness);
            }
            
            // Tính fitness trung bình
            double averageFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    
            // Điều kiện dừng động
            if (Math.abs(averageFitness - previousAverageFitness) < 0.001) {
                System.out.println("stop at generation "+ generation);
                storedMap(sortPopulation(population).get(0));
                return;
            } // Dừng nếu không cải thiện
            previousAverageFitness = averageFitness;
    
            // Chọn cha mẹ và tạo thế hệ mới
            List<int[][]> newPopulation = new ArrayList<>();
            while (newPopulation.size() < POPULATION_SIZE) {
                int[][] parent1 = selectParent(population, fitnessScores);
                int[][] parent2;
                do {
                    parent2 = selectParent(population, fitnessScores);
                } while (parent1 == parent2);
    
                int[][] child = crossoverMaps(parent1, parent2, monsterCount);
                mutate(child);
    
                double childFitness = evaluateFitness(child);
                if (childFitness > 0.5 * averageFitness) { // Ngưỡng để thêm con vào
                    newPopulation.add(child);
                }
            }
    
            // Elitism: Giữ lại 10% cá thể tốt nhất
            int eliteCount = (int) (POPULATION_SIZE * 0.1);
            population.sort((a, b) -> Double.compare(evaluateFitness(b), evaluateFitness(a)));
            List<int[][]> elites = new ArrayList<>(population.subList(0, eliteCount));
            newPopulation.addAll(elites);
    
            // Thay thế quần thể hiện tại
            population = newPopulation;
        }
    
        // Trả về bản đồ tốt nhất
        storedMap(sortPopulation(population).get(0));
    }

    public static String createListItem(){
        String re = new String();
        int currentItemScore = 0;
        int sizeOfListItem = 0;
        List<String> listItem = new ArrayList<>();
        for (String string : LoadResource.itemScoreMap.keySet()) {
            if(LoadResource.itemScoreMap.get(string) != 0){
                sizeOfListItem++;
                listItem.add(string);
            }
        }

        int[] itemQuantity = new int[sizeOfListItem];
        int rand;
        // limit some item in map.
        while (currentItemScore < itemScore) {
            rand = (int) (Math.random() * sizeOfListItem);
            if(((listItem.get(rand).equals("shoe")|| 
                listItem.get(rand).equals("shield") ||
                listItem.get(rand).equals("clock") ||
                listItem.get(rand).equals("heart"))&& itemQuantity[rand] > 3)
                || listItem.get(rand).equals("coin"))continue;
            itemQuantity[rand]++;
            currentItemScore += LoadResource.itemScoreMap.get(listItem.get(rand));
        }
        rand = (int) (Math.random() * 4 + 1);
        itemQuantity[listItem.indexOf("coin")] = rand;// quantity of coin is not more 5;
        for(int i = 0; i < listItem.size(); i++){
            re += String.valueOf(itemQuantity[i])+ " " +listItem.get(i) + ";";
        }
        re = re.substring(0, re.length()-1);
        return re;
    }
    
    public static List<int[]> getMonsterList(int[][] map){
        List<int[]> re = new ArrayList<>();
        for(int i = 0; i < map.length; i++ ){
            for(int j = 0; j < map[0].length; j++){
                if(map[i][j] == 3){
                    re.add(new int[]{i,j});
                }
            }
        }
        return re;
    }
 
    public static String toStringMonsterList(List<int[]> monsterList){
        String re = "";
        for(int[]a : monsterList){
            re += String.valueOf(a[0]) + "," + String.valueOf(a[1]) + ";" ;
        }
        if (re.length() > 0) {
            re = re.substring(0, re.length()-1);
        }
        return re.toString();
    }

    public static void storedMap(int[][] map) {
        try {
            FileWriter writer = new FileWriter(Main.res + "/maps/levelRandom.txt");

            writer.write("grass_2\n");
            writer.write(createListItem()+"\n");

            List<int[]> monterList = new ArrayList<>();
            String stringMap = new String();
            for(int i = 0; i < map.length; i++ ){
                stringMap +="\n";
                for(int j = 0; j < map[0].length; j++){
                    if(map[i][j] == 3){
                        monterList.add(new int[]{i,j});
                        stringMap += String.valueOf(0) + " ";
                    }
                    else{
                        stringMap += String.valueOf(map[i][j]) + " ";
                    }
                }
            }
            
            writer.write(toStringMonsterList(monterList)+ "\n");
            writer.write("1,1");
            writer.write(stringMap);
            writer.close();
            System.out.println("Create map completed");
        } catch (Exception e) {
            System.out.println("Errol save file");
        }
    }

    public static void main(String[] args) {
        generateMap(7,16,12);
    }
}
