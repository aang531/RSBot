package PathMaker;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class GUI extends JFrame{
    private JPanel root;
    private JButton resetButton;
    private JButton removeSelectedButton;
    private JButton addTileButton;
    public JList tileList;
    private JButton outputButton;

    public PathMaker pm;

    public GUI(final PathMaker pm){
        super("PathMaker");
        this.pm = pm;
        setContentPane(root);
        pack();
        setVisible(true);
        setResizable(false);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                pm.exit();
                e.getWindow().dispose();
            }
        });
        outputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OutputWindow(pm.getOutputText());
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pm.resetButton();
                tileList.setModel(new DefaultListModel());
            }
        });
        removeSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pm.removeTileButtonPressed(tileList.getSelectedIndex());
            }
        });
        addTileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pm.addTileButtonPressed();
            }
        });
    }
}
