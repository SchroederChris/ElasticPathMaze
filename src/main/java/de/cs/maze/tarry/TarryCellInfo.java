package de.cs.maze.tarry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import lombok.Getter;
import de.cs.maze.Direction;
import de.cs.maze.MazeCell;
import de.cs.maze.RouteInfo;

public class TarryCellInfo {
  @Getter
  private MazeCell cell;

  @Getter
  private boolean crossroads = false;

  @Getter
  private boolean deadend = false;

  private Map<Direction, RouteInfo> adjacentRouteInfos = new HashMap<>();
  private Map<Direction, TarryCellMarker> adjacentMarkers = new HashMap<>();

  public TarryCellInfo(MazeCell cell) {
    setCell(cell);

    adjacentMarkers.put(Direction.NORTH, new TarryCellMarker());
    adjacentMarkers.put(Direction.EAST, new TarryCellMarker());
    adjacentMarkers.put(Direction.SOUTH, new TarryCellMarker());
    adjacentMarkers.put(Direction.WEST, new TarryCellMarker());

    determineCrossroadsOrDeadend();
  }

  public void setCell(MazeCell cell) {
    this.cell = cell;

    adjacentRouteInfos.put(Direction.NORTH, cell.getNorth());
    adjacentRouteInfos.put(Direction.EAST, cell.getEast());
    adjacentRouteInfos.put(Direction.SOUTH, cell.getSouth());
    adjacentRouteInfos.put(Direction.WEST, cell.getWest());
  }

  public RouteInfo getRouteInfo(Direction direction) {
    return adjacentRouteInfos.get(direction);
  }

  public TarryCellMarker getMarker(Direction direction) {
    return adjacentMarkers.get(direction);
  }

  public void stop(Direction direction) {
    adjacentMarkers.get(direction).setStop(true);
  }

  public void last(Direction direction) {
    adjacentMarkers.get(direction).setLast(true);
  }

  public Direction goAhead(Direction direction) {
    Direction nextDirection = direction;

    for (Entry<Direction, RouteInfo> entry : adjacentRouteInfos.entrySet()) {
      if (entry.getValue().equals(RouteInfo.UNEXPLORED)) {
        nextDirection = entry.getKey();
        break;
      }
      if (entry.getValue().equals(RouteInfo.VISITED)) {
        Direction directionCandidate = entry.getKey();
        if (!directionCandidate.equals(Direction.reverse(direction))) {
          nextDirection = directionCandidate;
        }
      }
    }

    return nextDirection;
  }

  public Map<Direction, TarryCellMarker> getMarkers() {
    return Collections.unmodifiableMap(adjacentMarkers);
  }

  private void determineCrossroadsOrDeadend() {
    int unblockedDirections = 0;

    for (RouteInfo routeInfo : adjacentRouteInfos.values()) {
      if (!routeInfo.equals(RouteInfo.BLOCKED)) {
        unblockedDirections += 1;
      }
    }

    if (unblockedDirections == 1) {
      deadend = true;
    } else if (unblockedDirections > 2) {
      crossroads = true;
    }
  }

}
