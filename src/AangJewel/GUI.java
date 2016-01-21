package AangJewel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame{
    public JComboBox jewelType;
    public JComboBox gemType;
    public JButton startButton;
    private JPanel root;
    public JComboBox location;

    final AangJewel aj;

    public GUI(final AangJewel aj) {
        super("AangJewel");
        this.aj = aj;
        setContentPane(root);
        pack();
        setVisible(true);
        setResizable(false);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                aj.exit();
                e.getWindow().dispose();
            }
        });

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                aj.startButtonPress();
            }
        });
    }
}
