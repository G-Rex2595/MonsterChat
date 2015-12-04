//
//  ChatView.swift
//  Chat
//
//  Created by Vishal Gill on 12/2/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import Foundation
import UIKit
import MultipeerConnectivity

var blockList:[String] = [""]


class ChatView: UIViewController, MCSessionDelegate, UITableViewDelegate,UITableViewDataSource{


    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        self.view.endEditing(true)
        
    }
    
    @IBOutlet var chatTable: UITableView!
    @IBOutlet var msgfield: UITextField!
    
    
    var messagesArray = [String]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = Singleton.sharedInstance.backgroundColor
        chatsession.delegate = self
        
        chatTable.delegate = self
        chatTable.dataSource = self
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfSectionsInTableView(chatTable: UITableView) -> Int {
        return 1;
    }
    
    func tableView(chatTable: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messagesArray.count
    }
    
    func tableView(chatTable: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let tableCell = UITableViewCell()
        tableCell.textLabel!.text = messagesArray[indexPath.row]
        
        if messagesArray[indexPath.row] is String
        {
            var temp: String = messagesArray[indexPath.row] as! String
            var tempArr = temp.componentsSeparatedByString(":")
            var blockUser = tempArr[0]
            for i in 0 ... blockList.count-1
            {
                if blockUser == blockList[i]
                {
                    tableCell.textLabel?.text = "This user has been blocked"
                    return tableCell
                }
                
            }
        }
        else
        {
//            var temp: String = cellContent[indexPath.row-1] as! String
//            var tempArr = temp.componentsSeparatedByString(":")
//            var blockUser = tempArr[0]
//            for i in 0 ... blockList.count-1
//            {
//                if blockUser == blockList[i]
//                {
//                    cell.textLabel?.text = "This message has been blocked"
//                    return cell
//                }
//                
//            }
        }
        
        return tableCell
    }
    
    func tableView(chatTable: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return 40.0
    }
    
    func tableView(tableView: UITableView, commitEditingStyle editingStyle: UITableViewCellEditingStyle, forRowAtIndexPath indexPath: NSIndexPath) {
        if editingStyle == UITableViewCellEditingStyle.Delete {
            messagesArray.removeAtIndex(indexPath.row)
            tableView.deleteRowsAtIndexPaths([indexPath], withRowAnimation: UITableViewRowAnimation.Automatic)
        }
    }
    
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        
        print("didselect was called")
        let optionMenu = UIAlertController(title: nil, message: "Choose Option", preferredStyle: .ActionSheet)
        
        let blockAction = UIAlertAction(title: "Block user", style: .Default, handler: {
            (alert: UIAlertAction!) -> Void in
            print("blocked")
            if self.messagesArray[indexPath.row] is String
            {
                var temp: String = self.messagesArray[indexPath.row] as! String
                var tempArr = temp.componentsSeparatedByString(":")
                var blockUser = tempArr[0]
                blockList.append(blockUser)
            }
            else
            {
                //                var temp: String = self.cellContent[indexPath.row-1] as! String
                //                var tempArr = temp.componentsSeparatedByString(":")
                //                var blockUser = tempArr[0]
                //                blockList.append(blockUser)
            }
        })
        
        let cancelAction = UIAlertAction(title: "Cancel", style: .Cancel, handler: {
            (alert: UIAlertAction!) -> Void in
            print("Cancelled")
        })
        
        optionMenu.addAction(blockAction)
        optionMenu.addAction(cancelAction)
        
        self.presentViewController(optionMenu, animated: true, completion: nil)
    }
    

    
    @IBAction func sendData(sender: UIButton) {
        
        
        //let msg = Message.init(username: peerID.displayName, message: msgfield.text!, id: "some ID", roomName: discoveryInfo["room"]!)
        
        //let encodedMsg = msg.dataUsingEncoding(NSUTF8StringEncoding)
        
        let timestamp = NSDateFormatter.localizedStringFromDate(NSDate(), dateStyle: .MediumStyle, timeStyle: .ShortStyle)

        var usernameMsgTime = Singleton.sharedInstance.userName
        usernameMsgTime.appendContentsOf(": " + self.msgfield.text!)
        usernameMsgTime.appendContentsOf(": " + timestamp)
        
        let msg = usernameMsgTime.dataUsingEncoding(NSUTF8StringEncoding)
        
        if(self.msgfield.text! != ""){
            do {
                try chatsession.sendData(msg!, toPeers: chatsession.connectedPeers, withMode: MCSessionSendDataMode.Unreliable)
                self.messagesArray.append(usernameMsgTime)
                self.chatTable.reloadData()
                print("sent \(msg) to \(chatsession.connectedPeers)")
            } catch {
                print("couldnt sendData")
            }

        }

        
        //self.updateChat(self.msgfield.text!, fromPeer: self.peerID)
        

        self.msgfield.text = ""
    }

    
    func session(chatsession : MCSession, peer checkPeer: MCPeerID, didChangeState state: MCSessionState) {
        
        switch state{
            
        case MCSessionState.Connected:
            print("Connected to session:")
            
        case MCSessionState.Connecting:
            print("Connecting to session:")
            
        case MCSessionState.NotConnected:
            print("Disconnected from session:")
            NSOperationQueue.mainQueue().addOperationWithBlock { () -> Void in
              self.performSegueWithIdentifier("chatlist", sender: self)
            }
        }
    }
    
    func session(chatsession: MCSession, didReceiveData data: NSData,
        fromPeer peerID: MCPeerID)  {
            // This needs to run on the main queue
            print("didReceiveData was called")

            let msg = NSString(data: data, encoding: NSUTF8StringEncoding)

            dispatch_async(dispatch_get_main_queue()) {
                
                
                self.messagesArray.append(msg as! String)
                self.chatTable.reloadData()
            }
    }
    
    
    
    
    
    func session(session: MCSession,
        didStartReceivingResourceWithName resourceName: String,
        fromPeer peerID: MCPeerID, withProgress progress: NSProgress)  {
            
            // Called when a peer starts sending a file to us
    }
    
    func session(session: MCSession,
        didFinishReceivingResourceWithName resourceName: String,
        fromPeer peerID: MCPeerID,
        atURL localURL: NSURL, withError error: NSError?)  {
            // Called when a file has finished transferring from another peer
    }
    
    func session(session: MCSession, didReceiveStream stream: NSInputStream,
        withName streamName: String, fromPeer peerID: MCPeerID)  {
            // Called when a peer establishes a stream with us
    }



}