import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args)
    {
        Scanner scanner = new Scanner(System.in);

        Scheduler scheduler = new Scheduler();

        System.out.println("Choose Scheduling Algorithm:");
        System.out.println("1. SRTF Scheduling");
        System.out.println("2. FCAI Scheduling");
        System.out.println("3. SJF Scheduling");
        System.out.println("4. Priority Scheduling");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();

        if (choice < 1 || choice > 4) {
            System.out.println("Invalid choice! Exiting program.");
            return;
        }

        System.out.print("Enter number of processes: ");
        int processesCount = scanner.nextInt();


        List<Process> processes = new ArrayList<>();
        for (int i = 0; i < processesCount; i++) {
            if(choice == 1)
            {
                System.out.printf("Enter details for Process %d (Name, Arrival Time, Burst Time): ", i + 1);
                String name = scanner.next();
                int arrivalTime = scanner.nextInt();
                int burstTime = scanner.nextInt();
                processes.add(new Process(name, arrivalTime, burstTime, 0, 0));
            }
            else if (choice == 2)
            {
                System.out.printf("Enter details for Process %d (Name, Arrival Time, Burst Time, Priority, Quantum): ", i + 1);
                String name = scanner.next();
                int arrivalTime = scanner.nextInt();
                int burstTime = scanner.nextInt();
                int priority = scanner.nextInt();
                int quantum = scanner.nextInt();
                processes.add(new Process(name, arrivalTime, burstTime, priority, quantum));
            }
            else if(choice == 3)
            {
                System.out.printf("Enter details for Process %d (Name, Arrival Time, Burst Time): ", i + 1);
                String name = scanner.next();
                int arrivalTime = scanner.nextInt();
                int burstTime = scanner.nextInt();
                processes.add(new Process(name, arrivalTime, burstTime, 0, 0));
            }
            else
            {
                System.out.printf("Enter details for Process %d (Name, Arrival Time, Burst Time,priority): ", i + 1);
                String name = scanner.next();
                int arrivalTime = scanner.nextInt();
                int burstTime = scanner.nextInt();
                int priority = scanner.nextInt();
                processes.add(new Process(name, arrivalTime, burstTime, priority, 0));
            }
        }

        if (choice == 1)
            scheduler.SRTF_Scheduling(processes);
        else if (choice == 2)
            scheduler.FCAI_Scheduling(processes);
        else if (choice == 3)
            scheduler.nonPreemptiveSJF(processes);
        else {
            System.out.print("Enter value of contextSwitching: ");
            int contextSwitching= scanner.nextInt();
            scheduler.nonPreemptivePriority(processes,contextSwitching);
        }


        scanner.close();
    }
}
