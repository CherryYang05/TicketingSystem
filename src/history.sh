#!/bin/sh

javac -encoding UTF-8 -cp . ticketingsystem/GenerateHistory.java
java -cp . ticketingsystem/GenerateHistory 1 100 1 0 0 
./clean.sh