package data;

import java.nio.file.Path;

/**
 * Defines a permanent storage model
 * for the paths of the files the user
 * has added to his/her favorites. 
 * 
 * @author Petar Kovacevic
 * @version 1.0
 */
public interface IDataAccessObject {
	
	/**
	 * Get all favorite {@link Path}s
	 * 
	 * @return		an {@link Iterable} of favorite {@link Path}s
	 */
	public Iterable<Path> getFavorites(); 
	
	/**
	 * Get last added favorite {@link Path}
	 * 
	 * @return		a {@link Path} representing the last added favorite
	 */
	public Path getLastFavorite();
	
	/**
	 * Add a file path to favorites.
	 * 
	 * @param file	a {@link Path} to add
	 * @return		<tt>true</tt> if data is successfully saved
	 */
	public boolean addNewFavorite(Path file);
	
	/**
	 * Check if the file path is already in favorites.
	 * 
	 * @param file	a {@link Path} to check
	 * @return		<tt>true</tt> if data is already saved
	 */
	public boolean isFavorite(Path file);
	
	/**
	 * Delete a file path from favorites.
	 * 
	 * @param file	a {@link Path} to delete
	 * @return		<tt>true</tt> if data is successfully deleted
	 */
	public boolean deleteFavorite(Path file);
	
	/**
	 * Save current data to the permanent storage;
	 * 
	 * @return		<tt>true</tt> if all data is successfully saved
	 */
	public boolean saveToStorage();
	
	/**
	 * Delete everything from the permanent storage.
	 * 
	 * @return		<tt>true</tt> if all data is successfully deleted
	 */
	public boolean clearStorage();

}
