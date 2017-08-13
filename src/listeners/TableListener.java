package listeners;

import patcher.Patcher;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class TableListener implements MouseListener {

    private Patcher patcher;

    public TableListener(Patcher patcher) {
        this.patcher = patcher;

    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        // Calculate the column and row on the table.
        final int row = patcher.table.rowAtPoint(e.getPoint());
        final int column = patcher.table.columnAtPoint(e.getPoint());
        // If the column is the checkbox column, toggle the boolean value.
        if (column == 0) {
            patcher.table.setValueAt(!((Boolean)patcher.table.getValueAt(row, column)), row, column);
            patcher.table.updateUI();
        }
    }

    @Override
    public void mouseEntered(final MouseEvent arg0) { }

    @Override
    public void mouseExited(final MouseEvent arg0) { }

    @Override
    public void mousePressed(final MouseEvent arg0) { }

    @Override
    public void mouseReleased(final MouseEvent arg0) { }
}