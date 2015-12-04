//
//  Log.swift
//  Chat
//
//  Created by test on 12/4/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation

class Log
{
    let roomName: String
    let timeCreated: NSDate
    let logName: String
    var deleted: Bool
    
    init(roomName: String, timeCreated: NSDate, logName: String)
    {
        self.roomName = roomName
        self.timeCreated = timeCreated
        self.logName = logName
        self.deleted = false;
    }
    
    func getRoomName() -> String
    {
        return self.roomName
    }
    
    func getTimeCreated() -> NSDate
    {
        return self.timeCreated
    }
    
    func getLogName() -> String
    {
        return self.logName
    }
    
    func delete()
    {
        let fileManager = NSFileManager.defaultManager()
        do
        {
            try fileManager.removeItemAtPath("/" + self.logName)
        }
        catch is NSError
        {
            return
        }
        
        self.deleted = true
    }
    
    func wasDeleted() -> Bool
    {
        return self.deleted
    }
}