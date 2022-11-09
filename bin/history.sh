#!/bin/sh

javac -encoding UTF-8 -cp . ticketingsystem/GenerateHistory.java
# threadnum, testnum, isSequential, msec, nsec
java -cp . ticketingsystem/GenerateHistory 1 20 1 0 0 
rm ticketingsystem/*.class