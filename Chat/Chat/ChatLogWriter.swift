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
    let roomName: String
    let logName: String
    //let fileManager = NSFileManager.defaultManager()
    
    init(roomName: String)
    {
        self.roomName = roomName
        self.numMessages = 0
        let seconds = NSDate().timeIntervalSinceReferenceDate
        logName = roomName + " " + String(Int(seconds)) + ".log"
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
        //open file to append
        let log: NSFileHandle? = NSFileHandle(forUpdatingAtPath: "/" + self.logName)
        
        //check if logging is possible
        if log == nil
        {
            self.messages = []
            self.numMessages = 0
            return
        }
        else
        {
            //go to end to append
            log?.seekToEndOfFile()
            
            //log messages in buffer
            for m in self.messages
            {
                //get line for logging
                let time = m.getTime().timeIntervalSinceReferenceDate
                let logText = m.getUserName() + ":" + String(Int(time)) + ":" + m.getMessage() + "\n"
                
                log?.writeData(logText.dataUsingEncoding(NSUTF8StringEncoding)!)
            }
            
            //close log
            log?.closeFile()
        }
        
        //clear messages
        self.messages = []
        self.numMessages = 0
    }
}