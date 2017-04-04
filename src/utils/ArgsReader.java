package utils;

import java.io.File;

import exceptions.HeuristicNotFoundException;
import exceptions.NonExistingFileException;
import exceptions.StrategyNotFoundException;
import gps.SearchStrategy;
import model.heuristics.Heuristic;
import model.heuristics.PBBGHeuristic;
import model.heuristics.PBNearBGHeuristic;
import model.heuristics.SimpleHeuristic;

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
	
	public static Heuristic getHeuristic(String heuristic) throws HeuristicNotFoundException {
		switch (heuristic.toUpperCase()) {
		case "PBBG":
			return new PBBGHeuristic();
		case "PBNEARBG":
			return new PBNearBGHeuristic();
		case "SIMPLE":
			return new SimpleHeuristic();
		default:
			throw new HeuristicNotFoundException();
		}
	}
}
