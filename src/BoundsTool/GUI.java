package BoundsTool;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;

/**
 * Created by Cas on 30-12-2015.
 */
public class GUI extends JFrame {
    private JPanel root;
    private JSpinner xmin;
    private JSpinner zmin;
    private JSpinner ymin;
    private JSpinner xmax;
    private JSpinner ymax;
    private JSpinner zmax;
    private JButton SelectButton;
    private JLabel NameLabel;
    private JLabel IDlabel;

    private final BoundsTool bt;

    public GUI(final BoundsTool bt) {
        super("BoundsTool");
        this.bt = bt;
        setContentPane(root);
        pack();
        setVisible(true);
        setResizable(false);

        addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                bt.exit();
                e.getWindow().dispose();
            }
        });
        SelectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bt.selecting = true;
            }
        });

        xmin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    xmin.commitEdit();
                } catch (ParseException ex) {
                }
                bt.changeMinx((Integer)xmin.getValue());
            }
        });
        ymin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    ymin.commitEdit();
                } catch (ParseException ex) {
                }
                bt.changeMiny((Integer)ymin.getValue());
            }
        });
        zmin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    zmin.commitEdit();
                } catch (ParseException ex) {
                }
                bt.changeMinz((Integer)zmin.getValue());
            }
        });
        xmax.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    xmax.commitEdit();
                } catch (ParseException ex) {
                }
                bt.changeMaxx((Integer)xmax.getValue());
            }
        });
        ymax.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    ymax.commitEdit();
                } catch (ParseException ex) {
                }
                bt.changeMaxy((Integer)ymax.getValue());
            }
        });
        zmax.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                try {
                    zmax.commitEdit();
                } catch (ParseException ex) {
                }
                bt.changeMaxz((Integer)zmax.getValue());
            }
        });
    }

    public void updateValues(){
        xmin.setValue(bt.minx);
        ymin.setValue(bt.miny);
        zmin.setValue(bt.minz);
        xmax.setValue(bt.maxx);
        ymax.setValue(bt.maxy);
        zmax.setValue(bt.maxz);

        NameLabel.setText("Name: " + bt.selectedModel.name());
        IDlabel.setText("ID: " + bt.selectedModel.id());
    }
}
