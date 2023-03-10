import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class App {

    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        int size = 1000;
        List<Thread> threadList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Thread thread = new Thread(() -> {
                synchronized (sizeToFreq) {
                    String input = generateRoute("RLRFR", 100);

                    int count = StringUtils.countMatches(input, "R");
                    Integer value = sizeToFreq.get(count);
                    if (value != null) {
                        value++;
                        sizeToFreq.put(count, value);
                    } else {
                        value = 1;
                        sizeToFreq.put(count, value);
                    }
                }
            });
            threadList.add(thread);
            thread.start();
        }

        for (Thread thread : threadList) {
            thread.join();
        }

        //Находим максимальное
        Map.Entry<Integer, Integer> max = sizeToFreq.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get();
        System.out.println("Самое частое количество повторений " + max.getKey() + " (встретилось " + max.getValue() + " раз)");
        sizeToFreq.remove(max.getKey(), max.getValue());

        sizeToFreq.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(v -> {
                    System.out.println(" - " + v.getKey() + " (" + v.getValue() + " раз)");
                });
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
