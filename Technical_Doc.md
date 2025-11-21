# Whack-a-Mole Technical Documentation

## 1. Object-Oriented Design and Architecture

### Core Design Philosophy
The project implements a flexible, extensible architecture centered around polymorphism and abstraction. The design allows for easy addition of new game objects without modifying core game logic.

### Class Hierarchy Justification

**Abstract Base Class: HoleOccupant**
- Chosen as an abstract class (rather than interface) because all game objects share common state (`visible`, `timeRemaining`) and behavior (`tick()`, `hide()`)
- Defines the polymorphic contract through abstract methods `whack()` and `getImage()`
- Encapsulates lifecycle management through the `tick()` method that decrements time and handles visibility

**Concrete Implementations:**
- **Mole**: Standard target returning +100 points with 3-second visibility
- **Bomb**: Penalty object returning -500 points with 3-second visibility
- **BonusMole**: High-value target returning +1000 points with 2-second visibility (shorter duration increases difficulty)

This hierarchy demonstrates the Open/Closed Principle: the system is open for extension (new occupant types can be added) but closed for modification (core game logic remains unchanged).

### Key Design Patterns

**Composition over Inheritance:**
- `GameUI` contains `GameGrid`, `GameEngine`, and `HighScoreManager` rather than inheriting from them
- This provides better flexibility and maintainability

**Separation of Concerns:**
- **GameGrid**: Manages game state and occupant positions
- **GameEngine**: Controls game timing and spawning logic
- **GameUI**: Handles presentation and user interaction
- **HighScoreManager**: Encapsulates all persistence operations

## 2. Concurrency Strategy

### Thread Architecture

**Two-Thread Model:**
1. **UI Thread** (Event Dispatch Thread): Handles all Swing component updates and user input
2. **Game Engine Thread**: Manages game logic, timing, and object lifecycle

### Thread Safety Implementation

**Critical Section Protection:**
```java
public synchronized HoleOccupant get(int index)
public synchronized void spawn(int index, HoleOccupant occupant)
public synchronized void tickAll()
```
All `GameGrid` methods are synchronized to prevent race conditions when the game engine thread modifies state while the UI thread reads it for rendering.

**Thread-Safe UI Updates:**
All UI modifications originating from the `GameEngine` thread are marshaled back to the Event Dispatch Thread using `SwingUtilities.invokeLater()`:
- `updateGrid()`: Updates hole images
- `updateTime()`: Updates time label
- `showGameOver()`: Displays end-game dialog

### Graceful Shutdown Mechanism

The system implements a clean shutdown protocol:
1. Window closing event triggers `engine.stopGame()` and `engineThread.interrupt()`
2. `InterruptedException` is caught in the game loop's `Thread.sleep()` call
3. The `running` volatile flag ensures loop termination
4. Thread exits cleanly without resource leaks

**Volatile Flag Usage:**
The `running` boolean is declared `volatile` to ensure visibility across threads without full synchronization, allowing the UI thread to signal termination to the game engine thread.

### Concurrency Benefits
- **Responsive UI**: User clicks are processed immediately without waiting for game loop iterations
- **Smooth Timing**: Game progresses at consistent 1-second intervals regardless of UI activity
- **No Blocking**: UI never freezes during gameplay

## 3. Exception Handling Strategy

### Three-Tier Exception Architecture

**Tier 1: Checked Exception - HighScoreException**
- **Type**: Custom checked exception extending `Exception`
- **Purpose**: Represents recoverable I/O failures during score persistence
- **Usage**: Wraps `IOException` and `ClassNotFoundException` from serialization operations
- **Handling**: Caught at application startup; allows graceful degradation with empty score list

**Rationale**: Score loading failures shouldn't crash the application. Users can still play even if previous scores are unavailable.

**Tier 2: Unchecked Exception - InvalidGameStateException**
- **Type**: Custom unchecked exception extending `RuntimeException`
- **Purpose**: Signals programmer errors and impossible game states
- **Example**: Attempting to spawn an occupant in an already-occupied hole
- **Handling**: Should never occur in correct code; indicates a bug if thrown

**Rationale**: This represents a contract violation that shouldn't be recovered from during normal operation. Using an unchecked exception documents this as a programming error rather than an expected condition.

**Tier 3: Thread Interruption - InterruptedException**
- **Purpose**: Cooperative thread cancellation mechanism
- **Handling**: Used as a signal for graceful shutdown, not as an error
- **Implementation**: Sets `running = false` to exit game loop cleanly

### Exception Wrapping Pattern

The `HighScoreManager` demonstrates proper exception chaining:
```java
catch (IOException | ClassNotFoundException e) {
    throw new HighScoreException("Error loading scores", e);
}
```
This preserves the original exception's stack trace while providing domain-specific context.

## 4. Data Persistence Design

### Serialization Strategy

**PlayerScore Class:**
- Implements `Serializable` for object persistence
- Immutable design with final fields promotes thread safety
- Simple POJO structure ensures reliable serialization

**HighScoreManager Encapsulation:**
- All file I/O logic centralized in one class
- File path ("scores.dat") encapsulated as private constant
- Provides clean abstraction: callers work with `List<PlayerScore>` objects without knowing storage details

### Design Benefits
- **Simplicity**: Entire score list serialized as a single object
- **Type Safety**: Generic collections preserve type information
- **Error Isolation**: I/O exceptions contained within manager class
- **Extensibility**: Easy to switch storage mechanisms (database, cloud) by reimplementing manager interface

## 5. Additional Technical Highlights

**Polymorphic Event Handling:**
The `onHoleClick()` method demonstrates true polymorphism—no type checking required:
```java
score += o.whack();  // Runtime binding determines behavior
```

**Memory Management:**
GameGrid properly nullifies references to invisible occupants, allowing garbage collection and preventing memory leaks during extended gameplay.

**UI/UX Design:**
- Color-coded UI elements improve readability
- Rounded buttons with hover states enhance user experience
- Modal game-over dialog prevents accidental clicks during transition

This architecture successfully demonstrates mastery of OOP principles, concurrent programming, exception handling, and data persistence in a cohesive, production-quality application.