package pokemon_online.game.utils;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import pokemon_online.Configuration;
import pokemon_online.game.GameWorld.Cell;

public class GameUtilsTest {

    private static final int S = Configuration.CELL_SIZE_PXLS; // 32

    // -------------------------------------------------------------------------
    // getColumn(x)
    // -------------------------------------------------------------------------

    @Test
    public void getColumn_origin() {
        assertEquals(0, GameUtils.getColumn(0));
    }

    @Test
    public void getColumn_justBeforeCellBoundary() {
        assertEquals(0, GameUtils.getColumn(S - 1));
    }

    @Test
    public void getColumn_exactCellBoundary() {
        assertEquals(1, GameUtils.getColumn(S));
    }

    @Test
    public void getColumn_secondCell() {
        assertEquals(1, GameUtils.getColumn(S + 1));
    }

    @Test
    public void getColumn_largePosValue() {
        assertEquals(2, GameUtils.getColumn(2 * S));
    }

    @Test
    public void getColumn_negativeOnePixel() {
        assertEquals(-1, GameUtils.getColumn(-1));
    }

    @Test
    public void getColumn_exactNegativeCellBoundary() {
        // -32 is the start of cell -1
        assertEquals(-1, GameUtils.getColumn(-S));
    }

    @Test
    public void getColumn_justBeyondNegativeCellBoundary() {
        assertEquals(-2, GameUtils.getColumn(-S - 1));
    }

    // -------------------------------------------------------------------------
    // getRow(y) — same logic as getColumn
    // -------------------------------------------------------------------------

    @Test
    public void getRow_origin() {
        assertEquals(0, GameUtils.getRow(0));
    }

    @Test
    public void getRow_justBeforeCellBoundary() {
        assertEquals(0, GameUtils.getRow(S - 1));
    }

    @Test
    public void getRow_exactCellBoundary() {
        assertEquals(1, GameUtils.getRow(S));
    }

    @Test
    public void getRow_negativeOnePixel() {
        assertEquals(-1, GameUtils.getRow(-1));
    }

    @Test
    public void getRow_exactNegativeCellBoundary() {
        assertEquals(-1, GameUtils.getRow(-S));
    }

    @Test
    public void getRow_justBeyondNegativeCellBoundary() {
        assertEquals(-2, GameUtils.getRow(-S - 1));
    }

    // -------------------------------------------------------------------------
    // getX(column) / getY(row)
    // -------------------------------------------------------------------------

    @Test
    public void getX_zeroColumn() {
        assertEquals(0, GameUtils.getX(0));
    }

    @Test
    public void getX_positiveColumn() {
        assertEquals(S, GameUtils.getX(1));
        assertEquals(2 * S, GameUtils.getX(2));
    }

    @Test
    public void getX_negativeColumn() {
        assertEquals(-S, GameUtils.getX(-1));
        assertEquals(-2 * S, GameUtils.getX(-2));
    }

    @Test
    public void getY_zeroRow() {
        assertEquals(0, GameUtils.getY(0));
    }

    @Test
    public void getY_positiveRow() {
        assertEquals(S, GameUtils.getY(1));
        assertEquals(2 * S, GameUtils.getY(2));
    }

    @Test
    public void getY_negativeRow() {
        assertEquals(-S, GameUtils.getY(-1));
    }

    // -------------------------------------------------------------------------
    // Roundtrip: getColumn(getX(col)) == col  and  getRow(getY(row)) == row
    // -------------------------------------------------------------------------

    @Test
    public void roundtrip_column() {
        for (int col = -5; col <= 5; col++) {
            assertEquals("roundtrip failed for col=" + col,
                    col, GameUtils.getColumn(GameUtils.getX(col)));
        }
    }

    @Test
    public void roundtrip_row() {
        for (int row = -5; row <= 5; row++) {
            assertEquals("roundtrip failed for row=" + row,
                    row, GameUtils.getRow(GameUtils.getY(row)));
        }
    }

    // -------------------------------------------------------------------------
    // getCell(x, y)
    // -------------------------------------------------------------------------

    @Test
    public void getCell_origin() {
        Cell cell = GameUtils.getCell(0, 0);
        assertEquals(0, cell.getRow());
        assertEquals(0, cell.getColumn());
    }

    @Test
    public void getCell_positiveCoords() {
        Cell cell = GameUtils.getCell(S, 2 * S);
        assertEquals(2, cell.getRow());
        assertEquals(1, cell.getColumn());
    }

    @Test
    public void getCell_negativeCoords() {
        Cell cell = GameUtils.getCell(-S, -S);
        assertEquals(-1, cell.getRow());
        assertEquals(-1, cell.getColumn());
    }

    // -------------------------------------------------------------------------
    // radiant2degree(angRad)
    // -------------------------------------------------------------------------

    @Test
    public void radiant2degree_zero() {
        assertEquals(0.0, GameUtils.radiant2degree(0.0), 1e-9);
    }

    @Test
    public void radiant2degree_halfPi() {
        assertEquals(90.0, GameUtils.radiant2degree(Math.PI / 2), 1e-9);
    }

    @Test
    public void radiant2degree_pi() {
        assertEquals(180.0, GameUtils.radiant2degree(Math.PI), 1e-9);
    }

    @Test
    public void radiant2degree_minusPi() {
        // -PI and +PI both map to 180°
        assertEquals(180.0, GameUtils.radiant2degree(-Math.PI), 1e-9);
    }

    @Test
    public void radiant2degree_minusHalfPi() {
        assertEquals(270.0, GameUtils.radiant2degree(-Math.PI / 2), 1e-9);
    }
}
