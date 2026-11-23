package com.studentmarket.gui;

import java.awt.Component;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;


final class GuiUtil {

    private GuiUtil() {
    }

    static void baris(JPanel panel, int row, String label, JComponent field) {
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(4, 6, 4, 6);
        c.gridy = row;
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        panel.add(new JLabel(label), c);
        c.gridx = 1;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, c);
    }

    static void error(Component parent, Exception e) {
        String pesan = (e.getMessage() == null) ? e.toString() : e.getMessage();
        JOptionPane.showMessageDialog(parent, pesan, "Terjadi kesalahan", JOptionPane.ERROR_MESSAGE);
    }

    static void info(Component parent, String pesan) {
        JOptionPane.showMessageDialog(parent, pesan, "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }

    static DefaultTableModel modelTabel(String... kolom) {
        return new DefaultTableModel(kolom, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }
}
