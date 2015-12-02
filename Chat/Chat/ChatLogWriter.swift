//
//  ChatLogWriter.swift
//  MultipeerChat
//
//  Created by test on 12/2/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation

class ChatLogWriter
{
    var messages: [Message]
    var numMessages: Int
    let name: String
    
    init(roomName: String)
    {
        self.name = roomName
        self.numMessages = 0
        messages = []
    }
    
    //add messages to buffer and print buffer to file if necessary
    func addMessage(message: Message)
    {
        self.messages.append(message)
        self.numMessages++
        
        if (numMessages == 500)
        {
            //write to file
            writeMessages()
        }
    }
    
    func close()
    {
        writeMessages()
    }
    
    private func writeMessages()
    {
        //write messages from buffer
        
        //clear messages
        self.messages = []
        self.numMessages = 0
    }
}