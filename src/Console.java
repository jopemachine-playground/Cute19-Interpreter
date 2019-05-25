import java.awt.BorderLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import Node.Node;
import Node.NodePrinter;
import Scanner.CuteParser;
import java.awt.Color;
import java.awt.Font;

public class Console extends JFrame {

	JTextArea commandInputArea;
	JTextArea commandLogArea;
	String DefaultCursor = ">>";
	
	public static void main(String[] args) {
		Console console = new Console();
		console.setVisible(true);
	}

	public Console() {
		
		this.setTitle("PL Homework (Item 1)");
		this.setBounds(100, 100, 710, 586);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		commandLogArea = new JTextArea();
		commandLogArea.setFont(new Font("Consolas", Font.PLAIN, 18));
		commandLogArea.setEditable(false);
		commandLogArea.setBackground(Color.LIGHT_GRAY);
		commandLogArea.setLineWrap(true);
		commandLogArea.setToolTipText("Show you the command log");

		this.getContentPane().add(commandLogArea, BorderLayout.CENTER);
		
		commandLogArea.setText("PL HomeWork");

		commandInputArea = new JTextArea();
		commandInputArea.setForeground(Color.WHITE);
		commandInputArea.setBackground(Color.GRAY);
		commandInputArea.setFont(new Font("Consolas", Font.ITALIC, 19));
		commandInputArea.setToolTipText("Write the command");
		commandInputArea.setLineWrap(true);
		
		commandInputArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String input = commandInputArea.getText();
					e.consume();
					commandInputArea.setText("");
					if(isInBuildInFunction(input)) return;
					AppendLine(commandLogArea, DefaultCursor + " " + input);
					parsing(input);
				}
			}
		});
		
		getContentPane().add(commandInputArea, BorderLayout.NORTH);
	}
	
	private void AppendLine(JTextArea console, String command) {
		console.append("\n" + command);
	}

	private void parsing(String input) {
		CuteParser cuteParser = new CuteParser(input);
		CuteInterpreter interpreter = new CuteInterpreter();
		Node parseTree = cuteParser.parseExpr();
		Node resultNode = interpreter.runExpr(parseTree);
		NodePrinter nodePrinter = new NodePrinter(resultNode);
		AppendLine(commandLogArea, nodePrinter.prettyPrint());
	}
	
	private boolean isInBuildInFunction(String input) {
		switch(input) {
		case "clear" :
			commandLogArea.setText("PL HomeWork");
			return true;
		}
		
		return false;
	}

}
