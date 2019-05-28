import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;

import Node.Node;
import Node.NodePrinter;
import Scanner.CuteParser;

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
		
		commandLogArea = new JTextArea();
		commandLogArea.setForeground(Color.WHITE);
		commandLogArea.setText(DefaultCursor);
		commandLogArea.setFont(new Font("Consolas", Font.PLAIN, 18));
		commandLogArea.setBackground(Color.BLACK);
		commandLogArea.setLineWrap(true);
		commandLogArea.setToolTipText("Show you the command log");

		this.getContentPane().add(commandLogArea, BorderLayout.CENTER);
		
		logCharsNumber = commandLogArea.getText().length();

		commandLogArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				
				currentCaretPosition = commandLogArea.getCaretPosition();
				System.out.println(currentCaretPosition);
				// 프롬프트 및 기존의 로그를 지우지 못하게 하기 위한 코드
				if(logCharsNumber > commandLogArea.getText().length() - 1 
						|| currentCaretPosition < logCharsNumber) {
					System.out.println();
					e.consume();
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String wholeInput = commandLogArea.getText();
					String input = wholeInput.substring(logCharsNumber);
					e.consume();
					if(isInBuildInFunction(input)) return;
					parsing(input);
					AppendLine(commandLogArea, DefaultCursor);
					logCharsNumber = commandLogArea.getText().length();
				}
			}
		});
		
		commandLogArea.setCaretPosition(logCharsNumber);
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
			commandLogArea.setText(DefaultCursor);
			logCharsNumber = commandLogArea.getText().length();
			return true;
		}
		
		return false;
	}

}
