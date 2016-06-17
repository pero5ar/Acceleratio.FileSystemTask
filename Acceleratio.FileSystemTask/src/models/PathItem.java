package models;

import java.nio.file.Path;

/**
 * A model that represents a {@link Path} with
 * the String of {@link Path#getFileName()}
 * 
 * @author Petar Kovacevic
 * @version 1.0
 */
public class PathItem {

	/**
	 * a {@link Path} value
	 */
	private Path 	value;
	
	/**
	 * a String representing the {@link PathItem#value}'s file name
	 */
    private String	label;

    /**
     * Constructor. 
     * <p>
     * If the {@link PathItem#value} is <tt>null</tt>,
     * so is the {@link PathItem#label}.
     * 
     * @param value		a {@link Path} from which the object is built
     */
    public PathItem(Path value) {
        this.value = value;
        try {
        	label = value.getFileName().toString();
        } catch (NullPointerException e) {
        	label = null;
        }
    }

    /**
     * Get the {@link Path} value of an object.
     * 
     * @return		a {@link Path} value
     */
    public Path getValue() {
        return value;
    }

    /**
     * Get the String of {@link Path#getFileName()} value of an object.
     * 
     * @return		a String representing the {@link Path}'s file name
     */
    public String getLabel() {
        return label;
    }

    /**
     * {@inheritDoc}
     * @return		a String representing the {@link Path}'s file name
     */
    @Override
    public String toString() {
        return label;
    }
}
