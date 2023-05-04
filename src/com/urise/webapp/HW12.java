package com.urise.webapp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class HW12 {
    public static void main(String[] args) {
        System.out.println(minValue(new int[]{6, 1, 2, 3, 4, 5, 5}));
        System.out.println(minValueConvertToString(new int[]{9, 8}));
        System.out.println(oddOrEven(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 1)));
    }

    static int minValue(int[] values) {
        int[] arr = Arrays.stream(values).distinct().sorted().toArray();
        int result = 0;
        for (int number : arr) {
            result = 10 * result + number;
        }
        return result;
    }

    static int minValueConvertToString(int[] values) {
        String number = Arrays.stream(values).distinct().sorted().mapToObj(e -> e + "").collect(Collectors.joining());
        return Integer.parseInt(number);
    }

    static List<Integer> oddOrEven(List<Integer> integers) {
        Integer number = integers.stream().reduce(Integer::sum).orElse(0);
        return number % 2 == 0 ? integers.stream().filter(e -> e % 2 != 0).collect(Collectors.toList()) :
                integers.stream().filter(e -> e % 2 == 0).collect(Collectors.toList());
    }

}
