import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * SecureSure Insurance Claim Tracker
 * A small Swing GUI that lets a claims desk log claims one click at a time,
 * keeps a running history, and shows a milestone message every 10 claims.
 */
public class SecureSureClaimTracker {

    // A milestone message appears every time the total reaches a multiple of this number
    private static final int MILESTONE_INTERVAL = 10;

    // Every count that has been reached is stored here (1, 2, 3, ...)
    private final ArrayList<Integer> claimLog = new ArrayList<>();

    // Running total shown on screen
    private int totalClaims = 0;

    // GUI components that change as claims are added
    private final JLabel counterLabel = new JLabel("Claims Processed: 0", SwingConstants.CENTER);
    private final JLabel messageLabel = new JLabel("Click \"Add Claim\" to log your first claim.", SwingConstants.CENTER);
    private final JLabel historyLabel = new JLabel("Log: (empty)", SwingConstants.CENTER);

    // Builds the window and wires up the buttons
    private void createAndShowGui() {
        JFrame frame = new JFrame("SecureSure Claim Tracker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        counterLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        messageLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        historyLabel.setFont(new Font("SansSerif", Font.ITALIC, 13));

        JButton addButton = new JButton("Add Claim");
        JButton resetButton = new JButton("Reset");

        // Each click adds one claim (lambda ActionListener)
        addButton.addActionListener(e -> addClaim());
        resetButton.addActionListener(e -> resetClaims());

        JPanel textPanel = new JPanel(new GridLayout(3, 1, 0, 8));
        textPanel.add(counterLabel);
        textPanel.add(messageLabel);
        textPanel.add(historyLabel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        buttonPanel.add(addButton);
        buttonPanel.add(resetButton);

        frame.setLayout(new BorderLayout(10, 10));
        ((JComponent) frame.getContentPane()).setBorder(BorderFactory.createEmptyBorder(16, 20, 12, 20));
        frame.add(textPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        frame.setSize(520, 260);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Logs one new claim and refreshes the screen
    private void addClaim() {
        totalClaims++;
        claimLog.add(totalClaims);
        updateTotal();
    }

    // Recalculates the total from the log and updates every label
    private void updateTotal() {
        // Total is the last count stored in the log, or 0 if nothing has been logged yet
        if (claimLog.isEmpty()) {
            totalClaims = 0;
        } else {
            totalClaims = claimLog.get(claimLog.size() - 1);
        }

        counterLabel.setText("Claims Processed: " + totalClaims);
        messageLabel.setText(getMilestoneMessage(totalClaims));
        historyLabel.setText(buildHistoryText());
    }

    // Returns a milestone message when the total hits a multiple of 10
    private String getMilestoneMessage(int total) {
        if (total == 0) {
            return "Click \"Add Claim\" to log your first claim.";
        } else if (total % MILESTONE_INTERVAL == 0) {
            return "Congratulations! You've logged " + total + " claims.";
        } else {
            int remaining = MILESTONE_INTERVAL - (total % MILESTONE_INTERVAL);
            return remaining + " more to the next milestone.";
        }
    }

    // Uses a loop to turn the stored counts into text (shows only the latest 10)
    private String buildHistoryText() {
        if (claimLog.isEmpty()) {
            return "Log: (empty)";
        }
        StringBuilder text = new StringBuilder("Log: ");
        int start = Math.max(0, claimLog.size() - 10);
        for (int i = start; i < claimLog.size(); i++) {
            text.append(claimLog.get(i));
            if (i < claimLog.size() - 1) {
                text.append(", ");
            }
        }
        return text.toString();
    }

    // Clears the log and starts again from zero
    private void resetClaims() {
        claimLog.clear();
        updateTotal();
    }

    public static void main(String[] args) {
        // Swing components must be created and updated on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> new SecureSureClaimTracker().createAndShowGui());
    }
}
