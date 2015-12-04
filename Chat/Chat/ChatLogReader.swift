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
    let log: Log
    
    init(log: Log)
    {
        self.log = log
    }
    
    func getAll() -> [Message]
    {
        //declare variables
        var messages: [Message] = []
        
        //read all of the log
        if let reader = StreamReader(path: self.log.getLogName())
        {
            defer
            {
                reader.close()
            }
            while let line = reader.nextLine()
            {
                //temporary variables
                var userName: String = ""
                var seconds: Double = 0
                var message: String
                var index = line.startIndex
                
                //get username
                while line[index] != ":"
                {
                    userName += String(line[index])
                    index = index.advancedBy(1)
                }
                index = index.advancedBy(1)
                
                //get time
                while line[index] != ":"
                {
                    seconds *= 10
                    if let num = Double(String(line[index]))
                    {
                        seconds += num
                    }
                    index = index.advancedBy(1)
                }
                index = index.advancedBy(1)
                
                //convert time
                let time = NSDate(timeIntervalSinceReferenceDate: seconds)
                
                //get message content
                message = line.substringFromIndex(index)
                
                //set message
                let current = Message(username: userName, message: message, id: "", roomName: log.getRoomName())
                current.setTime(time)
                messages.append(current)
            }
        }
        
        return messages
    }
}

/* The following code provided courtesy of
 * Martin R from
 * http://stackoverflow.com/questions/24581517/read-a-file-url-line-by-line-in-swift
*/

class StreamReader  {
    
    let encoding : UInt
    let chunkSize : Int
    
    var fileHandle : NSFileHandle!
    let buffer : NSMutableData!
    let delimData : NSData!
    var atEof : Bool = false
    
    init?(path: String, delimiter: String = "\n", encoding : UInt = NSUTF8StringEncoding, chunkSize : Int = 4096) {
        self.chunkSize = chunkSize
        self.encoding = encoding
        
        if let fileHandle = NSFileHandle(forReadingAtPath: path),
            delimData = delimiter.dataUsingEncoding(encoding),
            buffer = NSMutableData(capacity: chunkSize)
        {
            self.fileHandle = fileHandle
            self.delimData = delimData
            self.buffer = buffer
        } else {
            self.fileHandle = nil
            self.delimData = nil
            self.buffer = nil
            return nil
        }
    }
    
    deinit {
        self.close()
    }
    
    /// Return next line, or nil on EOF.
    func nextLine() -> String? {
        precondition(fileHandle != nil, "Attempt to read from closed file")
        
        if atEof {
            return nil
        }
        
        // Read data chunks from file until a line delimiter is found:
        var range = buffer.rangeOfData(delimData, options: [], range: NSMakeRange(0, buffer.length))
        while range.location == NSNotFound {
            let tmpData = fileHandle.readDataOfLength(chunkSize)
            if tmpData.length == 0 {
                // EOF or read error.
                atEof = true
                if buffer.length > 0 {
                    // Buffer contains last line in file (not terminated by delimiter).
                    let line = NSString(data: buffer, encoding: encoding)
                    
                    buffer.length = 0
                    return line as String?
                }
                // No more lines.
                return nil
            }
            buffer.appendData(tmpData)
            range = buffer.rangeOfData(delimData, options: [], range: NSMakeRange(0, buffer.length))
        }
        
        // Convert complete line (excluding the delimiter) to a string:
        let line = NSString(data: buffer.subdataWithRange(NSMakeRange(0, range.location)),
            encoding: encoding)
        // Remove line (and the delimiter) from the buffer:
        buffer.replaceBytesInRange(NSMakeRange(0, range.location + range.length), withBytes: nil, length: 0)
        
        return line as String?
    }
    
    /// Start reading from the beginning of file.
    func rewind() -> Void {
        fileHandle.seekToFileOffset(0)
        buffer.length = 0
        atEof = false
    }
    
    /// Close the underlying file. No reading must be done after calling this method.
    func close() -> Void {
        fileHandle?.closeFile()
        fileHandle = nil
    }
}