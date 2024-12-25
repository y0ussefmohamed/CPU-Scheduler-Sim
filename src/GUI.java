import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private JFrame frame;
    private JTextField processNameField;
    private JTextField arrivalTimeField;
    private JTextField burstTimeField;
    private JTextField priorityField;
    private JComboBox<String> algorithmComboBox;
    private JButton nextButton;
    private JButton addProcessButton;
    private List<Process> processList;
    private JPanel processPanel;

    public GUI() {
        processList = new ArrayList<>();
        initialize();
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("Scheduling Algorithm Selection");
        frame.setBounds(100, 100, 600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(new BorderLayout());

        // Panel for process data entry
        processPanel = new JPanel();
        processPanel.setLayout(new BoxLayout(processPanel, BoxLayout.Y_AXIS));
        frame.getContentPane().add(new JScrollPane(processPanel), BorderLayout.CENTER);

        // Process input fields
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));
        processPanel.add(inputPanel);

        inputPanel.add(new JLabel("Process Name:"));
        processNameField = new JTextField();
        inputPanel.add(processNameField);

        inputPanel.add(new JLabel("Arrival Time:"));
        arrivalTimeField = new JTextField();
        inputPanel.add(arrivalTimeField);

        inputPanel.add(new JLabel("Burst Time:"));
        burstTimeField = new JTextField();
        inputPanel.add(burstTimeField);

        inputPanel.add(new JLabel("Priority (optional):"));
        priorityField = new JTextField();
        inputPanel.add(priorityField);

        // Button for adding a new process to the list
        addProcessButton = new JButton("Add Process");
        processPanel.add(addProcessButton);

        // Algorithm selection
        JPanel algorithmPanel = new JPanel();
        algorithmPanel.add(new JLabel("Select Algorithm:"));
        algorithmComboBox = new JComboBox<>();
        algorithmComboBox.addItem("SRTF");
        algorithmComboBox.addItem("FCAI");
        algorithmComboBox.addItem("SJF");
        algorithmComboBox.addItem("Priority");
        algorithmPanel.add(algorithmComboBox);
        processPanel.add(algorithmPanel);

        // Button for moving to the next step (process the list)
        nextButton = new JButton("Next");
        processPanel.add(nextButton);

        // Action Listener for the "Add Process" button
        addProcessButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String processName = processNameField.getText();
                String arrivalTimeText = arrivalTimeField.getText();
                String burstTimeText = burstTimeField.getText();
                String priorityText = priorityField.getText();

                if (processName.isEmpty() || arrivalTimeText.isEmpty() || burstTimeText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields for the process.");
                    return;
                }

                try {
                    int arrivalTime = Integer.parseInt(arrivalTimeText);
                    int burstTime = Integer.parseInt(burstTimeText);
                    int priority = priorityText.isEmpty() ? 0 : Integer.parseInt(priorityText);

                    Process newProcess = new Process(processName, arrivalTime, burstTime, priority, 0); // Quantum is 0 for now
                    processList.add(newProcess);

                    // Clear the input fields after adding the process
                    processNameField.setText("");
                    arrivalTimeField.setText("");
                    burstTimeField.setText("");
                    priorityField.setText("");

                    JOptionPane.showMessageDialog(frame, "Process added successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers for arrival, burst times, and priority.");
                }
            }
        });

        // Action Listener for the "Next" button (process the list)
        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ensure there are processes to schedule
                if (processList.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please add some processes first.");
                    return;
                }

                // Retrieve the selected scheduling algorithm
                String selectedAlgorithm = (String) algorithmComboBox.getSelectedItem();

                // Pass the process list to the Scheduler class
                Scheduler scheduler = new Scheduler();
                JFrame resultFrame = new JFrame("Scheduling Results");
                resultFrame.setBounds(100, 100, 600, 400);
                resultFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                resultFrame.getContentPane().setLayout(new BorderLayout());

                // Add a JTextArea to display the results
                JTextArea resultArea = new JTextArea();
                resultArea.setEditable(false);
                resultFrame.getContentPane().add(new JScrollPane(resultArea), BorderLayout.CENTER);

                // Call the appropriate scheduling method
                if ("SRTF".equals(selectedAlgorithm)) {
                    scheduler.SRTF_Scheduling(processList);
                } else if ("FCAI".equals(selectedAlgorithm)) {
                    scheduler.FCAI_Scheduling(processList);
                } else if ("SJF".equals(selectedAlgorithm)) {
                    scheduler.nonPreemptiveSJF(processList);
                } else if ("Priority".equals(selectedAlgorithm)) {
                    String contextSwitchingText = JOptionPane.showInputDialog(frame, "Enter Context Switching Time:", "0");
                    try {
                        int contextSwitching = Integer.parseInt(contextSwitchingText);
                        scheduler.nonPreemptivePriority(processList, contextSwitching);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(frame, "Invalid context switching time. Using default value of 0.");
                        scheduler.nonPreemptivePriority(processList, 0);
                    }
                }

                // Show the results frame after the scheduling is done
                resultFrame.setVisible(true);
            }
        });

        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new GUI(); // Create and show the GUI
            }
        });
    }
}
