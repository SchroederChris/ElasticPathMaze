package de.cs.maze;

public enum Direction {
  NORTH, WEST, EAST, SOUTH;

  public static Direction reverse(Direction direction) {
    Direction reverse = null;

    switch (direction) {
    case NORTH:
      reverse = Direction.SOUTH;
      break;
    case SOUTH:
      reverse = Direction.NORTH;
      break;
    case EAST:
      reverse = Direction.WEST;
      break;
    case WEST:
      reverse = Direction.EAST;
      break;
    }

    return reverse;
  }
}
