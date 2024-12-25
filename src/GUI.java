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
        inputPanel.setLayout(new GridLayout(3, 2));
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

        // Button for adding a new process to the list
        addProcessButton = new JButton("Add Process");
        processPanel.add(addProcessButton);

        // Algorithm selection
        JPanel algorithmPanel = new JPanel();
        algorithmPanel.add(new JLabel("Select Algorithm:"));
        algorithmComboBox = new JComboBox<>();
        algorithmComboBox.addItem("SRTF");
        algorithmComboBox.addItem("FCAI");
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

                if (processName.isEmpty() || arrivalTimeText.isEmpty() || burstTimeText.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please fill in all fields for the process.");
                    return;
                }

                try {
                    int arrivalTime = Integer.parseInt(arrivalTimeText);
                    int burstTime = Integer.parseInt(burstTimeText);

                    Process newProcess = new Process(processName, arrivalTime, burstTime, 0, 0); // Priority 0 for now
                    processList.add(newProcess);

                    // Clear the input fields after adding the process
                    processNameField.setText("");
                    arrivalTimeField.setText("");
                    burstTimeField.setText("");

                    JOptionPane.showMessageDialog(frame, "Process added successfully!");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numbers for arrival and burst times.");
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

                // Call the appropriate scheduling method and pass the JTextArea for displaying results
                if ("SRTF".equals(selectedAlgorithm)) {
                    scheduler.SRTF_Scheduling(processList);
                } else if ("FCAI".equals(selectedAlgorithm)) {
                    scheduler.FCAI_Scheduling(processList);  // Pass the JTextArea for FCAI results
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
                new GUI();  // Create and show the GUI
            }
        });
    }
}
