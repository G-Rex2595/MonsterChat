//
//  ChatRoomManager.swift
//  MultipeerChat
//
//  Created by test on 12/2/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation

class ChatRoomManager
{
    let network: P2PManager
    var currentRoom: ChatRoom?
    var username: String
    
    init(username: String)
    {
        self.username = username
        self.network = P2PManager.getInstance()
    }
    
    func GetAvailableChatRooms() -> [String]
    {
        return network.getAvailableChatRooms()
    }
    
    func joinRoom(roomName: String) -> ChatRoom
    {
        self.currentRoom!.exit()
        
        self.currentRoom = ChatRoom(network: self.network, username: self.username, roomName: roomName)
        
        return self.currentRoom!
    }
    
    func exit()
    {
        self.currentRoom!.exit()
    }
}
