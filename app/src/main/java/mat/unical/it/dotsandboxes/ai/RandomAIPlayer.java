/*
 * Dots and Boxes
 * Copyright (C) 2016  zDuo (Adithya J, Vazbloke)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package mat.unical.it.dotsandboxes.ai;


import java.util.ArrayList;
import java.util.List;

import mat.unical.it.dotsandboxes.model.Edge;
import mat.unical.it.dotsandboxes.model.Player;

public class RandomAIPlayer extends Player {

    protected final ArrayList<Edge> safeLines;
    protected final ArrayList<Edge> goodLines;
    protected final ArrayList<Edge> badLines;

    public RandomAIPlayer(String name) {
        super(name);

        safeLines = new ArrayList<>();
        goodLines = new ArrayList<>();
        badLines = new ArrayList<>();
    }

    protected Edge nextMove() {
        if (goodLines.size() != 0) return getBestGoodLine();
        if (safeLines.size() != 0) return getRandomSafeLine();

        return getRandomBadLine();
    }

    public Edge move() {
        initialiseLines();
        return nextMove();
    }

    @Override
    public void moveDLV() {

    }

    private void initialiseLines() {
        goodLines.clear();
        badLines.clear();
        safeLines.clear();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                if (!isHorizontalLineOccupied(i, j)) {
                    if (i == 0) {
                        switch (getBox(i, j).occupiedLineCount()) {
                            case 3:
                                goodLines.add(new Edge(i, j,0));
                                break;
                            case 2:
                                badLines.add(new Edge(i, j,0));
                                break;
                            case 1:
                            case 0:
                                safeLines.add(new Edge(i, j,0));
                        }
                    } else if (i == 5) {
                        switch (getBox(i - 1, j).occupiedLineCount()) {
                            case 3:
                                goodLines.add(new Edge(i, j,0));
                                break;
                            case 2:
                                badLines.add(new Edge(i, j,0));
                                break;
                            case 1:
                            case 0:
                                safeLines.add(new Edge(i, j,0));
                        }
                    } else {
                        if (getBox(i, j).occupiedLineCount() == 3
                                || getBox(i - 1, j).occupiedLineCount() == 3)
                            goodLines.add(new Edge(i, j,0));

                        if (getBox(i, j).occupiedLineCount() == 2
                                || getBox(i - 1, j).occupiedLineCount() == 2)
                            badLines.add(new Edge(i, j,0));

                        if (getBox(i, j).occupiedLineCount() < 2
                                && getBox(i - 1, j).occupiedLineCount() < 2)
                            safeLines.add(new Edge(i, j,0));
                    }
                }

                if (!isVerticalLineOccupied(j, i)) {
                    if (i == 0) {
                        if (getBox(j, i).occupiedLineCount() == 3)
                            goodLines.add(new Edge(j, i,1));
                    } else if (i == 5) {
                        if (getBox(j, i - 1).occupiedLineCount() == 3)
                            goodLines.add(new Edge(j, i,1));
                    } else {
                        if (getBox(j, i).occupiedLineCount() == 3
                                || getBox(j, i - 1).occupiedLineCount() == 3)
                            goodLines.add(new Edge(j, i,1));

                        if (getBox(j, i).occupiedLineCount() == 2
                                || getBox(j, i - 1).occupiedLineCount() == 2)
                            badLines.add(new Edge(j, i,1));

                        if (getBox(j, i).occupiedLineCount() < 2
                                && getBox(j, i - 1).occupiedLineCount() < 2)
                            safeLines.add(new Edge(j, i,1));
                    }
                }
            }
        }
    }

    protected Box getBox(int row, int column) {
        return new Box(isVerticalLineOccupied(row, column), isHorizontalLineOccupied(row, column),
                isVerticalLineOccupied(row, column + 1), isHorizontalLineOccupied(row + 1, column));
    }

    protected boolean isHorizontalLineOccupied(int row, int column) {
        return getGame().isLineOccupied(new Edge(row, column,0));
    }

    protected boolean isVerticalLineOccupied(int row, int column) {
        return getGame().isLineOccupied(new Edge(row, column,0));
    }

    protected Edge getBestGoodLine() {
        return goodLines.get(0);
    }

    protected Edge getRandomSafeLine() {
        return getRandomLine(safeLines);
    }

    protected Edge getRandomBadLine() {
        return getRandomLine(badLines);
    }

    private Edge getRandomLine(List<Edge> list) {
        return list.get((int) (list.size() * Math.random()));
    }
}
