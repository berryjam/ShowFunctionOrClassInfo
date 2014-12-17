package com.homework;

import java.awt.EventQueue;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

public class ClassGUI {

	private ClassHandler ch = new ClassHandler();

	public static final String CLASS_INFO_PATH = "/Users/berryjam/Tsinghua/软件体系结构/大作业相关/doxygen+ubigraph+GUI/类信息.txt";
	public static final String CLASS_HASH_INFO_PATH = "/Users/berryjam/Tsinghua/软件体系结构/大作业相关/doxygen+ubigraph+GUI/类信息散列表.txt";
	public static final String CLASS_FILE_HASH_INFO_PATH = "/Users/berryjam/Tsinghua/软件体系结构/大作业相关/doxygen+ubigraph+GUI/类路径散列表.txt";

	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JScrollPane scrollPane_rel;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ClassGUI window = new ClassGUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public static void show() {
		try {
			ClassGUI window = new ClassGUI();
			window.frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the application.
	 */
	public ClassGUI() {
		try {
			initialize();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void initialize() throws FileNotFoundException, IOException {
		frame = new JFrame();
		frame.setBounds(100, 100, 851, 482);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JLabel label = new JLabel("程序分析可视化工具");
		label.setBounds(321, 6, 117, 16);
		frame.getContentPane().add(label);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(6, 37, 382, 361);

		frame.getContentPane().add(tabbedPane);

		JTree relatedTree = new JTree();

		ch.constructClassMap(CLASS_HASH_INFO_PATH);
		ch.constructFilePathMap(CLASS_FILE_HASH_INFO_PATH);
		ch.constructClassesInfos(CLASS_INFO_PATH);
		relatedTree.setModel(getRelatedTreeModel());
		setCLassTreeMouseListener(relatedTree);
		scrollPane_rel = new JScrollPane(relatedTree);

		tabbedPane.addTab("类关联Top10信息", null, scrollPane_rel, null);

		PieDataset dataset = ch.createRelatedIDDataSet();
		JFreeChart chart = createChart(dataset, "类关联性比例图");
		panel = new ChartPanel(chart);
		panel.setBounds(400, 34, 445, 420);
		frame.getContentPane().add(panel);
	}

	public DefaultTreeModel getRelatedTreeModel() {
		return new DefaultTreeModel(new DefaultMutableTreeNode(new ClassNode(
				"-1", "类列表", null)) {
			{
				Collections.sort(ch.getClassInfos(),
						new Comparator<ClassInfo>() {

							@Override
							public int compare(ClassInfo o1, ClassInfo o2) {
								return Integer.compare(o2.getRelatedClassID()
										.size(), o1.getRelatedClassID().size());
							}
						});
				int count = 0;
				DefaultMutableTreeNode node = null;
				for (ClassInfo info : ch.getClassInfos()) {
					String id = info.getID();
					if (count < 10) {
						node = new DefaultMutableTreeNode(new ClassNode(id, ch
								.getClassMap().get(id), ch.getFileMap().get(
								ch.getRefMap().get(id))));
						for (String s : info.getRelatedClassID()) {
							DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(
									new ClassNode(s, ch.getClassMap().get(s),
											ch.getFileMap().get(
													ch.getRefMap().get(s))));
							node.add(tmp);
						}
						add(node);
						count++;
					} else
						break;
				}
			}
		});
	}

	public JFreeChart createChart(PieDataset dataset, String title) {

		JFreeChart chart = ChartFactory.createPieChart3D(title, // chart title
				dataset, // data
				true, // include legend
				true, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(290);
		plot.setDirection(Rotation.CLOCKWISE);
		plot.setForegroundAlpha(0.5f);
		return chart;

	}

	public void setCLassTreeMouseListener(JTree tree) {
		MouseListener ml = new MouseListener() {

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				int selRow = tree.getRowForLocation(e.getX(), e.getY());
				TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
				if (selRow != -1) {
					if (e.getClickCount() == 1) {
						DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath
								.getLastPathComponent();
						ClassNode cn = (ClassNode) node.getUserObject();
						String filePath = cn.getFilePath();
						if (filePath != null)
							TextEditorHelper.openFile(filePath);
						System.out.println("FilePath:" + cn.getFilePath());

					} else if (e.getClickCount() == 2) {
						System.out.println("Double click-" + "selRow:" + selRow
								+ ";" + "selPath:" + selPath);
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

		};
		tree.addMouseListener(ml);
	}
}
