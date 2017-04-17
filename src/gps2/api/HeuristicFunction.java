package gps2.api;

public interface HeuristicFunction {
    /**
     * Computes the value of the Heuristic for the given state.
     *
     * @param state
     *            The state where the Heuristic should be computed.
     * @return The value of the Heuristic.
     */
    Integer getHValue(GPSState state);
}