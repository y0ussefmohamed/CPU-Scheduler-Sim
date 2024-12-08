import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scheduler
{
    public void SRTF_Scheduling(List<Process> processes)
    {
        int time = 0;
        int completed = 0;
        int n = processes.size();

        List<Process> readyQueue = new ArrayList<>();

        while (completed < n) {
            // Add processes to ready queue based on arrival time
            for (Process process : processes) {
                if (process.getArrivalTime() <= time && process.getRemainingTime() > 0 && !readyQueue.contains(process)) {
                    readyQueue.add(process);
                }
            }

            // Sort ready queue by remaining time
            readyQueue.sort(Comparator.comparingInt(Process::getRemainingTime));

            if (!readyQueue.isEmpty()) {
                Process currentProcess = readyQueue.get(0);
                currentProcess.setRemainingTime(currentProcess.getRemainingTime() - 1);
                time++;

                // Check if the process is completed
                if (currentProcess.getRemainingTime() == 0) {
                    completed++;
                    currentProcess.setCompletionTime(time);
                    currentProcess.setTurnaroundTime(currentProcess.getCompletionTime() - currentProcess.getArrivalTime());
                    currentProcess.setWaitingTime(currentProcess.getTurnaroundTime() - currentProcess.getBurstTime());
                    readyQueue.remove(currentProcess);
                }
            } else {
                // If no process is ready, advance time
                time++;
            }
        }

        // Print results
        printResults(processes);
    }

    private void printResults(List<Process> processes) {
        System.out.println("Process\tArrival Time\tBurst Time\tCompletion Time\tWaiting Time\tTurnaround Time");
        int totalWaitingTime = 0;
        int totalTurnaroundTime = 0;

        for (Process process : processes) {
            totalWaitingTime += process.getWaitingTime();
            totalTurnaroundTime += process.getTurnaroundTime();
            System.out.printf("%s\t%d\t\t%d\t\t%d\t\t%d\t\t%d\n",
                    process.getName(),
                    process.getArrivalTime(),
                    process.getBurstTime(),
                    process.getCompletionTime(),
                    process.getWaitingTime(),
                    process.getTurnaroundTime());
        }

        System.out.printf("Average Waiting Time: %.2f\n", (double) totalWaitingTime / processes.size());
        System.out.printf("Average Turnaround Time: %.2f\n", (double) totalTurnaroundTime / processes.size());
    }
}