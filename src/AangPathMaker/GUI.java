package AangPathMaker;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.*;
import java.text.ParseException;

public class GUI extends JFrame {
    public JPanel root;
    public JButton newObstacleButton;
    public JButton removeSelectedTileButton;
    public JButton addTileButton;
    public JTextField tileX;
    public JTextField tileY;
    public JList obstacleList;
    public JList pathList;
    public JButton inputButton;
    public JButton outputButton;
    public JButton selectFirstTileButton;
    public JButton selectSecondTileButton;
    public JTextField yPos;
    public JTextField xPos;
    public JTextField width;
    public JTextField height;
    public JSpinner xStart;
    public JSpinner xEnd;
    public JSpinner yEnd;
    public JSpinner yStart;
    public JSpinner zEnd;
    public JSpinner zStart;
    public JButton selectButton;
    public JComboBox obstacleType;
    public JTextField action;
    public JLabel idLabel;
    public JButton removeSelectedObstacle;

    public GUI(final AangPathMaker apm){
        super("BoundsTool");
        setContentPane(root);
        pack();
        setVisible(true);
        setResizable(false);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                apm.exit();
                e.getWindow().dispose();
            }
        });

        pathList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                apm.pathListSelectedChanged();
            }
        });
        obstacleList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                apm.obstacleListSelectedChanged();
            }
        });
        addTileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.addTileButtonPressed();
            }
        });
        newObstacleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.newObstaclePressed();
            }
        });
        removeSelectedTileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.removeTileButtonPressed();
            }
        });
        removeSelectedObstacle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.removeObstaclePressed();
            }
        });
        tileX.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.tileXChanged();
            }
        });
        tileY.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.tileYChanged();
            }
        });
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.inputPressed();
            }
        });
        outputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.outputPressed();
            }
        });
        selectFirstTileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.selectFirstTilePressed();
            }
        });
        selectSecondTileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.selectSecondTilePressed();
            }
        });
        yPos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.yPosChanged();
            }
        });
        xPos.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.xPosChanged();
            }
        });
        width.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.widthChanged();
            }
        });
        height.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.heightChanged();
            }
        });
        xStart.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    xStart.commitEdit();
                } catch (ParseException ignored) {
                }
                apm.xStartChanged();
            }
        });
        xEnd.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    xEnd.commitEdit();
                } catch (ParseException ignored) {
                }
                apm.xEndChanged();
            }
        });
        yEnd.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    yEnd.commitEdit();
                } catch (ParseException ignored) {
                }
                apm.yEndChanged();
            }
        });
        yStart.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    yStart.commitEdit();
                } catch (ParseException ignored) {
                }
                apm.yStartChanged();
            }
        });
        zEnd.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    zEnd.commitEdit();
                } catch (ParseException ignored) {
                }
                apm.zEndChanged();
            }
        });
        zStart.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    zStart.commitEdit();
                } catch (ParseException ignored) {
                }
                apm.zStartChanged();
            }
        });
        selectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.selectObstaclePressed();
            }
        });
        obstacleType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.obstacleTypeChanged();
            }
        });
        action.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                apm.actionChanged();
            }
        });
        action.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                apm.actionChanged();
            }
        });
        tileX.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                apm.tileXChanged();
            }
        });
        tileY.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                apm.tileYChanged();
            }
        });
        xPos.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                apm.xPosChanged();
            }
        });
        yPos.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                apm.yPosChanged();
            }
        });
        width.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                apm.widthChanged();
            }
        });
        height.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {}

            @Override
            public void focusLost(FocusEvent e) {
                apm.heightChanged();
            }
        });
    }
}
