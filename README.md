# worldcup-football-scoreboard
Live Football World Cup Scoreboard

Initial project tasks break down:
* project skeleton
* configure logging
* start match feature
  * validate that teams names cannot be null
  * validate that homeTeam <> awayTeam
  * score is 0-0
  * same match cannot be started multiple times
  * match is visible on the scoreboard
* update score feature
  * scores are positive numbers
  * match is still in progress
* finish match feature
  * match is removed from the scoreboard
  * score cannot be updated once the match was finished
* live matches statistics
  * statistics include only in progress matches
  * matches are ordered according to requirements
* concurrency
  * in the same time multiple games can be started and finished
