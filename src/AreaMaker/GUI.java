package AreaMaker;

import PathMaker.PathMaker;

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

    public AreaMaker am;

    public GUI(final AreaMaker am){
        super("PathMaker");
        this.am = am;
        setContentPane(root);
        pack();
        setVisible(true);
        setResizable(false);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                am.exit();
                e.getWindow().dispose();
            }
        });
        outputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                am.toClipBoardButton();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                am.resetButton();
                tileList.setModel(new DefaultListModel());
            }
        });
        removeSelectedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                am.removeTileButton(tileList.getSelectedIndex());
            }
        });
        addTileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                am.addTileButton();
            }
        });
    }
}
