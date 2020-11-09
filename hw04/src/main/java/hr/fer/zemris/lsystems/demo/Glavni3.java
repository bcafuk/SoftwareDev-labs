package hr.fer.zemris.lsystems.demo;

import hr.fer.zemris.lsystems.gui.LSystemViewer;
import hr.fer.zemris.lsystems.impl.LSystemBuilderImpl;

/**
 * Opens a new window which allows an {@link LSystemBuilderImpl} to be configured by reading directives from a file.
 * <p>
 * The contents of this method have been copied verbatim from
 * the assignment document written by doc. dr. sc. Marko Čupić.
 */
public class Glavni3 {
    public static void main(String[] args) {
        LSystemViewer.showLSystem(LSystemBuilderImpl::new);
    }
}
