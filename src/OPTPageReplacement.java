import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;

public class OPTPageReplacement extends JFrame {

    private final JTextField framesInput, pagesInput, pageListInput;
    private final JLabel resultLabel, faultsLabel, pageErrorLabel;
    private final JTextArea matrixOutput;

    public OPTPageReplacement() {
        setTitle("Page Replacement Algorithm - OPT");
        setSize(800, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        // Frame input
        JLabel framesLabel = new JLabel("Số lượng Khung:");
        framesLabel.setBounds(50, 50, 150, 25);
        add(framesLabel);

        framesInput = new JTextField();
        framesInput.setBounds(200, 50, 50, 25);
        add(framesInput);

        // Pages input
        JLabel pagesLabel = new JLabel("Số lượng Trang:");
        pagesLabel.setBounds(50, 80, 150, 25);
        add(pagesLabel);

        pagesInput = new JTextField();
        pagesInput.setBounds(200, 80, 50, 25);
        add(pagesInput);

        // Page list input
        JLabel pageListLabel = new JLabel("Nhập các trang (ngăn cách bởi dấu phẩy)");
        pageListLabel.setBounds(50, 110, 200, 25);
        add(pageListLabel);

        pageListInput = new JTextField();
        pageListInput.setBounds(250, 110, 200, 25);
        add(pageListInput);

        // Run button
        JButton runButton = new JButton("RUN");
        runButton.setBounds(500, 50, 80, 25);
        runButton.addActionListener(new RunButtonListener());
        add(runButton);

        // Reset button
        JButton resetButton = new JButton("RESET");
        resetButton.setBounds(500, 80, 80, 25);
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFields();
            }
        });
        add(resetButton);

        // Result labels
        resultLabel = new JLabel("Kết quả sẽ xuất hiện ở đây.");
        resultLabel.setBounds(180, 180, 300, 25);
        add(resultLabel);

        faultsLabel = new JLabel("");
        faultsLabel.setBounds(100, 350, 300, 25);
        add(faultsLabel);

        pageErrorLabel = new JLabel("");
        pageErrorLabel.setBounds(100, 380, 300, 25);
        add(pageErrorLabel);

        // Matrix output
        matrixOutput = new JTextArea();
        matrixOutput.setBounds(50, 210, 700, 100);
        matrixOutput.setEditable(false);
        add(matrixOutput);
    }

    private void resetFields() {
        framesInput.setText("");
        pagesInput.setText("");
        pageListInput.setText("");
        resultLabel.setText("Kết quả sẽ xuất hiện ở đây.");
        faultsLabel.setText("");
        pageErrorLabel.setText("");
        matrixOutput.setText("");
    }

    private int[] convertStringToList(String input) {
        String[] parts = input.split(",");
        int[] pageList = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            pageList[i] = Integer.parseInt(parts[i].trim());
        }
        return pageList;
    }

    private void runOPTAlgorithm(int[] pageList, int frames) {
        int pages = pageList.length;
        int[][] matrix = new int[frames][pages];
        for (int[] row : matrix) Arrays.fill(row, -1);

        int[] frameList = new int[frames];
        Arrays.fill(frameList, -1);
        int faults = 0;

        for (int i = 0; i < pages; i++) {
            boolean hit = false;
            for (int j = 0; j < frames; j++) {
                if (frameList[j] == pageList[i]) {
                    hit = true;
                    break;
                }
            }

            if (!hit) {
                faults++;
                int replaceIndex = findOPTIndex(pageList, frameList, i, pages);
                frameList[replaceIndex] = pageList[i];
            }

            for (int j = 0; j < frames; j++) {
                matrix[j][i] = frameList[j];
            }
        }

        displayMatrix(matrix, frames, pages);
        resultLabel.setText("Tổng số Lỗi Trang: " + faults);
    }

    private int findOPTIndex(int[] pageList, int[] frameList, int currentIndex, int totalPages) {
        int[] nextUse = new int[frameList.length];
        Arrays.fill(nextUse, Integer.MAX_VALUE);

        for (int i = 0; i < frameList.length; i++) {
            for (int j = currentIndex + 1; j < totalPages; j++) {
                if (frameList[i] == pageList[j]) {
                    nextUse[i] = j;
                    break;
                }
            }
        }

        int maxIndex = 0;
        for (int i = 1; i < nextUse.length; i++) {
            if (nextUse[i] > nextUse[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private void displayMatrix(int[][] matrix, int frames, int pages) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < frames; i++) {
            for (int j = 0; j < pages; j++) {
                builder.append(matrix[i][j] == -1 ? "  -  " : String.format(" %3d ", matrix[i][j]));
            }
            builder.append("\n");
        }
        matrixOutput.setText(builder.toString());
    }

    private class RunButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                int frames = Integer.parseInt(framesInput.getText().trim());
                int pages = Integer.parseInt(pagesInput.getText().trim());
                int[] pageList = convertStringToList(pageListInput.getText().trim());
                runOPTAlgorithm(pageList, frames);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Nhập vào không hợp lệ. Vui lòng thử lại");
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new OPTPageReplacement().setVisible(true);
        });
    }
}