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
    var network: P2PManager
    var roomName: String
    var gui: ChatRoomView?
    let logWriter: ChatLogWriter
    
    init(network: P2PManager, username: String, roomName: String)
    {
        self.network = network
        self.username = username
        self.roomName = roomName
        self.messages = []
        self.logWriter = ChatLogWriter(roomName)
    }
    
    func sendMessage(text: String) -> Message
    {
        let message = Message(username: self.username, message: text, id: "id", roomName: self.roomName)
        
        self.messages.append(message)
        
        self.network.sendMessage(message)
        
        return message
    }
    
    func getMessages() ->[Message]
    {
        return self.messages
    }
    
    func addMessage(message: Message)
    {
        self.gui!.addMessage(message)
        self.logWriter.addMessage(message)
    }
    
    func exit()
    {
        //TODO: later sprint
        self.logWriter.close()
    }
    
    func setChatRoomView(view: ChatRoomView)
    {
        self.gui = view;
    }
    
    func setUsername(username: String)
    {
        self.username = username
    }
}