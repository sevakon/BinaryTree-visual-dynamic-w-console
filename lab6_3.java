import java.io.*;
import java.util.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class lab6_3 {
	BufferedReader br;
	StringTokenizer in;
	PrintWriter out;
	Node root = null;
	Node nullNode = null;

	public String nextToken() throws IOException {
		while (in == null || !in.hasMoreTokens()) {
			in = new StringTokenizer(br.readLine());
		}
		return in.nextToken();
	}

	public static void main(String[] args) {
		new lab6_3().run();
	}

	public void solve() throws IOException {
		Scanner in = new Scanner(System.in);
		//while (true) {
		//	String input;
		//	try {
		//		input = nextToken();
		//	} catch (NullPointerException e) {
		//		break;
		//	}
		//	if (input.equals("insert"))
		//		root = addRecursive(root, Integer.parseInt(nextToken()));
		//	else if (input.equals("exists"))
		//		out.println(containsNodeRecursive(root, Integer.parseInt(nextToken())) ? "true" : "false");
		//	else if (input.equals("next")) {
		//		int value = Integer.parseInt(nextToken());
		//		out.println(next(root, value) - value > 0 ? next(root, value) : "none");
		//		tempValueForNext = 0;
		//	} else if (input.equals("prev")) {
		//		int value = Integer.parseInt(nextToken());
		//		out.println(prev(root, value) - value > 0 ? "none" : prev(root, value));
		//		tempValueForPrev = 0;
		//	} else {
		//		root = deleteRecursive(root, Integer.parseInt(nextToken()));
		//	}
		//}
		//createFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		String input = "null";
		while (true) {
			input = JOptionPane.showInputDialog("Type a command (EXIT to escape)");
			if(input.equals("EXIT") || input.equals("exit")) 
				break;
			if (input.contains("insert")) {
				root = addRecursive(root, Integer.parseInt(input.substring(7)));
				updateFrame();
				}
			else if (input.equals("exists")) {
				out.println(containsNodeRecursive(root, in.nextInt()) ? "true" : "false");
				}
			else if (input.equals("next")) {
				int value = in.nextInt();
				out.println(next(root, value) - value > 0 ? next(root, value) : "none");
				tempValueForNext = 0;
			} else if (input.equals("prev")) {
				int value = in.nextInt();
				out.println(prev(root, value) - value > 0 ? "none" : prev(root, value));
				tempValueForPrev = 0;
			} else {
				root = deleteRecursive(root,  Integer.parseInt(input.substring(7)));
				updateFrame();
			}
		}
	}
	
	public void updateFrame() {
		BinaryTree bt = new BinaryTree();
		frame.add(bt);
		frame.setSize(1000, 800);
		frame.setVisible(true);
	}

	JFrame frame = new JFrame("Binary Tree visualization");


	public void run() {
		try {
			br = new BufferedReader(new FileReader("bstsimple.in"));
			out = new PrintWriter("bstsimple.out");
			solve();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	class Node {
		int value;
		Node left;
		Node right;

		Node(int value) {
			this.value = value;
			right = null;
			left = null;
		}
	}

	public Node addRecursive(Node current, int value) {
		if (current == null) {
			return new Node(value);
		}

		if (value < current.value) {
			current.left = addRecursive(current.left, value);
		} else if (value > current.value) {
			current.right = addRecursive(current.right, value);
		} else {
			return current;
		}

		return current;
	}

	public Node deleteRecursive(Node current, int value) {
		if (current == null) {
			return null;
		}

		if (value == current.value) {
			// no children
			if (current.left == null && current.right == null) {
				return null;
			}

			// one child
			if (current.right == null) {
				return current.left;
			}
			if (current.left == null) {
				return current.right;
			}

			// both children
			int smallestValue = findSmallestValue(current.right);
			current.value = smallestValue;
			current.right = deleteRecursive(current.right, smallestValue);
			return current;

		}
		if (value < current.value) {
			current.left = deleteRecursive(current.left, value);
			return current;
		}
		current.right = deleteRecursive(current.right, value);
		return current;
	}

	private int findSmallestValue(Node root) {
		return root.left == null ? root.value : findSmallestValue(root.left);
	}

	public boolean containsNodeRecursive(Node current, int value) {
		if (current == null) {
			return false;
		}
		if (value == current.value) {
			return true;
		}
		return value < current.value ? containsNodeRecursive(current.left, value)
				: containsNodeRecursive(current.right, value);
	}

	public int findHeight(Node current) {
		if (current == null)
			return 0;
		else if (current.left == null)
			return 1 + findHeight(current.right);
		else if (current.right == null)
			return 1 + findHeight(current.left);
		else
			return 1 + Math.max(findHeight(current.left), findHeight(current.right));
	}

	public int tempValueForPrev = 0;

	public int prev(Node node, int value) {
		int min = Integer.MAX_VALUE;
		if (node != null) {
			prev(node.left, value);
			if (node.value < value && node.value - value < min) {
				min = node.value - value;
				tempValueForPrev = node.value;
			}
			prev(node.right, value);
		}
		return tempValueForPrev;
	}

	public int tempValueForNext = 0;

	public int next(Node node, int value) {
		int min = Integer.MAX_VALUE;
		if (node != null) {
			if (node.value - value < min && node.value - value > 0) {
				min = node.value - value;
				tempValueForNext = node.value;
			}
			next(node.left, value);
			next(node.right, value);
		}
		return tempValueForNext;
	}

	public class BinaryTree extends JPanel {
		int treeSize = findHeight(root);
		int[] values = new int[(int) Math.pow(treeSize, 2)];
		int currentX = 0;
		int currentY = 0;
		final int nodeSize = 30;
		final int spaceSizeY = 70;
		int spaceSizeX = 0;
		int prevX = 0;
		int prevSpaceSizeX = 0;
		int step = 0;

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			this.setBackground(Color.WHITE);
			currentX = 500;
			currentY = 25;
			traverseLevelOrder();
			g.setColor(new Color(12, 210, 12));
			g.setFont(new Font("Arial", Font.BOLD, 25));
			g.drawString("This Biniary Tree's height is " + Integer.toString(treeSize), 25 + nodeSize - 5,
					25 + nodeSize - 5);
			g.setFont(new Font("Arial", Font.BOLD, 30));
			g.drawOval(currentX - nodeSize / 2, currentY, nodeSize, nodeSize);
			g.drawString(Integer.toString(root.value), 485 + 7, 25 + nodeSize - 5);
			currentY += spaceSizeY;
			int level = 1;
			int count = 0;
			for (int i = 1; i < (int) Math.pow(treeSize, 2); i++) {
				if (i >= 1 && i < 3) {
					prevX = 500;
					currentX = 400;
					spaceSizeX = 200;
					prevSpaceSizeX = 0;
				} else if (i >= 3 && i < 7) {
					spaceSizeX = 100;
					currentX = 350;
					prevX = 400;
					prevSpaceSizeX = 200;
				} else {
					currentX = 325;
					spaceSizeX = 50;
					prevX = 350;
					prevSpaceSizeX = 100;
				}
				count++;
				if (values[i] != Integer.MIN_VALUE) {
					g.drawOval(currentX - nodeSize / 2 + spaceSizeX * (i - (int) (Math.pow(2, level)) + 1), currentY,
							nodeSize, nodeSize);
					if (values[i] < 0)
						g.drawString(Integer.toString(values[i]),
								currentX - nodeSize / 2 + spaceSizeX * (i - (int) (Math.pow(2, level)) + 1),
								currentY + nodeSize - 5);
					else
						g.drawString(Integer.toString(values[i]),
								currentX - nodeSize / 2 + 7 + spaceSizeX * (i - (int) (Math.pow(2, level)) + 1),
								currentY + nodeSize - 5);

					g.drawLine(prevX + prevSpaceSizeX * step, currentY - spaceSizeY + nodeSize,
							currentX + spaceSizeX * (i - (int) (Math.pow(2, level)) + 1), currentY);
				}
				if (count % 2 == 0)
					step++;
				if (i + 2 == Math.pow(2, level + 1)) {
					level++;
					currentY += spaceSizeY;
					count = 0;
					step = 0;
				}
			}
		}

		public void traverseLevelOrder() {

			Queue<Node> nodes = new LinkedList<>();
			nodes.add(root);

			int i = 0;
			while (!nodes.isEmpty() && i != (int) Math.pow(treeSize, 2)) {
				Node node = nodes.remove();
				if (node != nullNode)
					values[i] = node.value;
				else
					values[i] = Integer.MIN_VALUE;
				if (node != nullNode && node.left != null)
					nodes.add(node.left);
				else
					nodes.add(nullNode);
				if (node != nullNode && node.right != null)
					nodes.add(node.right);
				else
					nodes.add(nullNode);
				i++;
			}

		}

	}

}
