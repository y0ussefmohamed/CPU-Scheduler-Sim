public class Process {
    private final String name;
    private final int arrivalTime;
    private final int burstTime;
    private final int priority;
    private int adjustedBurstTime;
    private int adjustedRemainingTime;
    private int remainingTime;
    private int completionTime;
    private int waitingTime;
    private int turnaroundTime;
    private int quantum;

    public Process(String name, int arrivalTime, int burstTime, int priority, int initialQuantum) {
        this.name = name;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
        this.remainingTime = burstTime;
        this.quantum = initialQuantum;
    }

    public String getName() {
        return name;
    }

    public int getArrivalTime() {
        return arrivalTime;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public int getPriority() {
        return priority;
    }

    public int getRemainingTime() {
        return remainingTime;
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public int getCompletionTime() {
        return completionTime;
    }

    public void setCompletionTime(int completionTime) {
        this.completionTime = completionTime;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int waitingTime) {
        this.waitingTime = waitingTime;
    }

    public int getTurnaroundTime() {
        return turnaroundTime;
    }

    public void setTurnaroundTime(int turnaroundTime) {
        this.turnaroundTime = turnaroundTime;
    }

    public int getQuantum() {
        return quantum;
    }

    public void setQuantum(int quantum) {
        this.quantum = quantum;
    }

    public boolean isCompleted() {
        return this.remainingTime == 0;
    }

    public int getAdjustedBurstTime() {
        return adjustedBurstTime;
    }

    public void setAdjustedBurstTime(int adjustedBurstTime) {
        this.adjustedBurstTime = adjustedBurstTime;
    }

    public int getAdjustedRemainingTime() {
        return adjustedRemainingTime;
    }

    public void setAdjustedRemainingTime(int adjustedRemainingTime) {
        this.adjustedRemainingTime = adjustedRemainingTime;
    }
}
