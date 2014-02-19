package de.cs.maze.tarry;

import de.cs.maze.MazeCell;

public class TarryMaze {
  private TarryCellInfo[][] maze = new TarryCellInfo[50][50];

  public TarryCellInfo getCellInfo(MazeCell cell) {
    return maze[cell.getX()][cell.getY()];
  }

  public void visit(TarryCellInfo cellInfo) {
    maze[cellInfo.getCell().getX()][cellInfo.getCell().getY()] = cellInfo;
  }
}
