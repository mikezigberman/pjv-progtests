# Java Programming Tests

## 📃 Content

- Description ✅
- ProgTest 1 ✅
- ProgTest 2 ✅
- ProgTest 3 ✅
- ProgTest 4 (:construction: in process)
- ProgTest 5 (:construction: in process)
- Semester work on the subject of [PJV](https://github.com/mikezigberman/sw_pjv "Semester work PJV") ✅

# Description 

## Java programming tests written during the second semester

## ProgTest 1: 

### Task assignment:

1. Create a project in IntelliJ Java with one package called `thedrake`
2. Inside this package, create a class `Offset2D` to represent a discrete displacement in 2D space. This will come in handy at many points in the game for scrolling around the board. Represent the coordinates as two attributes, of type. It implements the following methods in this class: `public final x y int`

```
// Constructor
public Offset2D(int x, int y)

// Checks if this offset is equal to another offset
public boolean equalsTo(int x, int y)

// Returns the new offset where the y coordinate has the sign reversed
public Offset2D yFlipped()
```

3. Create an enum `TroopFace` with the values `AVERS` and `REVERS`. This type determines the front and back of the unit.
4. Create a class `Troop` that will represent a combat unit in our game. Each unit has its own name (for example "Archer") and a so-called face pivot and reverse pivot. In the image of the unit, the pivot is marked with a symbol resembling `i`. The pivot coordinates can be different on the front and back side of the unit, see picture. That's why we need a face and a reverse pivot.

![download](https://user-images.githubusercontent.com/30218257/227719467-ed41a864-5336-4477-aac7-3be778df059d.png)

Let the class `Troop` have all the attributes and implement the following methods: `private final`
```
// The main constructor
public Troop(String name, Offset2D aversPivot, Offset2D reversPivot)

// A constructor that sets both pivots to the same value
public Troop(String name, Offset2D pivot)

// A constructor that sets both pivots to [1, 1]
public Troop(String name)

// Returns the unit name
public String name()

// Returns the pivot on the specified side of the unit
public Offset2D pivot(TroopFace face)
``` 

### Grade for the first progtest:

<img width="465" alt="Screenshot 2023-03-25 at 14 05 22" src="https://user-images.githubusercontent.com/30218257/227718941-64efd7b1-4f17-4722-97d5-2908d77e552b.png">

## ProgTest 2: 

### Task assignment:

1. Inside a package `thedrake` in your project, create an enum `PlayingSide` with the values `ORANGE` and `BLUE` that represents the color the player is playing as.
2. Next, create an interface `Tile`. This represents a tile on the board. Each tile can answer whether it can be entered or whether it contains a unit.
```
// Returns True if this tile is free and can be accessed.
public boolean canStepOn();

// Returns True if this tile contains a unit
public boolean hasTroop();
```
3. Create an implementation of the interface `Tile` named `TroopTile`. This immutable class will represent the tile the unit is standing on. This tile remembers the unit standing on it, the color the unit is playing for, and the side the unit is facing (back or face). Please note that our design works in such a way that the color and side are not remembered by the unit itself, but only by the tile on which it stands. This will save us a lot of work in the future.

```
// Constructor
public TroopTile(Troop troop, PlayingSide side, TroopFace face)

// Returns the color that the unit on this tile is playing as
public PlayingSide side()

// Returns the side the unit is facing
public TroopFace face()

// A unit standing on this tile
public Troop troop()

// Returns False because a tile with a unit cannot be entered
public boolean canStepOn()

// Returns True
public boolean hasTroop()

// Creates a new tile, with the unit facing the opposite side
// (from reverse to obverse or from obverse to reverse)
public TroopTile flipped()
```
4. From the preparation (download "Sample data" below), take the interface `BoardTile`, which represents tiles that do not contain units. These are empty tiles or mountains. Check out its implementation and include it in your package.

5. Next, take the interface from the preparation `TilePos`, which represents the position of the tile on the board. Tile coordinates are internally represented as two integers `i`(column number) and `j` (row number). The tile in the lower left corner corresponds to the coordinate `i=0`, `j=0`. The position of the tile can then also be represented using more human-friendly coordinates of the type `a1, c4` etc. So the plot `TilePos` has conversion methods to work with both types of coordinates.
It also contains an overloaded method `step()` that creates a new coordinate shifted in the direction of the specified offset. Method `stepByPlayingSide()` in addition, what it does is shift this coordinate according to the player's color. If blue plays, it works as a method `step()`. If he plays orange, he will move with the reverse y coordinate.
The method `isNextTo()` returns `true` if this position is adjacent to the specified position.
Note the implementation of `OFF_BOARD`, which represents a position that is outside the board. Whenever one of the methods `step...` should step outside the board, it returns exactly the position `OFF_BOARD`.

6. In preparation you will also find a class `BoardPos` implementing the `TilePos`; interface. You will also find the class here `PositionFactory`; which takes care of creating positions on a board of a given size. Thanks to this, the positions themselves can check if they happen to be outside the boundaries of the playing field.

7. The challenge of this task is to create a class `Board` representing the game plan. It consists of a two-dimensional array of tiles of the type `BoardTile`. Since the design of the entire game is immutable, the class `Board` is also immutable. So if we want to change some tile to another, we need to create a new copy of this class with this new tile in the right place. We will store the tiles in a two-dimensional array, which will therefore always need to be copied in their entirety. `Object.clone()` A method that can clone a one-dimensional array can come in handy here.

8. The class `Board` is slightly more complicated, download the template for its implementation from preparation. The template contains one inner class.  `Board.TileAt.` It's a tool that lets us tell which tile is in which position, because the tiles themselves don't remember their position. This suits us for the method withTiles see below.

9. In the class template, `Board` implement the highlighted methods
```
// Constructor. Creates a square board of the specified size where all tiles are empty, i.e. BoardTile.EMPTY
public Board(int dimension)

// Size of the game board
public int dimension()

// Returns the tile to the selected position.
public BoardTile at(TilePos pos)

// Creates a new game board with new tiles. All other tiles remain the same
public Board withTiles(Board.TileAt ...ats)

// Creates a PositionFactory instance to produce positions on this board
public PositionFactory positionFactory()
```

### Grade for the second progtest:

<img width="453" alt="Screenshot 2023-03-25 at 14 05 47" src="https://user-images.githubusercontent.com/30218257/227718965-b37e39df-51df-4245-aa4d-9f2eb9db1fa6.png">

## ProgTest 3:

### Task assignment:

1. BoardTroops class
- The class `BoardTroopsis` the most important class in the entire game as it takes care of the units on the board. The game state maintains an instance of this class for each player. Thus, one instance of this class holds units of only one side (blue or orange). 

- Download the class template BoardTroopsfrom the preparation (download "Sample data" below). The class contains these attributes.
- The color of the units the player is playing for: `playingSide`,
- a map that maps positions on the board to specific tiles of type `TroopTile`: `troopMap`,
- leader position on the board: `leaderPosition`,
- number of guards already laid: `guards`.

A class has two constructors, one primary that receives all necessary parameters and a secondary constructor

`public BoardTroops(PlayingSide playingSide)`

which creates an instance that has no units (you can use `Collections.emptyMap`), no guards, and the leader's position is `TilePos.OFF_BOARD`.

The methods of this class allow you to move units around the board according to the rules of the game. When moving units, we have to keep an eye on the position of the leader, because it tells us if someone has kicked out the leader.

`public Optional<TroopTile> at(TilePos pos)`

Returns the tile to the specified position, or `Optional.empty()` if there is no tile at that position.

`public PlayingSide playingSide()`

Returns the player's color

`public TilePos leaderPosition()`

Returns the current leader position. If the leader is not already deployed, returns `TilePos.OFF_BOARD`.

`public int guards()`

Returns the number of deployed guards. They are always guarded by either 0, 1 or 2.

`public boolean isLeaderPlaced()`

Returns `True` if the leader is already deployed, otherwise `False`.

`public boolean isPlacingGuards()`

Returns `True` when guards are being deployed. Guards are deployed if the leader is already deployed. The leader is always the first unit put into play, the second and third are the guards. Once the third unit is deployed, the guard deployment phase ends.

`public Set<Board.Pos> troopPositions()`

Returns the set of all positions that are occupied by some unit tile.

`public BoardTroops placeTroop(Troop troop, BoardPos target)`

Returns a new instance `BoardTroops` with a new tile `TroopTile` at the position `target` containing the `troop` face-up unit. This method throws an exception `IllegalArgumentException` if the specified position is already occupied by another tile.

If we are building the very first unit using this method, this unit is taken as the leader and the position needs to be set `leaderPosition`. With this, the game enters the guard building phase.

If we are building the second and third units using this method, we are in the guard building phase. This phase ends when both guards are built.

`public BoardTroops troopStep(BoardPos origin, BoardPos target)`

Returns a new instance `BoardTroops` with the tile `TroopTile` at the position `origin` moved to the position `target` and rotated to the opposite side. This method throws an exception `IllegalStateException` if we are in the state of building a leader or building guards, because in these phases the tiles cannot be moved yet. The method further throws an exception `IllegalArgumentException` if the position is originempty or the position is `target` already occupied.

Attention, if we remove the leader using this method, its position must be set back to `TilePos.OFF_BOARD`.

2. Army class

- A class `Army` represents the entire army of one player, i.e. not only units on the board, but also captured units and units on the stack. Captured units and the stack are represented as lists. Units on the board are managed by an instance of the class `BoardTroops`.

### Grade for the third progtest:

<img width="462" alt="Screenshot 2023-04-07 at 16 40 12" src="https://user-images.githubusercontent.com/30218257/230627522-589037aa-32ae-49e4-92e9-24401fb7ceac.png">

## ProgTest 4 (:construction: in process)

## ProgTest 5 (:construction: in process)

## Semester work on the subject of [PJV](https://github.com/mikezigberman/sw_pjv "Semester work PJV")
