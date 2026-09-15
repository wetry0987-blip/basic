package suanfati;

import java.util.ArrayDeque;
import java.util.Deque;

public class TwoStackGreedy {
    public static int minMoves(int[] arr) {
        Deque<Integer> main = new ArrayDeque<>();
        int n = arr.length;
        for (int i = 0; i < n; i++) {
            main.push(arr[i]);
            Deque<Integer> temp = new ArrayDeque<>();

            int[] order = arr.clone();
            java.util.Arrays.sort(order);
            int moves = 0;
            for (int target : order) {
                if (main.peek() == target && !main.isEmpty()) {
                    main.pop();
                    continue;
                }
                if (temp.peek() == target && !temp.isEmpty()) {
                    temp.pop();
                    continue;
                }

                Deque<Integer> src = contains(main, target) ? main : temp;
                Deque<Integer> dst = (main == src) ? temp : main;
                while (src.peek() != target) {
                    dst.push(src.pop());
                    moves++;
                }
                src.pop();

            }
            return moves;


        }


    }

    public static boolean contains(Deque<Integer> stack, int target) {
        for (int num : stack) {
            if (num == target) {return true;}
        }
        return false;
    }
}
