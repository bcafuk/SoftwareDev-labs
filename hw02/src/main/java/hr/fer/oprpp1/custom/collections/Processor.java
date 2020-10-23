package hr.fer.oprpp1.custom.collections;

/**
 * A generic class to be extended and used in the strategy pattern.
 *
 * @author Borna Cafuk
 */
public interface Processor {
    /**
     * Processes an object.
     *
     * @param value an object to process
     */
    void process(Object value);
}
