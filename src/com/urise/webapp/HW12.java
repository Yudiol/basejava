package com.urise.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HW12 {
    public static void main(String[] args) {
        System.out.println(minValueMySolution(new int[]{6, 1, 2, 3, 4, 5, 5}));
        System.out.println(minValueAnotherManSolution(new int[]{6, 1, 2, 3, 4, 5, 5}));
        System.out.println(minValueConvertToString(new int[]{9, 8, 8, 9}));
        System.out.println(oddOrEven(Arrays.asList(1, 1,1, 2, 2)));
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
                .reduce((a, e) -> a * 10 + e)
                .orElse(0);
    }

    static int minValueConvertToString(int[] values) {
        return Integer.parseInt(Arrays.stream(values).distinct().sorted().mapToObj(e -> e + "").collect(Collectors.joining()));
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
//        boolean b = integers.stream().reduce(Integer::sum).map(e -> e % 2 == 0).orElse(false);
//        return integers.stream().collect(Collectors.groupingBy(el -> el % 2 == 0)).get(!b);
        return integers.stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        e -> {
                            return e.stream().collect(Collectors.groupingBy(el -> el % 2 == 1))
                                    .get(e.stream().reduce(Integer::sum).orElse(0) % 2 == 0);
                        }));
    }
}
