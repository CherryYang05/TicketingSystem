#!/bin/sh

javac -encoding UTF-8 -cp . ticketingsystem/GenerateHistory.java
# 参数说明：threadnum, testnum, isSequential, msec, nsec
java -cp . ticketingsystem/GenerateHistory 2 100 0 0 0 > history
# 参数说明：threadNum, historyFile, isPosttime, outputFile
java -Xss1024m -Xmx400g -jar VeriLin.jar 1 history 1 failedHistory
rm ticketingsystem/*.class