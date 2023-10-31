package cz.muni.fi.pv168.project.ui;

import javax.swing.*;
import java.awt.*;

public class ColoredCircle extends JComponent {
    Color color;
    public ColoredCircle(Color color) {
        this.color = color;
        setPreferredSize(new Dimension(15, 15));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(color);
        g.fillOval(0,0,15,15);
    }
}

