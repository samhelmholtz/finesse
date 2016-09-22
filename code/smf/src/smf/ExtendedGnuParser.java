/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package smf;

import java.util.ListIterator;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.ParseException;

/**
 *
 * @author sam
 */
public class ExtendedGnuParser extends GnuParser {

    private boolean ignoreUnrecognizedOption;

    public ExtendedGnuParser(final boolean ignoreUnrecognizedOption) {
        this.ignoreUnrecognizedOption = ignoreUnrecognizedOption;
    }

    @Override
    protected void processOption(final String arg, final ListIterator iter) throws     ParseException {
        boolean hasOption = getOptions().hasOption(arg);

        if (hasOption || !ignoreUnrecognizedOption) {
            super.processOption(arg, iter);
        }
    }

}
