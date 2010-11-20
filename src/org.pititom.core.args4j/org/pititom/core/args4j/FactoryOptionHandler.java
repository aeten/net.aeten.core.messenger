package org.pititom.core.args4j;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.OptionDef;
import org.kohsuke.args4j.spi.OptionHandler;
import org.kohsuke.args4j.spi.Parameters;
import org.kohsuke.args4j.spi.Setter;
import org.pititom.core.Configurable;
import org.pititom.core.Singleton;

/**
 * {@link Class} {@link OptionHandler}.
 * 
 * @author Thomas Pérennou
 */
public class FactoryOptionHandler extends OptionHandler<Singleton<Configurable<String>>> {

	public static final String CONFIGURATION_OPTION_NAME = "-c";
	public static final String[] CONFIGURATION_OPTION_ALIASES = {"--configuration"};

	public FactoryOptionHandler(CmdLineParser parser, OptionDef option,
			Setter<Singleton<Configurable<String>>> setter) {
		super(parser, option, setter);
	}

	@Override
	public int parseArguments(Parameters params) throws CmdLineException {
		String className = params.getParameter(0);
		String configuration;
		try {
			if (CONFIGURATION_OPTION_NAME.equals(params.getParameter(1)) || contains(params.getParameter(1), CONFIGURATION_OPTION_ALIASES)) {
				configuration = params.getParameter(2);
				configuration = configuration.replaceAll("\\\\\"", "\"");
			} else {
				configuration = null;
			}
		} catch (CmdLineException exception) {
			configuration = null;
		}

		try {
			@SuppressWarnings("unchecked")
			Singleton<Configurable<String>> factory = new Singleton<Configurable<String>>((Class<Configurable<String>>)Thread.currentThread().getContextClassLoader().loadClass(className), configuration);
			setter.addValue(factory);
		} catch (ClassNotFoundException exception) {
			throw new CmdLineException(this.owner, exception);
		}
		return (configuration == null) ? 1 : 3;
	}

	@Override
	public String getDefaultMetaVariable() {
		return "FACTORY";
	}

	private static boolean contains(String element, String[] list) {
		for (String item : list) {
			if (element.equals(item)) {
				return true;
			}
		}
		return false;

	}
}
