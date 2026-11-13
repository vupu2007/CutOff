import java.util.Arrays;
import java.util.Random;

/**
 * Main — Chạy thí nghiệm tìm CUTOFF tối ưu cho MergeSort và QuickSort.
 *
 * Quy trình:
 *   1. Với mỗi N (kích thước mảng):
 *      - Warm-up JIT (chạy thử 1 lần, không tính thời gian).
 *      - Với mỗi CUTOFF:
 *          + Tạo mảng "gốc" random (cùng SEED → công bằng giữa các cutoff).
 *          + Chạy TRIALS lần: mỗi lần clone mảng gốc rồi sắp xếp.
 *          + Đo bằng System.nanoTime(), cộng dồn, lấy trung bình.
 *      - In bảng kết quả và in ra CUTOFF tốt nhất cho mỗi N.
 *
 * Ghi chú:
 *   - Lý thuyết: CUTOFF tối ưu thường rơi vào khoảng 8–16 cho merge sort
 *     và 8–24 cho quick sort (tùy máy / JDK).
 *   - Nhỏ quá  → nhiều lần gọi đệ quy, overhead lớn.
 *   - Lớn quá  → InsertionSort O(k²) làm chậm.
 *   - Nếu kết quả dao động nhiều, tăng TRIALS (ví dụ 5–10) để ổn định hơn.
 */
public class Main {

    // ---------- CẤU HÌNH (có thể chỉnh tùy ý) ----------
    private static final int[] SIZES   = { 10_000, 50_000, 100_000, 500_000 };
    private static final int[] CUTOFFS = { 1, 4, 8, 12, 16, 20, 24, 32, 48, 64 };
    private static final int  TRIALS   = 3;
    private static final long SEED     = 42L;

    public static void main(String[] args) {
        System.out.println("=== THI NGHIEM TIM CUTOFF TOI UU ===");
        System.out.println("Sizes   = " + Arrays.toString(SIZES));
        System.out.println("Cutoffs = " + Arrays.toString(CUTOFFS));
        System.out.println("Trials  = " + TRIALS + ", Seed = " + SEED);
        System.out.println();

        System.out.printf("%-10s %-8s %-12s %-12s%n",
                "N", "CUTOFF", "Merge(ms)", "Quick(ms)");
        System.out.println("---------------------------------------------");

        for (int n : SIZES) {
            warmup(n);                       // "hâm" JIT cho công bằng

            double bestM = Double.MAX_VALUE, bestQ = Double.MAX_VALUE;
            int    cutM  = -1,               cutQ  = -1;

            for (int c : CUTOFFS) {
                double tM = avgTime(n, c, true);
                double tQ = avgTime(n, c, false);
                System.out.printf("%-10d %-8d %-12.3f %-12.3f%n", n, c, tM, tQ);

                if (tM < bestM) { bestM = tM; cutM = c; }
                if (tQ < bestQ) { bestQ = tQ; cutQ = c; }
            }
            System.out.printf(
                    "  >> N=%d : best Merge CUTOFF=%d (%.3f ms) | best Quick CUTOFF=%d (%.3f ms)%n%n",
                    n, cutM, bestM, cutQ, bestQ);
        }
    }

    /** Warm-up JIT: chạy mỗi thuật toán 1 lần với mảng random. */
    private static void warmup(int n) {
        int[] a = randomArray(n, SEED);
        int[] b = a.clone();
        MergeSortCutoff.sort(a, 16);
        QuickSortCutoff.sort(b, 16);
        if (!isSorted(a) || !isSorted(b))
            throw new IllegalStateException("Warmup failed!");
    }

    /**
     * Đo thời gian trung bình (ms) sắp xếp mảng cỡ n với CUTOFF cho trước.
     * @param merge true → MergeSortCutoff, false → QuickSortCutoff
     */
    private static double avgTime(int n, int cutoff, boolean merge) {
        int[] base = randomArray(n, SEED);   // cùng seed → cùng dữ liệu mọi lần

        long totalNs = 0;
        for (int t = 0; t < TRIALS; t++) {
            int[] arr = base.clone();        // mỗi trial dùng mảng tươi

            long start = System.nanoTime();
            if (merge) MergeSortCutoff.sort(arr, cutoff);
            else       QuickSortCutoff.sort(arr, cutoff);
            long end   = System.nanoTime();

            totalNs += (end - start);

            // Kiểm tra đúng đắn — tránh JIT "tối ưu hóa" sai
            if (!isSorted(arr))
                throw new IllegalStateException(String.format(
                        "Khong dung! n=%d cutoff=%d merge=%s", n, cutoff, merge));
        }
        return totalNs / (double) TRIALS / 1e6;   // ns → ms
    }

    /** Sinh mảng int ngẫu nhiên kích thước n với seed cố định. */
    private static int[] randomArray(int n, long seed) {
        Random r = new Random(seed);
        int[] a = new int[n];
        for (int i = 0; i < n; i++) a[i] = r.nextInt();
        return a;
    }

    /** Kiểm tra mảng đã sắp xếp tăng dần. */
    private static boolean isSorted(int[] a) {
        for (int i = 1; i < a.length; i++)
            if (a[i - 1] > a[i]) return false;
        return true;
    }
}