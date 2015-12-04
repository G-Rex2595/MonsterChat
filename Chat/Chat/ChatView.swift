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


class ChatView: UIViewController, MCSessionDelegate, UITableViewDelegate, UITableViewDataSource{


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
        
        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
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
    
    
    
    
    func numberOfSectionsInTableView(chatTable: UITableView) -> Int {
        return 1;
    }
    
    func tableView(chatTable: UITableView, numberOfRowsInSection section: Int) -> Int {
        return messagesArray.count
    }
    
    func tableView(chatTable: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let tableCell = UITableViewCell()
        tableCell.textLabel!.text = messagesArray[indexPath.row]
        //tableCell.detailTextLabel

        return tableCell
    }
    
    func tableView(chatTable: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return 20.0
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