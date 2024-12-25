import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.*;
import static java.lang.Math.ceil;

public class Scheduler
{
   public void SRTF_Scheduling(List<Process> processes) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        int agingFactor = 1; // Factor to apply aging, can be adjusted

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Move processes to the ready queue
            for (int i = 0; i < processes.size(); i++) {
                if (processes.get(i).getArrivalTime() <= currentTime) {
                    readyQueue.add(processes.remove(i));
                    i--;
                }
            }

            // Apply aging: Adjust remaining time for long-waiting processes in the ready queue
            for (Process p : readyQueue) {
                int waitingTime = currentTime - (p.getArrivalTime() + (p.getBurstTime() - p.getRemainingTime()));
                p.setAdjustedRemainingTime(Math.max(1, p.getRemainingTime() - waitingTime / agingFactor));
            }

            // Sort the ready queue by adjusted remaining time
            readyQueue.sort(Comparator.comparingInt(Process::getAdjustedRemainingTime));

            if (!readyQueue.isEmpty()) {
                // Execute the process with the shortest adjusted remaining time
                Process current = readyQueue.get(0);
                current.setRemainingTime(current.getRemainingTime() - 1);
                currentTime++;

                if (current.getRemainingTime() == 0) {
                    // Process completed
                    readyQueue.remove(current);
                    current.setCompletionTime(currentTime);
                    current.setTurnaroundTime(current.getCompletionTime() - current.getArrivalTime());
                    current.setWaitingTime(current.getTurnaroundTime() - current.getBurstTime());
                    completedProcesses.add(current);
                    System.out.printf("Process %s completed at time %d.\n", current.getName(), currentTime);
                }
            } else {
                currentTime++;
            }
        }

        processes.addAll(completedProcesses); // Restore the completed processes for printing results
        print_SRTF_Results(processes);
    }

    public void print_SRTF_Results(List<Process> processes)
    {
        System.out.println("\nProcess\tArrival Time\tBurst Time\tCompletion Time\tWaiting Time\tTurnaround Time");
        System.out.println("-----------------------------------------------------------------------------------");

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : processes) {
            System.out.printf("%-7s\t%-12d\t%-9d\t%-16d\t%-12d\t%-14d\n",
                    process.getName(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getCompletionTime(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime());

            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        double avgWaitingTime = totalWaitingTime / processes.size();
        double avgTurnaroundTime = totalTurnaroundTime / processes.size();

        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
    }
    // ####################################################################################################################
   public void nonPreemptiveSJF(List<Process> processes) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;
        int agingFactor = 1; // Increment to reduce burst time for aging

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            // Move processes to the ready queue
            for (int i = 0; i < processes.size(); i++) {
                if (processes.get(i).getArrivalTime() <= currentTime) {
                    readyQueue.add(processes.remove(i));
                    i--;
                }
            }

            // Apply aging: decrease burst time for long-waiting processes in the ready queue
            for (Process p : readyQueue) {
                int waitingTime = currentTime - p.getArrivalTime();
                p.setAdjustedBurstTime(Math.max(1, p.getBurstTime() - waitingTime / agingFactor));
            }

            // Sort the ready queue by adjusted burst time
            readyQueue.sort(Comparator.comparingInt(Process::getAdjustedBurstTime));

            if (!readyQueue.isEmpty()) {
                // Execute the process with the shortest adjusted burst time
                Process current = readyQueue.remove(0);
                current.setWaitingTime(currentTime - current.getArrivalTime());
                current.setCompletionTime(currentTime + current.getBurstTime());
                current.setTurnaroundTime(current.getCompletionTime() - current.getArrivalTime());
                currentTime += current.getBurstTime();

                completedProcesses.add(current);
                System.out.printf("Process %s executed at time %d.\n", current.getName(), currentTime);
            } else {
                currentTime++;
            }
        }

        processes.addAll(completedProcesses); // Restore the completed processes for printing results
        print_SJF_Results(processes);
    }
    public void print_SJF_Results(List<Process> processes) {
        System.out.println("\nProcess\tArrival Time\tBurst Time\tCompletion Time\tWaiting Time\tTurnaround Time");
        System.out.println("-----------------------------------------------------------------------------------");

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : processes) {
            System.out.printf("%-7s\t%-12d\t%-9d\t%-16d\t%-12d\t%-14d\n",
                    process.getName(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getCompletionTime(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime());

            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        double avgWaitingTime = totalWaitingTime / processes.size();
        double avgTurnaroundTime = totalTurnaroundTime / processes.size();

        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
    }

    // ####################################################################################################################
    public void nonPreemptivePriority(List<Process> processes, int contextSwitchTime) {
        processes.sort(Comparator.comparingInt(Process::getArrivalTime));
        List<Process> readyQueue = new ArrayList<>();
        List<Process> completedProcesses = new ArrayList<>();
        int currentTime = 0;

        while (!processes.isEmpty() || !readyQueue.isEmpty()) {
            for (int i = 0; i < processes.size(); i++) {
                if (processes.get(i).getArrivalTime() <= currentTime) {
                    readyQueue.add(processes.remove(i));
                    i--;
                }
            }
            readyQueue.sort(Comparator.comparingInt(Process::getPriority));

            if (!readyQueue.isEmpty()) {
                if (!completedProcesses.isEmpty()) {
                    currentTime += contextSwitchTime;
                }
                Process current = readyQueue.remove(0);
                current.setWaitingTime(currentTime - current.getArrivalTime());
                current.setCompletionTime(currentTime + current.getBurstTime());
                current.setTurnaroundTime(current.getWaitingTime() +current.getBurstTime());
                currentTime += current.getBurstTime();

                completedProcesses.add(current);
                System.out.printf("Process %s executed at time %d.\n", current.getName(), currentTime);
            } else {
                currentTime++;
            }
        }

        processes.addAll(completedProcesses);
        print_Priority_Results(processes);
    }
    public void print_Priority_Results(List<Process> processes) {
        System.out.println("\nProcess\tArrival Time\tBurst Time\tPriority\tCompletion Time\tWaiting Time\tTurnaround Time");
        System.out.println("-------------------------------------------------------------------------------------------------");

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : processes) {
            System.out.printf("%-7s\t%-12d\t%-9d\t%-9d\t%-16d\t%-12d\t%-14d\n",
                    process.getName(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getPriority(),
                    process.getCompletionTime(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime());

            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        double avgWaitingTime = totalWaitingTime / processes.size();
        double avgTurnaroundTime = totalTurnaroundTime / processes.size();

        System.out.println("-------------------------------------------------------------------------------------------------");
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
    }

    // ####################################################################################################################
    public void FCAI_Scheduling(List<Process> processes) {
        int time = 0;
        int completed = 0;
        int n = processes.size();

        double v1 = calculateV1(processes);
        double v2 = calculateV2(processes);

        Queue<Process> readyQueue = new LinkedList<>();
        boolean interrupt = false;

        while (completed < n) {
            // Add processes to the ready queue based on arrival time
            for (Process process : processes) {
                if (process.getArrivalTime() <= time && process.getRemainingTime() > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            if (!readyQueue.isEmpty()) {
                // Sort the queue by FCAI Factor
                List<Process> sortedProcesses = new ArrayList<>(readyQueue);
                int finalTime1 = time;
                sortedProcesses.sort(Comparator.comparingInt(p -> calculateFCAIFactor(p, finalTime1, v1, v2)));
                Process currentProcess;
                if (!interrupt) {
                    currentProcess = readyQueue.poll();
                } else {
                    currentProcess = sortedProcesses.get(0);
                    readyQueue.remove(currentProcess);
                }
                interrupt = false;

                int quantum = currentProcess.getQuantum();
                int executionTime = (int) Math.ceil(quantum * 0.4);

                if (executionTime > currentProcess.getRemainingTime()) {
                    executionTime = currentProcess.getRemainingTime();
                }

                // Log the process and its quantum before execution
                System.out.println("Time: " + time + " - Executing process: " + currentProcess.getName()
                        + " with quantum: " + quantum);

                // Execute the process for 40% of the quantum
                for (int i = 0; i < executionTime; i++) {
                    currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                    time++;

                    // Add newly arrived processes to the queue
                    for (Process process : processes) {
                        if (process.getArrivalTime() <= time && process.getRemainingTime() > 0 && !readyQueue.contains(process)) {
                            readyQueue.add(process);
                        }
                    }
                }

                if (currentProcess.getRemainingTime() > 0) {
                    List<Process> updatedProcesses = new ArrayList<>(readyQueue);

                    int finalTime = time;
                    updatedProcesses.sort(Comparator.comparingInt(p -> calculateFCAIFactor(p, finalTime, v1, v2)));

                    if (updatedProcesses.get(0) != currentProcess) {
                        int unusedQuantum = quantum - executionTime;
                        currentProcess.setQuantum(currentProcess.getQuantum() + unusedQuantum);
                        readyQueue.remove(currentProcess);
                        readyQueue.add(currentProcess);
                        interrupt = true;
                        System.out.println("Process " + currentProcess.getName() + " quantum updated to: "
                                + currentProcess.getQuantum());
                        continue;
                    }
                }

                // Process completion or quantum update
                if (currentProcess.isCompleted()) {
                    readyQueue.remove(currentProcess);
                    completed++;
                    currentProcess.setCompletionTime(time);
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());

                    // Log the process completion
                    System.out.println("Time: " + time + " - Process " + currentProcess.getName() + " is completed.");
                } else {
                    for (int i = 1; (i <= quantum - executionTime) && (!currentProcess.isCompleted()); i++) {
                        currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                        time++;
                        for (Process process : processes) {
                            if (process.getArrivalTime() <= time && process.getRemainingTime() > 0 && !readyQueue.contains(process)) {
                                readyQueue.add(process);
                            }
                        }

                        List<Process> updatedProcesses = new ArrayList<>(readyQueue);

                        int finalTime = time;
                        updatedProcesses.sort(Comparator.comparingInt(p -> calculateFCAIFactor(p, finalTime, v1, v2)));

                        if (updatedProcesses.get(0) != currentProcess) {
                            int unusedQuantum = quantum - executionTime - i;
                            currentProcess.setQuantum(currentProcess.getQuantum() + unusedQuantum);
                            readyQueue.remove(currentProcess);
                            readyQueue.add(currentProcess);
                            interrupt = true;
                            System.out.println("Process " + currentProcess.getName() + " quantum updated to: "
                                    + currentProcess.getQuantum());
                            break;
                        }
                    }
                    if (interrupt) continue;

                    if (currentProcess.isCompleted()) {
                        currentProcess.setCompletionTime(time);
                        currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                        currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                        readyQueue.remove(currentProcess); // Remove from the queue
                        completed++; // Update completed count
                        interrupt = false; // Reset preemption flag

                        // Log the process completion
                        System.out.println("Time: " + time + " - Process " + currentProcess.getName() + " is completed.");
                    } else {
                        currentProcess.setQuantum(currentProcess.getQuantum() + 2);
                        System.out.println("Process " + currentProcess.getName() + " quantum updated to: "
                                + currentProcess.getQuantum());
                        readyQueue.remove(currentProcess);
                        readyQueue.add(currentProcess);
                    }
                }
            } else {
                time++;
            }
        }

        printResults(processes);
    }


    private double calculateV1(List<Process> processes) {
        int lastArrivalTime = processes.stream().mapToInt(Process::getArrivalTime).max().orElse(1);
        return lastArrivalTime / 10.0;
    }

    private double calculateV2(List<Process> processes) {
        int maxBurstTime = processes.stream().mapToInt(Process::getBurstTime).max().orElse(1);
        return maxBurstTime / 10.0;
    }

    private int calculateFCAIFactor(Process process, int currentTime, double v1, double v2) {
        return (int) ceil((10 - process.getPriority()) +
                (process.getArrivalTime() / v1) +
                (process.getRemainingTime() / v2));
    }

    public void printResults(List<Process> processes) {
        System.out.println("\nProcess\tArrival Time\tBurst Time\tPriority\tCompletion Time\tWaiting Time\tTurnaround Time");
        System.out.println("-----------------------------------------------------------------------------------");

        double totalWaitingTime = 0;
        double totalTurnaroundTime = 0;

        for (Process process : processes) {
            System.out.printf("%-7s\t%-12d\t%-9d\t%-9d\t%-16d\t%-12d\t%-14d\n",
                    process.getName(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getPriority(),
                    process.getCompletionTime(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime());

            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
        }

        double avgWaitingTime = totalWaitingTime / processes.size();
        double avgTurnaroundTime = totalTurnaroundTime / processes.size();

        System.out.println("-----------------------------------------------------------------------------------");
        System.out.printf("Average Waiting Time: %.2f\n", avgWaitingTime);
        System.out.printf("Average Turnaround Time: %.2f\n", avgTurnaroundTime);
    }
}
