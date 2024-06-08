public class Task2 {

    public static void main(String[] args) {
        int n = 10;
        printFib(n);
    }

    public static void printFib(int n) {
        System.out.println("Fibonacci sequence from 1 to " + n + ":");

        int prev = 0;
        int current = 1;

        System.out.print(prev + " " + current);

        for (int i = 3; i <= n; i++) {
            int next = prev + current;
            System.out.print(" " + next);
            prev = current;
            current = next;
        }

        System.out.println();
    }
}
