//
//  ViewController.swift
//  Chat
//
//  Created by Vishal Gill on 11/26/15.
//  Copyright © 2015 Vishal Gill. All rights reserved.
//

import UIKit
import MultipeerConnectivity

let serviceType = "Monster-Chat"
var chatsession : MCSession!
var browsession : MCSession!
var peerID: MCPeerID!

var discoveryInfo = [String: String]()
var foundPeers = [MCPeerID]()
var invHandler: ((Bool, MCSession)->Void)!

var peerInformation = [peerInfo]()

var checkPeer = MCPeerID!()

class ChatList: UIViewController, MCNearbyServiceAdvertiserDelegate, MCNearbyServiceBrowserDelegate, MCSessionDelegate, UITableViewDelegate, UITableViewDataSource {
    
    override func touchesBegan(touches: Set<UITouch>, withEvent event: UIEvent?) {
        //self.view.resignFirstResponder()
        self.view.endEditing(true)
        
    }
    
    var browser: MCNearbyServiceBrowser!
    var assistant : MCNearbyServiceAdvertiser!

    
    @IBOutlet weak var table: UITableView!
    @IBOutlet weak var nameOfRoom: UITextField!
    
    

    @IBAction func createChat(sender: AnyObject) {
        
        //nameOfRoom.text = "\(Singleton.sharedInstance.userName)'s Chat"
        discoveryInfo["room"] = nameOfRoom.text
        peerID = MCPeerID(displayName: Singleton.sharedInstance.userName)
        chatsession = MCSession(peer: peerID)
        chatsession.delegate = self
        //self.session.delegate = self
        assistant = MCNearbyServiceAdvertiser(peer: peerID, discoveryInfo: discoveryInfo, serviceType: serviceType)
        assistant.delegate = self
        
        assistant.startAdvertisingPeer()
        self.table.reloadData()
    }
    

    
    override func viewDidLoad() {
        super.viewDidLoad()
        self.view.backgroundColor = Singleton.sharedInstance.backgroundColor
        
        peerID = MCPeerID(displayName: Singleton.sharedInstance.userName)
        chatsession = MCSession(peer: peerID)
        chatsession.delegate = self
        browser = MCNearbyServiceBrowser(peer: peerID, serviceType: serviceType)
        browser.delegate = self
        browser.startBrowsingForPeers()
        // Do any additional setup after loading the view, typically from a nib.
        self.table.delegate = self
        self.table.reloadData()
    }


    override func didReceiveMemoryWarning() {
        super.didReceiveMemoryWarning()
        // Dispose of any resources that can be recreated.
    }
    
    func numberOfSectionsInTableView(tableView: UITableView) -> Int {
        return 1;
    }
    
    func tableView(tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        return peerInformation.count
    }
    
    func tableView(tableView: UITableView, cellForRowAtIndexPath indexPath: NSIndexPath) -> UITableViewCell {
        let tableCell = UITableViewCell()
        
        if(peerInformation[indexPath.row].discInfo["room"] == ""){
            tableCell.textLabel?.text = "\(peerInformation[indexPath.row].peerID.displayName)'s chatroom"
        }else{
            tableCell.textLabel?.text = peerInformation[indexPath.row].discInfo["room"]!+" chatroom"
        }
        
        return tableCell
    }
    
    func tableView(tableView: UITableView, heightForRowAtIndexPath indexPath: NSIndexPath) -> CGFloat {
        return 35.0
    }
    
    func tableView(tableView: UITableView, didSelectRowAtIndexPath indexPath: NSIndexPath) {
        let selectedPeer = peerInformation[indexPath.row].peerID
        browser.invitePeer(selectedPeer, toSession: chatsession, withContext: nil, timeout: 20)
        print("invited \(selectedPeer.displayName)")
    }
    
    func browser(browser: MCNearbyServiceBrowser,
        foundPeer peerID: MCPeerID,
        withDiscoveryInfo info: [String : String]?){
            
        if(peerID.displayName == Singleton.sharedInstance.userName){
            return
        }

        peerInformation.append(peerInfo(peerID: peerID, discInfo: info!))

        self.table.reloadData()
            
    }
    
    func browser(browser: MCNearbyServiceBrowser,
        lostPeer peerID: MCPeerID){
            print("lost peer")

            for (index, peerInfo) in peerInformation.enumerate(){
                if peerInfo.peerID == peerID {
                    peerInformation.removeAtIndex(index)
                    self.table.reloadData()
                    return
                }
            }
    }
    
    func advertiser(advertiser: MCNearbyServiceAdvertiser,
        didReceiveInvitationFromPeer peerID: MCPeerID,
        withContext context: NSData?,
        invitationHandler: (Bool,
        MCSession) -> Void){
            
            print("recieved invite from \(peerID.displayName)")

            invHandler = invitationHandler
            let alert = UIAlertController(title: "", message: "\(peerID.displayName) wants to chat with you.", preferredStyle: UIAlertControllerStyle.Alert)
            
            let acceptAction: UIAlertAction = UIAlertAction(title: "Accept", style: UIAlertActionStyle.Default) { (alertAction) -> Void in
                invitationHandler(true, chatsession)
                checkPeer = peerID
                print("accepted")
            }
            
            let declineAction = UIAlertAction(title: "Cancel", style: UIAlertActionStyle.Cancel) { (alertAction) -> Void in
                invitationHandler(false, chatsession)
                print("declined")
            }
            
            alert.addAction(acceptAction)
            alert.addAction(declineAction)
            
            NSOperationQueue.mainQueue().addOperationWithBlock { () -> Void in
                self.presentViewController(alert, animated: true, completion: nil)
            }
            
    }
    
    func session(chatsession : MCSession, peer checkPeer: MCPeerID, didChangeState state: MCSessionState) {
        
        switch state{
            
        case MCSessionState.Connected:
            print("Connected to session:")
            NSOperationQueue.mainQueue().addOperationWithBlock { () -> Void in
            self.performSegueWithIdentifier("chatview", sender: self)
            }
        case MCSessionState.Connecting:
            print("Connecting to session:")
            
        case MCSessionState.NotConnected:
            print("Did not connect to session:")
            //NSOperationQueue.mainQueue().addOperationWithBlock { () -> Void in
              //  self.performSegueWithIdentifier("chatview", sender: self)
            //}
        }
    }
    
    func session(session: MCSession, didReceiveData data: NSData,
        fromPeer peerID: MCPeerID)  {
            
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




class peerInfo
{
    var peerID: MCPeerID
    var discInfo: [String: String]
    
    init(peerID: MCPeerID, discInfo: [String: String])
    {
        self.peerID = peerID
        self.discInfo = discInfo
    }
    
}
