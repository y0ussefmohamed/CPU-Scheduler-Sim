import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter number of processes: ");
        int n = scanner.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            System.out.printf("Enter details for Process %d (Name, Arrival Time, Burst Time): ", i + 1);
            String name = scanner.next();
            int arrivalTime = scanner.nextInt();
            int burstTime = scanner.nextInt();
            processes.add(new Process(name, arrivalTime, burstTime));
        }

        Scheduler scheduler = new Scheduler();
        scheduler.SRTF_Scheduling(processes);
    }
}