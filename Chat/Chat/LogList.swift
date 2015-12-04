//
//  LogList.swift
//  Chat
//
//  Created by test on 12/4/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation

class LogList
{
    static func getList() -> [Log]
    {
        //set variables for listing logs
        let fileManager = NSFileManager.defaultManager()
        let path = NSBundle.mainBundle().resourcePath!
        let enumerator:NSDirectoryEnumerator = fileManager.enumeratorAtPath(path)!
        var logs: [Log] = []
        
        //go through directory
        while let element = enumerator.nextObject() as? String
        {
            //get .log files
            if element.hasSuffix(".log")
            {
                //variables for getting log info
                var roomName: String = ""
                var index = element.startIndex
                var seconds: Double = 0
                
                //get room name
                while element[index] != " "
                {
                    roomName += String(element[index])
                    index = index.advancedBy(1)
                }
                index = index.advancedBy(1)
                
                //get time
                while element[index] != "."
                {
                    seconds *= 10
                    if let num = Double(String(element[index]))
                    {
                        seconds += num
                    }
                    index = index.advancedBy(1)
                }
                
                //convert time to NSDate
                let time = NSDate(timeIntervalSinceReferenceDate: seconds)
                
                logs.append(Log(roomName: roomName, timeCreated: time, logName: element))
            }
        }
        
        return logs
    }
}