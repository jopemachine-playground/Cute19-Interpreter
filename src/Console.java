import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;

import Node.Node;
import Node.NodePrinter;
import Scanner.CuteParser;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class Console extends JFrame {

	JTextArea commandLogArea;

	String DefaultCursor = ">>";

	private int logCharsNumber = 0;

	private int currentCaretPosition;

	public static void main(String[] args) {
		Console console = new Console();
		console.setVisible(true);
	}

	public Console() {

		this.setTitle("PL Homework (Item 1)");
		this.setBounds(100, 100, 710, 586);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);

		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 694, 547);
		getContentPane().add(panel);

		commandLogArea = new JTextArea();
		commandLogArea.setBounds(0, 0, 104, 24);
		commandLogArea.setForeground(Color.WHITE);
		commandLogArea.setText(DefaultCursor);
		commandLogArea.setFont(new Font("Arial", Font.PLAIN, 17));
		commandLogArea.setBackground(Color.BLACK);
		commandLogArea.setLineWrap(true);
		commandLogArea.setToolTipText("Show you the command log");

		logCharsNumber = commandLogArea.getText().length();

		commandLogArea.addKeyListener(new KeyListener() {
			@Override
			public void keyPressed(KeyEvent e) {

				currentCaretPosition = commandLogArea.getCaretPosition();

				if (currentCaretPosition < logCharsNumber) {
					e.consume();
					return;
				}

				if ((logCharsNumber == commandLogArea.getText().length()) && e.getKeyCode() == KeyEvent.VK_BACK_SPACE
						|| e.getKeyCode() == KeyEvent.VK_LEFT) {
					e.consume();
					return;
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String wholeInput = commandLogArea.getText();
					String input = wholeInput.substring(logCharsNumber);
					e.consume();
					if (isInBuildInFunction(input))
						return;
					parsing(input);
					AppendLine(commandLogArea, DefaultCursor);
					logCharsNumber = commandLogArea.getText().length();
				}
			}

			@Override
			public void keyTyped(KeyEvent e) {

				currentCaretPosition = commandLogArea.getCaretPosition();

				if (currentCaretPosition < logCharsNumber) {
					e.consume();
					return;
				}

				if ((logCharsNumber == commandLogArea.getText().length()) && e.getKeyCode() == KeyEvent.VK_BACK_SPACE
						|| e.getKeyCode() == KeyEvent.VK_LEFT) {
					e.consume();
					return;
				}

			}

			@Override
			public void keyReleased(KeyEvent e) {

				currentCaretPosition = commandLogArea.getCaretPosition();

				if (currentCaretPosition < logCharsNumber) {
					e.consume();
					return;
				}

				if ((logCharsNumber == commandLogArea.getText().length()) && e.getKeyCode() == KeyEvent.VK_BACK_SPACE
						|| e.getKeyCode() == KeyEvent.VK_LEFT) {
					e.consume();
					return;
				}

			}
		});
		panel.setLayout(null);

		commandLogArea.setCaretPosition(logCharsNumber);

		JScrollPane scrollPane = new JScrollPane(commandLogArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBounds(0, 0, 694, 547);
		panel.add(scrollPane);
		scrollPane.setLayout(new ScrollPaneLayout());
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
		switch (input) {
		case "clear":
			commandLogArea.setText(DefaultCursor);
			logCharsNumber = commandLogArea.getText().length();
			return true;
		}

		return false;
	}
}
