package entity.monster;

import entity.Monster;
import java.awt.Point;
import java.util.List;

/**
 * Interface định nghĩa hành vi của các trạng thái của Monster
 */
public interface MonsterState {
    /**
     * Cập nhật trạng thái của monster dựa trên các điều kiện
     * @param monster Monster cần cập nhật trạng thái
     */
    void updateState(Monster monster);

    /**
     * Tính toán lại đường đi cho monster
     * @param monster Monster cần tính toán đường đi
     */
    void recalculatePath(Monster monster);

    /**
     * Kiểm tra xem monster có thể chuyển sang trạng thái khác không
     * @param monster Monster cần kiểm tra
     * @return true nếu có thể chuyển trạng thái, false nếu không
     */
    // boolean canTransition(Monster monster);

    /**
     * Lấy tên của trạng thái
     * @return Tên trạng thái
     */
    String getName();
} 