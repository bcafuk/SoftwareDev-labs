package hr.fer.oprpp1.custom.scripting.nodes;

/**
 * A root node representing a document.
 *
 * @author Borna Cafuk
 */
public class DocumentNode extends Node {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < numberOfChildren(); i++)
            sb.append(getChild(i).toString());

        return sb.toString();
    }
}
