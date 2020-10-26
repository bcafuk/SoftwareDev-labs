package hr.fer.oprpp1.custom.scripting.nodes;

import hr.fer.oprpp1.custom.scripting.elems.Element;
import hr.fer.oprpp1.custom.scripting.elems.ElementVariable;

import java.util.Objects;

/**
 * A node representing a {@code for}-loop.
 */
public class ForLoopNode extends Node {
    /**
     * The variable over which to iterate.
     */
    private ElementVariable variable;
    /**
     * The expression with which to initialize the variable before iterating.
     */
    private Element startExpression;
    /**
     * Iteration stops once the variable becomes equal to this expression.
     */
    private Element endExpession;
    /**
     * An expression by which to increment the variable in every iteration of the loop. May be {@code null}.
     */
    private Element step;

    /**
     * Creates a new for loop node
     *
     * @param variable        the variable to iterate over
     * @param startExpression the expression for initializing the variable
     * @param endExpession    the expression with which to compare the variable
     * @param step            the expression to be added to the variable every iteration
     * @throws NullPointerException if {@code variable} is {@code null}
     * @throws NullPointerException if {@code startExpression} is {@code null}
     * @throws NullPointerException if {@code endExpession} is {@code null}
     */
    public ForLoopNode(ElementVariable variable, Element startExpression, Element endExpession, Element step) {
        Objects.requireNonNull(variable, "The variable must not be null.");
        Objects.requireNonNull(startExpression, "The start expression must not be null.");
        Objects.requireNonNull(endExpession, "The end expression must not be null.");

        this.variable = variable;
        this.startExpression = startExpression;
        this.endExpession = endExpession;
        this.step = step;
    }

    /**
     * Gets the variable element.
     *
     * @return the variable
     */
    public ElementVariable getVariable() {
        return variable;
    }

    /**
     * Gets the start expression element.
     *
     * @return the start expression
     */
    public Element getStartExpression() {
        return startExpression;
    }

    /**
     * Gets the end expression element.
     *
     * @return the end expression
     */
    public Element getEndExpession() {
        return endExpession;
    }

    /**
     * Gets the step expression element.
     *
     * @return the step expression, or {@code null} if it was omitted
     */
    public Element getStep() {
        return step;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("{$ FOR ");

        sb.append(variable).append(' ').append(startExpression).append(' ').append(endExpession).append(' ');

        if (step != null)
            sb.append(step).append(' ');

        sb.append("$}");

        for (int i = 0; i < numberOfChildren(); i++)
            sb.append(getChild(i).toString());

        sb.append("{$ END $}");

        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ForLoopNode))
            return false;
        if (!super.equals(o))
            return false;

        ForLoopNode loopNode = (ForLoopNode) o;
        return variable.equals(loopNode.variable) &&
                startExpression.equals(loopNode.startExpression) &&
                endExpession.equals(loopNode.endExpession) &&
                Objects.equals(step, loopNode.step);
    }
}
