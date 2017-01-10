import javax.swing.JFrame;

import java.awt.GridBagLayout;

import javax.swing.JButton;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import java.awt.Font;

import javax.swing.AbstractAction;

import java.awt.event.ActionEvent;

import javax.swing.Action;

import sun.misc.Queue;

import com.sun.xml.internal.bind.v2.Messages;

import java.awt.event.ActionListener;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.DefaultListModel;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JComboBox;
import javax.swing.SwingConstants;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


public class GuiMgr extends JFrame{
	static Queue<Message> queue;
	static Logger logger;
	private JTextField textField;
	private JComboBox<String> selectTemplate;
	private JButton btnStop;
	private JButton btnPair;
	public JButton getBtnPair() {
		return btnPair;
	}

	public void setBtnPair(JButton btnPair) {
		this.btnPair = btnPair;
	}

	public JButton getBtnUnPair() {
		return btnUnPair;
	}

	public void setBtnUnPair(JButton btnUnPair) {
		this.btnUnPair = btnUnPair;
	}

	private JButton btnUnPair;
	public JButton getBtnStop() {
		return btnStop;
	}

	public void setBtnStop(JButton btnStop) {
		this.btnStop = btnStop;
	}

	public JButton getBtnStart() {
		return btnStart;
	}

	public void setBtnStart(JButton btnStart) {
		this.btnStart = btnStart;
	}

	private JButton btnStart;
	DefaultTableModel modelLogTable = new DefaultTableModel();
	private JTable logTable;
	private JTextField textFieldTmpltRetries;
	
	public JTextField getTextFieldTmpltRetries() {
		return textFieldTmpltRetries;
	}

	public void setTextFieldTmpltRetries(JTextField textFieldTmpltRetries) {
		this.textFieldTmpltRetries = textFieldTmpltRetries;
	}

	public JTextField getTextField() {
		return textField;
	}

	public void setTextField(JTextField textField) {
		this.textField = textField;
	}

	public JComboBox getSelectTemplate() {
		return selectTemplate;
	}

	public void setSelectTemplate(JComboBox selectTemplate) {
		this.selectTemplate = selectTemplate;
	}

	public GuiMgr(Queue<Message> q, Logger log) {
		logger = log;
		queue = q;
		setTitle("Tester");
		//this.pack();
		this.setSize(600, 750);
		this.setLocationRelativeTo(null);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[]{53, 128, 140, 54, 69, 0};
		gridBagLayout.rowHeights = new int[]{14, 40, 38, 45, 42, 0, 0, 0, 420, 0};
		gridBagLayout.columnWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		gridBagLayout.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		getContentPane().setLayout(gridBagLayout);
		
		JButton btnFinishSession = new JButton("Finish Session");
		btnFinishSession.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Finish Session Pressed");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.BUTTON;
				newMsg.iSource = Message.sourceType.FINISHSESS;
				queue.enqueue(newMsg);
			}
		});
		
		JButton btnStartSession = new JButton("Start Session");
		btnStartSession.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				logger.info("Start Session Pressed");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.BUTTON;
				newMsg.iSource = Message.sourceType.STARTSESS;
				queue.enqueue(newMsg);
			}
		});
		
		JLabel lblNewLabel = new JLabel("Device");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 18));
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.fill = GridBagConstraints.VERTICAL;
		gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel.gridx = 1;
		gbc_lblNewLabel.gridy = 1;
		getContentPane().add(lblNewLabel, gbc_lblNewLabel);
		
		textField = new JTextField();
		textField.setText("10.120.9.122");
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.insets = new Insets(0, 0, 5, 5);
		gbc_textField.fill = GridBagConstraints.BOTH;
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 1;
		getContentPane().add(textField, gbc_textField);
		textField.setColumns(10);
		
		btnPair = new JButton("Pair");
		btnPair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Pair Pressed");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.BUTTON;
				newMsg.iSource = Message.sourceType.PAIR;
				queue.enqueue(newMsg);
			}
		});
		GridBagConstraints gbc_btnPair = new GridBagConstraints();
		gbc_btnPair.fill = GridBagConstraints.BOTH;
		gbc_btnPair.insets = new Insets(0, 0, 5, 5);
		gbc_btnPair.gridx = 3;
		gbc_btnPair.gridy = 1;
		getContentPane().add(btnPair, gbc_btnPair);
		
		btnUnPair = new JButton("Un Pair");
		btnUnPair.setEnabled(false);
		btnUnPair.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Un Pair Pressed");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.BUTTON;
				newMsg.iSource = Message.sourceType.UNPAIR;
				queue.enqueue(newMsg);
			}
		});
		GridBagConstraints gbc_btnUnPair = new GridBagConstraints();
		gbc_btnUnPair.insets = new Insets(0, 0, 5, 0);
		gbc_btnUnPair.fill = GridBagConstraints.BOTH;
		gbc_btnUnPair.gridx = 4;
		gbc_btnUnPair.gridy = 1;
		getContentPane().add(btnUnPair, gbc_btnUnPair);
		GridBagConstraints gbc_btnStartSession = new GridBagConstraints();
		gbc_btnStartSession.fill = GridBagConstraints.BOTH;
		gbc_btnStartSession.insets = new Insets(0, 0, 5, 5);
		gbc_btnStartSession.gridx = 3;
		gbc_btnStartSession.gridy = 2;
		getContentPane().add(btnStartSession, gbc_btnStartSession);
		GridBagConstraints gbc_btnFinishSession = new GridBagConstraints();
		gbc_btnFinishSession.fill = GridBagConstraints.BOTH;
		gbc_btnFinishSession.insets = new Insets(0, 0, 5, 0);
		gbc_btnFinishSession.gridx = 4;
		gbc_btnFinishSession.gridy = 2;
		getContentPane().add(btnFinishSession, gbc_btnFinishSession);
		
		btnStop = new JButton("STOP");
		btnStop.setEnabled(false);
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("STOP Pressed");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.BUTTON;
				newMsg.iSource = Message.sourceType.STOP;
				queue.enqueue(newMsg);
			}
		});
		
		btnStart = new JButton("RUN");
		btnStart.setEnabled(false);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("RUN Pressed");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.BUTTON;
				newMsg.iSource = Message.sourceType.RUN;
				queue.enqueue(newMsg);
			}
		});
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnStart = new GridBagConstraints();
		gbc_btnStart.fill = GridBagConstraints.BOTH;
		gbc_btnStart.gridheight = 2;
		gbc_btnStart.insets = new Insets(0, 0, 5, 5);
		gbc_btnStart.gridx = 1;
		gbc_btnStart.gridy = 3;
		getContentPane().add(btnStart, gbc_btnStart);
		btnStop.setFont(new Font("Tahoma", Font.PLAIN, 14));
		GridBagConstraints gbc_btnStop = new GridBagConstraints();
		gbc_btnStop.insets = new Insets(0, 0, 5, 5);
		gbc_btnStop.fill = GridBagConstraints.BOTH;
		gbc_btnStop.gridheight = 2;
		gbc_btnStop.gridx = 2;
		gbc_btnStop.gridy = 3;
		getContentPane().add(btnStop, gbc_btnStop);
		
		JButton btnAddCommand = new JButton("Add Command");
		btnAddCommand.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Add Command Pressed");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.BUTTON;
				newMsg.iSource = Message.sourceType.ADDCMD;
				queue.enqueue(newMsg);
			}
		});
		GridBagConstraints gbc_btnAddCommand = new GridBagConstraints();
		gbc_btnAddCommand.fill = GridBagConstraints.BOTH;
		gbc_btnAddCommand.insets = new Insets(0, 0, 5, 5);
		gbc_btnAddCommand.gridx = 3;
		gbc_btnAddCommand.gridy = 3;
		getContentPane().add(btnAddCommand, gbc_btnAddCommand);
		
		JButton btnAddTemplate = new JButton("Add Template");
		btnAddTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Add Template Pressed");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.BUTTON;
				newMsg.iSource = Message.sourceType.ADDTEMPLATE;
				queue.enqueue(newMsg);
			}
		});
		GridBagConstraints gbc_btnAddTemplate = new GridBagConstraints();
		gbc_btnAddTemplate.fill = GridBagConstraints.BOTH;
		gbc_btnAddTemplate.insets = new Insets(0, 0, 5, 0);
		gbc_btnAddTemplate.gridx = 4;
		gbc_btnAddTemplate.gridy = 3;
		getContentPane().add(btnAddTemplate, gbc_btnAddTemplate);
		
		JButton btnEditTemplate = new JButton("Edit Template");
		btnEditTemplate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logger.info("Edit Template Pressed");
				Message newMsg = new Message();
				newMsg.iMsgType = Message.msgType.BUTTON;
				newMsg.iSource = Message.sourceType.EDITTEMPLATE;
				newMsg.iServiceType = Message.serviceType.EDIT;
				queue.enqueue(newMsg);
			}
		});
		GridBagConstraints gbc_btnEditTemplate = new GridBagConstraints();
		gbc_btnEditTemplate.fill = GridBagConstraints.BOTH;
		gbc_btnEditTemplate.insets = new Insets(0, 0, 5, 0);
		gbc_btnEditTemplate.gridx = 4;
		gbc_btnEditTemplate.gridy = 4;
		getContentPane().add(btnEditTemplate, gbc_btnEditTemplate);
		
		JLabel lblNewLabel_1 = new JLabel("Select Template to Run");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		lblNewLabel_1.setFont(new Font("Tahoma", Font.ITALIC, 13));
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.gridx = 1;
		gbc_lblNewLabel_1.gridy = 5;
		getContentPane().add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		JLabel lblTemplateRetries = new JLabel("Template Retries");
		lblTemplateRetries.setFont(new Font("Tahoma", Font.ITALIC, 12));
		GridBagConstraints gbc_lblTemplateRetries = new GridBagConstraints();
		gbc_lblTemplateRetries.insets = new Insets(0, 0, 5, 5);
		gbc_lblTemplateRetries.gridx = 3;
		gbc_lblTemplateRetries.gridy = 5;
		getContentPane().add(lblTemplateRetries, gbc_lblTemplateRetries);
		
		selectTemplate = new JComboBox<String>();
		GridBagConstraints gbc_selectTemplate = new GridBagConstraints();
		gbc_selectTemplate.gridwidth = 2;
		gbc_selectTemplate.insets = new Insets(0, 0, 5, 5);
		gbc_selectTemplate.fill = GridBagConstraints.HORIZONTAL;
		gbc_selectTemplate.gridx = 1;
		gbc_selectTemplate.gridy = 6;
		getContentPane().add(selectTemplate, gbc_selectTemplate);
		
		textFieldTmpltRetries = new JTextField();
		textFieldTmpltRetries.setHorizontalAlignment(SwingConstants.CENTER);
		textFieldTmpltRetries.setText("1");
		GridBagConstraints gbc_textFieldTmpltRetries = new GridBagConstraints();
		gbc_textFieldTmpltRetries.insets = new Insets(0, 0, 5, 5);
		gbc_textFieldTmpltRetries.fill = GridBagConstraints.BOTH;
		gbc_textFieldTmpltRetries.gridx = 3;
		gbc_textFieldTmpltRetries.gridy = 6;
		getContentPane().add(textFieldTmpltRetries, gbc_textFieldTmpltRetries);
		textFieldTmpltRetries.setColumns(10);
		
		JLabel lblLogs = new JLabel("Logs");
		lblLogs.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 14));
		GridBagConstraints gbc_lblLogs = new GridBagConstraints();
		gbc_lblLogs.anchor = GridBagConstraints.WEST;
		gbc_lblLogs.insets = new Insets(0, 0, 5, 5);
		gbc_lblLogs.gridx = 1;
		gbc_lblLogs.gridy = 7;
		getContentPane().add(lblLogs, gbc_lblLogs);					
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 4;
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 8;
		getContentPane().add(scrollPane, gbc_scrollPane);
		
		logTable = new JTable(modelLogTable);
		scrollPane.setViewportView(logTable);
		
		DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
		centerRenderer.setHorizontalAlignment( JLabel.CENTER );
		centerRenderer.setDoubleBuffered(false);
		
		logTable.setFont(new Font("Tahoma", Font.BOLD, 8));
		logTable.setRowHeight(23);
		logTable.setTableHeader(null);
		modelLogTable.setColumnCount(4);

		logTable.getColumnModel().getColumn(0).setCellRenderer( centerRenderer );	// Time
		logTable.getColumnModel().getColumn(1).setCellRenderer( centerRenderer );	// Type
		logTable.getColumnModel().getColumn(2).setCellRenderer( centerRenderer );	// Command / Response Text
		logTable.getColumnModel().getColumn(3).setCellRenderer( centerRenderer );	// Function / Result code
		
		logTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		this.setSize(600, 750);
		this.show();
	}

	public void initTemplateList(DefaultListModel<String> tmpltList) {
		this.selectTemplate.removeAllItems();
		for(int i = 0; i < tmpltList.size(); i++)
			this.selectTemplate.addItem(tmpltList.getElementAt(i));
	}
	
	public void addDataInLogTable(String col1, String col2, String col3, String col4) throws Exception {
		Vector<String> rowData = new Vector<String>();
		rowData.add(col1);
		rowData.add(col2);
		rowData.add(col3);
		rowData.add(col4);
		this.modelLogTable.addRow(rowData);
	}
	
//	public void viewAddCommandBox() throws Exception {
//		
//		JFrame frame = new JFrame();
//		JPanel panel = new JPanel();
//		
//		JLabel label = new JLabel("Command Name:");
//		label.setBounds(10,10,10,10);
//		
//		frame.getContentPane().add(panel);
//		
//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		frame.setSize(500, 500);
//		frame.show();
//	} 
}
