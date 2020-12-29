# Quattro

Quttro is a 4 person Tic Tac Toe game integrated as a Discord bot. You can ether host your own bot or you can simply invite an existing datacenter hosted online public bot
to your server using [this link](https://quattro.github.com/)

# Usage

Quattor has some simple game rules. Basicly you have 4 players playing tic tac toe on a 8x8 board. When you join the game you get a pre programmed color asignement. Players are on move in the order they joined the game. The game will loop trought the players until a winner gets found. The same moment game detects a winner game ends and the winner gets displayed. You can win by having 4 of you charracters in the row just like in the regular tic tac toe (horisontal, vertical and diagonal)

You can control the bot with these commands:

`q!create` - This will create a new game
`q!join @Username` - You will join the Username's game
`q!put <m> <n>` - This will put your charecter on the board on cords m,n (NOTE: m and n must be in range from 0 to 7)
`q!leave` - with this you will leave the lobby, if your are the game owner or if the game has already started and you leave all the members will get kicked and the game will end
`q!games` - This will print out all the available games on the current server

There are few other things you need to know:

**When you create a new game that game will only be available in the text channel where you created the game. This feature is added in order to force players to use only one channel to play the game**
