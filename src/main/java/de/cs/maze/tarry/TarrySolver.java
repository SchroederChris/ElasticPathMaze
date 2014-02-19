package de.cs.maze.tarry;

import java.util.Map.Entry;

import de.cs.maze.Direction;
import de.cs.maze.MazeCell;
import de.cs.maze.MazeClient;
import de.cs.maze.RouteInfo;

/**
 * http://de.wikipedia.org/wiki/L%C3%B6sungsalgorithmen_f%C3%BCr_Irrg%C3%A4rten
 */
public class TarrySolver {
  private TarryMaze maze = new TarryMaze();
  private MazeClient client = new MazeClient();
  private TarryCellInfo lastCrossroads;

  public MazeCell findExit() {
    MazeCell firstCell = client.init();
    TarryCellInfo firstCellInfo = new TarryCellInfo(firstCell);
    maze.visit(firstCellInfo);

    Direction direction = getNextDirection(firstCellInfo, null);
    TarryCellInfo currentCellInfo = move(firstCell, direction);

    while (!currentCellInfo.getCell().isAtEnd()) {
      System.out.println("(" + currentCellInfo.getCell().getX() + "|"
          + currentCellInfo.getCell().getY() + ")");

      if (currentCellInfo.isDeadend()) {
        currentCellInfo = jumpToLastCrossroads(currentCellInfo.getCell()
            .getMazeGuid());
      } else if (currentCellInfo.isCrossroads()) {
        lastCrossroads = currentCellInfo;
        if (!currentCellInfo.getCell().isPreviouslyVisited()) {
          currentCellInfo.last(Direction.reverse(direction));
        }
        direction = getNextDirection(currentCellInfo, direction);
        currentCellInfo.stop(direction);
        currentCellInfo = move(currentCellInfo.getCell(), direction);
      } else {
        direction = getNextDirection(currentCellInfo, direction);
        currentCellInfo = move(currentCellInfo.getCell(), direction);
      }
    }

    return currentCellInfo.getCell();
  }

  private Direction getNextDirection(TarryCellInfo cellInfo, Direction direction) {
    Direction nextDirection = null;

    if (cellInfo.isCrossroads()) {
      boolean directionSelected = false;

      // try to find an unmarked exit
      for (Entry<Direction, TarryCellMarker> directionMarker : cellInfo
          .getMarkers().entrySet()) {
        if (!directionMarker.getValue().isStop()
            && !directionMarker.getValue().isLast()) {
          Direction directionCandidate = directionMarker.getKey();
          if (!cellInfo.getRouteInfo(directionCandidate).equals(
              RouteInfo.BLOCKED)) {
            nextDirection = directionCandidate;
            directionSelected = true;
            break;
          }
        }
      }

      // no unmarked exit
      if (!directionSelected) {
        // => take the one marked 'last'
        for (Entry<Direction, TarryCellMarker> directionMarker : cellInfo
            .getMarkers().entrySet()) {
          if (directionMarker.getValue().isLast()) {
            Direction directionCandidate = directionMarker.getKey();
            if (!cellInfo.getRouteInfo(directionCandidate).equals(
                RouteInfo.BLOCKED)) {
              nextDirection = directionCandidate;
              directionSelected = true;
              break;
            }
          }
        }
      }

      if (!directionSelected) {
        throw new IllegalStateException("all directions marked with STOP");
      }
    } else if (cellInfo.isDeadend() && direction != null) {
      nextDirection = Direction.reverse(direction);
    } else {
      nextDirection = cellInfo.goAhead(direction);
    }

    return nextDirection;
  }

  private TarryCellInfo jumpToLastCrossroads(String mazeGuid) {
    if (lastCrossroads == null) {
      throw new IllegalStateException(
          "entered deadend without prior crossroads");
    }

    MazeCell jumpedCrossroadsCell = client.jump(mazeGuid, lastCrossroads
        .getCell().getX(), lastCrossroads.getCell().getY());

    TarryCellInfo cellInfo = maze.getCellInfo(jumpedCrossroadsCell);
    cellInfo.setCell(jumpedCrossroadsCell);
    return cellInfo;
  }

  private TarryCellInfo move(MazeCell cell, Direction direction) {
    MazeCell currentCell = client.move(cell.getMazeGuid(), direction);
    TarryCellInfo currentCellInfo = maze.getCellInfo(currentCell);
    if (currentCellInfo == null) {
      currentCellInfo = new TarryCellInfo(currentCell);
      maze.visit(currentCellInfo);
    } else {
      currentCellInfo.setCell(currentCell);
    }
    return currentCellInfo;
  }
}
