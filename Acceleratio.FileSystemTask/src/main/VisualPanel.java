package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import data.IDataAccessObject;
import models.PathItem;

/**
 * The {@link JPanel} implementation
 * called from the {@link VisualFrame}.
 * 
 * @author Petar Kovacevic
 * @version 1.0
 */
public class VisualPanel extends JPanel {

	private static final long	serialVersionUID = 1L;	// ignore
	
	private IDataAccessObject	data;
	private Path 				currentPath;
	
	private JTextField			txtPath;
	private JTextPane			txtStatus;
	private JComboBox<PathItem>	cbFavorite;
	private JButton				btnPath;
	private JTree				treeHierarchy;
	private JPanel				centerSubPanel;
	private JScrollPane			treeView;
	
	public VisualPanel(IDataAccessObject data) {
		this.data = data;
	}
	
	/**
	 * Initialization of {@link VisualPanel}'s components.
	 */
	public void initGUI() {
		setLayout(new BorderLayout(0, 0));
		add(createNorthPanel(), BorderLayout.NORTH);
		add(createCenterPanel(), BorderLayout.CENTER);
		add(createEastPanel(), BorderLayout.EAST);
		add(createSouthPanel(), BorderLayout.SOUTH);
		try {
			currentPath = data.getLastFavorite().getParent();
		} catch (NullPointerException e) {
			
		}
		refresh();
	}
	
	private JPanel createNorthPanel() {
		JPanel northSubPanel = new JPanel();
		northSubPanel.setLayout(new BoxLayout(northSubPanel, BoxLayout.X_AXIS));
		
		JLabel lblPath = new JLabel(" Input absolute path:   ");
		northSubPanel.add(lblPath);
		
		txtPath = new JTextField();
		northSubPanel.add(txtPath);
		
		northSubPanel.add(createBtnPath());
		
		return northSubPanel;
	}
	
	private JButton createBtnPath() {
		btnPath = new JButton("Go to");
		btnPath.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Path newPath = Paths.get(txtPath.getText());
				if (newPath.toFile().exists()) {
					currentPath = newPath;
					cbFavorite.setSelectedIndex(-1);
					refresh();
				} else {
					txtStatus.setText("path not valid");
				}
			}
		});
		txtPath.addActionListener(btnPath.getActionListeners()[0]);
		return btnPath;
	}
	
	private JPanel createCenterPanel() {
		centerSubPanel = new JPanel();
		centerSubPanel.setLayout(new GridLayout(0, 1, 0, 0));
		
		treeHierarchy = new JTree();
		treeView = new JScrollPane(treeHierarchy);
		centerSubPanel.add(treeView);
		
		return centerSubPanel;
	}
	
	private JPanel createEastPanel() {
		JPanel eastSubPanel = new JPanel();
		eastSubPanel.setLayout(new BoxLayout(eastSubPanel, BoxLayout.Y_AXIS));
		
		JLabel lblStatusDescription = new JLabel("<html><i>Choose a file from"
												+ "<br>the hieararchy tree"
												+ "<br>to add to favorites"
												+ "</i></html>");
		eastSubPanel.add(lblStatusDescription);
		
		eastSubPanel.add(createBtnAddFavorite());
		
		txtStatus = new JTextPane();
		txtStatus.setEditable(false);
		txtStatus.setEnabled(false);
		txtStatus.setBackground(Color.DARK_GRAY);
		eastSubPanel.add(txtStatus);
		
		return eastSubPanel;
	}
	
	private JButton createBtnAddFavorite() {
		JButton btnAddFavorite = new JButton("Add to favorites");
		btnAddFavorite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!currentPath.toFile().isFile()) {
					txtStatus.setText("please select a file first");
					return;
				}
				if (data.addNewFavorite(currentPath)) {
					refreshCbFavorite(currentPath);
					txtStatus.setText("new favorite added");
					return;
				}
				txtStatus.setText("failed to add to favorite");
			}
		});
		return btnAddFavorite;
	}
	
	private JPanel createSouthPanel() {
		JPanel southSubPanel = new JPanel();
		southSubPanel.setLayout(new BoxLayout(southSubPanel, BoxLayout.X_AXIS));
		
		JLabel lblFavorite = new JLabel(" Choose favorite:   ");
		southSubPanel.add(lblFavorite);
		
		southSubPanel.add(createCbFavorite());
		
		southSubPanel.add(createBtnDeleteFavorite());
		
		return southSubPanel;
	}
	
	private JComboBox<PathItem> createCbFavorite() {
		cbFavorite = new JComboBox<>();
		refreshCbFavorite(data.getLastFavorite());
		cbFavorite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (cbFavorite.getSelectedIndex() != -1) {  
					PathItem currentPathItem = (PathItem) cbFavorite.getSelectedItem();
					currentPath = currentPathItem.getValue().getParent();
					refresh();
		        }
			}
		});
		cbFavorite.setSelectedItem(new PathItem(data.getLastFavorite()));
		return cbFavorite;
	}
	
	private void refreshCbFavorite(Path refreshPath) {
		cbFavorite.removeAllItems();
		for (Path favorite : data.getFavorites()) {
			cbFavorite.addItem(new PathItem(favorite));
		}
		currentPath = refreshPath;
		cbFavorite.setSelectedIndex(-1);
	}
	
	private JButton createBtnDeleteFavorite() {
		JButton btnDeleteFavorite = new JButton("Delete from favorites");
		btnDeleteFavorite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				PathItem currentPathItem = (PathItem) cbFavorite.getSelectedItem();
				try {
					if (data.deleteFavorite(currentPathItem.getValue())) {
						txtStatus.setText("deleted " + currentPathItem);
						refreshCbFavorite(null);
					} else {
						txtStatus.setText("failed to delete " + currentPathItem);
					}
				} catch(NullPointerException e1) {
					txtStatus.setText("please select favorite to delete");
				}
			}
		});
		return btnDeleteFavorite;
	}
	
	private boolean rebuildTreeHierarchy(Path path) {
		boolean success = true;
		centerSubPanel.remove(treeView);
		try {
			treeHierarchy = HierarchyTreeLogic.buildFromPath(path);
		} catch (Exception e) {
			success = false;
			treeHierarchy = new JTree();
		}
		treeHierarchy.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		treeHierarchy.addTreeSelectionListener(new TreeSelectionListener() {
			@Override
		    public void valueChanged(TreeSelectionEvent e) {
		        DefaultMutableTreeNode node = (DefaultMutableTreeNode)
		        				treeHierarchy.getLastSelectedPathComponent();
		        if (node == null) return;
		        PathItem currentPathItem = (PathItem) node.getUserObject();
				currentPath = currentPathItem.getValue().toAbsolutePath();
		    }
		});
		for (int i = 0; i < treeHierarchy.getRowCount(); i++) {
			treeHierarchy.expandRow(i);
		}
		treeView = new JScrollPane(treeHierarchy);
		centerSubPanel.add(treeView);
		return success;
	}
	
	/**
	 * Refresh the {@link VisualPanel#treeView} form the {@link VisualPanel#currentPath}.
	 */
	private void refresh() {
		boolean success = rebuildTreeHierarchy(currentPath);
		txtPath.setText(success ? currentPath.toString() : "");
		txtStatus.setText((success ? 
							"built tree from path "
							: "failed to build tree from path "
							) + currentPath);
	}
}
