/**
 * InsertionSort — Sắp xếp chèn.
 * Được dùng làm BASE CASE cho MergeSort/QuickSort khi đoạn con <= CUTOFF.
 * Lý do: InsertionSort có hằng số rất nhỏ, với mảng cỡ vài chục phần tử nó
 * nhanh hơn đệ quy + overhead của merge/partition.
 */
public class InsertionSort {

    /**
     * Sắp xếp tăng dần đoạn a[lo..hi) tại chỗ.
     *   - lo: inclusive
     *   - hi: exclusive
     *
     * Ý tưởng: duyệt i từ lo+1 -> hi-1. Với mỗi i, "chèn" a[i] vào đúng vị trí
     * trong đoạn đã sắp a[lo..i-1] bằng cách dịch phải các phần tử lớn hơn a[i].
     *
     * Độ phức tạp: O(k^2) với k = hi - lo. Nhưng hằng số rất nhỏ.
     */
    public static void sort(int[] a, int lo, int hi) {
        for (int i = lo + 1; i < hi; i++) {
            int key = a[i];      // phần tử cần chèn
            int j = i - 1;
            while (j >= lo && a[j] > key) {  // dịch phải các phần tử > key
                a[j + 1] = a[j];
                j--;
            }
            a[j + 1] = key;      // đặt key vào đúng chỗ
        }
    }

    /** Overload tiện dụng: sắp xếp cả mảng. */
    public static void sort(int[] a) {
        sort(a, 0, a.length);
    }
}