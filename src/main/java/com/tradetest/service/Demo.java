package com.tradetest.service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedTransferQueue;
import java.util.function.Consumer;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.tradetest.exception.WorkerException;
import com.tradetest.model.Trade;
import com.tradetest.util.ConsumerWrapper;
import com.tradetest.worker.ReadDataWorker;

public class Demo {

	private static String path = "/home/tejas/Downloads/trades-data/trades.json";

	public static void main(String[] args) throws IOException, WorkerException {

		new ReadDataWorker().work(path, (t) -> true, System.out::println);
	}

	private static Gson gson = new Gson();
	// .forEach((trade) -> this.cache((Trade) trade));

	private void readFile() throws IOException {
		List<Trade> trades = new ArrayList<>();
		FileInputStream inputStream = null;
		Scanner sc = null;
		try {
			inputStream = new FileInputStream(path);
			sc = new Scanner(inputStream, "UTF-8");
			while (sc.hasNextLine()) {
				String line = sc.nextLine();

				Trade trade = gson.fromJson(line, Trade.class);
				trades.add(trade);
				// System.out.println(line);
			}
			// note that Scanner suppresses exceptions
			if (sc.ioException() != null) {

				throw sc.ioException();
			}
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
			if (sc != null) {
				sc.close();
			}
		}
	}

	public List<Trade> convert(String tradeString) throws IOException {
		List<Trade> trades = new ArrayList<>();
		Trade trade = gson.fromJson(tradeString, Trade.class);
		trades.add(trade);
		return trades;
	}

	public List<Trade> readJsonStream(InputStream in) throws IOException {
		JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
		List<Trade> trades = new ArrayList<>();
		reader.beginArray();
		while (reader.hasNext()) {
			Trade trade = gson.fromJson(reader, Trade.class);
			trades.add(trade);
		}
		reader.endArray();
		reader.close();
		return trades;
	}

	private static ConcurrentMap<String, SortedSet<Trade>> tradeMap = new ConcurrentHashMap<>();

	private Optional<Collection<Trade>> fetch(String name) {
		// fetch(name).ifPresent(this::send);
		return Optional.of(tradeMap.get(name));
	}

	private Comparator<Trade> tradeComparator = (trade1, trade2) -> trade2.getTime().compareTo(trade1.getTime());

	private void cache(Trade trade) {

		tradeMap.putIfAbsent(trade.getName(), new TreeSet<Trade>(tradeComparator));

		tradeMap.get(trade.getName()).add(trade);

	}

	private LinkedTransferQueue<Trade> tradeQueue = null;

	Consumer<Trade> dataTransferConsumer = ConsumerWrapper.handlingConsumer(trade -> tradeQueue.transfer(trade),
			InterruptedException.class);
}
