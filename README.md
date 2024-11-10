# worldcup-football-scoreboard
Live Football World Cup Scoreboard

# Features
* start match feature
  * start a match with initial score 0-0. Once the match is created it will be visible on the scoreboard
  * constraints:
    * homeTeam and awayTeam are mandatory
    * homeTeam <> awayTeam
* update score feature
  * update scores for an in-progress (live) match. A score object consists:
    * team (mandatory)
    * score (positive number)
  * constraints
    * match must be in progress
    * both score inputs are mandatory, and they contain the absolute score value
* finish match feature
  * finish an in-progress match. Once the match is finished, it is removed from the scoreboards
* summary of matches in progress feature
  * return a list of in-progress matches ordered by their total score. The matches with the
    same total score will be returned ordered by the most recently started match in the
    scoreboard.
  * EG:
    * if following matches are started in the specified order and their scores
      respectively updated:
       * Mexico 0 - Canada 5
       * Spain 10 - Brazil 2
       * Germany 2 - France 2
       * Uruguay 6 - Italy 6
       * Argentina 3 - Australia 1
    * The summary should be as follows:
      1. Uruguay 6 - Italy 6
      2. Spain 10 - Brazil 2
      3. Mexico 0 - Canada 5
      4. Argentina 3 - Australia 1
      5. Germany 2 - France 2 
      
# Prerequisites
Ensure that you have the following installed:
 * Java 17 (or newer)
 * Maven

Adding the Library to Your Project
* To include the library in your project, add the following dependency in your pom.xml
````
 <dependency>
      <groupId>com.scoreboard</groupId>
      <artifactId>worldcup-football-scoreboard</artifactId>
      <version>0.0.1-SNAPSHOT</version>
  </dependency>
````
# Usage
```
import com.football.scoreboard.api.service.ScoreboardService;
import com.football.scoreboard.impl.ScoreboardServiceImpl;

public class MyExample {
  public static void main(String[] args) {
    ScoreboardService scoreboard = new ScoreboardServiceImpl();
    scoreboard.startMatch("homeTeam", "awayTeam");
  }
}
```

## Contributing

Pull requests are welcome. For major changes, please open an issue first
to discuss what you would like to change. Please make sure to update tests as appropriate.

## License

This project is licensed under the **Apache License 2.0** – see the [license](LICENSE.txt) file for details.

# Notes
Initial project tasks break down:
* project skeleton
* configure logging
* start match feature
  * validate that teams names cannot be null
  * validate that homeTeam <> awayTeam
  * score is 0-0
  * same match cannot be started multiple times
  * match is visible on the scoreboard
  * match name comparison should be case-insensitive
* update score feature
  * scores are positive numbers
  * match is still in progress
* finish match feature
  * match is removed from the scoreboard
  * score cannot be updated once the match was finished
* summary of in-progress (live) matches
  * summary includes only in-progress matches
  * matches are ordered according to requirements
* concurrency
  * in the same time multiple games can be started and finished

I chose ConcurrentHashMap instead of CopyOnWriteArrayList because of the frequent writes that might be needed for updating the scores.

Initially I have added logback as dependency to have proper logs, but I realized that by doing it i do not offer the library user 
the possibility to use a logger provider of his choice.