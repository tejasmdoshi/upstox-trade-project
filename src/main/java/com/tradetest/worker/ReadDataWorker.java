package com.tradetest.worker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

import com.google.gson.Gson;
import com.tradetest.exception.WorkerException;
import com.tradetest.model.Trade;

/***
 * Reads the Trades data input (line by line from JSON), and sends the packet to
 * the FSM (Finite-State-Machine) thread.
 * 
 * @author tejas
 */
public class ReadDataWorker {

	private static Logger LOG = Logger.getLogger(ReadDataWorker.class.getName());

	private static Gson GSON = new Gson();

	public void work(String filePath, Predicate<Trade> predicate, Consumer<Trade> action) throws WorkerException {

		// Read data file line by line
		try (Stream<String> inputStream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {

			// Convert string stream to object stream
			Stream<Trade> trades = convert(inputStream);

			// Filter and perform action on individual trade
			perform(trades, predicate, action);

		} catch (IOException e) {

			LOG.log(Level.SEVERE, e.getMessage());
			throw new WorkerException(e);
		}

	}

	private Stream<Trade> convert(Stream<String> inputStream) {

		return inputStream.map((tradeLine) -> GSON.fromJson(tradeLine, Trade.class));
	}

	private void perform(Stream<Trade> stream, Predicate<Trade> predicate, Consumer<Trade> action) {

		stream.filter(predicate).forEach(action);
	}
}
