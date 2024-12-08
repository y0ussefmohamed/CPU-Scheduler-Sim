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

        printResults(processes);
    }

    public void printResults(List<Process> processes)
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
}