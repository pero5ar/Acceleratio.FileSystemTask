package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.TreeSet;

/**
 * A text file permanent storage
 * model implementation of {@link IDataAccessObject}.
 * 
 * @author Petar Kovacevic
 * @version 1.0
 */
public class TextFileData implements IDataAccessObject {

	private File 				dataFile;
	private Collection<Path>	favorites;
	private Path 				lastFavorite;
	
	/**
	 * Constructor.
	 * 
	 * @param dataFile			the {@link Path} of the storage file
	 * @throws IOException		I/O error while reading the file
	 */
	public TextFileData(Path dataFile) throws IOException {
		this.dataFile = dataFile.toFile();
		favorites = new TreeSet<Path>();
		if (!readFile()) {
			throw new IOException();
		}
	}
	
	private boolean readFile() {
		try (BufferedReader reader = new BufferedReader(new FileReader(dataFile))) {
			String line;
		    while ((line = reader.readLine()) != null) {
		    	favorites.add(lastFavorite = Paths.get(line.trim()));
		    }
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	private boolean overwriteFile() {
		try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile))) {
			for (Path file : favorites) {
				writer.println(file.toString());
			}
		} catch (IOException e) {
			return false;
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	public Iterable<Path> getFavorites() {
		return favorites;
	}

	/**
	 * {@inheritDoc}
	 */
	public Path getLastFavorite() {
		return lastFavorite;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean addNewFavorite(Path file) {
		if (favorites.add(file)) {
			lastFavorite = file;
			return true;
		}
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isFavorite(Path file) {
		return favorites.contains(file);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean deleteFavorite(Path file) {
		return favorites.remove(file);
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean saveToStorage() {
		return overwriteFile();
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean clearStorage() {
		favorites.clear();
		lastFavorite = null;
		return overwriteFile();
	}

}
