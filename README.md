Chess
==================================================

This program is chess engine with a basic ai. The program provides a simple interface for making moves and will validate legal moves and observe end of game conditions.


In addition to that, the engine allows for a customizable board of any size, with gaps in the board, and arbitrary piece location.<br>
The ai will also adapt to these board conditions and adjust play.

![alt text](http://i.snag.gy/brZue.jpg "A sample board game with a custom setup")


AI
---
There are several AI programmed in with varying strengths.

<b><u>MinMaxAI</u></b><br>
The default AI. It will construct a tree of all possible moves and determine the best recursively.
It uses a simple MinMax search and will prune branches it deems pointless;<br>
<b><u>FastMinMaxAI</u></b><br>
Similar to the MinMaxAI, it will perform simpler calculations in order to search more quickly and more deeply.
