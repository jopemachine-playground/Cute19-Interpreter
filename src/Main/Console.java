package Main;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import Node.Node;
import Node.NodePrinter;
import Scanner.CuteParser;

public class Console extends JFrame {

	private JTextArea commandLogArea;
	private JTextField textField;
	
	private Node parseTree;
	private Node resultNode;
	private NodePrinter nodePrinter;
	
	private CuteParser cuteParser;
	private CuteInterpreter interpreter;
	
	private static final String defaultPrompt = "$";
	private static final String initialString = "Welcome to Cute Interpreter!";
	
	private static Console INSTANCE;
	
	private String errBuffer = "";
	
	public static Console getInstance() {
		if(INSTANCE == null) {
			INSTANCE = new Console();
		}
		return INSTANCE;
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Console frame = Console.getInstance();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private Console() {

		setTitle("Cute Interpreter");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		JPanel contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 0, 775, 563);
		contentPane.add(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setFont(new Font("Consolas", Font.PLAIN, 13));
		panel.setBackground(Color.WHITE);
		scrollPane.setViewportView(panel);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[] {0, 760, 0};
		gbl_panel.rowHeights = new int[]{24, 0};
		gbl_panel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		commandLogArea = new JTextArea();
		commandLogArea.setEditable(false);
		commandLogArea.setFont(new Font("Consolas", Font.PLAIN, 13));
		commandLogArea.setBounds(new Rectangle(0, 0, 0, 24));
		commandLogArea.setBorder(new EmptyBorder(0, 0, 0, 0));
		GridBagConstraints gbc_textArea = new GridBagConstraints();
		gbc_textArea.gridwidth = 2;
		gbc_textArea.fill = GridBagConstraints.HORIZONTAL;
		gbc_textArea.anchor = GridBagConstraints.NORTH;
		gbc_textArea.insets = new Insets(0, 0, 5, 5);
		gbc_textArea.gridx = 0;
		gbc_textArea.gridy = 0;
		panel.add(commandLogArea, gbc_textArea);
		commandLogArea.append(initialString);

		JTextField preTextField = new JTextField();
		preTextField.setRequestFocusEnabled(false);
		preTextField.setFont(new Font("Consolas", Font.PLAIN, 13));
		preTextField.setBorder(new EmptyBorder(0, 0, 0, 0));
		GridBagConstraints gbc_preTextField = new GridBagConstraints();
		gbc_preTextField.insets = new Insets(0, 0, 0, 5);
		gbc_preTextField.anchor = GridBagConstraints.NORTH;
		gbc_preTextField.fill = GridBagConstraints.HORIZONTAL;
		gbc_preTextField.gridx = 0;
		gbc_preTextField.gridy = 1;
		panel.add(preTextField, gbc_preTextField);
		preTextField.setColumns(2);
		preTextField.setText(defaultPrompt);
		
		textField = new JTextField();
		textField.setFont(new Font("Consolas", Font.PLAIN, 13));
		textField.setBorder(new EmptyBorder(0, 0, 0, 0));
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.NORTH;
		gbc_textField.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField.gridx = 1;
		gbc_textField.gridy = 1;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);

		this.addWindowListener(new WindowAdapter() {
			public void windowOpened(WindowEvent e) {
				textField.requestFocus();
			}
		});

		textField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String input = textField.getText();
				textField.setText("");

				if(isInBuildInFunction(input)) {
					return;
				}
				
				cuteParser = new CuteParser(input);
				interpreter = new CuteInterpreter(input);
				parseTree = cuteParser.parseExpr();
				resultNode = interpreter.runExpr(parseTree);
				
				// if errBuffer is set, clear buffer and print err message
				if(errBuffer != "") {
					printConsole(input, errBuffer);
					setErrBuffer("");
					return;
				}
				
				nodePrinter = new NodePrinter(resultNode);
				String output = nodePrinter.prettyPrint();
				printConsole(input, output);
				setErrBuffer("");
			}
		});
	}

	private boolean isInBuildInFunction(String input) {
		switch (input) {
		case "clear":
			commandLogArea.setText(initialString);
			textField.setText("");
			return true;
		}

		return false;
	}
	
	public void setErrBuffer(String err) {
		errBuffer = err;
	}
	
	public void printConsole(String input, String result) {
		
		if(input.length() != 0) {
			commandLogArea.append("\n" + defaultPrompt + "  " + input);	
		}
		
		if(result.length() != 0) {
			commandLogArea.append("\n...  " + result);
		}
	}
	
}
