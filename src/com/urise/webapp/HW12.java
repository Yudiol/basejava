package com.urise.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class HW12 {
    public static void main(String[] args) {
        System.out.println(minValueMySolution(new int[]{6, 1, 2, 3, 4, 5, 5}));
        System.out.println(minValueAnotherManSolution(new int[]{6, 1, 2, 3, 4, 5, 5}));
        System.out.println(minValueConvertToString(new int[]{9, 8, 8, 9}));
        System.out.println(oddOrEven(Arrays.asList(1, 1, 1, 2, 2)));
    }

    static int minValueMySolution(int[] values) {
        int[] arr = Arrays.stream(values).distinct()
                .sorted()
                .toArray();
        int result = 0;
        for (int number : arr) {
            result = 10 * result + number;
        }
        return result;
    }

    static int minValueAnotherManSolution(int[] values) {
        return Arrays.stream(values)
                .distinct()
                .sorted()
                .reduce(0, (a, e) -> a * 10 + e);
    }

    static int minValueConvertToString(int[] values) {
        return Integer.parseInt(Arrays.stream(values).distinct().sorted().mapToObj(e -> e + "").collect(Collectors.joining()));
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        final Map<Boolean, List<Integer>> map = integers.stream()
                .collect(Collectors.groupingBy(i -> i % 2 == 0));
        return map.get(map.get(false).size() % 2 != 0);
    }
}
