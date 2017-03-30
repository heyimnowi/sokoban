package utils;

import java.io.File;

import exceptions.NonExistingFileException;
import exceptions.StrategyNotFoundException;
import gps.SearchStrategy;

public class ArgsReader {
	
	public static String getFilePath(String nameFile) throws NonExistingFileException {
		String path = "res/boards/" + nameFile;
		File f = new File(path);
		if(f.exists() && !f.isDirectory()) { 
			return path;
		}
		throw new NonExistingFileException();
	}

	public static SearchStrategy getStrategy(String strategy) throws exceptions.StrategyNotFoundException {
		SearchStrategy strategyValue;
		try {
			strategyValue = SearchStrategy.valueOf(strategy.toUpperCase());
			return strategyValue;
		} catch (Exception e) {
			throw new StrategyNotFoundException();
		}
	}
}
