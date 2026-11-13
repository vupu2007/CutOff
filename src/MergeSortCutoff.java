/**
 * MergeSortCutoff — Sắp xếp trộn có tích hợp CUTOFF.
 *
 * Nguyên lý:
 *   sort(a, lo, hi):
 *     - Nếu (hi - lo) <= cutoff   → InsertionSort(a, lo, hi) rồi return.
 *     - Ngược lại: chia đôi, đệ quy 2 nửa, rồi merge.
 *
 * Tối ưu quan trọng: nếu a[mid-1] <= a[mid] thì 2 nửa đã "nối" đúng thứ tự,
 * bỏ qua bước merge (rất có lợi trên dữ liệu gần như đã sắp).
 *
 * Chỉ cấp phát 1 mảng aux kích thước n, dùng lại cho mọi lần merge
 * (tránh cấp phát trong đệ quy).
 */
public class MergeSortCutoff {

    /** Sắp xếp cả mảng với CUTOFF mặc định. */
    public static void sort(int[] a) {
        sort(a, 16);
    }

    /**
     * Sắp xếp cả mảng với CUTOFF cho trước.
     * @param a      mảng cần sắp xếp
     * @param cutoff đoạn con tối đa để chuyển sang InsertionSort (>= 1)
     */
    public static void sort(int[] a, int cutoff) {
        if (a == null || a.length < 2) return;
        if (cutoff < 1) cutoff = 1;           // bảo vệ: cutoff phải >= 1
        int[] aux = new int[a.length];        // cấp phát 1 lần duy nhất
        sort(a, aux, 0, a.length, cutoff);
    }

    /** Hàm đệ quy: sắp xếp a[lo..hi). */
    private static void sort(int[] a, int[] aux, int lo, int hi, int cutoff) {
        // ---- BASE CASE: đoạn đủ nhỏ → InsertionSort ----
        if (hi - lo <= cutoff) {
            InsertionSort.sort(a, lo, hi);
            return;
        }

        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo,  mid, cutoff);
        sort(a, aux, mid, hi, cutoff);

        // Tối ưu: 2 nửa đã "nối" đúng thứ tự → không cần merge
        if (a[mid - 1] <= a[mid]) return;

        merge(a, aux, lo, mid, hi);
    }

    /** Trộn 2 đoạn đã sắp a[lo..mid) và a[mid..hi) thành a[lo..hi). */
    private static void merge(int[] a, int[] aux, int lo, int mid, int hi) {
        // Copy nguyên đoạn vào aux (chỉ copy phần cần merge)
        System.arraycopy(a, lo, aux, lo, hi - lo);

        int i = lo;    // con trỏ nửa trái
        int j = mid;   // con trỏ nửa phải

        for (int k = lo; k < hi; k++) {
            if      (i >= mid)         a[k] = aux[j++]; // nửa trái cạn
            else if (j >= hi)          a[k] = aux[i++]; // nửa phải cạn
            else if (aux[i] <= aux[j]) a[k] = aux[i++]; // lấy bên trái
            else                       a[k] = aux[j++]; // lấy bên phải
        }
    }
}