package AangPathMaker;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OutputWindow extends JFrame {
    private JButton toClipboard;
    private JTextArea outputText;
    private JPanel root;

    public OutputWindow(String text) {
        super("Output");
        setContentPane(root);
        pack();
        setVisible(true);

        outputText.setText(text);

        toClipboard.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                StringSelection ss = new StringSelection(outputText.getText());
                clipboard.setContents(ss, ss);
            }
        });
    }
}
