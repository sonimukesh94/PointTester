import javax.swing.DefaultListModel;
import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JList;

import java.awt.GridBagConstraints;

import javax.swing.JTextField;

import com.sun.org.apache.bcel.internal.generic.LSTORE;

import java.awt.Insets;
import javax.swing.JComboBox;
import java.awt.List;


public class TestWindow2 extends JFrame{
	private JTextField textField;
	public TestWindow2() {
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{0, 0, 0, 0, 53, 0, 0, 0, 0, 0};
		gridBagLayout.rowHeights = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 1.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		DefaultListModel<JTextField> listModel = new DefaultListModel<JTextField>();
		


		GridBagConstraints gbc_list_1 = new GridBagConstraints();
		gbc_list_1.insets = new Insets(0, 0, 5, 5);
		gbc_list_1.gridx = 2;
		gbc_list_1.gridy = 4;
		
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 7;
		gbc_textField.gridy = 5;
		getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		JList list = new JList(listModel);
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 0, 5);
		gbc_list.gridheight = 4;
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 4;
		gbc_list.gridy = 6;
		getContentPane().add(list, gbc_list);
		
		for(int i = 0; i < 5; i++)
		{
			listModel.addElement(new JTextField("Test"));
		}
		//this.setSize(500, 500);
		//this.show();
	}

}
