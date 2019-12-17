package com.zgz.count;

import java.util.Arrays;

/**
 * @author  Zhu
 * @Desc    java算法统计
 * @URL     https://blog.csdn.net/weixin_41190227/article/details/86600821
 */
public class Count {

    public static void main(String[] args) {

        int count[] = {123,2,23,41,4,34,123,43,26,89,87,6654,78,36,0,46574,12458,126,23687,45586,13244,14752,43};

        /**
         * 归并排序====>算法描述
         * 步骤1：把长度为n的输入序列分成两个长度为n/2的子序列；
         * 步骤2：对这两个子序列分别采用归并排序；
         * 步骤3：将两个排序好的子序列合并成一个最终的排序序列。
         */
        MergeSort(count);


        /**
         * 快速排序====>算法描述
         * 步骤1：从数列中挑出一个元素，称为 “基准”（pivot ）；
         * 步骤2：重新排序数列，所有元素比基准值小的摆放在基准前面，所有元素比基准值大的摆在基准的后面（相同的数可以到任一边）。在这个分区退出之后，该基准就处于数列的中间位置。这个称为分区（partition）操作；
         * 步骤3：递归地（recursive）把小于基准值元素的子数列和大于基准值元素的子数列排序。
         */
        QuickSort(count,0,count.length);

        /**
         * 二分查找
         */
        String result = binarySearch(count, 23);
        System.out.println(result);
    }




    //====================================归并排序====================================
    /**
     * 归并排序
     * @param array
     * @return
     */
    public static int[] MergeSort(int[] array) {
        if (array.length < 2) {
            return array;
        }
        int mid = array.length / 2;
        int[] left = Arrays.copyOfRange(array, 0, mid);
        int[] right = Arrays.copyOfRange(array, mid, array.length);
        return merge(MergeSort(left), MergeSort(right));

    }

    /**
     * 归并排序——将两段排序好的数组结合成一个排序数组
     * @param left
     * @param right
     * @return
     */
    public static int[] merge(int[] left, int[] right) {
        int[] result = new int[left.length + right.length];
        for (int index = 0, i = 0, j = 0; index < result.length; index++) {
            if (i >= left.length){
                result[index] = right[j++];
            }else if (j >= right.length){
                result[index] = left[i++];
            }else if (left[i] > right[j]){
                result[index] = right[j++];
            }else{
                result[index] = left[i++];
            }
        }
        return result;
    }



    //====================================快速排序====================================
    /**
     * 快速排序方法
     * @param array
     * @param start
     * @param end
     * @return
     */
    public static int[] QuickSort(int[] array, int start, int end) {
        if (array.length < 1 || start < 0 || end >= array.length || start > end) return null;
        int smallIndex = partition(array, start, end);
        if (smallIndex > start)
            QuickSort(array, start, smallIndex - 1);
        if (smallIndex < end)
            QuickSort(array, smallIndex + 1, end);
        return array;
    }
    /**
     * 快速排序算法——partition
     * @param array
     * @param start
     * @param end
     * @return
     */
    public static int partition(int[] array, int start, int end) {
        int pivot = (int) (start + Math.random() * (end - start + 1));
        int smallIndex = start - 1;
        swap(array, pivot, end);
        for (int i = start; i <= end; i++)
            if (array[i] <= array[end]) {
                smallIndex++;
                if (i > smallIndex)
                    swap(array, i, smallIndex);
            }
        return smallIndex;
    }

    /**
     * 交换数组内两个元素
     * @param array
     * @param i
     * @param j
     */
    public static void swap(int[] array, int i, int j) {
        int temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    //====================================二分查找====================================
    public static String binarySearch(int[] a, int target) {
        //开始位置
        int start = 0;
        //截止位置
        int end = a.length - 1;
        //中间位置
        //这里注意一下优先级，否则会造成死循环。
        int mid =  ((end - start) >> 1) + start;
        //记录目标位置；
        int index = -1;
        while(true) {
            //判断中间是不是要查找的元素
            if(a[mid] == target) {
                index = mid;
                break;
            }else {//如果不是
                //判断中间这个元素跟目标值的关系，大于则
                //查找的在target在 mid前面
                if(a[mid] > target) {
                    end = mid - 1;
                }else {
                    //大于则查找的在target在 mid后面
                    start = mid + 1;
                }
                //新的中间位置
                mid = ((end - start)>> 1) + start;
            }
        }
        return "位置是："+index;
    }
}
