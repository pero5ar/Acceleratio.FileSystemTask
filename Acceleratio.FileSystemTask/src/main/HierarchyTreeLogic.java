package main;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

import models.PathItem;

/**
 * Static class containing the logic to build 
 * {@link VisualPanel}'s file hierarchy tree.
 * 
 * @author Petar Kovacevic
 * @version 1.0
 */
public class HierarchyTreeLogic {

	/**
	 * Generates the {@link JTree} representing the 
	 * file hierarchy tree of a selected file path.
	 * 
	 * @param path			a {@link Path} of a file to build the tree from
	 * @return				a {@link JTree} filled with nodes
	 * @throws IOException	if an I/O error occurs while walking file tree
	 */
	public static JTree buildFromPath(Path path) throws IOException {
		DefaultMutableTreeNode topNode = new DefaultMutableTreeNode(new PathItem(path.getRoot()));
		DefaultMutableTreeNode node = topNode;
		for (PathItem item : parentsList(path)) {
			DefaultMutableTreeNode newNode = new DefaultMutableTreeNode(item);
			node.add(newNode);
			node = newNode;
		}
		for (PathItem item : filesList(path)) {
			node.add(new DefaultMutableTreeNode(item));
		}
		return new JTree(topNode);
	}
	
	/**
	 * Get a list of parent directories of a file
	 * 
	 * @param path			a {@link Path} of the selected file
	 * @return				a {@link List} containing parents' {@link Path}s
	 */
	private static List<PathItem> parentsList(Path path) {
		List<PathItem> list = new ArrayList<>();
		Path newPath = path;
		while((newPath = newPath.getParent()) != null) {
			list.add(new PathItem(newPath));
		}
		Collections.reverse(list);
		return list;
	}
	
	/**
	 * Get a list of sibling files of a file
	 * 
	 * @param path			a {@link Path} of the selected file
	 * @return				a {@link List} containing siblings' {@link Path}s
	 * @throws IOException	if an I/O error occurs while walking file tree
	 */
	private static List<PathItem> filesList(Path path) throws IOException {
		List<PathItem> list = new ArrayList<>();
		FileVisitor<Path> visitor = new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				if (path.equals(file.getParent())) {
					list.add(new PathItem(file.toAbsolutePath()));
				}
				return FileVisitResult.CONTINUE;
			}
			@Override
		    public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				return FileVisitResult.CONTINUE;
			}
		};
		Files.walkFileTree(path, visitor);
		return list;
	}
}
