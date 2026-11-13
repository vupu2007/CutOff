import java.util.Random;

/**
 * QuickSortCutoff — Sắp xếp nhanh có CUTOFF, dùng 3-way partition.
 *
 * Tại sao 3-way (Dutch National Flag)?
 *   - Với 2-way partition, nếu có nhiều phần tử trùng pivot → O(n^2).
 *   - 3-way chia mảng thành 3 vùng: < pivot | == pivot | > pivot.
 *     Vùng "== pivot" KHÔNG cần đệ quy tiếp → tránh được worst case trên
 *     dữ liệu nhiều trùng lặp.
 *
 * Random có seed cố định để kết quả thí nghiệm tái lập được.
 */
public class QuickSortCutoff {

    private static final Random RNG = new Random(42L);

    public static void sort(int[] a) {
        sort(a, 16);
    }

    public static void sort(int[] a, int cutoff) {
        if (a == null || a.length < 2) return;
        if (cutoff < 1) cutoff = 1;
        sort(a, 0, a.length, cutoff);
    }

    /** Sắp xếp a[lo..hi). */
    private static void sort(int[] a, int lo, int hi, int cutoff) {
        // ---- BASE CASE: đoạn đủ nhỏ → InsertionSort ----
        if (hi - lo <= cutoff) {
            InsertionSort.sort(a, lo, hi);
            return;
        }

        // Chọn pivot ngẫu nhiên để tránh worst case (mảng đã sắp / ngược)
        int pivotIdx = lo + RNG.nextInt(hi - lo);
        int pivot = a[pivotIdx];

        // ---- 3-way partition ----
        // Vùng < pivot : [lo .. lt-1]
        // Vùng = pivot : [lt .. gt]
        // Vùng > pivot : [gt+1 .. hi-1]
        int lt = lo;
        int i  = lo;
        int gt = hi - 1;
        while (i <= gt) {
            if      (a[i] < pivot) swap(a, lt++, i++);
            else if (a[i] > pivot) swap(a, i, gt--);  // KHÔNG tăng i
            else                   i++;
        }
        // Lúc này phần "= pivot" đã nằm đúng chỗ, không cần đệ quy.
        sort(a, lo, lt, cutoff);           // đệ quy phần < pivot
        sort(a, gt + 1, hi, cutoff);       // đệ quy phần > pivot
    }

    private static void swap(int[] a, int i, int j) {
        int t = a[i]; a[i] = a[j]; a[j] = t;
    }
}
