# lazarusgame
Lazarus a single player game written in Java 8. The player must dodge the falling boxes and escape the mob. Lazarus uses singleton, observer, and MVC design patterns.

Controls: 
  ↑: up 
  ←: left 
  →: right 
  ↓: down

Game Rules:
  1) The player must dodge all incoming boxes falling from above and build a staircase to escape.
  2) Each box has weight. If a box with a heavier weight falls on boxes with lower weight, the lower weight boxes will e destroyed.
  3) The game is over when the player reaches the exit.
  4) This game comes with three levels of difficulty. Boxes will fall faster depending on the level of difficulty.
