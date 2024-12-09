import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.*;

import static java.lang.Math.ceil;
import static java.lang.Math.max;

public class Scheduler
{
    public void SRTF_Scheduling(List<Process> processes)
    {
        int time = 0;
        int completed = 0;
        int n = processes.size();

        List<Process> readyQueue = new ArrayList<>();

        while (completed < n)
        {
            // Add processes to ready queue based on arrival time
            for (Process process : processes)
                if (process.getArrivalTime() <= time && process.getRemainingTime() > 0 && !readyQueue.contains(process))
                    readyQueue.add(process);


            readyQueue.sort(Comparator.comparingInt(Process::getRemainingTime));

            if (!readyQueue.isEmpty())
            {
                Process currentProcess = readyQueue.get(0);
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                time++;

                if (currentProcess.isCompleted())
                {
                    completed++;
                    currentProcess.setCompletionTime(time);
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    readyQueue.remove(currentProcess);
                }
            } else /// No Process is Ready
                time++;
        }

        printResultss(processes);
    }

    public void printResultss(List<Process> processes)
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
                if(!interrupt){
                    currentProcess = readyQueue.poll();
                }
                else {
                    currentProcess = sortedProcesses.get(0);
                    readyQueue.remove(currentProcess);
                }
                interrupt = false;

                int quantum = currentProcess.getQuantum();
                int executionTime = (int) ceil(quantum * 0.4);

                if (executionTime > currentProcess.getRemainingTime()) {
                    executionTime = currentProcess.getRemainingTime();
                }

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

                // Check for preemption
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
                } else {
                    for (int i = 0; (i < quantum - executionTime) && (!currentProcess.isCompleted()); i++) {
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
                            break;
                        }
                    }
                    if(interrupt)
                        continue;
                    if (currentProcess.isCompleted()) {
                        currentProcess.setCompletionTime(time);
                        currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                        currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                        readyQueue.remove(currentProcess); // Remove from the queue
                        completed++; // Update completed count
                        interrupt = false; // Reset preemption flag
                    }
                    else {
                        currentProcess.setQuantum(currentProcess.getQuantum() + 2);
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