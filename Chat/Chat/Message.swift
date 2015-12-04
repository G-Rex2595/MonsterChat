//
//  Message.swift
//  MultipeerChat
//
//  Created by test on 12/2/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation

class Message
{
    var username: String
    var message: String
    var id: String
    var roomName: String
    var time: NSDate
    
    init(username: String, message: String, id: String, roomName: String)
    {
        self.message = message
        self.username = username
        self.id = id
        self.roomName = roomName
        self.time = NSDate()
    }
    
    func getUserName() -> String
    {
        return self.username
    }
    
    func getMessage() -> String
    {
        return self.message;
    }
    
    func getID() -> String
    {
        return self.id
    }
    
    func getRoomName() -> String
    {
        return self.roomName
    }
    
    func getTime() -> NSDate
    {
        return self.time
    }
    
    func setTime(time: NSDate)
    {
        self.time = time
    }
    
    func setTime()
    {
        self.time = NSDate()
    }
}