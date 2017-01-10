import java.awt.Component;

import javax.swing.*;
import javax.swing.border.*;

import java.awt.event.*;

public class JSpinnerList extends JList<JSpinner> {
  protected static Border noFocusBorder = new EmptyBorder(1, 1, 1, 1);
  
  public JSpinnerList() {
    setCellRenderer(new CellRenderer());
    addMouseListener(new MouseAdapter() {
    	
	});
//    addMouseListener(new MouseAdapter() {
//      public void mousePressed(MouseEvent e) {
////        int index = locationToIndex(e.getPoint());
//////        if (index != -1) {
//////          JSpinner spinner = (JSpinner) getModel().getElementAt(index);
//////          spinner.setSelected(!spinner.isSelected());
////          repaint();
////        }
////      }
//    }
//    setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  }

  public JSpinnerList(DefaultListModel<JSpinner> model){
    this();
    setModel(model);
  }

  protected class CellRenderer implements ListCellRenderer<JSpinner> {
    public Component getListCellRendererComponent(
        JList<? extends JSpinner> list, JSpinner value, int index,
        boolean isSelected, boolean cellHasFocus) {
    	JSpinner spinner = value;

      //Drawing spinner, change the appearance here
      spinner.setBackground(isSelected ? getSelectionBackground()
          : getBackground());
      spinner.setForeground(isSelected ? getSelectionForeground()
          : getForeground());
      spinner.setEnabled(true);
      spinner.setFont(getFont());
      spinner.setAlignmentX(HORIZONTAL_WRAP);
      spinner.setAlignmentY(VERTICAL_WRAP);

//      spinner.setFocusPainted(false);
//      spinner.setBorderPainted(true);
      spinner.setBorder(isSelected ? UIManager
          .getBorder("List.focusCellHighlightBorder") : noFocusBorder);
      return spinner;
    }
  }
}