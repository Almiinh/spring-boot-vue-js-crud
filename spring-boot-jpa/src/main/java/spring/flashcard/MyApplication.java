package spring.flashcard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.*;

/**
 * The {@code MyApplication} class is the entry point for the {@link @SpringBootApplication}.
 * It configures a custom logger and starts the application.
 */
@SpringBootApplication
public class MyApplication {

	/**
	 * Get a custom logger for the specified class.
	 *
	 * @param clazz The class for which the logger is created.
	 * @param <T>   The type of the class.
	 * @return A customized logger.
	 */
	public static <T> Logger getLogger(Class<T> clazz) {

		// We create a Logger using java.util.logging, with the name "MyApplication"
		Logger logger = Logger.getLogger(clazz.getSimpleName());

		LogManager.getLogManager().reset();

		// We create a custom formatter and customize the output format for the logger
		Formatter customFormatter = new Formatter() {
			@Override
			public String format(LogRecord record) {
				return switch (record.getLevel().getName()) {
					case "SEVERE" ->
							"\u001B[41m SEVERE \u001B[0m \u001B[31m" + record.getLoggerName() + ": " + record.getMessage() + "\u001B[0m\n";
					case "INFO" ->
							"\u001B[42m \u001B[30mINFO \u001B[0m \u001B[32m" + record.getMessage() + "\u001B[0m\n";
					case "WARNING" ->
							"\u001B[43m \u001B[30mWARNING \u001B[0m \u001B[33m" + record.getMessage() + "\u001B[0m\n";
					case "CONFIG" ->
							"\u001B[44m \u001B[30mCONFIG \u001B[0m \u001B[34m" + record.getMessage() + "\u001B[0m\n";
					case "FINE" ->
							"\u001B[45m FINE \u001B[0m \u001B[35m" + record.getMessage() + "\u001B[0m\n";
					case "FINER" ->
							"\u001B[46m \u001B[30mFINER \u001B[0m \u001B[36m" + record.getMessage() + "\u001B[0m\n";
					case "FINEST" ->
							"\u001B[47m \u001B[30mFINEST \u001B[0m \u001B[37m" + record.getMessage() + "\u001B[0m\n";
					default ->
							throw new IllegalStateException("Unexpected value: " + record.getLevel().getName());
				};
			}
		};

		// Set Handler
		ConsoleHandler consoleHandler = new ConsoleHandler();
		consoleHandler.setLevel(Level.ALL);
		consoleHandler.setFormatter(customFormatter);
		logger.setLevel(Level.ALL);
		logger.addHandler(consoleHandler);
		return logger;
	}

	/**
	 * The main method of the application.
	 *
	 * @param args Command-line arguments.
	 * @throws URISyntaxException If there is an issue with URI syntax.
	 */
	public static void main(String[] args) throws URISyntaxException {
		Logger logger = getLogger(MyApplication.class);

		// We test some logging messages for every level
		logger.severe("This is a severe message");
		logger.info("This is an info message");
		logger.warning("This is a warning message");
		logger.config("This is a config message");
		logger.fine("This is a fine level message");
		logger.finer("This is a finer level message");
		logger.finest("This is the finest level message");

		// We start the app
		SpringApplication.run(MyApplication.class);

		logger.info("MyApplication successfully started at port 8080!");
		logger.info("Consult H2 Database here " + new URI("http://localhost:8080/h2-ui/"));
	}

	public static ConfigurableApplicationContext context;

	public static void connect() {
		if (context != null)
			return;
		// We start the app
		context = SpringApplication.run(MyApplication.class);
	}
}
