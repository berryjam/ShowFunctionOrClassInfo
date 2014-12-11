package com.homework;

import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

public class FunctionGUI {

	private FunctionHandler fh = new FunctionHandler();

	public static String functionInfoPath = "/Users/berryjam/Tsinghua/软件体系结构/大作业相关/doxygen+ubigraph+GUI/函数信息.txt";
	public static String functionHashInfoPath = "/Users/berryjam/Tsinghua/软件体系结构/大作业相关/doxygen+ubigraph+GUI/函数信息散列表.txt";

	private JFrame frame;
	private JTabbedPane tabbedPane;
	private JPanel panel;
	private JScrollPane scrollPane_ref;
	private JScrollPane scrollPane_refby;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FunctionGUI window = new FunctionGUI();
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
					FunctionGUI window = new FunctionGUI();
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
	public FunctionGUI() {
		try {
			initialize();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

		JTree refbyTree = new JTree();
		JTree refTree = new JTree();

		fh.constructFunMap(functionHashInfoPath);
		fh.constructFunsInfos(functionInfoPath);
		refbyTree.setModel(getReferencedbyTreeModel());
		refTree.setModel(getReferenceTreeModel());
		scrollPane_ref = new JScrollPane(refTree);

		tabbedPane.addTab("函数调用次数Top10信息", null, scrollPane_ref, null);

		scrollPane_refby = new JScrollPane(refbyTree);
		tabbedPane.addTab("函数被调用次数Top10信息", null, scrollPane_refby, null);

		PieDataset dataset = fh.createReferencedbyDataSet();
		JFreeChart chart = createChart(dataset, "函数被调用比例图");
		panel = new ChartPanel(chart);
		panel.setBounds(400, 34, 445, 420);
		frame.getContentPane().add(panel);

		tabbedPane.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				for (int i = 0; i < tabbedPane.getTabCount(); i++) {
					Rectangle rect = tabbedPane.getBoundsAt(i);
					if (rect.contains(e.getX(), e.getY())) {
						switch (i) {
						case 0:
							frame.getContentPane().remove(panel);
							PieDataset dsRef = fh.createReferenceDataSet();
							JFreeChart chartRef = createChart(dsRef, "函数调用比例图");
							panel = new ChartPanel(chartRef);
							panel.setBounds(400, 34, 445, 420);
							frame.getContentPane().add(panel);
							panel.repaint();
							break;
						case 1:
							frame.getContentPane().remove(panel);
							PieDataset dsRefby = fh.createReferencedbyDataSet();
							JFreeChart chartRefby = createChart(dsRefby,
									"函数被调用比例图");
							panel = new ChartPanel(chartRefby);
							panel.setBounds(400, 34, 445, 420);
							frame.getContentPane().add(panel);
							panel.repaint();
							break;
						}
						break;
					}
				}
			}

		});

	}

	public DefaultTreeModel getReferencedbyTreeModel() {
		return new DefaultTreeModel(new DefaultMutableTreeNode("函数列表") {
			{
				Collections.sort(fh.getFunctionInfos(),
						new Comparator<FunctionInfo>() {

							@Override
							public int compare(FunctionInfo o1, FunctionInfo o2) {
								return Integer.compare(o2.getReferencedbyID()
										.size(), o1.getReferencedbyID().size());
							}
						});
				int count = 0;
				DefaultMutableTreeNode node = null;
				for (FunctionInfo info : fh.getFunctionInfos()) {
					if (count < 10) {
						node = new DefaultMutableTreeNode(fh.getFunctionMap()
								.get(info.getID()));
						for (String s : info.getReferencedbyID()) {
							DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(
									fh.getFunctionMap().get(s));
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

	public DefaultTreeModel getReferenceTreeModel() {
		return new DefaultTreeModel(new DefaultMutableTreeNode("函数列表") {
			{
				Collections.sort(fh.getFunctionInfos(),
						new Comparator<FunctionInfo>() {

							@Override
							public int compare(FunctionInfo o1, FunctionInfo o2) {
								return Integer.compare(o2.getReferenceID()
										.size(), o1.getReferenceID().size());
							}
						});
				int count = 0;
				DefaultMutableTreeNode node = null;
				for (FunctionInfo info : fh.getFunctionInfos()) {
					if (count < 10) {
						node = new DefaultMutableTreeNode(fh.getFunctionMap()
								.get(info.getID()));
						for (String s : info.getReferenceID()) {
							DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(
									fh.getFunctionMap().get(s));
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
