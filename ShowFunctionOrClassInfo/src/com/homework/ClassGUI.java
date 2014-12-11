package com.homework;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

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

		ch.constructFunMap(CLASS_HASH_INFO_PATH);
		ch.constructFunsInfos(CLASS_INFO_PATH);
		relatedTree.setModel(getRelatedTreeModel());
		scrollPane_rel = new JScrollPane(relatedTree);

		tabbedPane.addTab("类关联Top10信息", null, scrollPane_rel, null);

		PieDataset dataset = ch.createRelatedIDDataSet();
		JFreeChart chart = createChart(dataset, "类关联性比例图");
		panel = new ChartPanel(chart);
		panel.setBounds(400, 34, 445, 420);
		frame.getContentPane().add(panel);
	}

	public DefaultTreeModel getRelatedTreeModel() {
		return new DefaultTreeModel(new DefaultMutableTreeNode("类列表") {
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
					if (count < 10) {
						node = new DefaultMutableTreeNode(ch.getClassMap().get(
								info.getID()));
						for (String s : info.getRelatedClassID()) {
							DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(
									ch.getClassMap().get(s));
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

	public ImageIcon rescaleImage(File source, int maxHeight, int maxWidth) {
		int newHeight = 0, newWidth = 0; // Variables for the new height and
											// width
		int priorHeight = 0, priorWidth = 0;
		BufferedImage image = null;
		ImageIcon sizeImage;

		try {
			image = ImageIO.read(source); // get the image
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println("Picture upload attempted & failed");
		}

		sizeImage = new ImageIcon(image);

		if (sizeImage != null) {
			priorHeight = sizeImage.getIconHeight();
			priorWidth = sizeImage.getIconWidth();
		}

		// Calculate the correct new height and width
		if ((float) priorHeight / (float) priorWidth > (float) maxHeight
				/ (float) maxWidth) {
			newHeight = maxHeight;
			newWidth = (int) (((float) priorWidth / (float) priorHeight) * (float) newHeight);
		} else {
			newWidth = maxWidth;
			newHeight = (int) (((float) priorHeight / (float) priorWidth) * (float) newWidth);
		}

		// Resize the image

		// 1. Create a new Buffered Image and Graphic2D object
		BufferedImage resizedImg = new BufferedImage(newWidth, newHeight,
				BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = resizedImg.createGraphics();

		// 2. Use the Graphic object to draw a new image to the image in the
		// buffer
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
				RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		g2.drawImage(image, 0, 0, newWidth, newHeight, null);
		g2.dispose();

		// 3. Convert the buffered image into an ImageIcon for return
		return (new ImageIcon(resizedImg));
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
}
