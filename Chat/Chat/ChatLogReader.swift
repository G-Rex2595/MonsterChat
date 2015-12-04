//
//  ChatLogReader.swift
//  Chat
//
//  Created by test on 12/4/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation

class ChatLogReader
{
    let logName: String
    
    init(log: Log)
    {
        self.logName = log.getLogName()
    }
    
    func getAll() -> [Message]
    {
        //read all of the log
        //{
            //convert lines into messages
        //}
        
        return []
    }
}