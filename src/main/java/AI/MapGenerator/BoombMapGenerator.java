package AI.MapGenerator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class BoombMapGenerator {
    private int wall = 1;
    private int destroyable = 2;
    private int empty = 0;
    private int POPULATION_SIZE = 100;
    private int GENERATIONS = 100;
    private double MUTATION_RATE = 0.05;
    public BoombMapGenerator(){
    }

    public void generateMap(int monsterCount, int mapWidth, int mapHeight) {
        List<int[][]> population = new ArrayList<>();
        // Random rand = new Random();
    
        // Khởi tạo quần thể ban đầu
        for (int i = 0; i < POPULATION_SIZE; i++) {
            int[][] newMap = randomMap(mapWidth, mapHeight);
            placeMonsters(newMap, monsterCount);
            if(Fitness.evaluateFitness(newMap) < 0.7){
                i--;
                continue;
            }
            population.add(newMap);
        }
    
        double previousAverageFitness = 0.0;
    
        // Lặp qua các thế hệ
        for (int generation = 0; generation < GENERATIONS; generation++) {
            // Tính điểm fitness
            List<Double> fitnessScores = new ArrayList<>();
            for (int[][] map : population) {
                double fitness = Fitness.evaluateFitness(map);
                fitnessScores.add(fitness);
                if(fitness <0)System.out.println(fitness);
            }
            
            // Tính fitness trung bình
            double averageFitness = fitnessScores.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
    
            // Điều kiện dừng động
            if (Math.abs(averageFitness - previousAverageFitness) < 0.001) {
                System.out.println("stop at generation "+ generation);
                StoredMap.saveMap(sortPopulation(population).get(0));
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
    
                double childFitness = Fitness.evaluateFitness(child);
                if (childFitness > 0.5 * averageFitness) { // Ngưỡng để thêm con vào
                    newPopulation.add(child);
                }
            }
            int eliteCount = (int) (POPULATION_SIZE * 0.1);
            population.sort((a, b) -> Double.compare(Fitness.evaluateFitness(b), Fitness.evaluateFitness(a)));
            List<int[][]> elites = new ArrayList<>(population.subList(0, eliteCount));
            newPopulation.addAll(elites);
            population = newPopulation;
        }
        StoredMap.saveMap(sortPopulation(population).get(0));
    }

    public int[][] randomMap(int mapWidth, int mapHeight){
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

        //bỏ trống điểm bắt đầu
        map[1][1] = empty;
        map[2][1] = empty;
        map[1][2] = empty;
        return map;
    }

    public void placeMonsters(int[][] map, int monsterCount) {
        Random rand = new Random();
        int placedMonsters = 0;
    
        while (placedMonsters < monsterCount) {
            int x = rand.nextInt(map.length);
            int y = rand.nextInt(map[0].length);
            if (map[x][y] == 0 ) {
                map[x][y] = 3; 
                placedMonsters++;
            }
        }
    }
    

    private List<int[][]> sortPopulation(List<int[][]> population) {
        population.sort((map1, map2) -> Double.compare(Fitness.evaluateFitness(map2), Fitness.evaluateFitness(map1)));
        return population;
    }

    public  int[][] selectParent(List<int[][]> population, List<Double> fitnessScores) {
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

    public  int[][] crossoverMaps(int[][] parent1, int[][] parent2, int monsterCount) {
        int rows = parent1.length;
        int cols = parent1[0].length;
        int[][] offspring = new int[rows][cols];
    
        // Giao phối: Chọn nửa trên từ parent1, nửa dưới từ parent2
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i < rows / 2) {
                    offspring[i][j] = parent1[i][j]; // Nửa trên từ parent1
                    
                } else {
                    offspring[i][j] = parent2[i][j]; // Nửa dưới từ parent2
                    if(parent1[i][j] == 3 || parent2[i][j] == 3){
                        offspring[i][j] = 3;
                        continue;
                    }
                }
            }
        }
    
        // Đảm bảo số lượng quái vật trong offspring
        adjustMonsters(offspring, monsterCount);
    
        return offspring;
    }
    

   public  void adjustMonsters(int[][] map, int monsterCount) {
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

    

    private  void mutate(int[][] map) {
        Random rand = new Random();
        int rows = map.length;
        int cols = map[0].length;

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

    public static void main(String[] args) {
        BoombMapGenerator a = new BoombMapGenerator();
        a.generateMap(8, 16, 12);
    }
}
