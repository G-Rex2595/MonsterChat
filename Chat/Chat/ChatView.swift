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


class ChatView: UIViewController, MCSessionDelegate, UITableViewDelegate,UITableViewDataSource, UITextFieldDelegate{
    
    @IBOutlet var chatTable: UITableView!
    @IBOutlet var msgfield: UITextField!
    
    @IBOutlet weak var chatrooms: UIButton!
    @IBOutlet weak var send: UIButton!
    
    var messagesArray = [String]()
    var umtArray = [Umt]()
    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = Singleton.sharedInstance.backgroundColor
        chatsession.delegate = self
        
        chatTable.delegate = self
        chatTable.dataSource = self
        
        self.msgfield.delegate = self
        
        NSNotificationCenter.defaultCenter().addObserver(self, selector: Selector("keyboardWillShow:"), name:UIKeyboardWillShowNotification, object: nil);
        NSNotificationCenter.defaultCenter().addObserver(self, selector: Selector("keyboardWillHide:"), name:UIKeyboardWillHideNotification, object: nil);
        
        self.chatrooms.tintColor = Singleton.sharedInstance.textColor
        self.send.tintColor = Singleton.sharedInstance.textColor
        self.msgfield.tintColor = Singleton.sharedInstance.textColor

        
        self.chatrooms.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(chatrooms.titleLabel?.font?.pointSize)!)
        self.send.titleLabel?.font = UIFont(name: Singleton.sharedInstance.font, size:(send.titleLabel?.font?.pointSize)!)
        self.msgfield.font = UIFont(name: Singleton.sharedInstance.font, size:(msgfield.font?.pointSize)!)


        // Do any additional setup after loading the view, typically from a nib.
    }
    
    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func keyboardWillShow(sender: NSNotification) {
        self.view.frame.origin.y -= 200
    }
    
    func keyboardWillHide(sender: NSNotification) {
        self.view.frame.origin.y += 200
    }
    
    func textFieldShouldReturn(nameOfRoom: UITextField) -> Bool {
        self.view.endEditing(true)
        return true
    }
    
    func numberOfSectionsInTableView(chatTable: UITableView) -> Int {
        return 1;
    }
    
    func tableView(chatTable: UITableView, numberOfRowsInSection section: Int) -> Int {
        return umtArray.count
    }
    
    func tableView(chatTable: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {

        let tableCell = UITableViewCell(style: .Subtitle, reuseIdentifier: nil)
        
        var detailMsg = ""
        
        if(Singleton.sharedInstance.timeStamp == "Standard"){
            detailMsg = "Sent by: " + "\(umtArray[indexPath.row].getUsername())" + ", \(umtArray[indexPath.row].getTime())"
        }else if(Singleton.sharedInstance.timeStamp == "Military"){
            detailMsg = "Sent by: " + "\(umtArray[indexPath.row].getUsername())" + ", \(umtArray[indexPath.row].getTime())"
        }else{
            detailMsg = "Sent by: " + "\(umtArray[indexPath.row].getUsername())"
        }

        tableCell.textLabel!.text = umtArray[indexPath.row].getMessage()
        tableCell.detailTextLabel?.text = detailMsg

        return tableCell
    }
    
    func tableView(chatTable: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return 40.0
    }
    
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        
        let selectedUser = umtArray[indexPath.row].getUsername()
        var blockmessage = ""
        if(Singleton.sharedInstance.blockList.contains(umtArray[indexPath.row].getUsername())){
            blockmessage = "Do you want to unblock \(selectedUser)"
        }else{
            blockmessage = "Do you want to block \(selectedUser)"
        }
        
        let alert = UIAlertController(title: "Block User", message: blockmessage, preferredStyle: UIAlertControllerStyle.Alert)
        
        let acceptAction: UIAlertAction = UIAlertAction(title: "Yes", style: UIAlertActionStyle.Default) { (alertAction) -> Void in
        if(Singleton.sharedInstance.blockList.contains(self.umtArray[indexPath.row].getUsername())){
        Singleton.sharedInstance.blockList.removeAtIndex(Singleton.sharedInstance.blockList.indexOf(self.umtArray[indexPath.row].getUsername())!)
            }
        else{
            Singleton.sharedInstance.blockList.append(selectedUser)
            }
            self.chatTable.reloadData()
        }
        
        let declineAction = UIAlertAction(title: "No", style: UIAlertActionStyle.Cancel) { (alertAction) -> Void in
        }
        
        alert.addAction(acceptAction)
        alert.addAction(declineAction)
        
        NSOperationQueue.mainQueue().addOperationWithBlock { () -> Void in
            self.presentViewController(alert, animated: true, completion: nil)
        }
    }
    

    
    @IBAction func sendData(sender: UIButton) {
        
        //military
        
        let date = NSDate()
        let calendar = NSCalendar.currentCalendar()
        let components = calendar.components([.Hour, .Minute], fromDate: date)
        let hour = components.hour
        let minutes = components.minute
        
        let time = "\(hour):\(minutes)\n"

        var usernameMsgTime = Singleton.sharedInstance.userName
        usernameMsgTime.appendContentsOf("|" + self.msgfield.text!)
        usernameMsgTime.appendContentsOf("|" + time)
        
        let umtMsg = Umt(username: Singleton.sharedInstance.userName, message: msgfield.text!, time: time)
        
        let msg = usernameMsgTime.dataUsingEncoding(NSUTF8StringEncoding)
        
        if(self.msgfield.text! != ""){
            do {
                try chatsession.sendData(msg!, toPeers: chatsession.connectedPeers, withMode: MCSessionSendDataMode.Reliable)
                
                self.umtArray.append(umtMsg)
                self.chatTable.reloadData()
            } catch {

            }

        }

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
            
            let msgArr = msg?.componentsSeparatedByString("|")
            
            let a = Umt(username: msgArr![0], message: msgArr![1], time: msgArr![2])
            print("username: \(a.username)")
            print("message: \(a.message)")
            print("time: \(a.getTime())")


            dispatch_async(dispatch_get_main_queue()) {
                
                if(Singleton.sharedInstance.blockList.contains(a.getUsername())){
                    return
                }
                
                self.umtArray.append(a)
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

class Umt
{

    var username: String
    var message: String
    var time: String
    
    init(username: String, message: String, time: String){
        self.username = username
        self.message = message
        self.time = time
        
    }
    
    func getUsername() -> String{
        return self.username
    }
    
    func getMessage() -> String{
        return self.message
    }
    
    func getTime() -> String{
        return self.time
    }
    
}