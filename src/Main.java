import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Scheduler scheduler = new Scheduler();

        System.out.print("Enter number of processes: ");
        int processesCount = scanner.nextInt();

        List<Process> processes = new ArrayList<>();

        for (int i = 0; i < processesCount; i++) {
            System.out.printf("Enter details for Process %d (Name, Arrival Time, Burst Time, Priority, Initial Quantum): ", i + 1);
            String name = scanner.next();
            int arrivalTime = scanner.nextInt();
            int burstTime = scanner.nextInt();
            int priority = scanner.nextInt();
            int initialQuantum = scanner.nextInt();
            processes.add(new Process(name, arrivalTime, burstTime, priority, initialQuantum));
        }

        scheduler.FCAI_Scheduling(processes);
    }
}
