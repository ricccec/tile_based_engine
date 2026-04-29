package pokemon_online.land;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.Before;
import org.junit.Test;

public class LandBuilderTest {

    // -------------------------------------------------------------------------
    // Minimal JSON fixtures
    // -------------------------------------------------------------------------

    /** 2×2 map: two tiles, one walkable cell, one non-walkable, one door, one text. */
    private static final String SIMPLE_JSON =
        "{"
        + "\"name\":\"TestMap\","
        + "\"rowsCount\":2,"
        + "\"colsCount\":2,"
        + "\"tiles\":["
        + "  {\"id\":0,\"name\":\"Grass\",\"img\":\"Erba.gif\"},"
        + "  {\"id\":1,\"name\":\"Wall\",\"img\":\"Mattoni.gif\"}"
        + "],"
        + "\"texts\":[],"
        + "\"rows\":["
        + "  {\"row\":0,\"columns\":["
        + "    {\"row\":0,\"col\":0,\"tile\":0,\"walkable\":true},"
        + "    {\"row\":0,\"col\":1,\"tile\":1,\"walkable\":false}"
        + "  ]},"
        + "  {\"row\":1,\"columns\":["
        + "    {\"row\":1,\"col\":0,\"door\":{\"targetLand\":\"OtherMap\",\"targetRow\":5,\"targetCol\":3}},"
        + "    {\"row\":1,\"col\":1}"
        + "  ]}"
        + "]"
        + "}";

    /** Map with a door whose targetLand is null — should be silently ignored. */
    private static final String NULL_DOOR_JSON =
        "{"
        + "\"name\":\"NullDoorMap\","
        + "\"rowsCount\":1,"
        + "\"colsCount\":1,"
        + "\"tiles\":["
        + "  {\"id\":0,\"name\":\"Floor\",\"img\":\"Erba.gif\"}"
        + "],"
        + "\"texts\":[],"
        + "\"rows\":["
        + "  {\"row\":0,\"columns\":["
        + "    {\"row\":0,\"col\":0,"
        + "     \"door\":{\"targetLand\":null,\"targetRow\":0,\"targetCol\":0}}"
        + "  ]}"
        + "]"
        + "}";

    /** Map with an animated tile (img is a JSON array of frame objects). */
    private static final String ANIMATED_TILE_JSON =
        "{"
        + "\"name\":\"AnimMap\","
        + "\"rowsCount\":1,"
        + "\"colsCount\":1,"
        + "\"tiles\":["
        + "  {\"id\":0,\"name\":\"Water\",\"img\":["
        + "    {\"frame\":0,\"img\":\"Water1.gif\"},"
        + "    {\"frame\":1,\"img\":\"Water2.gif\"}"
        + "  ]}"
        + "],"
        + "\"texts\":[],"
        + "\"rows\":["
        + "  {\"row\":0,\"columns\":["
        + "    {\"row\":0,\"col\":0,\"tile\":0}"
        + "  ]}"
        + "]"
        + "}";

    /** Map with a tileset-based tile (img is a JSON object). */
    private static final String TILESET_TILE_JSON =
        "{"
        + "\"name\":\"TilesetMap\","
        + "\"rowsCount\":1,"
        + "\"colsCount\":1,"
        + "\"tiles\":["
        + "  {\"id\":0,\"name\":\"Road\",\"img\":{"
        + "    \"tilesheet\":\"kanto.png\","
        + "    \"x\":0,\"y\":0,\"width\":32,\"height\":32,\"scale\":1.0"
        + "  }}"
        + "],"
        + "\"texts\":[],"
        + "\"rows\":["
        + "  {\"row\":0,\"columns\":["
        + "    {\"row\":0,\"col\":0,\"tile\":0}"
        + "  ]}"
        + "]"
        + "}";

    /** Map with a text game-object. */
    private static final String TEXT_OBJECT_JSON =
        "{"
        + "\"name\":\"TextMap\","
        + "\"rowsCount\":2,"
        + "\"colsCount\":2,"
        + "\"tiles\":["
        + "  {\"id\":0,\"name\":\"Floor\",\"img\":\"Erba.gif\"}"
        + "],"
        + "\"texts\":["
        + "  {\"text\":\"Hello world\",\"row\":0,\"col\":1}"
        + "],"
        + "\"rows\":["
        + "  {\"row\":0,\"columns\":["
        + "    {\"row\":0,\"col\":0},"
        + "    {\"row\":0,\"col\":1}"
        + "  ]},"
        + "  {\"row\":1,\"columns\":["
        + "    {\"row\":1,\"col\":0},"
        + "    {\"row\":1,\"col\":1}"
        + "  ]}"
        + "]"
        + "}";

    // -------------------------------------------------------------------------

    private LandBuilder builder;
    private JSONParser parser;

    @Before
    public void setUp() {
        builder = new LandBuilder();
        parser  = new JSONParser();
    }

    private JSONObject parse(String json) {
        try {
            return (JSONObject) parser.parse(json);
        } catch (ParseException e) {
            throw new RuntimeException("Test JSON is invalid: " + e.getMessage(), e);
        }
    }

    // -------------------------------------------------------------------------
    // Metadata
    // -------------------------------------------------------------------------

    @Test
    public void buildLand_parsesName() {
        Land land = builder.buildLand(parse(SIMPLE_JSON));
        assertEquals("TestMap", land.getName());
    }

    @Test
    public void buildLand_parsesDimensions() {
        Land land = builder.buildLand(parse(SIMPLE_JSON));
        assertEquals(2, land.getRowsCount());
        assertEquals(2, land.getColsCount());
    }

    // -------------------------------------------------------------------------
    // Tile parsing
    // -------------------------------------------------------------------------

    @Test
    public void buildLand_parsesTileNames() {
        Land land = builder.buildLand(parse(SIMPLE_JSON));

        // Cell (0,0) uses tile id=0 → "Grass"
        assertNotNull(land.getCellTile(0, 0));
        assertEquals("Grass", land.getCellTile(0, 0).getName());

        // Cell (0,1) uses tile id=1 → "Wall"
        assertNotNull(land.getCellTile(0, 1));
        assertEquals("Wall", land.getCellTile(0, 1).getName());
    }

    @Test
    public void buildLand_cellWithoutTile_returnsNullTile() {
        Land land = builder.buildLand(parse(SIMPLE_JSON));
        // Row 1 cells do not specify a "tile" key
        assertNull(land.getCellTile(1, 0));
        assertNull(land.getCellTile(1, 1));
    }

    @Test
    public void buildLand_parsesAnimatedTile_multipleFrames() {
        // Should not throw; the tile must be usable at frame 0 and frame 1
        Land land = builder.buildLand(parse(ANIMATED_TILE_JSON));
        Tile waterTile = land.getCellTile(0, 0);
        assertNotNull(waterTile);
        assertNotNull(waterTile.getImage(0));
        assertNotNull(waterTile.getImage(1));
    }

    @Test
    public void buildLand_parsesTilesetTile_doesNotThrow() {
        // Tile image as a tileset JSON object should be parsed without error
        Land land = builder.buildLand(parse(TILESET_TILE_JSON));
        assertNotNull(land.getCellTile(0, 0));
        assertEquals("Road", land.getCellTile(0, 0).getName());
    }

    // -------------------------------------------------------------------------
    // Walkability
    // -------------------------------------------------------------------------

    @Test
    public void buildLand_parsesWalkableTrue() {
        Land land = builder.buildLand(parse(SIMPLE_JSON));
        assertTrue(land.isWalkable(0, 0));
    }

    @Test
    public void buildLand_parsesWalkableFalse() {
        Land land = builder.buildLand(parse(SIMPLE_JSON));
        assertFalse(land.isWalkable(0, 1));
    }

    // -------------------------------------------------------------------------
    // Door parsing
    // -------------------------------------------------------------------------

    @Test
    public void buildLand_doorWithValidTarget_doesNotThrow() {
        // Building a map that contains a door must not throw; coverage of the
        // happy path inside buildDoor / setCellDoor
        Land land = builder.buildLand(parse(SIMPLE_JSON));
        assertNotNull(land);
    }

    @Test
    public void buildLand_doorWithNullTarget_isIgnored() {
        // A door whose "targetLand" is null must be silently skipped.
        // There is no public getDoor API on Land, but building must succeed.
        Land land = builder.buildLand(parse(NULL_DOOR_JSON));
        assertNotNull(land);
        assertEquals("NullDoorMap", land.getName());
    }

    // -------------------------------------------------------------------------
    // Text (interactive) objects
    // -------------------------------------------------------------------------

    @Test
    public void buildLand_parsesTextObject_addsToLand() {
        Land land = builder.buildLand(parse(TEXT_OBJECT_JSON));
        // One text object should have been spawned
        assertEquals(1, land.getObjects().size());
    }

    // -------------------------------------------------------------------------
    // Builder is stateless between calls
    // -------------------------------------------------------------------------

    @Test
    public void buildLand_calledTwice_producesIndependentLands() {
        // TODO Find a better/more-robust way to assert stateless-ness
        Land first  = builder.buildLand(parse(SIMPLE_JSON));
        Land second = builder.buildLand(parse(NULL_DOOR_JSON));

        assertEquals("TestMap",    first.getName());
        assertEquals("NullDoorMap", second.getName());
        assertEquals(2, first.getRowsCount());
        assertEquals(1, second.getRowsCount());
    }
}
