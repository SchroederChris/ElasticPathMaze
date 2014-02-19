package de.cs.maze;

import org.junit.Test;

import de.cs.maze.tarry.TarrySolver;

public class MazeTest {

  @Test
  public void tarry() {
    TarrySolver solver = new TarrySolver();
    MazeCell exitCell = solver.findExit();
    System.out.println(exitCell.getNote());
  }

}
