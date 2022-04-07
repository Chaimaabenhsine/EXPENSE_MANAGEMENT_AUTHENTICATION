package projet.micro.auth.security.filters;


import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Util {

    private Util() {
        // Default constructor
    }

    public static boolean isNotBlank(String val) {
        return !isBlank(val);
    }

    public static  boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    public static  String trim(String s) {
        return s.trim();
    }

    public static <T> boolean isNotEmpty(Set<T> val) {
        return !isEmpty(val);
    }

    public static <T> boolean isEmpty(List<T> s) {
        return s == null || s.isEmpty();
    }

    public static <T> boolean isNotEmpty(List<T> val) {
        return !isEmpty(val);
    }

    public static <T> boolean isEmpty(Set<T> s) {
        return s == null || s.isEmpty();
    }

    public static <T> List<T> convertToLinkedList(T[] array){
        return new LinkedList<>(Arrays.asList(array));
    }

    public static String[] split(String s, String separator) {
        return isNotBlank(s) ? s.split(separator) : new String[0];
    }

    public static <T>  String concatMessageWithParam(String message,  T param) {
        return String.format(message, param);
    }

    public static <T> Stream<Stream<T>> getPermutationOf(final List<T> items) {
        return IntStream.range(0, factorial(items.size())).mapToObj(i -> permutation(i, items).stream());
    }

    private static int factorial(final int num) {
        return IntStream.rangeClosed(2, num).reduce(1, (x, y) -> x * y);
    }

    private static <T> List<T> permutation(final int count, final LinkedList<T> input, final List<T> output) {
        if (input.isEmpty()) { return output; }

        final long factorial = factorial(input.size() - 1);
        output.add(input.remove((int)(count / factorial)));
        return permutation((int) (count % factorial), input, output);
    }

    private static <T> List<T> permutation(final int count, final List<T> items) {
        return permutation(count, new LinkedList<>(items), new ArrayList<>());
    }
}