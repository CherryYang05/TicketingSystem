#!/bin/sh

javac -encoding UTF-8 -cp . ticketingsystem/GenerateHistory.java
javac -encoding UTF-8 -cp . ticketingsystem/Replay.java
# threadnum, testnum, isSequential, msec, nsec
java -cp . ticketingsystem/GenerateHistory 1 100 1 0 0 > history
java -cp . ticketingsystem/Replay 1 history 1 failedHistory
rm ticketingsystem/*.class