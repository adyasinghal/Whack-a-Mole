# Whack-a-Mole
A sophisticated Java implementation of the classic Whack-a-Mole arcade game, demonstrating advanced Object-Oriented Programming principles, multithreading, and GUI development.  

## Features
- Multiple Game Objects: Interact with Moles (+100 points), Bonus Moles (+1000 points), and avoid Bombs (-500 points)
- Dynamic Gameplay: Objects appear randomly across a 3x3 grid with varying lifespans
- Time Challenge: 30-second countdown adds urgency to the gameplay
- Persistent High Scores: Score tracking across sessions using Java serialization
- Responsive UI: Clean, colorful Swing-based interface with smooth animations
- Concurrent Design: Separate game engine thread ensures lag-free user experience

## How To Play
### Setup
```
# Clone the repository
git clone https://github.com/yourusername/whack-a-mole.git

# Navigate to project directory
cd whack-a-mole

# Compile the project
javac -d bin src/**/*.java

# Run the game
java -cp bin game.ui.GameUI
```
1. Start the Game: Run the application to begin  
2. Click to Whack: Click on objects as they appear from holes  

🐹 Mole: Click for +100 points  
💎 Bonus Mole: Click quickly for +1000 points (appears for 2 seconds)  
💣 Bomb: Avoid! Costs -500 points if clicked  

3. Beat the Clock: Score as many points as possible before time runs out
4. Track Progress: View your score and remaining time at the top of the screen
5. Compete: Try to beat the high score displayed on the game-over screen

## Project Structure

<img width="475" height="488" alt="image" src="https://github.com/user-attachments/assets/87bf6590-a0b9-47a6-8468-391572385d66" />

<!-- 
whack-a-mole/  
├── src/  
│   ├── exceptions/  
│   │   ├── HighScoreException.java  
│   │   └── InvalidGameStateException.java  
│   ├── game/  
│   │   ├── engine/  
│   │   │   ├── GameEngine.java  
│   │   │   └── GameGrid.java  
│   │   ├── occupants/  
│   │   │   ├── HoleOccupant.java (abstract)  
│   │   │   ├── Mole.java  
│   │   │   ├── Bomb.java  
│   │   │   └── BonusMole.java  
│   │   └── ui/  
│   │       └── GameUI.java  
│   └── persistence/  
│       ├── HighScoreManager.java  
│       └── PlayerScore.java  
├── images/  
│   ├── mole.png  
│   ├── bomb.png  
│   └── bonus.png  
├── scores.dat (generated after first game) 
-->

[UML Diagram](https://github.com/adyasinghal/Whack-a-Mole/blob/main/UML_Diagram.png)  

## [Technical Documentation](https://github.com/adyasinghal/Whack-a-Mole/blob/main/Technical_Doc.md)  
