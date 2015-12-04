//
//  ChatRoom.swift
//  MultipeerChat
//
//  Created by test on 12/2/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation

class ChatRoom
{
    var username: String
    var roomName: String
    let logWriter: ChatLogWriter
    
    init(username: String, roomName: String)
    {
        self.username = username
        self.roomName = roomName
        self.logWriter = ChatLogWriter(roomName: roomName)
    }
    
    func sendMessage(text: String)
    {
        let message = Message(username: self.username, message: text, id: "id", roomName: self.roomName)
        addMessage(message)
    }
    
    func addMessage(message: Message)
    {
        self.logWriter.addMessage(message)
    }
    
    func exit()
    {
        //TODO: later sprint
        self.logWriter.close()
    }
    
    func setUsername(username: String)
    {
        self.username = username
    }
}