package de.cs.maze;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MazeCell {
  private String mazeGuid;
  private String note;
  private boolean atEnd;
  private boolean previouslyVisited;
  private RouteInfo north;
  private RouteInfo east;
  private RouteInfo south;
  private RouteInfo west;
  private int x;
  private int y;
}
