#!/bin/sh

javac -encoding UTF-8 -cp . ticketingsystem/GenerateHistory.java
java -cp . ticketingsystem/GenerateHistory 4 10 0 0 0 
./clean.sh